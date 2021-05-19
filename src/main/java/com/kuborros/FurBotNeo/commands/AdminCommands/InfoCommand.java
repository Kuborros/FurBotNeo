
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

package com.kuborros.FurBotNeo.commands.AdminCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.kuborros.FurBotNeo.utils.config.FurConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@CommandInfo(
        name = "Info",
        description = "Returns information about mentioned member."
)
@Author("Kuborros")
public class InfoCommand extends AdminCommand {

    //Emotes start
    private static final String NAMETAG = "\ud83d\udcdb";
    private static final String IDBADGE = "\ud83c\udd94";
    private static final String TIMER1 = "\u23f0";
    private static final String TIMER2 = "\u23f3";
    private static final String HAT = "\ud83c\udfa9";
    private static final String CROWNED = "\ud83d\udc51";
    private static final String ROBOT = "\uD83E\uDD16";
    private static final String FURRY = "\uD83D\uDC3E";
    //Emotes end


    public InfoCommand() {
        this.name = "info";
        this.help = "Shows info about specific user";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.KICK_MEMBERS};
    }

    @Override
    protected void doCommand(CommandEvent event) {

        FurConfig config = (FurConfig) event.getClient().getSettingsManager().getSettings(guild);

        if (event.getMessage().getMentionedUsers().isEmpty()) {
            event.reply("You must mention User to be scanned by the NSA!");
        } else {
            StringBuilder rolebuild = new StringBuilder();
            List<User> mentionedUsers = event.getMessage().getMentionedUsers();
            mentionedUsers.forEach((User user) -> {
                String roles;
                String ownerguy = "";
                String bot = "";
                Member member = guild.getMember(user);
                assert member != null;
                boolean me = member.getUser() == event.getJDA().getSelfUser();
                String name;
                if (!member.getRoles().isEmpty())
                    for (Role role : member.getRoles()) {
                        rolebuild.append(role.getName());
                        rolebuild.append(" , ");
                    }
                if (rolebuild.length() > 3) {
                    rolebuild.delete(rolebuild.length() - 3, rolebuild.length());
                } else rolebuild.append("None");
                roles = rolebuild.toString();
                if (member.isOwner()) {
                    ownerguy = CROWNED + " OWNER!";
                }
                if (member.getUser().isBot()) {
                    bot = me ? FURRY : ROBOT;
                    bot += "    A BOT!";
                }
                name = me ? Objects.requireNonNull(config).getBotName() : member.getEffectiveName();

                sendEmbed(event, "Data collected by NSA about: " + name, "What we know: \n"
                                + NAMETAG + "Full Discord name: " + member.getEffectiveName() + "#" + member.getUser().getDiscriminator() + "\n"
                                + IDBADGE + "User ID: " + member.getId() + "\n"
                                + TIMER1 + "Server join date: " + member.getTimeJoined().format(DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss")) + "\n"
                                + TIMER2 + "Discord join date: " + member.getUser().getTimeCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss")) + "\n"
                                + HAT + "Current roles: " + roles + "\n\n"
                        , member.getUser().getAvatarUrl()
                        , ownerguy + bot
                );
                rolebuild.delete(0, rolebuild.length());
            });
        }

    }

    private void sendEmbed(CommandEvent event, String title, String description, String imgUrl, String footer) {
        event.getChannel().sendMessage(
                new MessageBuilder().setEmbed(
                        new EmbedBuilder().setTitle(title, null).setThumbnail(imgUrl).setDescription(description).setColor(Color.BLUE).setFooter(footer, null).build()
                ).build()).queue();
    }
}
