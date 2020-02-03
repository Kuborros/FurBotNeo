package com.kuborros.FurBotNeo.commands.ShopCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;

public class BuyCommand extends ShopCommand {

    public BuyCommand() {
        this.name = "shop";
        this.children = new Command[]{new BuyItemCommand(), new BuyRoleCommand(), new BuyVipCommand()};
        this.help = "Lets you access _the shop_";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.category = new Command.Category("Shop");
    }

    @Override
    protected void doCommand(CommandEvent event) {



    }
}