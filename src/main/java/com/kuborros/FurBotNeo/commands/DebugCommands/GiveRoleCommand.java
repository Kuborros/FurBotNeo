package com.kuborros.FurBotNeo.commands.DebugCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.Permission;

import static com.kuborros.FurBotNeo.BotMain.inventoryCache;

@CommandInfo(
        name = "GiveRole",
        description = "Gives user role with provided id (It is not validated against items.json!)!"
)
@Author("Kuborros")
public class GiveRoleCommand extends DebugCommand {

    public GiveRoleCommand() {
        this.name = "giverole";
        this.help = "Gives you role with specified id";
        this.arguments = "<role>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Category("Debug");
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    protected void doCommand(CommandEvent event) {
        inventoryCache.setInventory(inventory.addToRoles(event.getArgs()));
        event.reactSuccess();
    }
}
