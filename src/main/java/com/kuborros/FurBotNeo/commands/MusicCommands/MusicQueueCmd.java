
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

package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.jagrosh.jdautilities.menu.Paginator;
import com.kuborros.FurBotNeo.utils.audio.AudioInfo;
import net.dv8tion.jda.api.Permission;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@CommandInfo(
        name = "MusicQueue",
        description = "Lists tracks currently in the playback queue."
)
@Author("Kuborros")
public class MusicQueueCmd extends MusicCommand {

    private final EventWaiter waiter;

    public MusicQueueCmd(EventWaiter waiter) {
        this.name = "queue";
        this.aliases = new String[]{"playlist"};
        this.help = "Shows current playlist";
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK};
        this.category = new Category("Music");
        this.waiter = waiter;
    }

    @Override
    public void doCommand(CommandEvent event) {

        if (!hasPlayer(guild) || getTrackManager(guild).getQueuedTracks().isEmpty()) {

            event.reply(sendGenericEmbed("The queue is currently empty!", ""));

        } else if (getTrackManager(guild).getQueuedTracks().size() < 20) {

            //Prettier queue listing, for up to 20 tracks

            StringBuilder sb = new StringBuilder();
            Set<AudioInfo> queue = getTrackManager(guild).getQueuedTracks();
            ArrayList<String> tracks = new ArrayList<>(queue.size());

            queue.forEach(audioInfo -> tracks.add(buildQueueMessage(audioInfo)));

            tracks.forEach(sb::append);

            event.reply(sendGenericEmbed("**QUEUE**",
                    "*[" + queue.size() + " Tracks" + "]*\n\n" + sb
            ));

        } else {

            //Over 20 tracks we switch to paginated output to avoid super long embeds

            Paginator.Builder builder = new Paginator.Builder();

            builder.allowTextInput(false)
                    .setBulkSkipNumber(5)
                    .waitOnSinglePage(false)
                    .setColor(Color.GREEN)
                    .setItemsPerPage(20)
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
        }
    }


}