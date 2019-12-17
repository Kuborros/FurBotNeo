package com.kuborros.FurBotNeo.commands.LewdCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;

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

        List<Member> members = event.getMessage().getMentionedMembers();
        Member member = event.getMember();

        if (members.isEmpty()) {
            event.reply("You have to mention _someone_ to lick!");
            return;
        }

        if (members.contains(event.getMember())) {
            String rep = isFurry ? "You could lick someone to show your affection~" : "I guess you could just lick your hand.";
            event.reply("You want to lick... yourself? \n" + rep);
            return;
        }

        if (members.contains(guild.getSelfMember())) {
            if (isFurry) event.reply("**=w=**, *Licks you too, with a long batty tongue*");
            else event.reply("*Lick!* \n *Enjoy the metal aftertaste~*");
            return;
        }

        event.reply(member.getEffectiveName() + " licks " + members.get(0).getEffectiveName() + "!");
    }
}
