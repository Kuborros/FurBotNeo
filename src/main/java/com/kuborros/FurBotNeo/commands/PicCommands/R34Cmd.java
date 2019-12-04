
package com.kuborros.FurBotNeo.commands.PicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.jagrosh.jdautilities.menu.Slideshow;
import com.kuborros.FurBotNeo.net.apis.GelEngine;
import com.kuborros.FurBotNeo.net.apis.NoImgException;
import net.dv8tion.jda.api.Permission;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.kuborros.FurBotNeo.BotMain.db;

@CommandInfo(
        name = "R34",
        description = "Searches for nsfw/sfw images on R34.xxx."
)
@Author("Kuborros")
public class R34Cmd extends PicCommand {

    private final EventWaiter waiter;
    
    public R34Cmd(EventWaiter waiter){
        this.name = "r34";
        this.help = "Searches for _pictures_ on R34";
        this.arguments = "<Tags>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.cooldown = 5;
        this.waiter = waiter;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.category = new Category("ImageBoards"); 
        db.registerCommand(this.name);        
    }
    
    @Override
    protected void doCommand(CommandEvent event) {
        GelEngine api;
        List<String> result;
        Slideshow.Builder builder = new Slideshow.Builder();
        db.updateCommandStats(event.getAuthor().getId(), this.name);

        if (!guildNSFW) {
            LOG.info("Image commands disabled by server owner, ignoring.");
            return;
        }

        if (!event.getTextChannel().isNSFW()) {
            event.replyWarning("This command works only on NSFW channels! (For obvious reasons)");
            return;
        }

        builder.allowTextInput(false)
                .setBulkSkipNumber(5)
                .waitOnSinglePage(false)
                .setColor(Color.PINK)
                .setEventWaiter(waiter)
                .setText("")
                .setDescription("R34")
                .setFinalAction(message -> message.clearReactions().queue())
                .setTimeout(5, TimeUnit.MINUTES);


        api = new GelEngine("https://rule34.xxx/index.php?page=dapi&s=post&q=index&limit=100");


        try {
            result = !event.getArgs().isEmpty() ? api.getImageSetTags(event.getArgs()) : api.getImageSetRandom();
            builder.setUrls(result.toArray(new String[0]));
        } catch (NoImgException e) {
            event.replyWarning("No results found!");
            return;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            event.reply(errorResponseEmbed(e));
            return;
        }
        builder.build().display(event.getTextChannel());
    }
}



