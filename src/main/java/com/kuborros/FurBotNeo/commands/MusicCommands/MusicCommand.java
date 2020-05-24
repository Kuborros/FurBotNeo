
package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.kuborros.FurBotNeo.utils.audio.AudioInfo;
import com.kuborros.FurBotNeo.utils.audio.AudioPlayerSendHandler;
import com.kuborros.FurBotNeo.utils.audio.TrackManager;
import com.kuborros.FurBotNeo.utils.audio.invidious.InvidiousAudioSourceManager;
import com.kuborros.FurBotNeo.utils.config.FurConfig;
import com.kuborros.FurBotNeo.utils.store.MemberInventory;
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
import net.dv8tion.jda.api.entities.*;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.List;
import java.util.*;

import static com.kuborros.FurBotNeo.BotMain.*;


abstract class MusicCommand extends Command {

    private static final Logger LOG = LoggerFactory.getLogger("MusicCommands");

    static final String NOTE = ":musical_note:";
    static final String OKAY = "\u2705";
    static final String NO = "\u274C";

    static Guild guild;
    private static CommandClient client;
    private static FurConfig config;
    String input;
    private static AudioEventListener audioEventListener;

    protected MemberInventory inventory;
    protected boolean isDJ = false;

    private final int PLAYLIST_LIMIT = 40;
    private static final AudioPlayerManager myManager = new DefaultAudioPlayerManager();
    private static final Map<String, Map.Entry<AudioPlayer, TrackManager>> players = new HashMap<>();


    MusicCommand() {
        audioEventListener = new AudioEventAdapter() {
            @Override
            public void onTrackStart(AudioPlayer player, AudioTrack track) {

                Set<AudioInfo> queue = getTrackManager(guild).getQueuedTracks();
                List<AudioInfo> tracks = new ArrayList<>(queue);

                Objects.requireNonNull(guild.getTextChannelById(config.getAudioChannel())).getManager().setTopic("Last track: " + track.getInfo().title).queue();

                EmbedBuilder eb = new EmbedBuilder()
                        .setColor(Color.CYAN)
                        .setTitle(NOTE + "   **Now Playing**   ")
                        .addField("Current Track", "`(" + getTimestamp(track.getDuration()) + ")`  " + track.getInfo().title, false);
                if (tracks.size() > 1) {
                    eb.addField("Next Track", "`(" + getTimestamp(tracks.get(1).getTrack().getDuration()) + ")`  " + tracks.get(1).getTrack().getInfo().title, false);
                }
                Objects.requireNonNull(guild.getTextChannelById(config.getAudioChannel())).sendMessage(eb.build()).queue();
            }
        };


        YoutubeAudioSourceManager youtubeAudioSourceManager = new YoutubeAudioSourceManager(true);
        youtubeAudioSourceManager.configureRequests(config -> RequestConfig.copy(config).setCookieSpec(CookieSpecs.IGNORE_COOKIES).build());


        myManager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());
        myManager.registerSourceManager(new BandcampAudioSourceManager());
        myManager.registerSourceManager(new VimeoAudioSourceManager());
        myManager.registerSourceManager(new TwitchStreamAudioSourceManager());
        myManager.registerSourceManager(new BeamAudioSourceManager());

        if (cfg.isInvidioEnabled()) myManager.registerSourceManager(new InvidiousAudioSourceManager());
        else myManager.registerSourceManager(youtubeAudioSourceManager);

        myManager.registerSourceManager(new HttpAudioSourceManager()); //Might be not that safe

    }

    boolean hasPlayer(Guild guild) {
        return players.containsKey(guild.getId());
    }

    AudioPlayer getPlayer(Guild guild) {
        AudioPlayer p;
        p = hasPlayer(guild) ? players.get(guild.getId()).getKey() : createPlayer(guild);
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
            msg.getChannel().sendMessage(sendFailEmbed("You are not in a voice channel!", "I cannot join you, if you aren't there~")).queue();
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
                    Objects.requireNonNull(botchat).sendMessage(sendGenericEmbed("**Added Track**",
                            "`(" + getTimestamp(track.getDuration()) + ")`  " + track.getInfo().title)
                    ).queue();
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

                    Objects.requireNonNull(botchat).sendMessage(sendGenericEmbed("**Added Playlist**",
                            "`(" + "Tracks: " + playlist.getTracks().size() + ")`  " + playlist.getName())
                    ).queue();

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
                Objects.requireNonNull(botchat).sendMessage(sendFailEmbed("**Search failed**", "No playable tracks have been found!")).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                LOG.warn("Error loading track: ", exception);
                if (exception.severity != FriendlyException.Severity.FAULT) {
                    Objects.requireNonNull(botchat).sendMessage(sendErrorEmbed("**Error while fetching music:**", exception)).queue();
                }
            }
        });
    }

    void loadTrack(String identifier, Member author, Message msg) {
        if (Objects.requireNonNull(author.getVoiceState()).getChannel() == null) {
            msg.getChannel().sendMessage(sendFailEmbed("You are not in a voice channel!", "I cannot join you, if you aren't there~")).queue();
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
                    Objects.requireNonNull(botchat).sendMessage(sendGenericEmbed("**Added Track**",
                            "`(" + getTimestamp(track.getDuration()) + ")`  " + track.getInfo().title)
                    ).queue();
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

                    Objects.requireNonNull(botchat).sendMessage(sendGenericEmbed("**Added Playlist**",
                            "`(" + "Tracks: " + playlist.getTracks().size() + ")`  " + playlist.getName())
                    ).queue();

                    for (int i = 0; i < Math.min(playlist.getTracks().size(), PLAYLIST_LIMIT); i++) {
                        getTrackManager(guild).queue(playlist.getTracks().get(i), author, botchat);
                    }
                }
            }

            @Override
            public void noMatches() {
                Objects.requireNonNull(botchat).sendMessage(sendFailEmbed("**Search failed**", "No playable tracks have been found!")).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                LOG.warn("Error loading track: ", exception);
                if (exception.severity != FriendlyException.Severity.FAULT) {
                    Objects.requireNonNull(botchat).sendMessage(sendErrorEmbed("**Error while fetching music:**", exception)).queue();
                }
            }
        });
    }


    boolean isIdle(Guild guild) {
        return (!hasPlayer(guild) || getPlayer(guild).getPlayingTrack() == null);
    }

    boolean skipTrack(Guild guild) {
        if (isIdle(guild)) {
            return false;
        }
        getPlayer(guild).stopTrack();
        return true;
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

    private MessageEmbed bannedResponseEmbed() {
        String random = randomResponse.getRandomDeniedMessage(guild);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("You are blocked from bot commands!")
                .setDescription(random)
                .setColor(Color.ORANGE);
        return builder.build();
    }

    @Override
    protected void execute(CommandEvent event) {
        guild = event.getGuild();
        client = event.getClient();

        if (event.getAuthor().isBot()) return;

        inventory = inventoryCache.getInventory(event.getMember().getId(), guild.getId());
        if (inventory.isBanned()) {
            event.reply(bannedResponseEmbed());
            return;
        }
        //Token award per command use.
        //Should be tweaked later
        if (cfg.isShopEnabled()) {
            inventoryCache.setInventory(inventory.addTokens(10));
            isDJ = (inventory.getOwnedItems().contains("dj_badge") || inventory.isVIP());
        }


        event.getMember().getRoles().forEach(role -> {
            if (role.getName().contains("Music DJ")) isDJ = true;
        });

        if (cfg.isLegacySkipAudio() || event.getAuthor().getId().equals(cfg.getOwnerId())) {
            isDJ = true;
        }

        config = (FurConfig) event.getClient().getSettingsManager().getSettings(guild);
        if (!event.getTextChannel().equals(guild.getTextChannelById(config.getAudioChannel()))) return;
        getPlayer(guild).removeListener(audioEventListener);
        getPlayer(guild).addListener(audioEventListener);
        input = event.getArgs();
        doCommand(event);
    }

    protected MessageEmbed sendErrorEmbed(String msg, Exception e) {
        return sendErrorEmbed(msg, e.getLocalizedMessage());
    }

    protected MessageEmbed sendErrorEmbed(String msg, String ex) {
        String random = randomResponse.getRandomErrorMessage(guild);
        EmbedBuilder eb = new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle(client.getError() + "  " + msg)
                .setDescription(random);
        if (!ex.isBlank()) {
            eb.addField("", "`` " + ex + " ``", true);
        }
        return eb.build();
    }

    protected MessageEmbed sendFailEmbed(String title, String msg) {
        return sendGenericEmbed(title, msg, client.getError());
    }

    protected MessageEmbed sendGenericEmbed(String title, String msg) {
        return sendGenericEmbed(title, msg, NOTE);
    }

    protected MessageEmbed sendGenericEmbed(String title, String msg, String emote) {
        EmbedBuilder eb = new EmbedBuilder()
                .setColor(Color.CYAN)
                .setTitle(emote + "  " + title);
        if (!msg.isBlank()) {
            eb.addField("", msg, true);
        }
        return eb.build();
    }


    protected abstract void doCommand(CommandEvent event);
}