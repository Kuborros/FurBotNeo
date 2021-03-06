
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
        name = "MusicVolume",
        description = "Sets playback volume per-guild (values over 150 can cause distortion)."
)
@Author("Kuborros")
public class MusicVolumeCmd extends MusicCommand {

    public MusicVolumeCmd() {
        this.name = "volume";
        this.arguments = "<Volume>";
        this.help = "Sets the playback volume";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK};
        this.category = new Category("Music");
    }

    @Override
    public void doCommand(CommandEvent event) {
        if (isDJ) {
            if (event.getArgs().isEmpty()) {
                event.reply(sendFailEmbed("Please set valid volume!", "Correct values are anywhere from 0 (muted) to 1000 (horrible noise)"));
            } else {
                int vol = 100;
                try {
                    vol = Integer.decode(event.getArgs());
                } catch (NumberFormatException e) {
                    event.reply(sendFailEmbed("Please type in a valid number!", "Correct values are anywhere from 0 (muted) to 1000 (horrible noise)"));
                }
                setVolume(guild, vol);
                event.reply(sendGenericEmbed(String.format("Volume set to: %d", getPlayer(guild).getVolume()), ""));
            }
        } else {
            event.reply(sendFailEmbed("Only DJs can change the music volume!", "You all know why it's a thing..."));
        }
    }
}