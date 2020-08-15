package com.kuborros.FurBotNeo.commands.ShopCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.Permission;

import static com.kuborros.FurBotNeo.BotMain.inventoryCache;

@CommandInfo(
        name = "GrantRole",
        description = "Gives user role with provided id (It is not validated against items.json!)!"
)
@Author("Kuborros")
public class GrantRoleCommand extends ShopCommand {

    public GrantRoleCommand() {
        this.name = "grantrole";
        this.help = "Gives you role with specified id";
        this.arguments = "<role>";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_MANAGE};
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Category("Shop");
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    protected void doCommand(CommandEvent event) {
        inventoryCache.setInventory(inventory.addToRoles(event.getArgs()));
        event.reactSuccess();
    }
}
