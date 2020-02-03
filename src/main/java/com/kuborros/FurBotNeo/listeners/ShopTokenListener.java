package com.kuborros.FurBotNeo.listeners;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static com.kuborros.FurBotNeo.BotMain.inventoryCache;

public class ShopTokenListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        String msg = event.getMessage().getContentStripped();
        if (msg.isEmpty() || event.getAuthor().isBot()) return;

        //Very basic awarding of 1 token per msg. To be expanded.
        if (event.isFromType(ChannelType.TEXT)) {
            inventoryCache.setInventory(inventoryCache.getInventory(author.getId(), event.getGuild().getId()).addTokens(1));
        }

    }
}
