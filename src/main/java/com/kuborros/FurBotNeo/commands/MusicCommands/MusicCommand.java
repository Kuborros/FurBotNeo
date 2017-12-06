/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.kuborros.FurBotNeo.utils.audio.AudioInfo;
import com.kuborros.FurBotNeo.utils.audio.AudioPlayerSendHandler;
import com.kuborros.FurBotNeo.utils.audio.TrackManager;
import com.kuborros.FurBotNeo.utils.msg.ChannelFinder;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;
import java.util.*;

/**
 *
 * @author Kuborros
 */
@SuppressWarnings("ALL")
public abstract class MusicCommand extends Command{
    
    protected final static String NOTE = ":musical_note:  ";
    
    protected static Guild guild;
    protected static ChannelFinder finder;
    protected String input;
    protected static AudioEventListener audioEventListener;
    
    protected final int PLAYLIST_LIMIT = 40;
    protected static AudioPlayerManager myManager = new DefaultAudioPlayerManager();
    protected static Map<String, Map.Entry<AudioPlayer, TrackManager>> players = new HashMap<>();

        
    public MusicCommand() {
        this.audioEventListener = new AudioEventAdapter() {
            @Override
            public void onTrackStart(AudioPlayer player, AudioTrack track) {
                
                Set<AudioInfo> queue = getTrackManager(guild).getQueuedTracks();
                ArrayList<AudioInfo> tracks = new ArrayList<>();
                queue.forEach(audioInfo -> tracks.add(audioInfo));
                
                finder.FindBotChat().getManager().setTopic("Last track: " + track.getInfo().title).queue();
                
                EmbedBuilder eb = new EmbedBuilder()
                        .setColor(Color.CYAN)
                        .setDescription(NOTE + "   **Now Playing**   ")
                        .addField("Current Track", "`(" + getTimestamp(track.getDuration()) + ")`  " + track.getInfo().title, false);
                if (tracks.size() > 1){
                    eb.addField("Next Track", "`(" + getTimestamp(tracks.get(1).getTrack().getDuration()) + ")`  " + tracks.get(1).getTrack().getInfo().title, false);
                }
                finder.FindBotChat().sendMessage(
                        eb.build()
                ).queue();
            }
        };
        AudioSourceManagers.registerRemoteSources(myManager);
    }

    protected boolean hasPlayer(Guild guild) {
        return players.containsKey(guild.getId());
    }

    protected AudioPlayer getPlayer(Guild guild) {
        AudioPlayer p;
        if (hasPlayer(guild)) {
            p = players.get(guild.getId()).getKey();
        } else {
            p = createPlayer(guild);
        }
        return p;
    }
    
     protected void setVolume(Guild guild, int vol) {
        AudioPlayer p;
        if (hasPlayer(guild)) {
            p = players.get(guild.getId()).getKey();
            p.setVolume(vol);
        }
    }

    protected TrackManager getTrackManager(Guild guild) {
        return players.get(guild.getId()).getValue();
    }

    protected AudioPlayer createPlayer(Guild guild) {
        AudioPlayer nPlayer = myManager.createPlayer();
        TrackManager manager = new TrackManager(nPlayer);
        nPlayer.addListener(manager);
        guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(nPlayer));
        players.put(guild.getId(), new AbstractMap.SimpleEntry<>(nPlayer, manager));
        return nPlayer;
    }

    protected void reset(Guild guild) {
        players.remove(guild.getId());
        getPlayer(guild).destroy();
        getTrackManager(guild).purgeQueue();
        guild.getAudioManager().closeAudioConnection();
    }

    protected void loadTrackNext(String identifier, Member author, Message msg) {
        if (author.getVoiceState().getChannel() == null) {
            msg.getChannel().sendMessage("You are not in a Voice Channel!").queue();
            return;
        }

        guild = author.getGuild();
        getPlayer(guild);

        myManager.setFrameBufferDuration(100);
        myManager.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {

                AudioInfo currentTrack = getTrackManager(guild).getQueuedTracks().iterator().next();
                Set<AudioInfo> queuedTracks = getTrackManager(guild).getQueuedTracks();
                queuedTracks.remove(currentTrack);
                getTrackManager(guild).purgeQueue();
                getTrackManager(guild).queue(currentTrack.getTrack(), author);
                getTrackManager(guild).queue(track, author);
                queuedTracks.forEach(audioInfo -> getTrackManager(guild).queue(audioInfo.getTrack(), author));
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.getSelectedTrack() != null) {
                    trackLoaded(playlist.getSelectedTrack());
                } else if (playlist.isSearchResult()) {
                    trackLoaded(playlist.getTracks().get(0));
                } else {
                    AudioInfo currentTrack = getTrackManager(guild).getQueuedTracks().iterator().next();
                    Set<AudioInfo> queuedTracks = getTrackManager(guild).getQueuedTracks();
                    queuedTracks.remove(currentTrack);
                    getTrackManager(guild).purgeQueue();
                    getTrackManager(guild).queue(currentTrack.getTrack(), author);
                    for (int i = 0; i < Math.min(playlist.getTracks().size(), PLAYLIST_LIMIT); i++) {
                        getTrackManager(guild).queue(playlist.getTracks().get(i), author);
                    }
                    queuedTracks.forEach(audioInfo -> getTrackManager(guild).queue(audioInfo.getTrack(), author));
                }
            }

            @Override
            public void noMatches() {
                msg.getChannel().sendMessage("\u26A0 No playable tracks were found.").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
               if(!exception.severity.equals(FriendlyException.Severity.FAULT)) {
                    msg.getChannel().sendMessage("\u274C Error while fetching music: " + exception.getMessage()).queue();
        }
    }
        });
    }

    protected void loadTrack(String identifier, Member author, Message msg) {
        if (author.getVoiceState().getChannel() == null) {
            msg.getChannel().sendMessage("You are not in a Voice Channel!").queue();
            return;
        }

        guild = author.getGuild();
        getPlayer(guild);


        myManager.setFrameBufferDuration(5000);
        myManager.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                getTrackManager(guild).queue(track, author);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.getSelectedTrack() != null) {
                    trackLoaded(playlist.getSelectedTrack());
                } else if (playlist.isSearchResult()) {
                    trackLoaded(playlist.getTracks().get(0));
                } else {

                    for (int i = 0; i < Math.min(playlist.getTracks().size(), PLAYLIST_LIMIT); i++) {
                        getTrackManager(guild).queue(playlist.getTracks().get(i), author);
                    }
                }
            }

            @Override
            public void noMatches() {
                msg.getChannel().sendMessage("\u26A0 No playable tracks were found.").queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
               if(!exception.severity.equals(FriendlyException.Severity.FAULT)) {
                    msg.getChannel().sendMessage("\u274C Error while fetching music: " + exception.getMessage()).queue();
        }
    }
        });
    }

    protected boolean isIdle(Guild guild, CommandEvent event) {
        if (!hasPlayer(guild) || getPlayer(guild).getPlayingTrack() == null) {
            event.reply("No music is being played at the moment!");
            return true;
        }
        return false;
    }

    protected void forceSkipTrack(Guild guild) {
        getPlayer(guild).stopTrack();
        
    }
    
        protected String buildQueueMessage(AudioInfo info) {
        AudioTrackInfo trackInfo = info.getTrack().getInfo();
        String title = trackInfo.title;
        long length = trackInfo.length;
        return "`[ " + getTimestamp(length) + " ]` " + title + "\n";
    }

    protected String getTimestamp(long milis) {
        long seconds = milis / 1000;
        long hours = Math.floorDiv(seconds, 3600);
        seconds -= (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds -= (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
    }

    protected String getOrNull(String s) {
        return s.isEmpty() ? "N/A" : s;
    }
    
        @Override
        protected void execute(CommandEvent event) {
            guild = event.getGuild();
            finder = new ChannelFinder(guild);
            if (!event.getTextChannel().equals(finder.FindBotChat())) return;
            if (!event.getAuthor().isBot()) {            
                getPlayer(guild).removeListener(audioEventListener);
                getPlayer(guild).addListener(audioEventListener);
                input = event.getArgs();
                doCommand(event);
            }
    }    
    public abstract void doCommand(CommandEvent event);
}