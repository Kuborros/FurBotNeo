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

import java.util.Arrays;
import java.util.List;

@CommandInfo(
        name = "BigText",
        description = "Writes entered text using big letter emojis."
)
@Author("Kuborros")

public class BigTextCmd extends GeneralCommand {
    private final StringBuilder result = new StringBuilder();
    //Galaxy brain right here
    private static final List<String> alphabet = Arrays.asList("abcdefghijklmnopqrstuvwxyz".split(""));

    public BigTextCmd() {
        this.name = "bigtext";
        this.help = "Makes your words huge!";
        this.arguments = "<Words>";
        this.guildOnly = true;
        this.category = new Command.Category("Basic");
    }

    @Override
    protected void doCommand(CommandEvent event) {
        if (event.getArgs().isEmpty()) {
            event.replyWarning("Put something in to biggyfy!");
            return;
        }
        String[] text = event.getArgs().toLowerCase().split("");
        for (String letter : text) {
            if (letter.equalsIgnoreCase(" ")) result.append(":black_large_square:");
            else if (letter.equalsIgnoreCase(".")) result.append(":black_small_square:");
            else if (letter.equalsIgnoreCase("!")) result.append(":exclamation:");
            else if (letter.equalsIgnoreCase("?")) result.append(":question:");
            else if (letter.equalsIgnoreCase("-") || letter.equalsIgnoreCase("_")) result.append(":heavy_minus_sign:");
            else if (alphabet.contains(letter)) {
                result.append(":regional_indicator_").append(letter).append(":");
            }
        }
        event.reply(result.toString());
        result.delete(0, result.length());
    }
}