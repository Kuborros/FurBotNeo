/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package com.kuborros.FurBotNeo.commands.GeneralCommands;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;
import java.util.Random;

/**
 *
 * @author Kuborros
 */
public class R8BallCmd extends Command{

    public R8BallCmd()
    {
        this.name = "8ball";
        this.help = "Magic 8ball that knows all!";
        this.guildOnly = true;        
        this.category = new Command.Category("Basic"); 
}
    @Override
    public void execute(CommandEvent event){
        Random rand = new Random();
        int roll = rand.nextInt(8);
            switch(roll){
                case 0:
                event.reply("Yes");
                break;
                case 1:
                event.reply("No");
                break;
                case 2:
                event.reply("Maybe");
                break;
                case 3:
                event.reply("Rather not");
                break;
                case 4:
                event.reply("Rather yes");
                break;
                case 5:
                event.reply("nope.avi");
                break;
                case 6:
                event.reply("Definietly yes!");
                break;
                case 7:
                event.reply("Absolutely not!");
                break;                
                default:
                event.reply("Rather Notte~");
                break;
            }
    }
    
}
