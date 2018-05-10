package com.kuborros.FurBotNeo.commands.LastFmCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Paginator;
import de.umass.lastfm.Artist;
import de.umass.lastfm.Caller;
import de.umass.lastfm.Tag;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LastFmTopArtCmd extends LastFmCommand {

    private final EventWaiter waiter;

    public LastFmTopArtCmd(EventWaiter waiter) {
        this.name = "lasttop";
        this.arguments = "<Tag>";
        this.help = "Shows top 10 current scrobbled artists";
        this.guildOnly = true;
        this.waiter = waiter;
    }

    @Override
    protected void doCommand(CommandEvent event) {
        if (event.getArgs().isEmpty()) {
            event.replyError("Provide a correct tag");
        }
        Collection<Artist> topArtists = Tag.getTopArtists(event.getArgs(), key);
        if (Caller.getInstance().getLastResult().isSuccessful() && !topArtists.isEmpty()) {
            List<String> topArt = new ArrayList<>();

            Paginator.Builder builder = new Paginator.Builder();

            builder.showPageNumbers(true)
                    .setBulkSkipNumber(5)
                    .allowTextInput(false)
                    .setColor(Color.RED)
                    .setEventWaiter(waiter)
                    .setText("")
                    .useNumberedItems(true)
                    .setItemsPerPage(10)
                    .setFinalAction(message -> message.clearReactions().queue())
                    .setTimeout(5, TimeUnit.MINUTES);

            for (Artist artist : topArtists) {
                topArt.add(artist.getName());
            }
            builder.addItems(topArt.toArray(new String[0]));

            builder.build().display(event.getTextChannel());
        } else event.replyError("Unable to find top artists for this tag.");
    }
}
