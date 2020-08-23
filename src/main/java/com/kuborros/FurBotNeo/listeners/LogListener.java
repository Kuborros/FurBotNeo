
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


import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogListener extends ListenerAdapter{

    private static final Logger LOG = LoggerFactory.getLogger("ChatLogs");

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        Message message = event.getMessage();
        String msg = message.getContentStripped();
        if (msg.isEmpty() || event.getAuthor().isBot()) return;

        if (event.isFromType(ChannelType.TEXT)) {
            TextChannel textChannel = event.getTextChannel();
            Member member = event.getMember();

            String name;
            if (message.isWebhookMessage()) name = author.getName();
            else name = member != null ? member.getEffectiveName() : "Name Unavailable";

            LOG.info("[{}] ({}): {}", textChannel.getName(), name, msg);
        } else if (event.isFromType(ChannelType.PRIVATE)) {
            LOG.info("[PRIV] ({}): {}", author.getName(), msg);
        } else if (event.isFromType(ChannelType.GROUP)) {
            LOG.info("[GRP] ({}): {}", author.getName(), msg);
        }
    }
}

