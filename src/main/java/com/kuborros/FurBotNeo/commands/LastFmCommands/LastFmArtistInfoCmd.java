package com.kuborros.FurBotNeo.commands.LastFmCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import de.umass.lastfm.Artist;
import de.umass.lastfm.Caller;
import de.umass.lastfm.ImageSize;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;

public class LastFmArtistInfoCmd extends LastFmCommand {


    public LastFmArtistInfoCmd() {
        {
            this.name = "lastartist";
            this.arguments = "<Artist name>";
            this.help = "Shows basic info about artist";
            this.guildOnly = true;
        }
    }

    @Override
    protected void doCommand(CommandEvent event) {
        if (event.getArgs().isEmpty()) {
            event.replyError("Provide a correct name");
            return;
        }
        Artist artist = Artist.getInfo(event.getArgs(), key);
        if (Caller.getInstance().getLastResult().isSuccessful()) {

            EmbedBuilder eb = new EmbedBuilder()
                    .setColor(Color.RED)
                    .setTitle("Last.fm info for Artist: " + event.getArgs(), artist.getUrl())
                    .addField("Artist name: ", "" + artist.getName(), true)
                    .addField("About: ", "" + artist.getWikiSummary(), false)
                    .addField("Listeners: ", String.valueOf(artist.getListeners()), true)
                    .addField("Playcount: ", String.valueOf(artist.getPlaycount()), true)
                    .addField("Wiki last updated: ", format.format(artist.getWikiLastChanged()), true)
                    .addField("Can be streamed: ", artist.isStreamable() ? "Yes" : "No", true);

            if (!artist.getImageURL(ImageSize.MEDIUM).isEmpty()) {
                eb.setThumbnail(artist.getImageURL(ImageSize.MEDIUM));
            }
            event.reply(eb.build());

        }
    }
}