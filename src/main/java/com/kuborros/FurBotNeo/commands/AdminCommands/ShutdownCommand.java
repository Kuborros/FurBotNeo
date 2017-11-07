package com.kuborros.FurBotNeo.commands.AdminCommands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import net.dv8tion.jda.core.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ShutdownCommand extends Command {

    public ShutdownCommand()
    {
        this.name = "shutdown";
        this.help = "Safely shuts off the bot";
        this.guildOnly = false;
        this.ownerCommand = true;
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.KICK_MEMBERS};        
    }

    @Override
    protected void execute(CommandEvent event) {
        Logger LOG = LoggerFactory.getLogger("CommandExec");
        event.reply("If you say so... \n" + event.getSelfMember().getEffectiveName() + " shutting down!");
        event.getJDA().shutdown();
        LOG.info("Bot shutting down");
        System.exit(0);
    }
    
}