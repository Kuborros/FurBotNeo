package com.kuborros.FurBotNeo.commands.LewdCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;

@CommandInfo(
        name = "Cuddle",
        description = "Allows you to cuddle someone!"
)
@Author("Kuborros")
public class CuddleCommand extends LewdCommand {

    public CuddleCommand() {
        this.name = "cuddle";
        this.aliases = new String[]{"snuggle"};
        this.help = "Allows you to cuddle with someone!";
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
            event.reply("You have to mention _someone_ to cuddle with!");
            return;
        }

        if (members.contains(event.getMember())) {
            event.reply("You want to cuddle with... yourself? \n Honestly, you can cuddle with _me_ if you want~");
            return;
        }

        if (members.contains(guild.getSelfMember())) {
            if (isFurry) event.reply("\uD83D\uDE3B *Cuddles up with you*");
            else
                event.reply("Ofcourse you can cuddle with me \uD83D\uDE0A \n Afterall my server is comfortably warm at 31°C");
            return;
        }

        event.reply(member.getEffectiveName() + " softly cuddles with " + members.get(0).getEffectiveName());
    }
}
