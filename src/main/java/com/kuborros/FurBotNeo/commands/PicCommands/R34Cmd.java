/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.PicCommands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import com.jagrosh.jdautilities.menu.Slideshow;
import com.jagrosh.jdautilities.waiter.EventWaiter;
import com.kuborros.FurBotNeo.net.apis.GelEngine;
import net.dv8tion.jda.core.Permission;
import org.json.JSONException;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.kuborros.FurBotNeo.BotMain.db;

/**
 *
 * @author Kuborros
 */
public class R34Cmd extends Command{
   
    private Permission[] perms = {Permission.MESSAGE_EMBED_LINKS};
    private EventWaiter waiter;
    
    public R34Cmd(EventWaiter waiter){
        this.name = "r34";
        this.help = "Searches for _pictures_ on R34";
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
                .setDescription("R34")
                .setTimeout(5, TimeUnit.MINUTES);



        if (!event.getArgs().isEmpty()){
                api = new GelEngine("https://rule34.xxx/index.php?page=dapi&s=post&q=index&tags=" + event.getArgs().replaceAll(" ", "+") + "&limit=20");
        } else {
                api = new GelEngine("https://rule34.xxx/index.php?page=dapi&s=post&q=index&limit=20");
        }
                try {
                result = api.getGelPic();
                builder.setUrls(result.toArray(new String[result.size()]));
                } catch (JSONException e) {
                    event.reply("No results found!");
                    return;
                }
        builder.build().display(event.getTextChannel());
        }
}



