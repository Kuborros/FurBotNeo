package com.kuborros.FurBotNeo.commands.LewdCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;

@CommandInfo(
        name = "Hug",
        description = "Allows you to hug someone!"
)
@Author("Kuborros")
public class HugCommand extends LewdCommand {

    public HugCommand() {
        this.name = "hug";
        this.help = "Allows you to hug someone!";
        this.arguments = "<@user>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Category("Lewd");
    }

    @Override
    protected void doCommand(CommandEvent event) {

    }
}
