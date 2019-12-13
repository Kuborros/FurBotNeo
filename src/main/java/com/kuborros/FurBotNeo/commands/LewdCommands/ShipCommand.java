package com.kuborros.FurBotNeo.commands.LewdCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;

@CommandInfo(
        name = "Ship",
        description = "Allows you to ship people!"
)
@Author("Kuborros")
public class ShipCommand extends LewdCommand {

    public ShipCommand() {
        this.name = "ship";
        this.help = "Allows you to ship someone!";
        this.arguments = "<@user> <@user>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Category("Lewd");
    }

    @Override
    protected void doCommand(CommandEvent event) {

        List<Member> members = event.getMessage().getMentionedMembers();

        if (members.isEmpty()) {
            event.reply("You have to mention _people you want to ship together_!");
            return;
        }
    }
}
