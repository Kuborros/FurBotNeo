
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
import net.dv8tion.jda.api.Permission;

@CommandInfo(
        name = "MusicStop",
        description = "Stops current track, and clears the queue."
)
@Author("Kuborros")
public class MusicStopCmd extends MusicCommand{

    public MusicStopCmd() {
        this.name = "stop";
        this.help = "Completely stops music playback";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK,Permission.MANAGE_CHANNEL};
        this.category = new Category("Music");
    }

    @Override
    public void doCommand(CommandEvent event) {
        if (isDJ) {
            getTrackManager(guild).purgeStopQueue();
            if (skipTrack(guild)) {
                event.getTextChannel().getManager().setTopic("Music stopped.").queue();
                event.reply(sendGenericEmbed("Stopped playing!", "", ":stop_button:"));
            }
        } else {
            event.reply(sendFailEmbed("Only DJs can stop the tracks!", ""));
        }
    }
}
