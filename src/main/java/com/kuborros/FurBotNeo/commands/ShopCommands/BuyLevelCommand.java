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
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;

import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.kuborros.FurBotNeo.BotMain.inventoryCache;
import static com.kuborros.FurBotNeo.BotMain.storeItems;

@SuppressWarnings("ConstantConditions")
public class BuyLevelCommand extends ShopCommand {

    final EventWaiter waiter;
    String authorId;
    int levelcost, level;
    static boolean badgesOk;


    public BuyLevelCommand(EventWaiter waiter) {
        this.name = "level";
        this.help = "Check and buy levels!";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.category = new Command.Category("Shop");
        this.waiter = waiter;
        badgesOk = doLevelBadgesExist();
    }

    @Override
    protected void doCommand(CommandEvent event) {
        level = inventory.getLevel();
        levelcost = levelCost(level);
        boolean canBuy;
        String desc;
        if (inventory.getBalance() > levelcost) {
            desc = String.format("Your current level is %d! \n You can afford a level up for %d tokens~ \n Would you like to level up now?", level, levelcost);
            canBuy = true;
        } else {
            desc = String.format("Your current level is %d! \n A level up would cost you %d tokens.", level, levelcost);
            canBuy = false;
        }
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle(String.format("Level info for: %s", event.getMember().getEffectiveName()))
                .setColor(Color.ORANGE)
                .setThumbnail(event.getAuthor().getEffectiveAvatarUrl())
                .setDescription(desc);

        authorId = event.getAuthor().getId();
        if (canBuy) awaitResponse(event.getTextChannel().sendMessage(builder.build()).complete());
        else event.getMessage().clearReactions().complete();
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
                    .setTitle(String.format("Welcome to level %d, %s!", ++level, event.getUser().getName()))
                    .setDescription(String.format("Your next level-up will cost you %d tokens!", levelCost(level))) //Note that level var gets increased here!
                    .setThumbnail(event.getUser().getEffectiveAvatarUrl());
            //Leveling rewards
            if (badgesOk) {
                switch (level) {
                    case 10:
                        inventoryCache.setInventory(inventory.addToInventory("lvl10"));
                        builder.addField("You got a loot drop!", "Level 10 badge", true);
                        break;
                    case 25:
                        inventoryCache.setInventory(inventory.addToInventory("lvl25"));
                        builder.addField("You got a loot drop!", "Level 25 badge", true);
                        break;
                    case 50:
                        inventoryCache.setInventory(inventory.addToInventory("lvl50"));
                        builder.addField("You got a loot drop!", "Level 50 badge", true);
                        break;
                    case 100:
                        inventoryCache.setInventory(inventory.addToInventory("lvl100"));
                        builder.addField("You got a loot drop!", "Level 100 badge", true);
                        break;
                    case 250:
                        inventoryCache.setInventory(inventory.addToInventory("lvl250"));
                        builder.addField("You got a loot drop!", "Level 250 badge", true);
                        break;
                    case 500:
                        inventoryCache.setInventory(inventory.addToInventory("lvl500"));
                        builder.addField("You got a loot drop!", "Level 500 badge", true);
                        break;
                    case 1000:
                        inventoryCache.setInventory(inventory.addToInventory("lvl1000"));
                        builder.addField("You got a loot drop!", "Level 1000 badge", true);
                        break;
                    default:
                        break;
                }
            }

            try {
                message.editMessage(builder.build()).queue();
                message.clearReactions().queue();
            } catch (PermissionException ignored) {
            }
        }
    }

    //If these are not present we do not give out the items
    private boolean doLevelBadgesExist() {
        String[] badges = {"lvl10", "lvl25", "lvl50", "lvl100", "lvl250", "lvl500", "lvl1000"};
        return storeItems.getItemInventory().keySet().containsAll(Arrays.asList(badges));
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
