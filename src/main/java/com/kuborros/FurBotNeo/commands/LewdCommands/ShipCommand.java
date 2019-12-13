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
        Member member1, member2;

        String override100 = "Calculating love levels... \n" +
                "```[INFO] (AiThread-621) Feeling subsystem requesting override in subprocess LT-1. \n" +
                "[WARN] (ShippingThread-1) Override detected! \n" +
                ">>Set love 100 \n" +
                "[INFO] (AiThread-621) Resuming normal operation.``` \n" +
                "The results are: \n" +
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
        if (!members.contains(event.getSelfMember()) && (member1.getUser().isBot() || member2.getUser().isBot())) {
            event.reply("You can't ship normal bots! They don't really understand love... unlike me!");
            return;
        }

        event.reply("Calculating love levels... \n\n" +
                "The results are: \n" +
                member1.getEffectiveName() + " and " + member2.getEffectiveName() + " are" +
                getLoveLevel()
        );

    }

    private String getLoveLevel() {
        int level = random.nextInt(101);

        if (level == 100) {
            return " *a match made in heaven*! ❤";
        } else if ((level <= 99) && (level > 90)) {
            return " propably planning a wedding already :3";
        } else if ((level <= 90) && (level > 80)) {
            return " in close possibility of marriage!";
        } else if ((level <= 80) && (level > 70)) {
            return " together forever~!";
        } else if ((level <= 70) && (level > 60)) {
            return " in for a long relationship.";
        } else if ((level <= 60) && (level > 50)) {
            return "... **kissing right now!**";
        } else if ((level <= 50) && (level > 40)) {
            return " definietly a couple!";
        } else if ((level <= 40) && (level > 30)) {
            return " close to being a couple!";
        } else if ((level <= 30) && (level > 20)) {
            return " friends with benefits!";
        } else if ((level <= 20) && (level > 10)) {
            return " in the realm of *friendzone*! UwU";
        } else if ((level <= 10) && (level >= 0)) {
            return " not even friends...";
        } else return " so far away they broke the love machine!";
    }
}
