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
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.jagrosh.jdautilities.menu.Paginator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.kuborros.FurBotNeo.BotMain.storeItems;


@CommandInfo(
        name = "Inventory",
        description = "Lists entirety of your inventory."
)
@Author("Kuborros")
public class FullInventoryCommand extends ShopCommand {

    final EventWaiter waiter;

    public FullInventoryCommand(EventWaiter waiter) {
        this.name = "inventory";
        this.aliases = new String[]{"myitems"};
        this.help = "List your entire inventory.";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.category = new Command.Category("Shop");
        this.waiter = waiter;
    }

    @Override
    protected void doCommand(CommandEvent event) {

        Paginator.Builder builder = new Paginator.Builder();
        List<String> inv = inventory.getOwnedItems();
        if (inv.isEmpty()) {
            EmbedBuilder eBuilder = new EmbedBuilder().setTitle("**Contents of your inventory:**")
                    .setColor(Color.ORANGE)
                    .setDescription("You have nothing!");
            event.reply(eBuilder.build());
            return;
        }


        builder.allowTextInput(false)
                .setUsers(event.getAuthor())
                .waitOnSinglePage(false)
                .setColor(Color.ORANGE)
                .setItemsPerPage(10)
                .useNumberedItems(true)
                .setEventWaiter(waiter)
                .setText(String.format("**Contents of %s 's inventory:**", event.getMember().getEffectiveName()))
                .setFinalAction(message -> message.clearReactions().queue())
                .setTimeout(5, TimeUnit.MINUTES);

        builder.addItems(getPrettyItemNames(inv));
        builder.build().display(event.getTextChannel());

    }

    private String[] getPrettyItemNames(List<String> items) {
        ArrayList<String> names = new ArrayList<>();

        if (items.isEmpty()) return new String[]{""};

        JSONObject iNames = storeItems.getItemInventory();

        for (String item : items) {
            if (item.isBlank()) {
                names.add("Nothing!");
                break;
            }
            try {
                names.add(iNames.getJSONObject(item).getString("name"));
            } catch (JSONException e) {
                LOG.debug("Item not in items.json? {}", item);
                names.add("Broken item!");
            }
        }
        return names.toArray(new String[0]);
    }
}
