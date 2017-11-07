
package com.kuborros.FurBotNeo.listeners;


import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kuborros
 */
public class LogListener extends ListenerAdapter{
    
static final Logger LOG = LoggerFactory.getLogger("ChatLogs");
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {          
        User author = event.getAuthor();               
        Message message = event.getMessage();           
        String msg = message.getContent();       
        if (msg.isEmpty()) return;
        
        if (event.isFromType(ChannelType.TEXT))
        {
            TextChannel textChannel = event.getTextChannel();
            Member member = event.getMember();

            String name;
            if (message.isWebhookMessage()) name = author.getName();               
            else name = member.getEffectiveName();     

            String msgr ='[' + textChannel.getName() + "] "+ '(' + name + ')' + ": " + msg;
            LOG.info(msgr);
        }
        else if (event.isFromType(ChannelType.PRIVATE))
        {
            String msgr ="[PRIV] (" + author.getName() + "): " + msg;
            LOG.info(msgr);
        }
    }
}

