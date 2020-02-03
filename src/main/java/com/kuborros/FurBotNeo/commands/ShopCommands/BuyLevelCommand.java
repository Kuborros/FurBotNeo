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

public class BuyLevelCommand extends ShopCommand {

    static final String OKAY = "\u2705";
    static final String NO = "\u274C";
    EventWaiter waiter;
    String authorId;
    int levelcost, level;

    public BuyLevelCommand(EventWaiter waiter) {
        this.name = "level";
        this.help = "Check and buy levels!";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.category = new Command.Category("Shop");
        this.waiter = waiter;
    }

    @Override
    protected void doCommand(CommandEvent event) {
        level = inventory.getLevel();
        levelcost = levelCost(level);
        String desc;
        if (inventory.getBalance() > levelcost) {
            desc = String.format("Your current level is %d! \n You can afford a level up for %d tokens~ \n Would you like to level up now?", level, levelcost);
        } else {
            desc = String.format("Your current level is %d! \n A level up would cost you %d tokens.", level, levelcost);
        }
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle(String.format("Level info for: %s", event.getMember().getEffectiveName()))
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
            inventoryCache.setInventory(inventory.spendTokens(levelcost).addLevel());
            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(Color.ORANGE)
                    .setTitle(String.format("Welcome to level %d, %s!", level, event.getUser().getName()))
                    .setDescription(String.format("Your next level-up will cost you %d tokens!", levelCost(level++)))
                    .setThumbnail(event.getUser().getEffectiveAvatarUrl());
            try {
                message.editMessage(builder.build()).queue();
                message.clearReactions().queue();
            } catch (PermissionException ignored) {
            }
        }
    }


    private int levelCost(int level) {
        if (level == 0) {
            return 1;
        } else if (level < 10) {
            return (level * 5);
        } else if (level < 100) {
            return (level * 10);
        } else if (level < 1000) {
            return (level * 15);
        } else {
            return (level * 20);
        }
    }
}
