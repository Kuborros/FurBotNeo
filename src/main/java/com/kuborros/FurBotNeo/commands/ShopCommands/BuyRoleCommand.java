package com.kuborros.FurBotNeo.commands.ShopCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;

public class BuyRoleCommand extends ShopCommand {

    public BuyRoleCommand() {
        this.name = "role";
        this.help = "Purchase special roles here!";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.category = new Command.Category("Shop");
    }

    @Override
    protected void doCommand(CommandEvent event) {

    }
}