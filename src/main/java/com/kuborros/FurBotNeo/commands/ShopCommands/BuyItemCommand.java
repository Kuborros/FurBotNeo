package com.kuborros.FurBotNeo.commands.ShopCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.kuborros.FurBotNeo.utils.store.ShopItem;
import com.kuborros.FurBotNeo.utils.store.StoreDialog;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import org.json.JSONObject;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class BuyItemCommand extends ShopCommand {

    static ArrayList<ShopItem> availableItems = new ArrayList<>();
    EventWaiter waiter;

    public BuyItemCommand(EventWaiter waiter) {
        this.name = "item";
        this.help = "Purchase items here!";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.category = new Command.Category("Shop");
        this.waiter = waiter;
        loadItems();
    }

    @Override
    protected void doCommand(CommandEvent event) {

        StoreDialog.Builder builder = new StoreDialog.Builder();

        builder.setUsers(event.getAuthor())
                .setColor(Color.ORANGE)
                .setItemsPerPage(10)
                .useNumberedItems(true)
                .setEventWaiter(waiter)
                .setDefaultEnds("", "")
                .setSelectedEnds("**", "**")
                .setSelectionConsumer(this::buyItem)
                .setText(String.format("**Contents of %s 's inventory:**", event.getMember().getEffectiveName()))
                .setCanceled(message -> message.clearReactions().queue())
                .setTimeout(5, TimeUnit.MINUTES);

        builder.addChoices(availableItems);

        builder.build().display(event.getTextChannel());

    }

    private void buyItem(Message message, int selection) {


    }


    private void loadItems() {
        String json = "";

        try {
            json = new String(getClass().getClassLoader().getResourceAsStream("items.json").readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOG.error("Things went wrong while loading internal resource: ", e);
        }
        JSONObject jsonObj = new JSONObject(json).getJSONObject("items");

        jsonObj.keySet().forEach(keyStr -> {
            JSONObject item = jsonObj.getJSONObject(keyStr);
            int value = item.getInt("price");
            if (value > 0) {
                availableItems.add(new ShopItem(keyStr, item.getString("name"), item.getString("pic_url"), value));
            }
        });
    }


}
