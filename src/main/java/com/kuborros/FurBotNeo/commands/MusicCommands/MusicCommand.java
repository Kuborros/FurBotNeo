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
import com.kuborros.FurBotNeo.utils.config.FurConfig;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.beam.BeamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.sql.SQLException;
import java.util.*;

import static com.kuborros.FurBotNeo.BotMain.db;

/**
 *
 * @author Kuborros
 */
abstract class MusicCommand extends Command {

    private static final Logger LOG = LoggerFactory.getLogger("MusicCommands");

    final static String NOTE = ":musical_note:  ";

    private final static String ERROR = "\u274C  ";

    static Guild guild;
    private static FurConfig config;
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

                Objects.requireNonNull(guild.getTextChannelById(config.getAudioChannel())).getManager().setTopic("Last track: " + track.getInfo().title).queue();

                EmbedBuilder eb = new EmbedBuilder()
                        .setColor(Color.CYAN)
                        .setDescription(NOTE + "   **Now Playing**   ")
                        .addField("Current Track", "`(" + getTimestamp(track.getDuration()) + ")`  " + track.getInfo().title, false);
                if (tracks.size() > 1){
                    eb.addField("Next Track", "`(" + getTimestamp(tracks.get(1).getTrack().getDuration()) + ")`  " + tracks.get(1).getTrack().getInfo().title, false);
                }
                Objects.requireNonNull(guild.getTextChannelById(config.getAudioChannel())).sendMessage(eb.build()).queue();
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
        myManager.registerSourceManager(new HttpAudioSourceManager()); //Might be not that safe
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
        if (Objects.requireNonNull(author.getVoiceState()).getChannel() == null) {
            msg.getChannel().sendMessage(ERROR + "You are not in a Voice Channel!").queue();
            return;
        }

        TextChannel botchat = guild.getTextChannelById(config.getAudioChannel());

        guild = author.getGuild();
        getPlayer(guild);
        myManager.setFrameBufferDuration(5000);
        myManager.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {

                if (!isIdle(guild)) {
                    EmbedBuilder eb = new EmbedBuilder()
                            .setColor(Color.CYAN)
                            .setTitle(NOTE + "**Added Track**")
                            .addField("", "`(" + getTimestamp(track.getDuration()) + ")`  " + track.getInfo().title, true);
                    Objects.requireNonNull(botchat).sendMessage(eb.build()).queue();
                }

                AudioInfo currentTrack = getTrackManager(guild).getQueuedTracks().iterator().next();
                Set<AudioInfo> queuedTracks = getTrackManager(guild).getQueuedTracks();
                queuedTracks.remove(currentTrack);
                getTrackManager(guild).purgeQueue();
                getTrackManager(guild).queue(currentTrack.getTrack(), author, botchat);
                getTrackManager(guild).queue(track, author, botchat);
                queuedTracks.forEach(audioInfo -> getTrackManager(guild).queue(audioInfo.getTrack(), author, botchat));
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.getSelectedTrack() != null) {
                    trackLoaded(playlist.getSelectedTrack());
                } else if (playlist.isSearchResult()) {
                    trackLoaded(playlist.getTracks().get(0));
                } else {

                    TextChannel botchat = guild.getTextChannelById(config.getAudioChannel());

                    EmbedBuilder eb = new EmbedBuilder()
                            .setColor(Color.CYAN)
                            .setDescription(NOTE + "**Added Playlist**")
                            .addField("", "`(" + "Tracks: " + playlist.getTracks().size() + ")`  " + playlist.getName(), true);
                    Objects.requireNonNull(botchat).sendMessage(eb.build()).queue();

                    AudioInfo currentTrack = getTrackManager(guild).getQueuedTracks().iterator().next();
                    Set<AudioInfo> queuedTracks = getTrackManager(guild).getQueuedTracks();
                    queuedTracks.remove(currentTrack);
                    getTrackManager(guild).purgeQueue();
                    getTrackManager(guild).queue(currentTrack.getTrack(), author, botchat);
                    for (int i = 0; i < Math.min(playlist.getTracks().size(), PLAYLIST_LIMIT); i++) {
                        getTrackManager(guild).queue(playlist.getTracks().get(i), author, botchat);
                    }
                    queuedTracks.forEach(audioInfo -> getTrackManager(guild).queue(audioInfo.getTrack(), author, botchat));
                }
            }

            @Override
            public void noMatches() {
                EmbedBuilder eb = new EmbedBuilder()
                        .setColor(Color.CYAN)
                        .setDescription(ERROR + "**Search failed**")
                        .addField("", "No playable tracks have been found!", true);
                Objects.requireNonNull(botchat).sendMessage(eb.build()).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {

                LOG.warn("Error loading track: ", exception);

                if (!exception.severity.equals(FriendlyException.Severity.FAULT)) {
                    EmbedBuilder eb = new EmbedBuilder()
                            .setColor(Color.CYAN)
                            .setDescription(ERROR + "**Error while fetching music:**")
                            .addField("", exception.getMessage(), true);
                    Objects.requireNonNull(botchat).sendMessage(eb.build()).queue();
                }
            }
        });
    }

    void loadTrack(String identifier, Member author, Message msg) {
        if (Objects.requireNonNull(author.getVoiceState()).getChannel() == null) {
            msg.getChannel().sendMessage(ERROR + "You are not in a Voice Channel!").queue();
            return;
        }

        guild = author.getGuild();
        getPlayer(guild);

        TextChannel botchat = guild.getTextChannelById(config.getAudioChannel());

        myManager.setFrameBufferDuration(5000);
        myManager.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {

                if (!isIdle(guild)) {
                    EmbedBuilder eb = new EmbedBuilder()
                            .setColor(Color.CYAN)
                            .setDescription(NOTE + "**Added Track**")
                            .addField("", "`(" + getTimestamp(track.getDuration()) + ")`  " + track.getInfo().title, true);
                    Objects.requireNonNull(botchat).sendMessage(eb.build()).queue();
                }
                getTrackManager(guild).queue(track, author, botchat);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.getSelectedTrack() != null) {
                    trackLoaded(playlist.getSelectedTrack());
                } else if (playlist.isSearchResult()) {
                    trackLoaded(playlist.getTracks().get(0));
                } else {

                    EmbedBuilder eb = new EmbedBuilder()
                            .setColor(Color.CYAN)
                            .setDescription(NOTE + "**Added Playlist**")
                            .addField("", "`(" + "Tracks: " + playlist.getTracks().size() + ")`  " + playlist.getName(), true);
                    Objects.requireNonNull(botchat).sendMessage(eb.build()).queue();

                    for (int i = 0; i < Math.min(playlist.getTracks().size(), PLAYLIST_LIMIT); i++) {
                        getTrackManager(guild).queue(playlist.getTracks().get(i), author, botchat);
                    }
                }
            }

            @Override
            public void noMatches() {
                EmbedBuilder eb = new EmbedBuilder()
                        .setColor(Color.CYAN)
                        .setDescription(ERROR + "**Search failed**")
                        .addField("", "No playable tracks have been found!", true);
                Objects.requireNonNull(botchat).sendMessage(eb.build()).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {

                LOG.warn("Error loading track: ", exception);

                if (!exception.severity.equals(FriendlyException.Severity.FAULT)) {
                    EmbedBuilder eb = new EmbedBuilder()
                            .setColor(Color.CYAN)
                            .setDescription(ERROR + "**Error while fetching music:**")
                            .addField("", exception.getMessage(), true);
                    Objects.requireNonNull(botchat).sendMessage(eb.build()).queue();
                }
            }
        });
    }


    boolean isIdle(Guild guild) {
        return (!hasPlayer(guild) || getPlayer(guild).getPlayingTrack() == null);
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
        try {
            if (db.getBanStatus(event.getMember().getId(), guild.getId())) {
                event.reply(ERROR + "You are blocked from bot commands!");
                return;
            }
        } catch (SQLException e) {
            LOG.error("Error while contacting database: ", e);
        }
        config = (FurConfig) event.getClient().getSettingsManager().getSettings(guild);
        if (!event.getTextChannel().equals(guild.getTextChannelById(config.getAudioChannel()))) return;
            if (!event.getAuthor().isBot()) {            
                getPlayer(guild).removeListener(audioEventListener);
                getPlayer(guild).addListener(audioEventListener);
                input = event.getArgs();
                doCommand(event);
            }
    }

    protected abstract void doCommand(CommandEvent event);
}