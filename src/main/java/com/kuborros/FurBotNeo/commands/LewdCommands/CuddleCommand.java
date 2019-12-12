package com.kuborros.FurBotNeo.commands.LewdCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;

@CommandInfo(
        name = "Cuddle",
        description = "Allows you to cuddle someone!"
)
@Author("Kuborros")
public class CuddleCommand extends LewdCommand {

    public CuddleCommand() {
        this.name = "cuddle";
        this.help = "Allows you to cuddle with someone!";
        this.arguments = "<@user>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Category("Lewd");
    }

    @Override
    protected void doCommand(CommandEvent event) {

    }
}
