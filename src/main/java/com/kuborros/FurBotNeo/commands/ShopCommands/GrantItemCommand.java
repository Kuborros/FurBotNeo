package com.kuborros.FurBotNeo.commands.ShopCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.kuborros.FurBotNeo.utils.menus.StoreDialog;
import com.kuborros.FurBotNeo.utils.store.MemberInventory;
import com.kuborros.FurBotNeo.utils.store.ShopItem;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
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
        name = "GiveItem",
        description = "Gives user item with provided id (It is not validated against items.json!)!"
)
@Author("Kuborros")
public class GrantItemCommand extends ShopCommand {

    static final ArrayList<ShopItem> availableItems = new ArrayList<>();
    final EventWaiter waiter;
    ShopItem currItem;
    Member author, target;
    StoreDialog storeDialog;
    MemberInventory targetInventory;
    PrivateChannel priv;

    public GrantItemCommand(EventWaiter waiter) {
        this.name = "grantitem";
        this.help = "Gives mentioned user item selected in menu";
        this.arguments = "<@member>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Category("Shop");
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_MANAGE};
        this.waiter = waiter;
        loadItems();
    }

    @Override
    protected void doCommand(CommandEvent event) {

        if (event.getMessage().getMentionedMembers().isEmpty()) {
            event.replyWarning("You need to mention user you want to grant item to.");
            return;
        } else target = event.getMessage().getMentionedMembers().get(0);

        author = event.getMember();
        targetInventory = inventoryCache.getInventory(target.getId(), guild.getId());

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
                .setText(String.format("**Items  available for %s:**", target.getEffectiveName()))
                .setCanceled(message -> message.clearReactions().queue())
                .setTimeout(5, TimeUnit.MINUTES);

        builder.addChoices(availableItems);
        storeDialog = builder.build();
        priv = event.getAuthor().openPrivateChannel().complete();
        priv.sendMessage("Due to recent discord limitations, i cannot *remove* reactions, so i will need to spam you a bit... sorry uwu").complete();
        storeDialog.display(priv);

    }

    private void buyItem(Message message, int selection) {

        storeDialog.setNoUpdate(true);

        EmbedBuilder builder = new EmbedBuilder();
        currItem = availableItems.get(selection - 1);
        String preview = currItem.getUrl();
        boolean noPreview = preview.isBlank();


        boolean canBuy;
        if (targetInventory.getOwnedItems().contains(currItem.getDbName())) {
            builder.setTitle(String.format("Buying item: %s", currItem.getItemName()))
                    .setDescription("Wait a second... They already own it!");
            if (!noPreview) builder.setThumbnail(currItem.getUrl());
            canBuy = false;
        } else {
            builder.setTitle(String.format("Buying item: %s", currItem.getItemName()))
                    .setDescription("Would you like to grant it?");
            if (!noPreview) builder.setThumbnail(currItem.getUrl());
            canBuy = true;
        }
        message.delete().complete();
        message = priv.sendMessage(new MessageBuilder().setEmbed(builder.build()).setContent("").build()).complete();
        if (canBuy) awaitResponse(message);
    }

    private void awaitResponse(Message message) {

        message.addReaction(OKAY).complete();
        message.addReaction(NO).complete();

        waiter.waitForEvent(MessageReactionAddEvent.class,
                event -> checkReaction(event, message),
                event -> handleMessageReactionAddAction(event, message),
                5, TimeUnit.MINUTES, () -> message.clearReactions().queue());
    }

    private boolean checkReaction(MessageReactionAddEvent event, Message message) {
        if (event.getMessageIdLong() != message.getIdLong())
            return false;
        switch (event.getReactionEmote().getName()) {
            case OKAY:
            case NO:
                return !Objects.requireNonNull(event.getUser()).isBot();
            default:
                return false;
        }
    }

    private void handleMessageReactionAddAction(MessageReactionAddEvent event, Message message) {

        if (event.getReaction().getReactionEmote().getName().equals(NO)) {
            message.delete().queue();
            return;
        }
        if (event.getReaction().getReactionEmote().getName().equals(OKAY)) {
            inventoryCache.setInventory(targetInventory.addToInventory(currItem.getDbName()));
            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(Color.ORANGE)
                    .setTitle(String.format("%s has been granted to %s!", currItem.getItemName(), Objects.requireNonNull(target.getEffectiveName())))
                    .setDescription("Hope they like that new thingie~");
            if (!currItem.getUrl().isBlank()) builder.setThumbnail(currItem.getUrl());
            try {
                message.editMessage(builder.build()).queue();
            } catch (PermissionException ignored) {
            }
        }
    }


    private void loadItems() {

        JSONObject jsonObj = storeItems.getItemInventory();

        jsonObj.keySet().forEach(keyStr -> {
            JSONObject item = jsonObj.getJSONObject(keyStr);
            int value = item.getInt("price");
            availableItems.add(new ShopItem(keyStr, item.getString("name"), item.getString("pic_url"), value, ShopItem.ItemType.ITEM));
        });
    }
}