package com.kuborros.FurBotNeo.commands.AdminCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;

import static com.kuborros.FurBotNeo.BotMain.db;

public class GuildConfigCommand extends AdminCommand {

    public GuildConfigCommand() {
        this.name = "guildcfg";
        this.help = "Modifies bot configs for guild. **Incorrect edits might cause malfunctions**";
        this.arguments = "<key> <value>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.BAN_MEMBERS};
    }

    @Override
    protected void doCommand(CommandEvent event) {
        String[] args = event.getArgs().split(" ");
        if (event.getArgs().isEmpty() || args.length < 2) {
            event.replyWarning("You need to provide both variable you want to change, and what value change it to!");
            return;
        }
        boolean success = false;
        String value = MysqlRealScapeString(args[1]);
        switch (args[0]) {
            case "bot_name":
                success = db.updateGuildBotName(value, event.getGuild());
                break;
            case "prefix":
                success = db.updateGuildPrefix(value, event.getGuild());
                break;
            case "nsfw":
                success = db.updateGuildIsNSFW(value.equals("true"), event.getGuild());
                break;
            case "furry":
                success = db.updateGuildIsFurry(value.equals("true"), event.getGuild());
                break;
            case "music":
                success = db.updateGuildAudio(value, event.getGuild());
                break;
        }
        if (success) {
            event.reply("Configuration option changed!");
        } else {
            event.replyWarning("An error has occured in internal configuration system! This is **bad**");
        }

    }

    private String MysqlRealScapeString(String str) {
        String data = null;
        if (str != null && str.length() > 0) {
            str = str.replace("\\", "\\\\");
            str = str.replace("'", "\\'");
            str = str.replace("\0", "\\0");
            str = str.replace("\n", "\\n");
            str = str.replace("\r", "\\r");
            str = str.replace("\"", "\\\"");
            str = str.replace("\\x1a", "\\Z");
            str = str.replace("_", " ");
            data = str;
        }
        return data;
    }
}
