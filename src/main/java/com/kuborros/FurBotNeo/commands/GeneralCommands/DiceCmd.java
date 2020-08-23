
/*
 * Copyright © 2020 Kuborros (kuborros@users.noreply.github.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kuborros.FurBotNeo.commands.GeneralCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;

import java.util.Random;

@CommandInfo(
        name = "Dice",
        description = "Throws number of multi sided dice."
)
@Author("Kuborros")
public class DiceCmd extends GeneralCommand {
    private final StringBuilder rolls = new StringBuilder();
    private final Random rand = new Random();


    public DiceCmd() {
        this.name = "roll";
        this.help = "Rolls a dice!";
        this.arguments = "Basic: <number of sides> Advanced: <how many>d<sides>";
        this.guildOnly = true;
        this.category = new Command.Category("Basic");
    }

    @Override
    public void doCommand(CommandEvent event) {
        String dice = event.getArgs().toLowerCase();
        int numSides;
        if (dice.contains("d")) {
            String[] aDice = dice.split("d");
            int numDices;
            try {
                numDices = Integer.parseInt(aDice[0]);
                if (numDices > 20) numDices = 20;
                numSides = Integer.parseInt(aDice[1]);
                if (numSides > 255) numSides = 255;
            } catch (NumberFormatException e) {
                event.replyWarning("Please enter a valid set of numbers!");
                return;
            }
            int i = 0;
            do {
                int side = rand.nextInt(numSides) + 1;
                rolls.append(side).append(",");
                i++;
            } while (i < numDices);
            event.reply("You rolled: " + rolls);
            rolls.delete(0, rolls.length());
        } else {
            try {
                numSides = Integer.parseInt(dice);
                if (numSides > 255) numSides = 255;
            } catch (NumberFormatException e) {
                event.replyWarning("Please enter a valid number!");
                return;
            }
            int roll = rand.nextInt(numSides) + 1;
            event.reply("You rolled: " + roll);
        }

    }    
}
