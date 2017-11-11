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
public class DiceCmd extends Command{
    StringBuilder rolls = new StringBuilder();
    int numSides = 20;
    int numDices = 1;
    Random rand = new Random();
    
    
    public DiceCmd()
    {
        this.name = "roll";
        this.help = "Shows profile pic of mentioned user!";
        this.arguments = "Basic: <number of sides> Advanced: <how many>d<sides>";
        this.guildOnly = true;        
        this.category = new Command.Category("Basic"); 
}  
    @Override
    public void execute(CommandEvent event){  
        String dice = event.getArgs().toLowerCase();
        if (dice.contains("d")){
            String[] aDice = dice.split("d");
            try {
                numDices = Integer.parseInt(aDice[0]);
                if (numDices > 20) numDices = 20;
                numSides = Integer.parseInt(aDice[1]);
                if (numSides > 255) numSides = 255;
            }
            catch (NumberFormatException e) {
                event.replyError("Please enter a valid set of numbers!");
                return;                
            }
            int i = 0;
            do {
                int side = rand.nextInt(numSides) + 1;
                rolls.append(Integer.toString(side)).append(",");
                i++;
            } while (i < numDices);
            event.reply("You rolled: " + rolls.toString());
            rolls.delete(0, rolls.length());
        }
        else {
            try {
                numSides = Integer.parseInt(dice);
                if (numSides > 255) numSides = 255;
            } catch (NumberFormatException e) {
                event.replyError("Please enter a valid number!");
                return;
            }
            int roll = rand.nextInt(numSides) + 1;
            event.reply("You rolled: " + roll); 
        }
        
    }    
}
