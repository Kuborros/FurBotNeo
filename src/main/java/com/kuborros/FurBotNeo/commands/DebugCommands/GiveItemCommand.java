package com.kuborros.FurBotNeo.commands.DebugCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.Permission;

import static com.kuborros.FurBotNeo.BotMain.inventoryCache;

@CommandInfo(
        name = "GiveItem",
        description = "Gives user item with provided id (It is not validated against items.json!)!"
)
@Author("Kuborros")
public class GiveItemCommand extends DebugCommand {

    public GiveItemCommand() {
        this.name = "giveitem";
        this.help = "Gives you item with specified id";
        this.arguments = "<item>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Category("Debug");
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    protected void doCommand(CommandEvent event) {
        inventoryCache.setInventory(inventory.addToInventory(event.getArgs()));
        event.reactSuccess();
    }
}
