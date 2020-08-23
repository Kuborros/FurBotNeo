
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
        name = "MusicPlay",
        description = "Plays the provided track, if none is currently playing, otherwise puts it into the queue. Suports youtube search."
)
@Author("Kuborros")
public class PlayCommand extends MusicCommand{


    public PlayCommand() {
        this.name = "play";
        this.arguments = "<title|URL>";
        this.help = "Adds the song to current music queue";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK};
        this.category = new Category("Music");
    }

    @Override
    public void doCommand(CommandEvent event){

        this.input = (input != null && input.startsWith("http")) ? input : "ytsearch: " + input;

        if (event.getArgs().isEmpty()) {
            event.reply(sendFailEmbed("Please include a valid search.", "\"Valid\" means supported url, or a search term (that will be searched on youtube)"));
        } else {
            loadTrack(input, event.getMember(), event.getMessage());
            if (getPlayer(guild).isPaused())
                getPlayer(guild).setPaused(false);
        }
    }

}
