

package com.kuborros.FurBotNeo.commands.PicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.jagrosh.jdautilities.menu.Slideshow;
import com.kuborros.FurBotNeo.net.apis.E621Api;
import com.kuborros.FurBotNeo.net.apis.NoImgException;
import net.dv8tion.jda.api.Permission;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.kuborros.FurBotNeo.BotMain.db;

@CommandInfo(
        name = "E926",
        description = "Searches for sfw images on E926 (Safe for work variant of E621)."
)
@Author("Kuborros")
public class E926Cmd extends PicCommand {

    private final EventWaiter waiter;

    public E926Cmd(EventWaiter waiter) {
        this.name = "e926";
        this.help = "Searches for safe for work pictures on E926";
        this.arguments = "<Tags>";
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
        E621Api api;
        List<String> result;
        Slideshow.Builder builder = new Slideshow.Builder();
        db.updateCommandStats(event.getAuthor().getId(), this.name);

        builder.allowTextInput(false)
                .setBulkSkipNumber(5)
                .waitOnSinglePage(false)
                .setColor(Color.PINK)
                .setEventWaiter(waiter)
                .setText("")
                .setDescription("E621")
                .setFinalAction(message -> message.clearReactions().queue())
                .setTimeout(5, TimeUnit.MINUTES);

        api = new E621Api("https://e926.net/post/index.json?tags=");

        try {
            result = !event.getArgs().isEmpty() ? api.getImageSetTags(event.getArgs()) : api.getImageSetRandom();
            builder.setUrls(result.toArray(new String[0]));
        } catch (NoImgException e) {
            event.reply("No results found!");
            return;
        } catch (IOException e) {
            event.reply("Something went wrong! ```" + e.getLocalizedMessage() + "```");
            return;
        }
        builder.build().display(event.getTextChannel());
    }

}
