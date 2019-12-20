package com.kuborros.FurBotNeo.commands.AdminCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;


@CommandInfo(
        name = "Update",
        description = "Update and restart the bot"
)
@Author("Kuborros")
public class UpdateCommand extends AdminCommand {

    public UpdateCommand() {
        this.name = "update";
        this.help = "Updates the bot (if applicable) and restarts it.";
        this.guildOnly = true;
        this.ownerCommand = true;
        this.category = new Category("Moderation");
    }

    @Override
    protected void doCommand(CommandEvent event) {

    }
}
