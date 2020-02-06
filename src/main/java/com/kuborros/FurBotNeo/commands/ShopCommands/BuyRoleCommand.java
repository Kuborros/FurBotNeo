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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.kuborros.FurBotNeo.BotMain.inventoryCache;

@CommandInfo(
        name = "Role",
        description = "Purchase special roles here."
)
@Author("Kuborros")
public class BuyRoleCommand extends ShopCommand {

    static final ArrayList<ShopItem> availableRoles = new ArrayList<>();
    final EventWaiter waiter;
    ShopItem currRole;
    Member author;
    StoreDialog storeDialog;

    public BuyRoleCommand(EventWaiter waiter) {
        this.name = "role";
        this.help = "Purchase special roles here!";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_MANAGE};
        this.category = new Command.Category("Shop");
        this.waiter = waiter;
        loadRoles();
    }

    @Override
    protected void doCommand(CommandEvent event) {

        author = event.getMember();

        StoreDialog.Builder builder = new StoreDialog.Builder();

        builder.setUsers(event.getAuthor())
                .setColor(Color.ORANGE)
                .setItemsPerPage(10)
                .useNumberedItems(true)
                .setEventWaiter(waiter)
                .showPageNumbers(true)
                .setDefaultEnds("", "")
                .setSelectedEnds("**", "**")
                .setSelectionConsumer(this::buyItem)
                .setText(String.format("**Roles for sale avaible for %s:**", event.getMember().getEffectiveName()))
                .setCanceled(message -> message.clearReactions().queue())
                .setTimeout(5, TimeUnit.MINUTES);

        builder.addChoices(availableRoles);
        storeDialog = builder.build();
        storeDialog.display(event.getTextChannel());

    }

    private void buyItem(Message message, int selection) {

        storeDialog.setNoUpdate(true);

        EmbedBuilder builder = new EmbedBuilder();
        currRole = availableRoles.get(selection - 1);
        int value = currRole.getValue();
        int balance = inventory.getBalance();
        boolean canBuy;
        if (value > balance) {
            builder.setTitle(String.format("Buying Role: %s", currRole.getItemName()))
                    .setDescription(String.format("It seems you cannot afford it! \n It would cost you %d tokens to purchase this role.", currRole.getValue()))
                    .setColor(currRole.getColor());
            canBuy = false;
        } else if (inventory.getOwnedItems().contains(currRole.getDbName())) {
            builder.setTitle(String.format("Buying role: %s", currRole.getItemName()))
                    .setDescription("Wait a second... You already own it!")
                    .setColor(currRole.getColor());
            canBuy = false;
        } else {
            builder.setTitle(String.format("Buying role: %s", currRole.getItemName()))
                    .setDescription(String.format("Would you like to buy it for %d tokens?", currRole.getValue()))
                    .setColor(currRole.getColor());
            canBuy = true;
        }
        message.editMessage(new MessageBuilder().setEmbed(builder.build()).setContent("").build()).queue();
        if (canBuy) awaitResponse(message);
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
            inventoryCache.setInventory(inventory.addToInventory(currRole.getDbName()).spendTokens(currRole.getValue()));
            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(Color.ORANGE)
                    .setTitle(String.format("Congratulations on your purchase of %s, %s!", currRole.getItemName(), Objects.requireNonNull(event.getUser()).getName()))
                    .setDescription("Enjoy your new color~")
                    .setThumbnail(currRole.getUrl());
            try {
                message.editMessage(builder.build()).queue();
                message.clearReactions().queue();
            } catch (PermissionException ignored) {
            }
        }
    }

    private void loadRoles() {
        String json = "";

        try {
            json = new String(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("items.json")).readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOG.error("Things went wrong while loading internal resource: ", e);
        }
        JSONObject jsonObj = new JSONObject(json).getJSONObject("roles");

        jsonObj.keySet().forEach(keyStr -> {
            JSONObject item = jsonObj.getJSONObject(keyStr);
            int value = item.getInt("price");
            //Items with value 0 are not for sale.
            if (value > 0) {
                availableRoles.add(new ShopItem(keyStr, item.getString("name"), item.getString("role_color"), value, ShopItem.ItemType.ROLE));
            }
        });
    }
}
