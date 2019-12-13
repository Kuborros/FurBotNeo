package com.kuborros.FurBotNeo.commands.LewdCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;

@CommandInfo(
        name = "Boop",
        description = "Allows you to boop someone!"
)
@Author("Kuborros")
public class BoopCommand extends LewdCommand {

    public BoopCommand() {
        this.name = "boop";
        this.help = "Allows you to boop someone!";
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
            event.reply("You have to mention _someone_ to boop!");
            return;
        }

        if (members.contains(event.getMember())) {
            String rep = isFurry ? "I mean its not like there's a bat girl *right here* you can boop or anything." : "That would be *slightly* weird.";
            event.reply("You want to boop... yourself? \n" + rep);
            return;
        }

        if (members.contains(guild.getSelfMember())) {
            if (isFurry) event.reply("**=w=** *Gets booped*");
            else event.reply("*Boop!* \n ");
            return;
        }

        event.reply(member.getEffectiveName() + " boops " + members.get(0).getEffectiveName() + " right on their nose!");
    }
}
