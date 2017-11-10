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
    int numSides;
    int numDices;
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
        if (dice.isEmpty()) return; //add stuff
        if (dice.contains("d")){
            String[] aDice = dice.split("d");
            try {
                numDices = Integer.parseInt(aDice[0]);
                numSides = Integer.parseInt(aDice[1]);
            }
            catch (NumberFormatException e) {
                event.replyError("stuff");
                return;                
            }
            int i = 0;
            while (i <= numDices) {
                int side = rand.nextInt(numSides);
                rolls.append(Integer.toString(side)).append(",");
                i++;
            }
            event.reply("You rolled:" + rolls.toString());
        }
        else {
            try {
                numSides = Integer.parseInt(dice);
            } catch (NumberFormatException e) {
                event.replyError("stuff");
                return;
            }
            int roll = rand.nextInt(numSides) + 1;
            event.reply("You rolled :" + roll); 
        }
        
    }    
}
