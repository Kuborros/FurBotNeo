package com.kuborros.FurBotNeo;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.examples.command.AboutCommand;
import com.kuborros.FurBotNeo.commands.AdminCommands.*;
import com.kuborros.FurBotNeo.commands.GeneralCommands.*;
import com.kuborros.FurBotNeo.commands.MusicCommands.*;
import com.kuborros.FurBotNeo.commands.PicCommands.*;
import com.kuborros.FurBotNeo.listeners.BotEventListener;
import com.kuborros.FurBotNeo.listeners.LogListener;
import com.kuborros.FurBotNeo.listeners.MemberEventListener;
import com.kuborros.FurBotNeo.utils.config.Config;
import com.kuborros.FurBotNeo.utils.config.Database;
import com.kuborros.FurBotNeo.utils.config.FurrySettingsManager;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.awt.*;

public class BotMain {

    private static final Logger LOG = LoggerFactory.getLogger("Main");
    public static Config cfg;
    public static Database db;
    public static final FurrySettingsManager settingsManager = new FurrySettingsManager();

    public static void main(String[] args) {

        if (!System.getProperty("file.encoding").equals("UTF-8")) {
            LOG.info("Not running in UTF-8 mode! This ~might~ end badly for us!");
        }
        EventWaiter waiter = new EventWaiter();

        db = new Database();
        db.createTables();

        cfg = new Config();

        CommandClientBuilder client = new CommandClientBuilder();
        client.setOwnerId(cfg.getOWNER_ID());
        client.setEmojis("\u2705", "\u2757", "\u274C");
        client.setPrefix("!");
        client.setGuildSettingsManager(settingsManager);
        client.addCommands(

                new AboutCommand(Color.CYAN, "and im here to make this server a better place!",
                                        new String[]{"Picture commands!","Music player!","Cute furry mascot!"},
                                        Permission.ADMINISTRATOR, Permission.MANAGE_ROLES,
                                        Permission.MANAGE_SERVER, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_READ,
                                        Permission.MESSAGE_WRITE,Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_HISTORY, Permission.MESSAGE_EXT_EMOJI,
                                        Permission.MESSAGE_MANAGE, Permission.VOICE_CONNECT, Permission.VOICE_MOVE_OTHERS, Permission.VOICE_DEAF_OTHERS, 
                                        Permission.VOICE_MUTE_OTHERS, Permission.NICKNAME_CHANGE, Permission.NICKNAME_MANAGE),
                //General

                new R8BallCmd(),
                new ProfPicCmd(),
                new DiceCmd(),
                new BadJokeCmd(),
                new VoteCommand(),
                new CommandStatCmd(),
                new BigTextCmd(),
                
                // Imageboards
                new E621Cmd(waiter),
                new PokeCmd(waiter),
                new DanCmd(waiter),
                new GelCmd(waiter),
                new SafeCmd(waiter),
                new E926Cmd(waiter),
                new R34Cmd(waiter),
                
                //Music

                new PlayCommand(),
                new PlayNextCmd(),
                new PlayShuffleCmd(),
                new MusicTimeCommand(),
                new MusicInfoCmd(),
                new MusicPauseCmd(),
                new MusicQueueCmd(),
                new MusicResetCmd(),
                new MusicShuffleCmd(),
                new MusicClearCmd(),
                new MusicSkipCmd(),
                new MusicStopCmd(),
                new MusicVolumeCmd(),

                //Admin

                new InfoCommand(),
                new BotBanCmd(),
                new BotUnBanCmd(),
                new StatsCommand(),
                new GuildConfigCommand(),
                new EvalCommand(),
                new ShutdownCommand());





        try {
            JDABuilder builder = new JDABuilder(AccountType.BOT);
            builder.setToken(cfg.getBOT_TOKEN())
                    .setStatus(OnlineStatus.ONLINE)
                    .addEventListeners(waiter, client.build(), new LogListener(), new MemberEventListener(), new BotEventListener())
                    .setAutoReconnect(true)
                    .setEnableShutdownHook(true);
            builder.build();


        }
        catch (IllegalArgumentException e) {
            LOG.error("Error occured while starting: ", e);
            LOG.error("Please check if your discord bot token is in the configuration file, as this is most common cause of this error.");
            System.exit(101);
        }
        catch (LoginException e) {
            LOG.error("Stored token was rejected!");
            LOG.error("Please double-check your token.");
            System.exit(102);
        }
    }
}