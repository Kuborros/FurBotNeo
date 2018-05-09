package com.kuborros.FurBotNeo;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.examples.command.AboutCommand;
import com.kuborros.FurBotNeo.commands.AdminCommands.*;
import com.kuborros.FurBotNeo.commands.GeneralCommands.*;
import com.kuborros.FurBotNeo.commands.LastFmCommands.LastFmArtistInfoCmd;
import com.kuborros.FurBotNeo.commands.LastFmCommands.LastFmTopArtCmd;
import com.kuborros.FurBotNeo.commands.LastFmCommands.LastFmUserInfoCmd;
import com.kuborros.FurBotNeo.commands.MusicCommands.*;
import com.kuborros.FurBotNeo.commands.PicCommands.*;
import com.kuborros.FurBotNeo.listeners.BotEventListener;
import com.kuborros.FurBotNeo.listeners.LogListener;
import com.kuborros.FurBotNeo.listeners.MemberEventListener;
import com.kuborros.FurBotNeo.utils.config.Config;
import com.kuborros.FurBotNeo.utils.config.Database;
import com.kuborros.FurBotNeo.utils.config.FurrySettingsManager;
import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import de.umass.lastfm.Caller;
import de.umass.lastfm.cache.DatabaseCache;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.sql.SQLException;
import java.util.logging.Level;

public class BotMain {

    private static final Logger LOG = LoggerFactory.getLogger("Main");
    public static Config cfg;
    public static Database db;
    private static final FurrySettingsManager settingsManager = new FurrySettingsManager();

    public static void main(String args[]) {

        if (!System.getProperty("file.encoding").equals("UTF-8")) {
            LOG.warn("Not running in UTF-8 mode! This ~might~ end badly for us!");
        }
        EventWaiter waiter = new EventWaiter();

        db = new Database();
        db.createTables();

        try {
            Caller.getInstance().setCache(new DatabaseCache(db.getConn()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Caller.getInstance().getLogger().setLevel(Level.WARNING);
        Caller.getInstance().setUserAgent("DiscordBot/1.0");

        cfg = new Config();

        CommandClientBuilder client = new CommandClientBuilder();
        client.setOwnerId(cfg.getOWNER_ID());
        client.setEmojis("\u2705", "\u2757", "\u274C");
        client.setPrefix(cfg.getPREFIX());
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
                new MusicSkipCmd(),
                new MusicStopCmd(),
                new MusicVolumeCmd(),

                //Last.fm
                new LastFmUserInfoCmd(),
                new LastFmArtistInfoCmd(),
                new LastFmTopArtCmd(waiter),
                
                //Admin
                new InfoCommand(),
                new BotBanCmd(),
                new StatsCommand(),
                new EvalCommand(),
                new ShutdownCommand());





        try {
            new JDABuilder(AccountType.BOT)
                    .setToken(cfg.getBOT_TOKEN())
                    .setStatus(OnlineStatus.ONLINE)
                    .addEventListener(waiter)
                    .addEventListener(client.build())
                    .addEventListener(new LogListener())
                    .addEventListener(new MemberEventListener())
                    .addEventListener(new BotEventListener())                    
                    .setAudioEnabled(true)
                    .setAutoReconnect(true)
                    .setAudioSendFactory(new NativeAudioSendFactory())
                    .setEnableShutdownHook(true)
            

                    .buildAsync();
        }
        catch (IllegalArgumentException e) {
            LOG.error("No token has been provided!");
            LOG.error("Please put your discord bot token in the configuration file.");
            System.exit(101);
        }
        catch (LoginException e) {
            LOG.error("Stored token was rejected!");
            LOG.error("Please double-check your token.");
            System.exit(102);
        }
    }
}