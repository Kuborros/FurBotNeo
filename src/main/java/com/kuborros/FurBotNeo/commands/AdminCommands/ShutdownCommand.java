package com.kuborros.FurBotNeo.commands.AdminCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.Permission;

import static com.kuborros.FurBotNeo.BotMain.randomResponse;

@CommandInfo(
        name = "Shutdown",
        description = "Performs graceful shutdown of the bot."
)
@Author("Kuborros")
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
        event.reply(randomResponse.getRandomShutdownMessage());

        if (event.getJDA().getShardManager() != null) {
            event.getJDA().getShardManager().shutdown();
        } else event.getJDA().shutdown();

        LOG.info("Bot shutting down");
        System.exit(0);
    }
    
}