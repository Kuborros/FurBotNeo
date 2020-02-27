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
                new BuyItemCommand(waiter), new BuyRoleCommand(waiter), new BuyVipCommand(waiter),
                new BuyLevelCommand(waiter), new FullInventoryCommand(waiter), new FullRolesCommand(waiter)
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
                    .addField("Your latest items are: ", getPrettyInventoryItems(), false)
                    .addField("Your latest roles are: ", getPrettyInventoryRoles(), false);

            event.reply(builder.build());
        }
    }

    private String getPrettyInventoryItems() {
        List<String> items = inventory.getOwnedItems();
        StringBuilder inv = new StringBuilder();

        if (items.isEmpty()) return "None";

        if (items.size() > 4) {
            items = items.subList(0, 4);
        }

        JSONObject iNames = storeItems.getItemInventory();

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