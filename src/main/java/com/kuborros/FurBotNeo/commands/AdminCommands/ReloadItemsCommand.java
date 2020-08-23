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

import java.io.IOException;

import static com.kuborros.FurBotNeo.BotMain.storeItems;

@CommandInfo(
        name = "BotBan",
        description = "Bans user from using bot commands."
)
@Author("Kuborros")
public class ReloadItemsCommand extends AdminCommand {

    public ReloadItemsCommand() {
        this.name = "reloaditems";
        this.help = "Reloads shop item definition file";
        this.guildOnly = true;
        this.ownerCommand = true;
        this.hidden = true;
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    protected void doCommand(CommandEvent event) {
        try {
            storeItems.reloadItemsFile();
            event.replySuccess("Items reloaded successfully!");
        } catch (IOException e) {
            LOG.error("Exception occurred while reloading items.json: ", e);
            event.reply(errorResponseEmbed(e));
        }
    }
}
