package com.kuborros.FurBotNeo.commands.ShopCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;

import static com.kuborros.FurBotNeo.BotMain.cfg;

public class ShopCommand extends GameCommand {

    public ShopCommand() {
        this.name = "shop";
        this.children = new Command[]{new BuyItemCommand(), new BuyRoleCommand(), new BuyVipCommand()};
        this.help = "Lets you access _the shop_";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.category = new Command.Category("Shop");
    }

    @Override
    protected void doCommand(CommandEvent event) {

        if (!cfg.isShopEnabled()) {
            LOG.info("Shop disabled by instance owner, ignoring.");
            return;
        }


    }
}