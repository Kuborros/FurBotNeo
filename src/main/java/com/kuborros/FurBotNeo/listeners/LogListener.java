
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
        if (msg.isEmpty()) return;

        if (event.isFromType(ChannelType.TEXT)) {
            TextChannel textChannel = event.getTextChannel();
            Member member = event.getMember();

            String name;
            if (message.isWebhookMessage()) name = author.getName();
            else name = member != null ? member.getEffectiveName() : "Name Unavailable";

            LOG.info("[{}] " + "({}): {}", textChannel.getName(), name, msg);
        } else if (event.isFromType(ChannelType.PRIVATE)) {
            LOG.info("[PRIV] ({}): {}", author.getName(), msg);
        } else if (event.isFromType(ChannelType.GROUP)) {
            LOG.info("[GRP] ({}): {}", author.getName(), msg);
        }
    }
}

