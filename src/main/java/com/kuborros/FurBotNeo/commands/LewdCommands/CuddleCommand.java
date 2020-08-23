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
        name = "Cuddle",
        description = "Allows you to cuddle someone!"
)
@Author("Kuborros")
public class CuddleCommand extends LewdCommand {

    public CuddleCommand() {
        this.name = "cuddle";
        this.aliases = new String[]{"snuggle"};
        this.help = "Allows you to cuddle with someone!";
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
            event.reply("You have to mention _someone_ to cuddle with!");
            return;
        }

        if (members.contains(event.getMember())) {
            event.reply("You want to cuddle with... yourself? \n Honestly, you can cuddle with _me_ if you want~");
            return;
        }

        if (members.contains(guild.getSelfMember())) {
            if (isFurry) event.reply("\uD83D\uDE3B *Cuddles up with you*");
            else
                event.reply("Ofcourse you can cuddle with me \uD83D\uDE0A \n Afterall my server is comfortably warm at 31°C");
            return;
        }

        event.reply(member.getEffectiveName() + " softly cuddles with " + members.get(0).getEffectiveName());
    }
}
