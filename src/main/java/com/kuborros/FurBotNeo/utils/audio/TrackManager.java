
/*
 * Copyright © 2020 Kuborros (kuborros@users.noreply.github.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kuborros.FurBotNeo.utils.audio;


import com.kuborros.FurBotNeo.utils.msg.RandomResponse;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;


public class TrackManager extends AudioEventAdapter {


    private final AudioPlayer player;
    private final Queue<AudioInfo> queue;

    private static final Logger LOG = LoggerFactory.getLogger("MusicPlayback");


    public TrackManager(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track, Member author, TextChannel botchat) {
        track.setUserData(new RequesterInfo(author));
        AudioInfo info = new AudioInfo(track, author, botchat);
        queue.add(info);

        if (player.getPlayingTrack() == null) {
            player.playTrack(track);
        }
    }

    public void skipToTime(Long milis) {
        AudioTrack track = player.getPlayingTrack();
        if (track.isSeekable()) {
            track.setPosition(milis);
        }

    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        AudioInfo info = queue.element();
        VoiceChannel vChan = null;
        Guild guild = info.getAuthor().getGuild();
        try {
            vChan = Objects.requireNonNull(Objects.requireNonNull(info.getAuthor().getVoiceState())).getChannel();
        } catch (NullPointerException e) {
            player.stopTrack();
        }
        if (!guild.getAudioManager().isConnected()) {
            guild.getAudioManager().openAudioConnection(vChan);
        }
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        Guild g = Objects.requireNonNull(queue.poll()).getAuthor().getGuild();
        if (queue.isEmpty() || !endReason.mayStartNext) {
            player.stopTrack();
            new Thread(() -> g.getAudioManager().closeAudioConnection()).start();
        } else {
            player.playTrack(queue.element().getTrack());
        }
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        assert track != null;
        LOG.warn("Playback error occurred on track: " + track.getInfo().title, exception);
        EmbedBuilder eb = new EmbedBuilder()
                .setColor(Color.RED)
                .setDescription(RandomResponse.getRandomBaseErrorMessage())
                .setTitle("\u274C" + " **An playback error has occurred!**")
                .addField("Exception on playback of track: " + track.getInfo().title, exception.getLocalizedMessage(), false);
        getTrackInfo(track).getBotchat().sendMessage(eb.build()).queue();
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        assert track != null;
        LOG.warn("Track " + track.getInfo().title + " stuck for " + thresholdMs + " ms, skipping.");
        assert player != null;
        player.stopTrack();
    }

    public void shuffleQueue() {
        List<AudioInfo> tQueue = new ArrayList<>(getQueuedTracks());
        AudioInfo current = tQueue.get(0);
        tQueue.remove(0);
        Collections.shuffle(tQueue);
        tQueue.add(0, current);
        purgeQueue();
        queue.addAll(tQueue);
    }

    public void clearQueue() {
        if (!queue.isEmpty()) {
            try {
                AudioInfo curr = queue.peek();
                queue.clear();
                queue.add(curr);
            } catch (Exception e) {
                LOG.warn("Unable to clear queue: ", e);
            }
        }
    }

    public Set<AudioInfo> getQueuedTracks() {
        return new LinkedHashSet<>(queue);
    }

    public void purgeQueue() {
        queue.clear();
    }

    public void purgeStopQueue() {
        if (!queue.isEmpty()) {
            AudioInfo info = queue.element();
            queue.clear();
            queue.add(info);
        }
    }


    private AudioInfo getTrackInfo(AudioTrack track) {
        return queue.stream().filter(audioInfo -> audioInfo.getTrack().equals(track)).findFirst().orElse(null);
    }

}
