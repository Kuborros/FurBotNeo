/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.PicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Slideshow;
import com.kuborros.FurBotNeo.net.apis.DanApi;
import com.kuborros.FurBotNeo.net.apis.NoImgException;
import net.dv8tion.jda.core.Permission;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.kuborros.FurBotNeo.BotMain.db;

/**
 *
 * @author Kuborros
 */
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
        this.hidden = true;
        this.waiter = waiter;
        db.registerCommand(this.name);
    }

    @Override
    protected void doCommand(CommandEvent event) {
        Slideshow.Builder builder = new Slideshow.Builder();
        DanApi api;
        List<String> result;
        db.updateCommandStats(event.getAuthor().getId(), this.name);

        if (!event.getTextChannel().isNSFW()) {
            event.replyWarning("This command works only on NSFW channels! (For obvious reasons)");
            return;
        }

        List<String> tags = Arrays.asList(event.getArgs().split(" "));
        if (tags.size() > 2) {
            tags = tags.subList(0, 2);
        }

        builder.allowTextInput(false)
            .setBulkSkipNumber(5)
            .waitOnSinglePage(false)
            .setColor(Color.PINK)
            .setEventWaiter(waiter)
            .setText("")
            .setDescription("Danbooru")
                .setFinalAction(message -> message.clearReactions().queue())
            .setTimeout(5, TimeUnit.MINUTES);




        if (!event.getArgs().isEmpty()) {
            api = new DanApi("https://danbooru.donmai.us/posts.json?tags=" + String.join("+", tags) + "&random=true&limit=20");
        } else {
            api = new DanApi("https://danbooru.donmai.us/posts.json?random=true&limit=20");
        }
            try {
                result = api.getDanPic();
                builder.setUrls(result.toArray(new String[result.size()]));
            } catch (NoImgException e) {
                event.reply("No results found!");
                return;
            } catch (IOException e) {
                event.replyError(e.getLocalizedMessage());
            }
            Slideshow show = builder.build();
            show.display(event.getTextChannel());
    }
}