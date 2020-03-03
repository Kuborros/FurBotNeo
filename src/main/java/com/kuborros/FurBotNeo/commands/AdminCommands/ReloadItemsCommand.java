package com.kuborros.FurBotNeo.commands.AdminCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.Permission;

import java.io.IOException;

import static com.kuborros.FurBotNeo.BotMain.storeItems;

@CommandInfo(
        name = "BotBan",
        description = "Bans user from using bot commands."
)
@Author("Kuborros")
public class ReloadItemsCommand extends AdminCommand {

    public ReloadItemsCommand() {
        this.name = "reloaditems";
        this.help = "Reloads shop item definition file";
        this.guildOnly = true;
        this.ownerCommand = true;
        this.hidden = true;
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    protected void doCommand(CommandEvent event) {
        try {
            storeItems.reloadItemsFile();
            event.replySuccess("Items reloaded successfully!");
        } catch (IOException e) {
            LOG.error("Exception occurred while reloading items.json: ", e);
            event.reply(errorResponseEmbed(e));
        }
    }
}
