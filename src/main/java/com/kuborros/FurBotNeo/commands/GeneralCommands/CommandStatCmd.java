/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.GeneralCommands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import static com.kuborros.FurBotNeo.BotMain.db;
import com.kuborros.FurBotNeo.utils.msg.EmbedSender;
import java.awt.Color;
import java.sql.SQLException;
import java.util.Map;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.User;

/**
 *
 * @author Kuborros
 */
public class CommandStatCmd extends Command{
    
    private Permission[] perms = {Permission.MESSAGE_EMBED_LINKS};    
    
    public CommandStatCmd()
    {
        this.name = "picstat";
        this.help = "Tells you how perverted someone is!";
        this.arguments = "@user";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Command.Category("Basic");
        this.botPermissions = perms;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    protected void execute(CommandEvent event){
        StringBuilder builder = new StringBuilder();
        EmbedSender emb = new EmbedSender(event);
        User user = !event.getMessage().getMentionedUsers().isEmpty() ? event.getMessage().getMentionedUsers().get(0) : event.getAuthor();
        try {
            Map map = db.getCommandStats(user.getId());
            builder.append("\n");
            map.forEach((k,v) -> builder.append("``").append(k).append("`` used **").append(v).append("** times\n\n"));
            emb.sendEmbed("How many times " + user.getName() + " nutted to:", builder.toString(), Color.yellow);
        } catch (SQLException e){
            event.replyError("Something went wrong"); 
        }
    
}    
}
