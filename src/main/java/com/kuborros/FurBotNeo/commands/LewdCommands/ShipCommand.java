package com.kuborros.FurBotNeo.commands.LewdCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;
import java.util.Random;

import static com.kuborros.FurBotNeo.BotMain.cfg;

@CommandInfo(
        name = "Ship",
        description = "Allows you to ship people!"
)
@Author("Kuborros")
public class ShipCommand extends LewdCommand {

    Random random = new Random();

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
        boolean adminOverride = false;
        Member member1, member2;

        String override100 = "Calculating love levels... \n" +
                "**[WARN]** (*LoveThread-1*) Admin override detected! \n" +
                ">>Set love 100 \n" +
                "**[INFO]** (*AiThread-621*) Resuming. \n\n" +
                "... We are a match made in heaven! ❤";

        if (members.isEmpty()) {
            event.reply("You have to mention _people you want to ship together_!");
            return;
        }

        member1 = members.get(0);
        if (members.size() > 1) {
            member2 = members.get(1);
        } else {
            member2 = event.getMember();
        }

        if (member1.getId().contains(cfg.getOWNER_ID()) || member2.getId().contains(cfg.getOWNER_ID())) {
            if (members.contains(event.getSelfMember())) {
                event.reply(override100);
                return;
            }
        }

        if (member1.getUser().isBot() || member2.getUser().isBot()) {
            event.reply("You can't ship normal bots! They don't really undestand love... unlike me!");
            return;
        }

    }

    private String getLoveLevel(int level) {
        return "";
    }
}
