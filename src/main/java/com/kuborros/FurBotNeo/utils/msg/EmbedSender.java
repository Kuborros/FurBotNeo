


package com.kuborros.FurBotNeo.utils.msg;

import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;

/**
 *
 * @author Kuborros
 */
public class EmbedSender {
    
    private final CommandEvent event;
    
    public EmbedSender(CommandEvent event){
        this.event = event;
    }
    
       public void sendEmbed(String title, String description) {
                buildMessage(new EmbedBuilder().setTitle(title, null).setDescription(description).build(), event.getChannel());
        }
       public void sendEmbed(String title, String description, String imgUrl) {
                buildMessage(new EmbedBuilder().setTitle(title, null).setDescription(description).setThumbnail(imgUrl).build(), event.getChannel());
        }
       public void sendEmbed(String title, String description, String imgUrl, String footer) {
                buildMessage(new EmbedBuilder().setTitle(title, null).setThumbnail(imgUrl).setDescription(description).setFooter(footer, null).build(), event.getChannel());
        }
       public void sendEmbed(String title, String description, Color color) {
                buildMessage(new EmbedBuilder().setTitle(title, null).setDescription(description).setColor(color).build(), event.getChannel());
        }
       public void sendEmbed(String title, String description, String imgUrl, Color color) {
                buildMessage(new EmbedBuilder().setTitle(title, null).setDescription(description).setColor(color).setThumbnail(imgUrl).build(), event.getChannel());
        }
       public void sendEmbed(String title, String description, String imgUrl, String footer, Color color) {
                buildMessage(new EmbedBuilder().setTitle(title, null).setThumbnail(imgUrl).setDescription(description).setColor(color).setFooter(footer, null).build(), event.getChannel());
        }
        public void sendPicEmbed(String title, String imgUrl, Color color) {
                try {
                    buildMessage(new EmbedBuilder().setTitle(title, null).setImage(imgUrl).setColor(color).build(), event.getChannel());
                } catch (IllegalArgumentException e) {
                    sendErrorMessage();
                }
        }

        public void sendPicEmbed(String title, String imgUrl, String footer ,Color color) {
                try {
                    buildMessage(new EmbedBuilder().setTitle(title, null).setImage(imgUrl).setColor(color).setFooter(footer, null).build(), event.getChannel());
                } catch (IllegalArgumentException e) {
                    sendErrorMessage();
                }
        }
                   
        private void buildMessage(MessageEmbed embed, MessageChannel channel) {
            sendMessage(new MessageBuilder().setEmbed(embed).build(), channel);  
        }
        
        private void sendMessage(Message message, MessageChannel channel) {
            channel.sendMessage(message).queue();
        }

    private void sendErrorMessage() {
            MessageChannel channel = event.getChannel();
        channel.sendMessage("Invalid input or something went horribly wrong.").queue();
        }
}
    
