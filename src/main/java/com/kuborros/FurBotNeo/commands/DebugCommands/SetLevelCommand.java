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

package com.kuborros.FurBotNeo.commands.DebugCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.Permission;

import static com.kuborros.FurBotNeo.BotMain.inventoryCache;

@CommandInfo(
        name = "SetLevel",
        description = "Sets level of mentioned user"
)
@Author("Kuborros")
public class SetLevelCommand extends DebugCommand {

    public SetLevelCommand() {
        this.name = "setlevel";
        this.help = "Gives you item with specified id";
        this.arguments = "<item>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Category("Debug");
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    protected void doCommand(CommandEvent event) {
        int coins = Integer.parseInt(event.getArgs());
        inventoryCache.setInventory(inventory.setLevel(coins));
        event.reactSuccess();
    }
}
