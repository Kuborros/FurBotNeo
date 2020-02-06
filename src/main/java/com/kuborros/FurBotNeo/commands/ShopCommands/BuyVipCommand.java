package com.kuborros.FurBotNeo.commands.ShopCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;

import java.awt.*;
import java.util.concurrent.TimeUnit;

import static com.kuborros.FurBotNeo.BotMain.inventoryCache;

public class BuyVipCommand extends ShopCommand {

    final EventWaiter waiter;
    String authorId;
    int vipCost;

    public BuyVipCommand(EventWaiter waiter) {
        this.name = "vip";
        this.help = "Purchase VIP status here!";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_MANAGE};
        this.category = new Command.Category("Shop");
        this.waiter = waiter;
    }

    @Override
    protected void doCommand(CommandEvent event) {
        boolean vip = inventory.isVIP();
        vipCost = 10000; //Make it reasonable before release
        String desc;
        if (inventory.getBalance() > vipCost) {
            desc = String.format("You are not a VIP yet! \n You can however, afford becoming one for %d tokens~ \n Would you like to buy it?", vipCost);
        } else if (vip) {
            desc = "You are a VIP already! \n Congratulations!";
        } else {
            desc = String.format("You are not a VIP yet! \n It costs %d to become a VIP!", vipCost);
        }
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle(String.format("Vip status for: %s", event.getMember().getEffectiveName()))
                .setColor(Color.ORANGE)
                .setThumbnail(event.getAuthor().getEffectiveAvatarUrl())
                .setDescription(desc);

        authorId = event.getAuthor().getId();
        awaitResponse(event.getTextChannel().sendMessage(builder.build()).complete());
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
                return (event.getMember()).getId().equals(authorId);
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
            inventoryCache.setInventory(inventory.setVIP(true).spendTokens(vipCost));
            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(Color.ORANGE)
                    .setTitle(String.format("Congratulations on becoming VIP, %s!", event.getUser().getName()))
                    .setDescription("Enjoy your new benefits~")
                    .setThumbnail(event.getUser().getEffectiveAvatarUrl());
            try {
                message.editMessage(builder.build()).queue();
                message.clearReactions().queue();
            } catch (PermissionException ignored) {
            }
        }
    }
}
