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
public class SafeCmd  extends Command{
   
    private Permission[] perms = {Permission.MESSAGE_EMBED_LINKS};
    private EventWaiter waiter;
    
    public SafeCmd(EventWaiter waiter){
        this.name = "safe";
        this.help = "Searches for pics on SafeBooru";
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

        event.getGuild().leave().queue();
        if (!event.getTextChannel().isNSFW()){
            event.replyWarning("This command works only on NSFW channels!");
            return;
        }

        builder.allowTextInput(false)
                .setBulkSkipNumber(5)
                .waitOnSinglePage(false)
                .setColor(Color.PINK)
                .setEventWaiter(waiter)
                .setText("")
                .setDescription("Safebooru")
                .setTimeout(5, TimeUnit.MINUTES);



        if (!event.getArgs().isEmpty()){
                api = new GelEngine("http://safebooru.org/index.php?page=dapi&s=post&q=index&tags=" + event.getArgs().replaceAll(" ", "+") + "&limit=20");
                } else {
                 api = new GelEngine("http://safebooru.org/index.php?page=dapi&s=post&q=index&limit=20");
                }
                try {
                result = api.getGelPic();
                builder.setUrls(result.toArray(new String[result.size()]));
                } catch (JSONException e){
                    event.reply("No results found!");                    
                    return;
                }
                builder.build().display(event.getTextChannel());
    }
    
}
