/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.GeneralCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;
import java.sql.SQLException;
import java.util.Map;

import static com.kuborros.FurBotNeo.BotMain.db;

/**
 *
 * @author Kuborros
 */
public class CommandStatCmd extends Command{

    public CommandStatCmd()
    {
        this.name = "picstat";
        this.help = "Tells you how perverted someone is!";
        this.arguments = "@user";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Command.Category("Basic");
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
    }
    
    @Override
    @SuppressWarnings("unchecked")
    protected void execute(CommandEvent event){
        StringBuilder builder = new StringBuilder();
        User user = !event.getMessage().getMentionedUsers().isEmpty() ? event.getMessage().getMentionedUsers().get(0) : event.getAuthor();
        try {
            Map map = db.getCommandStats(user.getId());
            builder.append("\n");
            map.forEach((k,v) -> builder.append("``").append(k).append("`` used **").append(v).append("** times\n\n"));
            sendEmbed(event, String.format("How many times %s nutted to:", user.getName()), builder.toString());
        } catch (SQLException e){
            event.replyError("Something went wrong"); 
        }

    }

    private void sendEmbed(CommandEvent event, String title, String description) {
        event.getChannel().sendMessage(
                new MessageBuilder().setEmbed(
                        new EmbedBuilder().setTitle(title, null).setDescription(description).setColor(Color.yellow).build()
                ).build()).queue();
    }
}
