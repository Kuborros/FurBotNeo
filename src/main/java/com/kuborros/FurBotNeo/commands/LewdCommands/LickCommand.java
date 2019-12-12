package com.kuborros.FurBotNeo.commands.LewdCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;

@CommandInfo(
        name = "Lick",
        description = "Allows you to lick someone!"
)
@Author("Kuborros")
public class LickCommand extends LewdCommand {

    public LickCommand() {
        this.name = "lick";
        this.help = "Allows you to lick someone!";
        this.arguments = "<@user>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Category("Lewd");
    }

    @Override
    protected void doCommand(CommandEvent event) {

    }
}
