package com.kuborros.FurBotNeo.commands.AdminCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

import java.sql.SQLException;

import static com.kuborros.FurBotNeo.BotMain.db;

@CommandInfo(
        name = "BotUnBan",
        description = "Unbans user from bot commands."
)
@Author("Kuborros")
public class BotUnBanCmd extends AdminCommand {

    public BotUnBanCmd() {
        this.name = "botunban";
        this.help = "Unbans user from using bot commands";
        this.arguments = "<@user>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    protected void doCommand(CommandEvent event) {
        if (event.getMessage().getMentionedUsers().isEmpty()) {
            event.replyWarning("You must mention someone for me to unban!");
        } else {
            Member member = event.getMessage().getMentionedMembers().get(0);
            try {
                db.unbanUser(member.getId(), guild.getId());
            } catch (SQLException e) {
                LOG.error("Error while unbanning member: ", e);
                event.reply(errorResponseEmbed(e));
                return;
            }
            event.reply("User has been unbanned!");
        }
    }
}