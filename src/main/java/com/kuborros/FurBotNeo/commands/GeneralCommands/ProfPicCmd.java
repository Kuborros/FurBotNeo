/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.GeneralCommands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import java.awt.Color;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

/**
 *
 * @author Kuborros
 */
public class ProfPicCmd extends Command{
    public ProfPicCmd()
    {
        this.name = "profpic";
        this.help = "Shows profile pic of mentioned user!";
        this.arguments = "@user";
        this.guildOnly = true;        
        this.category = new Command.Category("Basic"); 
}
    @Override
    public void execute(CommandEvent event){  
            Message message = event.getMessage();   
            if (message.getMentionedUsers().isEmpty()) return;
            User user = message.getMentionedUsers().get(0);
            
            event.getTextChannel().sendMessage(
                new EmbedBuilder().setColor(Color.ORANGE)
                                  .setTitle("Profile picture of: " + user.getName())
                                  .setThumbnail(user.getAvatarUrl())
                                  .setDescription("[Click **here** to view it in full size!](" + user.getAvatarUrl() + ")" + "\n"
                                                + "[Click **here** to reverse google search it!](https://images.google.com/searchbyimage?image_url=" + user.getAvatarUrl() + ")" + "\n")
                                  .build()).queue();    
    }  
}
