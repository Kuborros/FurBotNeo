
package com.kuborros.FurBotNeo.listeners;


import com.kuborros.FurBotNeo.utils.config.FurConfig;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static com.kuborros.FurBotNeo.BotMain.db;
import static com.kuborros.FurBotNeo.BotMain.settingsManager;

public class MemberEventListener extends ListenerAdapter{

    private static final Logger LOG = LoggerFactory.getLogger("MemberInfo");

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        FurConfig config = settingsManager.getSettings(event.getGuild());

        boolean welcome = Objects.requireNonNull(config).isWelcomeMsg();
        TextChannel pub = event.getGuild().getDefaultChannel();
        if (welcome && event.getMember().getUser().isBot() && !event.getMember().getUser().equals(event.getJDA().getSelfUser())) {
            Objects.requireNonNull(pub).sendMessage("Another bot?" + "\n"
                    + "just make sure their commands dont start with \"" + Objects.requireNonNull(config.getPrefixes()).iterator().next() + "\", ok?").queue();
        }
        if (welcome) {
            Objects.requireNonNull(pub).sendMessage("Hello, " + event.getMember().getAsMention() + " and welcome on the " + event.getGuild().getName() + " server! :3").queue();
        }
        LOG.info("{} has joined the {} server!", event.getMember().getEffectiveName(), event.getGuild().getName());
        db.updateGuildMembers(event);
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        boolean welcome = Objects.requireNonNull(settingsManager.getSettings(event.getGuild())).isWelcomeMsg();
        if (event.getMember().getUser().isBot()) return;
        TextChannel pub = event.getGuild().getDefaultChannel();
        if (welcome) {
            Objects.requireNonNull(pub).sendMessage("Bye, " + event.getMember().getEffectiveName() + "! it was nice (or not) having you with us!").queue();
        }
        LOG.info("{} has left the {} server!", event.getMember().getEffectiveName(), event.getGuild().getName());
        db.updateGuildMembers(event);
    }      
}
