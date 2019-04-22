/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.kuborros.FurBotNeo.utils.audio.AudioInfo;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author Kuborros
 */
public class MusicQueueCmd extends MusicCommand{

    
    public MusicQueueCmd()
    {
        this.name = "queue";
        this.aliases = new String[]{"playlist"};
        this.help = "Shows current playlist";
        this.category = new Category("Music");
}
    @Override
    public void doCommand(CommandEvent event){

        /* Paginator implementation, looks worse than old one, might be more usefull however

        Paginator.Builder builder = new Paginator.Builder();

        if (!hasPlayer(guild) || getTrackManager(guild).getQueuedTracks().isEmpty()) {
            event.reply(NOTE + "The queue is currently empty!");
            return;
        }

        builder.allowTextInput(false)
                .setBulkSkipNumber(5)
                .waitOnSinglePage(false)
                .setColor(Color.GREEN)
                .setItemsPerPage(10)
                .useNumberedItems(true)
                .setEventWaiter(waiter)
                .setText("**Current queue is:**")
                .setFinalAction(message -> message.clearReactions().queue())
                .setTimeout(1, TimeUnit.MINUTES);


        Set<AudioInfo> queue = getTrackManager(guild).getQueuedTracks();
        ArrayList<String> tracks = new ArrayList<>();
        queue.forEach(audioInfo -> tracks.add(buildQueueMessage(audioInfo)));

        builder.addItems(tracks.toArray(new String[0]));

        builder.build().display(event.getTextChannel());

         */

        EmbedBuilder eb = new EmbedBuilder();

        if (!hasPlayer(guild) || getTrackManager(guild).getQueuedTracks().isEmpty()) {
            event.reply(NOTE + "The queue is currently empty!");
        } else {

            StringBuilder sb = new StringBuilder();
            Set<AudioInfo> queue = getTrackManager(guild).getQueuedTracks();
            ArrayList<String> tracks = new ArrayList<>();

            queue.forEach(audioInfo -> tracks.add(buildQueueMessage(audioInfo)));

            tracks.forEach(sb::append);

            eb.setColor(Color.GREEN).setDescription(
                    NOTE + "**QUEUE**\n\n" +
                            "*[" + queue.size() + " Tracks" + "]*\n\n" +
                    sb
            );

            event.reply(eb.build());

        }
    }


}