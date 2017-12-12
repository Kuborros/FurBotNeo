/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.PicCommands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.jagrosh.jdautilities.menu.Slideshow;
import com.jagrosh.jdautilities.waiter.EventWaiter;
import com.kuborros.FurBotNeo.net.apis.PokemonApi;
import com.kuborros.FurBotNeo.net.apis.WebmPostException;
import net.dv8tion.jda.core.Permission;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.kuborros.FurBotNeo.BotMain.db;

/**
 *
 * @author Kuborros
 */
public class PokeCmd  extends Command{
   
    private Permission[] perms = {Permission.MESSAGE_EMBED_LINKS};
    private EventWaiter waiter;
    
    public PokeCmd(EventWaiter waiter){
        this.name = "poke";
        this.help = "Searches for _pictures_ on AGNPH";
        this.arguments = "<Tags>";
        this.guildOnly = true;
        this.ownerCommand = false;
        this.cooldown = 5;
        this.waiter = waiter;
        this.botPermissions = perms;
        this.category = new Category("ImageBoards"); 
        db.registerCommand(this.name);        
    }
    
    @Override
    protected void execute(CommandEvent event) {
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
                .setTimeout(5, TimeUnit.MINUTES);




        if (!event.getArgs().isEmpty()){
            api = new PokemonApi("http://gallerhy.agn.ph/gallery/post/?search=" + event.getArgs().replaceAll(" ", "+") + "+order:random" + "&api=xml");
         } else {
            api = new PokemonApi("http://gallerhy.agn.ph/gallery/post/?api=xml");
         }
                try {
                result = api.PokeXml();
                builder.setUrls(result.toArray(new String[result.size()]));
                } catch (IllegalArgumentException e){
                    event.reply("No results found!");                    
                    return;
                } catch (WebmPostException e){
                    event.reply("Only results are webms!");                    
                    return;
                }
        builder.build().display(event.getTextChannel());
        }
}

    

