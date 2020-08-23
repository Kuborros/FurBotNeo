
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
        name = "MusicShuffle",
        description = "Randomises playlist order."
)
@Author("Kuborros")
public class MusicShuffleCmd extends MusicCommand {

    public MusicShuffleCmd() {
        this.name = "shuffle";
        this.help = "Shuffles the playlist";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK};
        this.category = new Category("Music");
    }

    @Override
    public void doCommand(CommandEvent event) {
        if (isDJ) {
            if (isIdle(guild)) {
                event.reply(sendFailEmbed("There is no queue for me to shuffle!", ""));
                return;
            }
            getTrackManager(guild).shuffleQueue();
            event.getTextChannel().sendMessage(sendGenericEmbed("Shuffled queue!", "For better, or for worse~", ":twisted_rightwards_arrows:")).queue();
        } else {
            event.reply(sendFailEmbed("Only DJs have the power to shuffle the playlist!", "If you don't like the current track, you can always vote to ``skip`` it!"));
        }
    }
}