/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.AdminCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author Kuborros
 */
public class InfoCommand extends AdminCommand {
    
    //Emotes start
    private static final String NAMETAG = "\ud83d\udcdb";
    private static final String IDBADGE = "\ud83c\udd94";
    private static final String TIMER1 = "\u23f0";
    private static final String TIMER2 = "\u23f3";
    private static final String GLOBE = "\ud83c\udf10";
    private static final String GAMEPAD = "\ud83c\udfae";
    private static final String HAT = "\ud83c\udfa9";
    private static final String CROWNED = "\ud83d\udc51";
    //Emotes end
    
    
     public InfoCommand()
    {
        this.name = "info";
        this.help = "Shows info about specific user";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.category = new Category("Moderation");
        this.userPermissions = new Permission[]{Permission.KICK_MEMBERS};
    }

    @Override
    protected void doCommand(CommandEvent event) {
        
        if (event.getMessage().getMentionedUsers().isEmpty())
                {
                    event.reply("You must mention User to be scanned by the NSA!");
                }
                else
                {
                    StringBuilder rolebuild = new StringBuilder();
                    List<User> mentionedUsers = event.getMessage().getMentionedUsers();
                    mentionedUsers.forEach((User user) -> {
                        String roles;
                        String online = "OFFLINE";
                        String game = "None";
                        String ownerguy = "";                       
                        Member member = event.getGuild().getMember(user);
                        assert member != null;
                        if (!member.getRoles().isEmpty())
                            for (Role role : member.getRoles()) {
                                rolebuild.append(role.getName());
                                rolebuild.append(" , ");
                            }
                          if (rolebuild.length() >3 ) {
                              rolebuild.delete(rolebuild.length()-3, rolebuild.length());
                          }
                          else rolebuild.append("None");
                          roles = rolebuild.toString();
                        if (!member.getOnlineStatus().equals(OnlineStatus.OFFLINE)){
                            online = "ONLINE";
                        }
                        if (member.getActivities().isEmpty()) {
                            game = member.getActivities().get(0).getName();
                        }
                        if (member.isOwner()){
                            ownerguy = CROWNED + " OWNER!";
                        }
                        sendEmbed(event, "Data collected by NSA about: " + member.getEffectiveName(), "What we know: \n"
                                + NAMETAG + "Full Discord name: " + member.getEffectiveName() + "#" + member.getUser().getDiscriminator() + "\n"
                                        + IDBADGE + "User ID: " + member.getId() + "\n"
                                        + TIMER1 + "Server join date: " + member.getTimeJoined().format(DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss")) + "\n"
                                        + TIMER2 + "Discord join date: " + member.getUser().getTimeCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss")) + "\n"
                                + GLOBE + "Status: "+ online +"\n"
                                + GAMEPAD + "In game: " + game +"\n"
                                + HAT + "Current roles: "+ roles +"\n\n"
                                , member.getUser().getAvatarUrl()
                                , ownerguy
                        );
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
