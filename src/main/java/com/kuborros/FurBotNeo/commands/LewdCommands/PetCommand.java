package com.kuborros.FurBotNeo.commands.LewdCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;

@CommandInfo(
        name = "Pet",
        description = "Allows you to pet someone!"
)
@Author("Kuborros")
public class PetCommand extends LewdCommand {

    public PetCommand() {
        this.name = "pet";
        this.help = "Allows you to pet someone!";
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
            event.reply("You have to mention _someone_ to pet!");
            return;
        }

        if (members.contains(event.getMember())) {
            String rep = isFurry ? "But i would love some petting, if you are up to it~" : "That's not as fun as it sounds sadly.";
            event.reply("You want to pet... yourself? \n" + rep);
            return;
        }

        if (members.contains(guild.getSelfMember())) {
            if (isFurry) event.reply("**Purrs**, *even though is definitely not a cat*");
            else event.reply("*Pet,pet!* \n Just dont bend my case with excessive pats~");
            return;
        }

        event.reply(member.getEffectiveName() + " pets " + members.get(0).getEffectiveName() + "! So cute!");
    }
}
