package com.kuborros.FurBotNeo.commands.LastFmCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import de.umass.lastfm.Caller;
import de.umass.lastfm.User;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;

public class LastFmUserInfoCmd extends LastFmCommand {


    public LastFmUserInfoCmd() {
        {
            this.name = "lastinfo";
            this.arguments = "<Username>";
            this.help = "Shows basic info about user";
            this.guildOnly = true;
        }
    }

    @Override
    protected void doCommand(CommandEvent event) {
        if (event.getArgs().isEmpty()) {
            event.replyError("Provide a correct username");
            return;
        }
        User user = User.getInfo(event.getArgs(), key);
        if (Caller.getInstance().getLastResult().isSuccessful()) {
            String gender;
            String age = String.valueOf(user.getAge()).equals("0") ? "Not set" : "" + String.valueOf(user.getAge());
            String country = user.getCountry().isEmpty() ? "Not set" : "" + user.getCountry();
            String name = user.getRealname() == null ? "Not set" : "" + user.getRealname();
            switch (user.getGender()) {
                case "m":
                    gender = "♂ Male️";
                    break;
                case "f":
                    gender = "♀️ Female";
                    break;
                default:
                    gender = "Not set";
                    break;
            }
            EmbedBuilder eb = new EmbedBuilder()

                    .setColor(Color.RED)
                    .setTitle("Last.fm info for user: " + event.getArgs(), user.getUrl())
                    .addField("Username: ", "" + user.getName(), true)
                    .addField("Real Name: ", name, true)
                    .addField("Gender: ", gender, true)
                    .addField("Country: ", country, true)
                    .addField("Age: ", age, true)
                    .addField("Playlists: ", String.valueOf(user.getNumPlaylists()), true)
                    .addField("Playcount: ", String.valueOf(user.getPlaycount()), true)
                    .addField("Date joined: ", format.format(user.getRegisteredDate()), true)
                    .addField("Is subscribed: ", user.isSubscriber() ? "Yes" : "No", true);

            if (!user.getImageURL().isEmpty()) {
                eb.setThumbnail(user.getImageURL());
            }
            event.reply(eb.build());
        } else event.replyError("User not found.");
    }
}
