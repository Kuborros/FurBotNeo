package com.kuborros.FurBotNeo.commands.ShopCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

import java.awt.*;


@CommandInfo(
        name = "Coins",
        description = "Shows your token balance."
)
@Author("Kuborros")
public class CoinsCommand extends ShopCommand {

    public CoinsCommand() {
        this.name = "coins";
        this.aliases = new String[]{"tokens", "wallet"};
        this.children = new Command[]{new GiveCoinsCmd()};
        this.help = "Shows your BatToken:tm: balance!";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.category = new Command.Category("Shop");
    }

    @Override
    protected void doCommand(CommandEvent event) {

        int moneys = inventory.getBalance();
        String footer;
        switch (moneys) {
            case 69:
            case 621:
                footer = "OwO";
                break;
            case 420:
                footer = "It's da weed number!";
                break;
            case 9001:
                footer = "Its over nine thousaaaaand";
                break;
            default:
                footer = "";
        }
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("Token balance of " + event.getAuthor().getName())
                .setColor(Color.ORANGE)
                .setDescription(String.format("** %d **", moneys))
                .setFooter(footer);
        event.reply(builder.build());

    }
}
