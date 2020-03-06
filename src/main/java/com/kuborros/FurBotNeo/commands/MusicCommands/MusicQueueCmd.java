
package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.kuborros.FurBotNeo.utils.audio.AudioInfo;
import net.dv8tion.jda.api.Permission;

import java.util.ArrayList;
import java.util.Set;

@CommandInfo(
        name = "MusicQueue",
        description = "Lists tracks currently in the playback queue."
)
@Author("Kuborros")
public class MusicQueueCmd extends MusicCommand{

    
    public MusicQueueCmd()
    {
        this.name = "queue";
        this.aliases = new String[]{"playlist"};
        this.help = "Shows current playlist";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK};
        this.category = new Category("Music");
}
    @Override
    public void doCommand(CommandEvent event){

        /* Paginator implementation, looks worse than old one, might be more useful however

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

        if (!hasPlayer(guild) || getTrackManager(guild).getQueuedTracks().isEmpty()) {
            event.reply(sendGenericEmbed("The queue is currently empty!", ""));
        } else {

            StringBuilder sb = new StringBuilder();
            Set<AudioInfo> queue = getTrackManager(guild).getQueuedTracks();
            ArrayList<String> tracks = new ArrayList<>(queue.size());

            queue.forEach(audioInfo -> tracks.add(buildQueueMessage(audioInfo)));

            tracks.forEach(sb::append);

            event.reply(sendGenericEmbed("**QUEUE**",
                    "*[" + queue.size() + " Tracks" + "]*\n\n" + sb
            ));

        }
    }


}