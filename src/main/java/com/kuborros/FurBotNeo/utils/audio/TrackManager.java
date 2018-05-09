/*
 * The MIT License
 *
 * Copyright 2017 Kuborros.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.kuborros.FurBotNeo.utils.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;


public class TrackManager extends AudioEventAdapter {


    private final AudioPlayer player;
    private final Queue<AudioInfo> queue;

    public TrackManager(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track, Member author) {
        AudioInfo info = new AudioInfo(track, author);
        queue.add(info);

        if (player.getPlayingTrack() == null) {
            player.playTrack(track);
        }
    }

// --Commented out by Inspection START (2017-12-25 23:51):
//    public void queueTimed(AudioTrack track, Member author, Long milis) {
//
//        if (track.isSeekable()) {
//            track.setPosition(milis);
//        }
//        AudioInfo info = new AudioInfo(track, author);
//        queue.add(info);
//
//        if (player.getPlayingTrack() == null) {
//            player.playTrack(track);
//        }
//    }
// --Commented out by Inspection STOP (2017-12-25 23:51)

    public void skipToTime(Long milis) {
        AudioTrack track = player.getPlayingTrack();
        if (track.isSeekable()) {
            track.setPosition(milis);
        }

    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        AudioInfo info = queue.element();
        VoiceChannel vChan = info.getAuthor().getVoiceState().getChannel();
        if (vChan == null) {
            player.stopTrack();
        } else {
            if (!info.getAuthor().getGuild().getAudioManager().isConnected()) {
                info.getAuthor().getGuild().getAudioManager().openAudioConnection(vChan);
            }
        }
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        Guild g = Objects.requireNonNull(queue.poll()).getAuthor().getGuild();
        if (queue.isEmpty()) {
            new Thread(() -> g.getAudioManager().closeAudioConnection()).start();
        } else {
            player.playTrack(queue.element().getTrack());
        }
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

    public Set<AudioInfo> getQueuedTracks() {
        return new LinkedHashSet<>(queue);
    }

    public void purgeQueue() {
        queue.clear();
    }


    // public AudioInfo getTrackInfo(AudioTrack track) {
    //     return queue.stream().filter(audioInfo -> audioInfo.getTrack().equals(track)).findFirst().orElse(null);
    // }

}
