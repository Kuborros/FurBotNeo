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
import com.kuborros.FurBotNeo.utils.menus.StoreDialog;
import com.kuborros.FurBotNeo.utils.store.ShopItem;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.kuborros.FurBotNeo.BotMain.inventoryCache;
import static com.kuborros.FurBotNeo.BotMain.storeItems;


@CommandInfo(
        name = "Item",
        description = "Purchase items here. Also losit some inventory info with no parameters."
)
@Author("Kuborros")
public class BuyItemCommand extends ShopCommand {

    static final ArrayList<ShopItem> availableItems = new ArrayList<>();
    final EventWaiter waiter;
    ShopItem currItem;
    Member author;
    StoreDialog storeDialog;


    public BuyItemCommand(EventWaiter waiter) {
        this.name = "item";
        this.help = "Purchase items here!";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_MANAGE};
        this.category = new Command.Category("Shop");
        this.waiter = waiter;
        loadItems();
    }

    @Override
    protected void doCommand(CommandEvent event) {

        author = event.getMember();

        StoreDialog.Builder builder = new StoreDialog.Builder();

        builder.setUsers(event.getAuthor())
                .setColor(Color.ORANGE)
                .setItemsPerPage(6)
                .useNumberedItems(true)
                .showPageNumbers(true)
                .setEventWaiter(waiter)
                .setDefaultEnds("", "")
                .setSelectedEnds("**", "**")
                .setSelectionConsumer(this::buyItem)
                .setText(String.format("**Items for sale available for %s:**", event.getMember().getEffectiveName()))
                .setCanceled(message -> message.clearReactions().queue())
                .setTimeout(5, TimeUnit.MINUTES);

        builder.addChoices(availableItems);
        storeDialog = builder.build();
        storeDialog.display(event.getTextChannel());

    }

    private void buyItem(Message message, int selection) {

        storeDialog.setNoUpdate(true);

        EmbedBuilder builder = new EmbedBuilder();
        currItem = availableItems.get(selection - 1);
        int value = currItem.getValue();
        int balance = inventory.getBalance();
        boolean canBuy;
        if (value > balance) {
            builder.setTitle(String.format("Buying item: %s", currItem.getItemName()))
                    .setDescription(String.format("It seems you cannot afford it! \n It would cost you %d tokens to purchase this item.", currItem.getValue()));
            if (!currItem.getUrl().isBlank()) builder.setThumbnail(currItem.getUrl());
            canBuy = false;
        } else if (inventory.getOwnedItems().contains(currItem.getDbName())) {
            builder.setTitle(String.format("Buying item: %s", currItem.getItemName()))
                    .setDescription("Wait a second... You already own it!");
            if (!currItem.getUrl().isBlank()) builder.setThumbnail(currItem.getUrl());
            canBuy = false;
        } else {
            builder.setTitle(String.format("Buying item: %s", currItem.getItemName()))
                    .setDescription(String.format("Would you like to buy it for %d tokens?", currItem.getValue()));
            if (!currItem.getUrl().isBlank()) builder.setThumbnail(currItem.getUrl());
            canBuy = true;
        }
        message.editMessage(new MessageBuilder().setEmbed(builder.build()).setContent("").build()).queue();
        if (canBuy) awaitResponse(message);
        else message.clearReactions().complete();
    }

    private void awaitResponse(Message message) {

        message.clearReactions().complete();
        message.addReaction(OKAY).complete();
        message.addReaction(NO).complete();

        waiter.waitForEvent(MessageReactionAddEvent.class,
                event -> checkReaction(event, message, author.getId()),
                event -> handleMessageReactionAddAction(event, message),
                5, TimeUnit.MINUTES, () -> message.clearReactions().queue());
    }

    private boolean checkReaction(MessageReactionAddEvent event, Message message, String authorId) {
        if (event.getMessageIdLong() != message.getIdLong())
            return false;
        switch (event.getReactionEmote().getName()) {
            case OKAY:
            case NO:
                return (Objects.requireNonNull(event.getMember())).getId().equals(authorId);
            default:
                return false;
        }
    }

    private void handleMessageReactionAddAction(MessageReactionAddEvent event, Message message) {

        if (event.getReaction().getReactionEmote().getName().equals(NO)) {
            message.clearReactions().queue();
            return;
        }
        if (event.getReaction().getReactionEmote().getName().equals(OKAY)) {
            inventoryCache.setInventory(inventory.addToInventory(currItem.getDbName()).spendTokens(currItem.getValue()));
            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(Color.ORANGE)
                    .setTitle(String.format("Congratulations on your purchase of %s, %s!", currItem.getItemName(), Objects.requireNonNull(event.getUser()).getName()))
                    .setDescription("Enjoy your new thingie~");
            if (!currItem.getUrl().isBlank()) builder.setThumbnail(currItem.getUrl());
            try {
                message.editMessage(builder.build()).queue();
                message.clearReactions().queue();
            } catch (PermissionException ignored) {
            }
        }
    }


    private void loadItems() {

        JSONObject jsonObj = storeItems.getItemInventory();

        jsonObj.keySet().forEach(keyStr -> {
            JSONObject item = jsonObj.getJSONObject(keyStr);
            int value = item.getInt("price");
            //Items with value 0 are not for sale.
            if (value > 0) {
                availableItems.add(new ShopItem(keyStr, item.getString("name"), item.getString("pic_url"), value, ShopItem.ItemType.ITEM));
            }
        });
    }


}
