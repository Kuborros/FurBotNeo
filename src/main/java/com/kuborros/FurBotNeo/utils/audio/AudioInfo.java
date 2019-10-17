
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