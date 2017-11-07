
package com.kuborros.FurBotNeo.utils.msg;

import java.util.List;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;

/**
 *
 * @author Kuborros
 */
public class ChannelFinder {
    
    private Guild guild;
    
    public ChannelFinder(Guild guild) {
        this.guild = guild;
    }
    
    public TextChannel FindBotChat() {
        List<TextChannel> channels = guild.getTextChannels();
        for(TextChannel channel : channels){
            if (channel.getName().contains("bot"))
                return channel;
        }
        return null;
    }
    
    public VoiceChannel FindMusicChannel() {
       List<VoiceChannel> channels = guild.getVoiceChannels();
       for (VoiceChannel channel : channels) {
           if (channel.getName().contains("music"))
               return channel;
       }
       return null;
    }
     
    public boolean HasBotChat() {
        List<TextChannel> channels = guild.getTextChannels();
        return channels.stream().anyMatch((channel) -> (channel.getName().contains("bot")));
    }
    
    public boolean HasMusicChannel() {
       List<VoiceChannel> channels = guild.getVoiceChannels();
       return channels.stream().anyMatch((channel) -> (channel.getName().contains("music")));
    }
     
}
