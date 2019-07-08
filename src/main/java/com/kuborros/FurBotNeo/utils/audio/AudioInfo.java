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


import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.annotation.Nonnull;

public class AudioInfo {

    private final AudioTrack track;
    private final Member author;
    private final TextChannel botchat;

    AudioInfo(AudioTrack track, Member author, TextChannel botchat) {
        this.track = track;
        this.author = author;
        this.botchat = botchat;
    }

    public AudioTrack getTrack() {
        return track;
    }

    public Member getAuthor() {
        return author;
    }

    TextChannel getBotchat() {
        return botchat;
    }

    @Override
    public String toString() {
        return "`[" + getTimestamp(track.getDuration()) + "]` **" + track.getInfo().title + "** - <@" + author.getEffectiveName() + ">";
    }

    @Nonnull
    private String getTimestamp(long milis) {
        long seconds = milis / 1000;
        long hours = Math.floorDiv(seconds, 3600);
        seconds -= (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds -= (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
    }
}