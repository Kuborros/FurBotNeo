package com.kuborros.FurBotNeo.commands.AdminCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;

public class AnnounceCommand extends AdminCommand {

    public AnnounceCommand() {
        this.name = "announce";
        this.help = "Sends global announcement.";
        this.arguments = "<scope> <announcement>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    protected void doCommand(CommandEvent event) {


    }


}
