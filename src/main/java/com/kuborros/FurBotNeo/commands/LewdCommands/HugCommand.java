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

package com.kuborros.FurBotNeo.commands.LewdCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;

@CommandInfo(
        name = "Hug",
        description = "Allows you to hug someone!"
)
@Author("Kuborros")
public class HugCommand extends LewdCommand {

    public HugCommand() {
        this.name = "hug";
        this.help = "Allows you to hug someone!";
        this.arguments = "<@user>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Category("Lewd");
    }

    @Override
    protected void doCommand(CommandEvent event) {

        List<Member> members = event.getMessage().getMentionedMembers();
        Member member = event.getMember();

        if (members.isEmpty()) {
            event.reply("You have to mention _someone_ to hug!");
            return;
        }

        if (members.contains(event.getMember())) {
            String rep = isFurry ? "You know, you could hug someone really fluffy, like me!" : "I won't stop you from trying i guess.";
            event.reply("You want to hug with... yourself? \n" + rep);
            return;
        }

        if (members.contains(guild.getSelfMember())) {
            if (isFurry)
                event.reply("**OwO** *Hugs you~* \n My wings are basically warm blankets ~~with bones~~, perfect for hugging! ");
            else event.reply("*Hugs you* \n How did you hug a bot, is a thing best left to your imagination.");
            return;
        }

        event.reply(members.get(0).getEffectiveName() + " got just hugged by " + member.getEffectiveName() + "!");
    }
}
