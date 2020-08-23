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
        name = "MusicClear",
        description = "Clears the play queue."
)
@Author("Kuborros")
public class MusicClearCmd extends MusicCommand {

    public MusicClearCmd() {
        this.name = "clear";
        this.help = "Clear the playlist";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK};
        this.category = new Category("Music");
    }

    @Override
    public void doCommand(CommandEvent event) {

        if (isDJ) {
            if (isIdle(guild)) {
                event.reply(sendFailEmbed("There is no playlist to clear!", "Duh."));
                return;
            }
            getTrackManager(guild).clearQueue();
            event.reply(sendGenericEmbed("Playlist cleared!", ""));
        } else {
            event.reply(sendFailEmbed("Only DJs can clear the playlist!", "If you don't like the track, you can always vote to ``skip`` it!"));
        }
    }
}