
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

package com.kuborros.FurBotNeo.commands.GeneralCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

@CommandInfo(
        name = "ProfPic",
        description = "Gets profile picture of mentioned user from discord servers, and provides reverse image search option."
)
@Author("Kuborros")
public class ProfPicCmd extends GeneralCommand {
    public ProfPicCmd() {
        this.name = "profpic";
        this.help = "Shows profile pic of mentioned user!";
        this.arguments = "@user";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.category = new Command.Category("Basic");
    }

    @Override
    public void doCommand(CommandEvent event) {
        Message message = event.getMessage();
        if (message.getMentionedUsers().isEmpty()) return;
        User user = message.getMentionedUsers().get(0);

        event.reply(
                new EmbedBuilder().setColor(Color.ORANGE)
                        .setTitle("Profile picture of: " + user.getName())
                        .setThumbnail(user.getAvatarUrl())
                        .setDescription("[Click **here** to view it in full size!](" + user.getAvatarUrl() + ")" + "\n"
                                + "[Click **here** to reverse google search it!](https://images.google.com/searchbyimage?image_url=" + user.getAvatarUrl() + ")" + "\n")
                        .build());
    }  
}
