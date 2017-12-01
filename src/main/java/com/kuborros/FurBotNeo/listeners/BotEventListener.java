/*
 * The MIT License
 *
 * Copyright 2017 Kuborros.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.kuborros.FurBotNeo.listeners;


import static com.kuborros.FurBotNeo.BotMain.cfg;
import static com.kuborros.FurBotNeo.BotMain.db;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.ReconnectedEvent;
import net.dv8tion.jda.core.events.guild.GuildAvailableEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.GuildUnavailableEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kuborros
 */
public class BotEventListener extends ListenerAdapter{
    
    static final Logger LOG = LoggerFactory.getLogger("BotInfo");

    
    @Override
    public void onReady(ReadyEvent event) {
        ClearConsole();
        LOG.info("FurryBot {} - Startup completed!", cfg.getVERSION());
        //event.getJDA().getPresence().setGame(Game.watching(" furry porn"));
        
        if (cfg.getOWNER_ID().equals("0")) {
           LOG.warn("Please set your own user ID in config.cfg! This gives you sudo powers in bot commands!");
        }
    db.setGuilds(event.getJDA());
    db.setCommandStats(event.getJDA());
    }
    
    @Override
    @SuppressWarnings("null")
    public void onGuildJoin(GuildJoinEvent event) {
        Guild guild = event.getGuild();
        LOG.info("Joining guild: {}! It's main channel is: {}", guild.getName(), guild.getDefaultChannel().getName());
        if (!guild.isAvailable()) {
            LOG.warn("...But it's unavaible right now!");
            return;
        }
        else if (!guild.checkVerification()) {
            LOG.warn("...But my verification level is too low to do anything there!");
            return;
        }
        else if (!guild.getSystemChannel().canTalk()) {
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
    public void onReconnect(ReconnectedEvent event) {
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
       
    public static void ClearConsole(){
       System.out.print("\033[H\033[2J");  
       System.out.flush(); 
   } 
}
