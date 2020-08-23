
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
        name = "MusicPause",
        description = "Pauses and unpauses current track."
)
@Author("Kuborros")
public class MusicPauseCmd extends MusicCommand {

    public MusicPauseCmd() {
        this.name = "pause";
        this.aliases = new String[]{"resume"};
        this.help = "Pauses music playback";
        this.guildOnly = true;
        this.userPermissions = new Permission[]{Permission.KICK_MEMBERS};
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK};
        this.category = new Category("Music");

    }

    @Override
    public void doCommand(CommandEvent event) {
        if (isDJ) {
            if (getPlayer(guild).isPaused()) {
                getPlayer(guild).setPaused(false);
                event.reply(sendGenericEmbed("Player resumed!", ""));
            } else {
                getPlayer(guild).setPaused(true);
                event.reply(sendGenericEmbed("Player paused!", "(Please don't forget to unpause it later!)"));
            }
        } else {
            event.reply(sendFailEmbed("Only DJs can pause the tracks!", "That's just a bit too much power for everyone to hold~"));
        }
    }

}