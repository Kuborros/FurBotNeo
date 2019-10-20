package com.kuborros.FurBotNeo.commands.AdminCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.Permission;

import java.util.Arrays;

import static com.kuborros.FurBotNeo.BotMain.db;

@CommandInfo(
        name = "GuildConfig",
        description = "Sets the specific per-guild configuration options."
)
@Author("Kuborros")
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
        boolean success;
        String value = MysqlRealScapeString(args[1]);
        String valueName = MysqlRealScapeString(Arrays.toString(args).replaceAll("[\\[\\],]", "").replaceFirst(args[0], ""));
        switch (args[0]) {
            case "name":
            case "bot_name":
                success = db.updateGuildBotName(valueName, event.getGuild());
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
            case "welcome":
                success = db.updateGuildWelcomeMsg(value.equals("true"), event.getGuild());
                break;
            case "music":
                success = db.updateGuildAudio(value, event.getGuild());
                break;
            default:
                event.replyError("Provided setting name is not valid!");
                return;
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
