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
