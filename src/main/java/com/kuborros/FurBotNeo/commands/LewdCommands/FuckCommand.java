package com.kuborros.FurBotNeo.commands.LewdCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;

@CommandInfo(
        name = "Fuck",
        description = "Allows you to fuck someone!"
)
@Author("Kuborros")
public class FuckCommand extends LewdCommand {

    EventWaiter waiter;

    public FuckCommand(EventWaiter waiter) {
        this.name = "fuck";
        this.help = "Allows you to fuck someone!";
        this.arguments = "<@user>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Category("Lewd");
        this.waiter = waiter;
    }

    @Override
    protected void doCommand(CommandEvent event) {

        List<Member> members = event.getMessage().getMentionedMembers();

        if (members.isEmpty()) {
            event.reply("You propably have to mention _someone_ to fuck... \n Unless you want to fuck yourself, that's okay too. ");
            return;
        }
    }
}
