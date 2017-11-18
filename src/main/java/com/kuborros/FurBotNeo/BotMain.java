package com.kuborros.FurBotNeo;

import com.jagrosh.jdautilities.commandclient.CommandClientBuilder;
import com.jagrosh.jdautilities.commandclient.examples.AboutCommand;
import com.jagrosh.jdautilities.waiter.EventWaiter;
import com.kuborros.FurBotNeo.commands.AdminCommands.EvalCommand;
import com.kuborros.FurBotNeo.commands.AdminCommands.InfoCommand;
import com.kuborros.FurBotNeo.commands.AdminCommands.ShutdownCommand;
import com.kuborros.FurBotNeo.commands.AdminCommands.StatsCommand;
import com.kuborros.FurBotNeo.commands.GeneralCommands.BadJokeCmd;
import com.kuborros.FurBotNeo.commands.GeneralCommands.DiceCmd;
import com.kuborros.FurBotNeo.commands.GeneralCommands.ProfPicCmd;
import com.kuborros.FurBotNeo.commands.GeneralCommands.R8BallCmd;
import com.kuborros.FurBotNeo.commands.GeneralCommands.VoteCommand;
import com.kuborros.FurBotNeo.commands.MusicCommands.MusicInfoCmd;
import com.kuborros.FurBotNeo.commands.MusicCommands.MusicPauseCmd;
import com.kuborros.FurBotNeo.commands.MusicCommands.MusicQueueCmd;
import com.kuborros.FurBotNeo.commands.MusicCommands.MusicResetCmd;
import com.kuborros.FurBotNeo.commands.MusicCommands.MusicShuffleCmd;
import com.kuborros.FurBotNeo.commands.MusicCommands.MusicSkipCmd;
import com.kuborros.FurBotNeo.commands.MusicCommands.MusicStopCmd;
import com.kuborros.FurBotNeo.commands.MusicCommands.MusicVolumeCmd;
import com.kuborros.FurBotNeo.commands.MusicCommands.PlayCommand;
import com.kuborros.FurBotNeo.commands.MusicCommands.PlayNextCmd;
import com.kuborros.FurBotNeo.commands.MusicCommands.PlayShuffleCmd;
import com.kuborros.FurBotNeo.commands.PicCommands.DanCmd;
import com.kuborros.FurBotNeo.commands.PicCommands.E621Cmd;
import com.kuborros.FurBotNeo.commands.PicCommands.GelCmd;
import com.kuborros.FurBotNeo.commands.PicCommands.PokeCmd;
import com.kuborros.FurBotNeo.commands.PicCommands.PoniCmd;
import com.kuborros.FurBotNeo.commands.PicCommands.R34Cmd;
import com.kuborros.FurBotNeo.commands.PicCommands.SafeCmd;
import com.kuborros.FurBotNeo.listeners.BotEventListener;
import com.kuborros.FurBotNeo.listeners.LogListener;
import com.kuborros.FurBotNeo.listeners.MemberEventListener;
import com.kuborros.FurBotNeo.utils.config.Config;
import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import java.awt.*;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotMain {

    static final Logger LOG = LoggerFactory.getLogger("Main");
    public static Config cfg;

    public static void main(String args[]) {

        if (!System.getProperty("file.encoding").equals("UTF-8")) {
            LOG.warn("Not running in UTF-8 mode! This ~might~ end badly for us!");
        }
        EventWaiter waiter = new EventWaiter();

        cfg = new Config();

        CommandClientBuilder client = new CommandClientBuilder();
        client.useDefaultGame();
        client.setOwnerId(cfg.getOWNER_ID());
        client.setEmojis("\u2705", "\u2757", "\u274C");
        client.setPrefix(cfg.getPREFIX());


        client.addCommands(

                new AboutCommand(Color.CYAN, "and im here to make this server a better place!", 
                                        new String[]{"Picture commands!","Music player!","Cute furry mascot!"},
                                        Permission.ADMINISTRATOR, Permission.BAN_MEMBERS, Permission.KICK_MEMBERS, Permission.MANAGE_ROLES,
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
                
                // Imageboards
                new E621Cmd(),
                new PokeCmd(),
                new DanCmd(),
                new GelCmd(),
                new SafeCmd(),
                new R34Cmd(),
                new PoniCmd(),
                
                //Music

                new PlayCommand(),
                new PlayNextCmd(),
                new PlayShuffleCmd(),
                new MusicInfoCmd(),
                new MusicPauseCmd(),
                new MusicQueueCmd(),
                new MusicResetCmd(),
                new MusicShuffleCmd(),
                new MusicSkipCmd(),
                new MusicStopCmd(),
                new MusicVolumeCmd(),

                
                
                //Admin
                new InfoCommand(),
                new StatsCommand(),
                new EvalCommand(),
                new ShutdownCommand());









        try {
            new JDABuilder(AccountType.BOT)
                    .setToken(cfg.getBOT_TOKEN())
                    .setStatus(OnlineStatus.DO_NOT_DISTURB)
                    .setGame(Game.of("Now loading..."))
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
        catch (RateLimitedException e)
        {
            LOG.error("We got ratelimited while logging in! \n Try again in a while.");
            System.exit(210);
        }
    }
     
    
}