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
        name = "Kiss",
        description = "Allows you to kiss someone!"
)
@Author("Kuborros")
public class KissCommand extends LewdCommand {

    public KissCommand() {
        this.name = "kiss";
        this.help = "Allows you to kiss someone!";
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
            event.reply("You have to mention _someone_ to kiss!");
            return;
        }

        if (members.contains(event.getMember())) {
            String rep = isFurry ? "Im sure there's a lonely *someone* around here..." : "Would kissing a mirror count \uD83E\uDD14";
            event.reply("You want to kiss... yourself? \n" + rep);
            return;
        }

        if (members.contains(guild.getSelfMember())) {
            if (isFurry) event.reply("**OwO** *Kisses you back*");
            else event.reply("*Smooch!* \n *You just kissed an overblown text file*. Was it worth it? (It was)");
            return;
        }

        event.reply(member.getEffectiveName() + " kisses " + members.get(0).getEffectiveName() + " on their lips!");
    }
}
