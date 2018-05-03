package com.kuborros.FurBotNeo.commands.AdminCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;

import java.sql.SQLException;

import static com.kuborros.FurBotNeo.BotMain.db;

public class BotBanCmd extends AdminCommand {

    public BotBanCmd() {
        this.name = "botban";
        this.help = "Bans user from using bot commands";
        this.arguments = "<@user> [time]";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
        this.hidden = true;
    }

    @Override
    protected void doCommand(CommandEvent event) {
        if (event.getMessage().getMentionedUsers().isEmpty()) {
            event.replyWarning("You must mention someone for me to ignore!");
        } else {
            Member member = event.getMessage().getMentionedMembers().get(0);
            try {
                db.addBannedUser(member.getUser().getId(), event.getGuild().getId());
            } catch (SQLException e) {
                event.replyError("Internal error has occured! ```\n" + e.getLocalizedMessage() + "\n```");
            }
            if (db.getBanStatus(member.getUser().getId(), event.getGuild().getId())) {
                event.reply("User has been blocked from bot commands!");
            } else event.reactError();
        }
    }
}
