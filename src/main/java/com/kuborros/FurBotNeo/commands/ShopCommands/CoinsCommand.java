package com.kuborros.FurBotNeo.commands.ShopCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;

import static com.kuborros.FurBotNeo.BotMain.cfg;

public class CoinsCommand extends GameCommand {

    public CoinsCommand() {
        this.name = "coins";
        this.help = "Shows your FurToken:tm: balance!";
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
