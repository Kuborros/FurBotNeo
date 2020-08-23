
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
        name = "8ball",
        description = "Basic magic 8ball command."
)
@Author("Kuborros")
public class R8BallCmd extends GeneralCommand {

    public R8BallCmd() {
        this.name = "8ball";
        this.help = "Magic 8ball that knows all!";
        this.guildOnly = true;
        this.category = new Command.Category("Basic");
    }

    @Override
    public void doCommand(CommandEvent event) {
        Random rand = new Random();
        int roll = rand.nextInt(8);
        switch (roll) {
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
                event.reply("Definitely yes!");
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
