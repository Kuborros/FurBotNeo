package com.kuborros.FurBotNeo.commands.GeneralCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.Arrays;
import java.util.List;

public class BigTextCmd extends Command {
    private final StringBuilder result = new StringBuilder();
    private static final List<String> alphabet = Arrays.asList("abcdefghijklmnopqrstuvwxyz".split(""));

    public BigTextCmd() {
        this.name = "bigtext";
        this.help = "Makes your words huge!";
        this.arguments = "<Words>";
        this.guildOnly = true;
        this.category = new Command.Category("Basic");
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getArgs().isEmpty()) {
            event.replyError("Put something in to biggyfy!");
            return;
        }
        String[] text = event.getArgs().toLowerCase().split("");
        List<String> toBiggyfy = Arrays.asList(text);
        for (String letter : toBiggyfy) {
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