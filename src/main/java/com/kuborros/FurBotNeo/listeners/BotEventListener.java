
/*
 * Copyright © 2020 Kuborros (kuborros@users.noreply.github.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kuborros.FurBotNeo.listeners;


import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.guild.GuildAvailableEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildUnavailableEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import static com.kuborros.FurBotNeo.BotMain.*;
import static net.dv8tion.jda.api.entities.Activity.watching;


public class BotEventListener extends ListenerAdapter{

    private static final Logger LOG = LoggerFactory.getLogger("BotInfo");
    private static final Properties versionInfo = new Properties();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {

        String version = "0";
        try {
            versionInfo.load(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("version.info")));
            version = versionInfo.getProperty("version");
        } catch (IOException e) {
            LOG.warn("IO error occurred while reading version.info", e);
        }

        ClearConsole();
        LOG.info("FurryBot Version {} - {}", version, randomResponse.getRandomBootupMessage());
        event.getJDA().getPresence().setActivity(watching(" furry lewds"));

        if (cfg.getOwnerId().equals("0")) {
            LOG.warn("Please set your own user ID in config.json! This gives you sudo powers in bot commands!");
        }
        db.setGuilds(event.getJDA());
        db.setCommandStats(event.getJDA());
    }


    @Override
    public void onGuildJoin(GuildJoinEvent event) {

        Guild guild = event.getGuild();

        boolean beaned = false;
        JSONArray banned = cfg.getBannedGuilds();

        for (int i = 0; banned.length() > i; i++) {
            if (banned.getString(i).contains(guild.getId())) {
                beaned = true;
            }
        }

        int bots = Math.toIntExact(guild.getMembers().stream().filter(member -> member.getUser().isBot()).count());
        int members = guild.getMemberCount();

        LOG.info("Joining guild: {}! \n" +
                "It's main channel is: {}. \n" +
                "It has {} members, including {} bots.", guild.getName(), Objects.requireNonNull(guild.getDefaultChannel()).getName(), members, bots);

        if (bots > (members - bots)) {
            LOG.warn("...But this guild contains more bots than real users!");
            leaveWithMsg(guild, "Your guild has more bots than humans! This is not the party i signed for... \n Please try again after lowering the bot amount.");
            return;
        }
        if (beaned) {
            LOG.warn("...But this guild is banned! Naughty!");
            leaveWithMsg(guild, "Your guild has been banned from this bot instance.");
            return;
        }
        if (!guild.getDefaultChannel().canTalk()) {
            LOG.warn("...But i can't write on it's default channel? This ~might~ be a problem!");
            return;
        }
        db.setGuild(guild);
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

    private void leaveWithMsg(Guild guild, String msg) {
        Objects.requireNonNull(guild.getOwner()).getUser().openPrivateChannel().complete().sendMessage(msg).queue();
        guild.leave().queue();
    }

}
