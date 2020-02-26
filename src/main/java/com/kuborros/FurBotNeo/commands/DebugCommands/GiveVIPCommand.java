package com.kuborros.FurBotNeo.commands.DebugCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.Permission;

import static com.kuborros.FurBotNeo.BotMain.inventoryCache;

@CommandInfo(
        name = "GiveVIP",
        description = "Sets VIP status of mentioned user."
)
@Author("Kuborros")
public class GiveVIPCommand extends DebugCommand {

    public GiveVIPCommand() {
        this.name = "setvip";
        this.help = "Sets your vip status";
        this.arguments = "<isVip?>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Category("Debug");
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    protected void doCommand(CommandEvent event) {
        boolean vip = Boolean.parseBoolean(event.getArgs());
        inventoryCache.setInventory(inventory.setVIP(vip));
        event.reactSuccess();
    }
}
