/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.PicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
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

/**
 *
 * @author Kuborros
 */
public class GelCmd extends PicCommand {

    private final EventWaiter waiter;
    
    public GelCmd(EventWaiter waiter){
        this.name = "gel";
        this.help = "Searches for _pictures_ on GelBooru";
        this.arguments = "<Tags>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.cooldown = 5;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.waiter = waiter;
        this.category = new Category("ImageBoards");  
        db.registerCommand(this.name);        
    }
    
    @Override
    protected void doCommand(CommandEvent event) {
        GelEngine api;
        List<String> result;
        Slideshow.Builder builder = new Slideshow.Builder();
        db.updateCommandStats(event.getAuthor().getId(), this.name);        

        if (!event.getTextChannel().isNSFW()){
            event.replyWarning("This command works only on NSFW channels! (For obvious reasons)");
            return;
        }

        builder.allowTextInput(false)
                .setBulkSkipNumber(5)
                .waitOnSinglePage(false)
                .setColor(Color.PINK)
                .setEventWaiter(waiter)
                .setText("")
                .setDescription("Gelbooru")
                .setFinalAction(message -> message.clearReactions().queue())
                .setTimeout(5, TimeUnit.MINUTES);


        api = new GelEngine("https://gelbooru.com/index.php?page=dapi&s=post&q=index&limit=20");

        try {
            if (!event.getArgs().isEmpty()) {
                result = api.getImageSetTags(event.getArgs());
            } else {
                result = api.getImageSetRandom();
            }
                    builder.setUrls(result.toArray(new String[0]));
                } catch (NoImgException e) {
                    event.reply("No results found!");                    
                    return;
                } catch (ParserConfigurationException | IOException | SAXException e) {
                    event.replyError("Something went wrong! ```\\n\" " + e.getLocalizedMessage() + "\"\\n```\"");
                    return;
                }
        builder.build().display(event.getTextChannel());
    }

    
}
