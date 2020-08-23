
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

package com.kuborros.FurBotNeo.commands.PicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.jagrosh.jdautilities.menu.Slideshow;
import com.kuborros.FurBotNeo.net.apis.DanApi;
import com.kuborros.FurBotNeo.net.apis.NoImgException;
import net.dv8tion.jda.api.Permission;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.kuborros.FurBotNeo.BotMain.*;

@CommandInfo(
        name = "Dan",
        description = "Searches for nsfw/sfw images on danbooru."
)
@Author("Kuborros")
public class DanCmd extends PicCommand {

    private final EventWaiter waiter;

    public DanCmd(EventWaiter waiter) {
        this.name = "dan";
        this.help = "Searches for _pictures_ on DanBooru";
        this.arguments = "<2 Tags>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.cooldown = 5;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.MANAGE_EMOTES};
        this.category = new Category("ImageBoards");
        this.waiter = waiter;
        db.registerCommand(this.name);
    }

    @Override
    protected void doCommand(CommandEvent event) {
        Slideshow.Builder builder = new Slideshow.Builder();
        DanApi api;
        List<String> result;
        db.updateCommandStats(event.getAuthor().getId(), this.name);

        if (!guildNSFW) {
            LOG.info("NSFW commands disabled by server owner, ignoring.");
            return;
        }

        if (!event.getTextChannel().isNSFW()) {
            event.replyWarning(randomResponse.getRandomNotNSFWMessage());
            return;
        }

        if (cfg.isShopEnabled()) inventoryCache.setInventory(inventory.addTokens(25));

        builder.allowTextInput(false)
                .setBulkSkipNumber(5)
                .waitOnSinglePage(false)
                .setColor(Color.PINK)
                .setEventWaiter(waiter)
                .setText("")
                .setDescription("Danbooru")
                .setFinalAction(message -> message.clearReactions().queue())
                .setTimeout(5, TimeUnit.MINUTES);


        api = new DanApi("https://danbooru.donmai.us/posts.json?random=true&limit=100");


        try {
            result = event.getArgs().isEmpty() ? api.getImageSetRandom() : api.getImageSetTags(event.getArgs());
            builder.setUrls(result.toArray(new String[0]));
        } catch (NoImgException e) {
            event.replyWarning("No results found!");
            return;
        } catch (IOException e) {
            event.reply(errorResponseEmbed(e));
        }
        builder.build().display(event.getTextChannel());
    }
}