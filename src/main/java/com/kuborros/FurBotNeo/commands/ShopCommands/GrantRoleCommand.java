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
        name = "GrantRole",
        description = "Gives user a role."
)
@Author("Kuborros")
public class GrantRoleCommand extends ShopCommand {

    static final ArrayList<ShopItem> availableRoles = new ArrayList<>();
    final EventWaiter waiter;
    ShopItem currRole;
    Member author, target;
    MemberInventory targetInventory;
    StoreDialog storeDialog;
    PrivateChannel priv;

    public GrantRoleCommand(EventWaiter waiter) {
        this.name = "grantrole";
        this.help = "Gives you role with specified id";
        this.arguments = "<role>";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_MANAGE};
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Category("Shop");
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
        this.waiter = waiter;
        loadRoles();
    }

    @Override
    protected void doCommand(CommandEvent event) {

        if (event.getMessage().getMentionedMembers().isEmpty()) {
            event.replyWarning("You need to mention user you want to grant role to.");
            return;
        } else target = event.getMessage().getMentionedMembers().get(0);

        if (target.getUser().isBot()) {
            event.replyWarning("Bots have no colored roles, silly!");
            return;
        }


        author = event.getMember();
        targetInventory = inventoryCache.getInventory(target.getId(), guild.getId());

        StoreDialog.Builder builder = new StoreDialog.Builder();

        builder.setUsers(event.getAuthor())
                .setColor(Color.ORANGE)
                .setItemsPerPage(6)
                .useNumberedItems(true)
                .setEventWaiter(waiter)
                .showPageNumbers(true)
                .setDefaultEnds("", "")
                .setSelectedEnds("**", "**")
                .setSelectionConsumer(this::buyItem)
                .setText(String.format("**Roles available for %s:**", event.getMember().getEffectiveName()))
                .setCanceled(message -> message.clearReactions().queue())
                .setTimeout(5, TimeUnit.MINUTES);

        builder.addChoices(availableRoles);
        priv = event.getAuthor().openPrivateChannel().complete();
        priv.sendMessage("Due to recent discord limitations, i cannot *remove* reactions, so you will need to do it yourself... sorry uwu").complete();
        storeDialog.display(priv);

    }

    private void buyItem(Message message, int selection) {

        storeDialog.setNoUpdate(true);

        EmbedBuilder builder = new EmbedBuilder();
        currRole = availableRoles.get(selection - 1);
        boolean canBuy;
        if (targetInventory.getOwnedRoles().contains(currRole.getDbName())) {
            builder.setTitle(String.format("Granting role: %s", currRole.getItemName()))
                    .setDescription("Wait a second... They already own it!")
                    .setColor(currRole.getColor());
            canBuy = false;
        } else {
            builder.setTitle(String.format("Granting role: %s", currRole.getItemName()))
                    .setDescription("Would you like to grant this role?")
                    .setColor(currRole.getColor());
            canBuy = true;
        }
        message.delete().queue();
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
            inventoryCache.setInventory(targetInventory.addToRoles(currRole.getDbName()));
            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(currRole.getColor())
                    .setTitle(String.format("Role %s has been granted to %s!", currRole.getItemName(), Objects.requireNonNull(target).getEffectiveName()))
                    .setDescription("Hopefully they like it~");
            try {
                message.editMessage(builder.build()).queue();
            } catch (PermissionException ignored) {
            }
        }
    }

    private void loadRoles() {
        JSONObject jsonObj = storeItems.getRoleInventory();
        jsonObj.keySet().forEach(keyStr -> {
            JSONObject item = jsonObj.getJSONObject(keyStr);
            availableRoles.add(new ShopItem(keyStr, item.getString("name"), item.getString("role_color"), item.getInt("price"), ShopItem.ItemType.ROLE));
        });
    }
}
