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


import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static com.kuborros.FurBotNeo.BotMain.cfg;
import static com.kuborros.FurBotNeo.BotMain.db;

public class MemberEventListener extends ListenerAdapter{

    private static final Logger LOG = LoggerFactory.getLogger("MemberInfo");

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        TextChannel pub = event.getGuild().getDefaultChannel();
        if (cfg.isGUILD_MSGS() && event.getMember().getUser().isBot() && !event.getMember().getUser().equals(event.getJDA().getSelfUser())) {
            Objects.requireNonNull(pub).sendMessage("Another bot?" + "\n"
                    + "just make sure their commands dont start with \"" + cfg.getPREFIX() + "\", ok?").queue();
        }
        if (event.getMember().getUser().getId().equals("348186098951913473") && cfg.isGUILD_MSGS()) {
            Objects.requireNonNull(pub).sendMessage("Hello, " + event.getMember().getAsMention() + " and welcome on the... \n Oh right, its just you. Welcome back???").queue();
        } else if (cfg.isGUILD_MSGS()) {
            Objects.requireNonNull(pub).sendMessage("Hello, " + event.getMember().getAsMention() + " and welcome on the " + event.getGuild().getName() + " server! :3").queue();
        }
        LOG.info("{} has joined the {} server!", event.getMember().getEffectiveName(), event.getGuild().getName());
        db.updateGuildMembers(event);
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        if (event.getMember().getUser().isBot()) return;
        TextChannel pub = event.getGuild().getDefaultChannel();
        if (cfg.isGUILD_MSGS()) {
            Objects.requireNonNull(pub).sendMessage("Bye, " + event.getMember().getEffectiveName() + "! it was nice (or not) having you with us!").queue();
        }
        if (event.getMember().getUser().getId().equals("348186098951913473")) {
            Objects.requireNonNull(pub).sendMessage("**He did it agaaain!**").queue();
        }
        LOG.info("{} has left the {} server!", event.getMember().getEffectiveName(), event.getGuild().getName());
        db.updateGuildMembers(event);
    }      
}
