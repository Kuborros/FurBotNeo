/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.PicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Slideshow;
import com.kuborros.FurBotNeo.net.apis.NoImgException;
import com.kuborros.FurBotNeo.net.apis.PokemonApi;
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
public class PokeCmd extends PicCommand {

    private final EventWaiter waiter;
    
    public PokeCmd(EventWaiter waiter){
        this.name = "poke";
        this.help = "Searches for _pictures_ on AGNPH";
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
        PokemonApi api;
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
                .setDescription("AGNPH")
                .setFinalAction(message -> message.clearReactions().queue())
                .setTimeout(5, TimeUnit.MINUTES);




        if (!event.getArgs().isEmpty()){
            api = new PokemonApi("http://gallerhy.agn.ph/gallery/post/?search=" + event.getArgs().replaceAll(" ", "+") + "+order:random" + "&api=xml");
         } else {
            api = new PokemonApi("http://gallerhy.agn.ph/gallery/post/?api=xml");
         }
                try {
                result = api.PokeXml();
                    builder.setUrls(result.toArray(new String[0]));
                } catch (NoImgException e) {
                    event.reply("No results found!");
                    return;
                } catch (IOException | ParserConfigurationException | SAXException ex) {
                    event.replyError("Something went wrong! ```\\n\" " + ex.getLocalizedMessage() + "\"\\n```\"");
                }
        builder.build().display(event.getTextChannel());
        }
}

    

