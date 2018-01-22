/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.PicCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Slideshow;
import com.kuborros.FurBotNeo.net.apis.GelEngine;
import com.kuborros.FurBotNeo.net.apis.NoImgException;
import net.dv8tion.jda.core.Permission;
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
public class GelCmd extends Command{

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
    protected void execute(CommandEvent event) {
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



        if (!event.getArgs().isEmpty()){
                api = new GelEngine("https://gelbooru.com/index.php?page=dapi&s=post&q=index&tags=" + event.getArgs().replaceAll(" ", "+") + "&limit=20");
         } else {
            api = new GelEngine("https://gelbooru.com/index.php?page=dapi&s=post&q=index&limit=20");
        }
                try {
                result = api.getGelPic();
                builder.setUrls(result.toArray(new String[result.size()]));
                } catch (NoImgException e) {
                    event.reply("No results found!");                    
                    return;
                } catch (ParserConfigurationException | IOException | SAXException e) {
                    event.replyError(e.getLocalizedMessage());
                    return;
                }
        builder.build().display(event.getTextChannel());
    }

    
}
