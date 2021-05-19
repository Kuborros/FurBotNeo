
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

import java.util.concurrent.atomic.AtomicReference;


public class LogListener extends ListenerAdapter{

    private static final Logger LOG = LoggerFactory.getLogger("ChatLogs");

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        Message message = event.getMessage();
        AtomicReference<String> msg = new AtomicReference<>(message.getContentStripped());
        if (!message.getEmbeds().isEmpty()) {
            message.getEmbeds().forEach( messageEmbed -> {
                if (!messageEmbed.isEmpty()) {
                    msg.set(msg.get() + messageEmbed.getUrl());
                }
            });

        }
        if (!message.getAttachments().isEmpty()) {
            message.getAttachments().forEach(attachment -> msg.set(String.format("%s \n Included attachment of type %s, with url %s",msg.get(),attachment.getContentType(),attachment.getUrl())));
        }

        if (msg.get().isEmpty() || event.getAuthor().isBot()) return;

        if (event.isFromType(ChannelType.TEXT)) {
            TextChannel textChannel = event.getTextChannel();
            Member member = event.getMember();

            String name;
            if (message.isWebhookMessage()) name = author.getName();
            else name = member != null ? member.getEffectiveName() : "Name Unavailable";

            LOG.info("[{}] ({}): {}", textChannel.getName(), name, msg.get());
        } else if (event.isFromType(ChannelType.PRIVATE)) {
            LOG.info("[PRIV] ({}): {}", author.getName(), msg.get());
        } else if (event.isFromType(ChannelType.GROUP)) {
            LOG.info("[GRP] ({}): {}", author.getName(), msg.get());
        }
    }
}

