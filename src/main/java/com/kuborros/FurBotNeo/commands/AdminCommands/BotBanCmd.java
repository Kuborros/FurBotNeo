package com.kuborros.FurBotNeo.commands.AdminCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

import java.sql.SQLException;
import java.util.Objects;

import static com.kuborros.FurBotNeo.BotMain.cfg;
import static com.kuborros.FurBotNeo.BotMain.db;


@CommandInfo(
        name = "BotBan",
        description = "Bans user from using bot commands."
)
@Author("Kuborros")
public class BotBanCmd extends AdminCommand {

    public BotBanCmd() {
        this.name = "botban";
        this.help = "Bans user from using bot commands";
        this.arguments = "<@user>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    protected void doCommand(CommandEvent event) {
        if (event.getMessage().getMentionedUsers().isEmpty()) {
            event.replyWarning("You must mention someone for me to ignore!");
        } else {
            Member member = event.getMessage().getMentionedMembers().get(0);
            if (Objects.equals(member.getId(), cfg.getOWNER_ID())) {
                event.reply("Can't ban my owner, silly.");
                return;
            }
            try {
                db.addBannedUser(member.getId(), guild.getId());
            } catch (SQLException e) {
                LOG.error("Error while banning member: ", e);
                event.reply(errorResponseEmbed(e));
            }
            try {
                if (db.getBanStatus(member.getId(), guild.getId())) {
                    event.reply("User has been blocked from bot commands!");
                }
            } catch (SQLException e) {
                LOG.error("Error while contacting database: ", e);
                event.reply(errorResponseEmbed(e));
            }
        }
    }
}
