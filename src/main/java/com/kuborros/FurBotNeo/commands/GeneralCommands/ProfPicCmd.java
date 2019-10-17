
package com.kuborros.FurBotNeo.commands.GeneralCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

@CommandInfo(
        name = "ProfPic",
        description = "Gets profile picture of mentioned user from discord servers, and provides reverse image search option."
)
@Author("Kuborros")
public class ProfPicCmd extends GeneralCommand {
    public ProfPicCmd()
    {
        this.name = "profpic";
        this.help = "Shows profile pic of mentioned user!";
        this.arguments = "@user";
        this.guildOnly = true;        
        this.category = new Command.Category("Basic"); 
}
    @Override
    public void doCommand(CommandEvent event) {
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
