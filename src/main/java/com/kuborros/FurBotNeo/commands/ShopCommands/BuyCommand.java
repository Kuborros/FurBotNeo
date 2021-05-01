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

package com.kuborros.FurBotNeo.commands.ShopCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.util.List;

import static com.kuborros.FurBotNeo.BotMain.storeItems;

public class BuyCommand extends ShopCommand {

    public BuyCommand(EventWaiter waiter) {
        this.name = "shop";
        this.children = new Command[]{
                new BuyRoleCommand(waiter)
        };
        this.help = "Lets you access _the shop_";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.category = new Command.Category("Shop");
    }

    @Override
    protected void doCommand(CommandEvent event) {

        if (event.getArgs().isBlank()) {
            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle(String.format("Inventory of %s.", event.getMember().getEffectiveName()))
                    .setThumbnail(event.getMember().getUser().getEffectiveAvatarUrl())
                    .setColor(Color.ORANGE)
                    .setDescription(String.format("You currently are level %d and hold %d coins.", inventory.getLevel(), inventory.getBalance()))
                    .addField("Your latest roles are: ", getPrettyInventoryRoles(), false)
                    .setFooter("Available store commands are: role");
            event.reply(builder.build());
        }
    }

    private String getPrettyInventoryRoles() {
        List<String> items = inventory.getOwnedRoles();
        StringBuilder inv = new StringBuilder();

        if (items.isEmpty()) return "None";

        if (items.size() > 4) {
            items = items.subList(0, 4);
        }

        JSONObject iNames = storeItems.getRoleInventory();

        for (String item : items) {
            if (item.isBlank()) {
                inv.append("Nothing!__");
                break;
            }
            try {
                inv.append(iNames.getJSONObject(item).getString("name")).append(", ");
            } catch (JSONException e) {
                LOG.debug("Item not in items.json? {}", item);
                inv.append("Broken item!").append(", ");
            }
        }
        return inv.substring(0, inv.length() - 2);
    }

}