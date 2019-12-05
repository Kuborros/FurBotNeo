
package com.kuborros.FurBotNeo.listeners;


import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.guild.GuildAvailableEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildUnavailableEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Objects;

import static com.kuborros.FurBotNeo.BotMain.cfg;
import static com.kuborros.FurBotNeo.BotMain.db;
import static net.dv8tion.jda.api.entities.Activity.watching;


public class BotEventListener extends ListenerAdapter{

    private static final Logger LOG = LoggerFactory.getLogger("BotInfo");

    
    @Override
    public void onReady(ReadyEvent event) {
        ClearConsole();
        LOG.info("FurryBot - Startup completed!");
        event.getJDA().getPresence().setActivity(watching(" furry lewds"));
        
        if (cfg.getOWNER_ID().equals("0")) {
           LOG.warn("Please set your own user ID in config.cfg! This gives you sudo powers in bot commands!");
        }
    db.setGuilds(event.getJDA());
    db.setCommandStats(event.getJDA());
    }
    
    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        Guild guild = event.getGuild();
        LOG.info("Joining guild: {}! It's main channel is: {}", guild.getName(), Objects.requireNonNull(guild.getDefaultChannel()).getName());
        if (!guild.isAvailable()) {
            LOG.warn("...But it's unavailable right now!");
            return;
        }
        else if (!guild.checkVerification()) {
            LOG.warn("...But my verification level is too low to do anything there!");
            return;
        }
        else if (!guild.getDefaultChannel().canTalk()) {
            LOG.warn("...But i can't write on it's public channel? This ~might~ be a problem!");
            return;
        }
        LOG.info("Joined guild: {}", guild.getName());
    }
    
    @Override
    public void onGuildLeave(GuildLeaveEvent event){
        Guild guild = event.getGuild();
        LOG.info("Left guild: {}!", guild.getName());
        
    }
    
    @Override
    public void onReconnect(@Nonnull ReconnectedEvent event) {
        LOG.info("Connection to discord servers restored!");
    }

    @Override
    public void onGuildUnavailable(GuildUnavailableEvent event) {
       LOG.warn("Guild became unavailable! Affected guild: {}", event.getGuild().getName());
    }
   
    @Override
    public void onGuildAvailable(GuildAvailableEvent event) {
       LOG.debug("Connected to guild: {}", event.getGuild().getName());
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    private static void ClearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
