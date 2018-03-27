/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
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
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.beam.BeamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;

import java.awt.*;
import java.util.*;

/**
 *
 * @author Kuborros
 */
abstract class MusicCommand extends Command {

    final static String NOTE = ":musical_note:  ";

    static Guild guild;
    private static ChannelFinder finder;
    String input;
    private static AudioEventListener audioEventListener;

    private final int PLAYLIST_LIMIT = 40;
    private static final AudioPlayerManager myManager = new DefaultAudioPlayerManager();
    private static final Map<String, Map.Entry<AudioPlayer, TrackManager>> players = new HashMap<>();


    MusicCommand() {
        audioEventListener = new AudioEventAdapter() {
            @Override
            public void onTrackStart(AudioPlayer player, AudioTrack track) {
                
                Set<AudioInfo> queue = getTrackManager(guild).getQueuedTracks();
                ArrayList<AudioInfo> tracks = new ArrayList<>(queue);

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


        YoutubeAudioSourceManager youtubeAudioSourceManager = new YoutubeAudioSourceManager(true);
        youtubeAudioSourceManager.configureRequests(config -> RequestConfig.copy(config).setCookieSpec(CookieSpecs.IGNORE_COOKIES).build());

        myManager.registerSourceManager(new SoundCloudAudioSourceManager());
        myManager.registerSourceManager(new BandcampAudioSourceManager());
        myManager.registerSourceManager(new VimeoAudioSourceManager());
        myManager.registerSourceManager(new TwitchStreamAudioSourceManager());
        myManager.registerSourceManager(new BeamAudioSourceManager());
        myManager.registerSourceManager(youtubeAudioSourceManager);
    }

    boolean hasPlayer(Guild guild) {
        return players.containsKey(guild.getId());
    }

    AudioPlayer getPlayer(Guild guild) {
        AudioPlayer p;
        if (hasPlayer(guild)) {
            p = players.get(guild.getId()).getKey();
        } else {
            p = createPlayer(guild);
        }
        return p;
    }

    void setVolume(Guild guild, int vol) {
        AudioPlayer p;
         if (hasPlayer(guild)) {
            p = players.get(guild.getId()).getKey();
            p.setVolume(vol);
        }
    }

    TrackManager getTrackManager(Guild guild) {
        return players.get(guild.getId()).getValue();
    }

    private AudioPlayer createPlayer(Guild guild) {
        AudioPlayer nPlayer = myManager.createPlayer();
        nPlayer.checkCleanup(10000);
        TrackManager manager = new TrackManager(nPlayer);
        nPlayer.addListener(manager);
        guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(nPlayer));
        players.put(guild.getId(), new AbstractMap.SimpleEntry<>(nPlayer, manager));
        return nPlayer;
    }

    void reset(Guild guild) {
        players.remove(guild.getId());
        getPlayer(guild).destroy();
        getTrackManager(guild).purgeQueue();
        guild.getAudioManager().closeAudioConnection();
    }

    void loadTrackNext(String identifier, Member author, Message msg) {
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

    void loadTrack(String identifier, Member author, Message msg) {
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
                if (!exception.severity.equals(FriendlyException.Severity.FAULT)) {
                    msg.getChannel().sendMessage("\u274C Error while fetching music: " + exception.getMessage()).queue();
                }
            }
        });
    }


    boolean isIdle(Guild guild, CommandEvent event) {
        if (!hasPlayer(guild) || getPlayer(guild).getPlayingTrack() == null) {
            event.reply("No music is being played at the moment!");
            return true;
        }
        return false;
    }

    void forceSkipTrack(Guild guild) {
        getPlayer(guild).stopTrack();
        
    }

    String buildQueueMessage(AudioInfo info) {
        AudioTrackInfo trackInfo = info.getTrack().getInfo();
        String title = trackInfo.title;
        long length = trackInfo.length;
        return "`[ " + getTimestamp(length) + " ]` " + title + "\n";
    }

    String getTimestamp(long milis) {
        long seconds = milis / 1000;
        long hours = Math.floorDiv(seconds, 3600);
        seconds -= (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds -= (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
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

    protected abstract void doCommand(CommandEvent event);
}