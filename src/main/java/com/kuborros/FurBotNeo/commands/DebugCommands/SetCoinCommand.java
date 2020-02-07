package com.kuborros.FurBotNeo.commands.DebugCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.Permission;

import static com.kuborros.FurBotNeo.BotMain.inventoryCache;

@CommandInfo(
        name = "GiveItem",
        description = "Gives useritem with provided it (It is not validated against items.json!)!"
)
@Author("Kuborros")
public class SetCoinCommand extends DebugCommand {

    public SetCoinCommand() {
        this.name = "setcoin";
        this.help = "Sets your amount of coins.";
        this.arguments = "<amount>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Category("Debug");
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    protected void doCommand(CommandEvent event) {
        int coins = Integer.parseInt(event.getArgs());
        inventoryCache.setInventory(inventory.setTokens(coins));
        event.reactSuccess();
    }
}
