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

package com.kuborros.FurBotNeo.commands.AdminCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

import static com.kuborros.FurBotNeo.BotMain.inventoryCache;

@CommandInfo(
        name = "BotUnBan",
        description = "Unbans user from bot commands."
)
@Author("Kuborros")
public class BotUnBanCmd extends AdminCommand {

    public BotUnBanCmd() {
        this.name = "botunban";
        this.help = "Unbans user from using bot commands";
        this.arguments = "<@user>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    protected void doCommand(CommandEvent event) {
        if (event.getMessage().getMentionedUsers().isEmpty()) {
            event.replyWarning("You must mention someone for me to unban!");
        } else {
            Member member = event.getMessage().getMentionedMembers().get(0);
            inventoryCache.setInventory(inventoryCache.getInventory(member.getId(), event.getGuild().getId()).setBotBan(false));
            if (!inventoryCache.getInventory(member.getId(), event.getGuild().getId()).isBanned()) {
                event.reply("User has been unbanned!");
            } else {
                errorResponseEmbed("Something went wrong while removing ban!");
            }
        }
    }
}