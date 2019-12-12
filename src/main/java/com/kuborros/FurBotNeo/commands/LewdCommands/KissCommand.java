package com.kuborros.FurBotNeo.commands.LewdCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;

@CommandInfo(
        name = "Kiss",
        description = "Allows you to kiss someone!"
)
@Author("Kuborros")
public class KissCommand extends LewdCommand {

    public KissCommand() {
        this.name = "kiss";
        this.help = "Allows you to kiss someone!";
        this.arguments = "<@user>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Category("Lewd");
    }

    @Override
    protected void doCommand(CommandEvent event) {

    }
}
