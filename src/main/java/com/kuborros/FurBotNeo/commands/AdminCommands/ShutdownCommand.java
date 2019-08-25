package com.kuborros.FurBotNeo.commands.AdminCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;


public class ShutdownCommand extends AdminCommand {

    public ShutdownCommand()
    {
        this.name = "shutdown";
        this.help = "Safely shuts off the bot";
        this.guildOnly = false;
        this.ownerCommand = true;
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.ADMINISTRATOR};
        this.hidden = true;
    }

    @Override
    protected void doCommand(CommandEvent event) {
        event.reply("If you say so... Commencing shut down!");
        event.getJDA().shutdown();
        LOG.info("Bot shutting down");
        System.exit(0);
    }
    
}