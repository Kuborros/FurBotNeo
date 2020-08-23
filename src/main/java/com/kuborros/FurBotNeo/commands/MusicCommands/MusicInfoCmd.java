
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

package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.kuborros.FurBotNeo.utils.audio.RequesterInfo;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.awt.*;

@CommandInfo(
        name = "MusicInfo",
        description = "Returns information on currently played track."
)
@Author("Kuborros")
public class MusicInfoCmd extends MusicCommand {

    public MusicInfoCmd() {
        this.name = "music";
        this.aliases = new String[]{"current", "musicinfo"};
        this.help = "Shows info about current song";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK};
        this.category = new Category("Music");
    }

    @Override
    public void doCommand(CommandEvent event) {

        if (!hasPlayer(guild) || getPlayer(guild).getPlayingTrack() == null) {
            event.getTextChannel().sendMessage(sendGenericEmbed("No music currently playing!", "")).queue();
        } else {
            AudioTrack track = getPlayer(guild).getPlayingTrack();
            AudioTrackInfo info = track.getInfo();
            RequesterInfo requester = (RequesterInfo) track.getUserData();
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.CYAN)
                    .setTitle(NOTE + "**Current Track Info**")
                    .addField(":cd:  Title", info.title, false)
                    .addField(":stopwatch:  Duration", "`[ " + getTimestamp(track.getPosition()) + " / " + getTimestamp(track.getInfo().length) + " ]`", false)
                    .addField(":microphone:  Channel / Author", info.author, false)
                    .setFooter("Requested by: " + requester.getName());
            event.reply(eb.build());
        }
    }

}
