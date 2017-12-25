/*
 * Here comes the text of your license
 * Each line should be prefixed with  *
 */
package com.kuborros.FurBotNeo.commands.AdminCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author Kuborros
 */
public class EvalCommand extends Command {

    private final ScriptEngine engine;


    public EvalCommand() {
        this.name = "eval";
        this.help = "Evaluates nashorn code";
        this.ownerCommand = true;
        this.guildOnly = true;
        this.arguments = "<code>";
        this.category = new Category("Moderation");

        engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            engine.eval("var imports = new JavaImporter(" +
                    "java.io," +
                    "java.lang," +
                    "java.util," +
                    "Packages.net.dv8tion.jda.core," +
                    "Packages.net.dv8tion.jda.core.entities," +
                    "Packages.net.dv8tion.jda.core.entities.impl," +
                    "Packages.net.dv8tion.jda.core.managers," +
                    "Packages.net.dv8tion.jda.core.managers.impl," +
                    "Packages.net.dv8tion.jda.core.utils);");
        } catch (ScriptException e) {
            LoggerFactory.getLogger("CommandExec").error(e.getMessage());
        }
    }

    protected void execute(CommandEvent event) {
        engine.put("event", event);
        engine.put("jda", event.getJDA());
        engine.put("guild", event.getGuild());
        engine.put("channel", event.getChannel());
        try {
            event.reply(event.getClient().getSuccess() + " Evaluated Successfully:\n```\n" + engine.eval(event.getArgs()) + " ```");
        } catch (ScriptException e) {
            event.reply(event.getClient().getError() + " An exception was thrown:\n```\n" + e + " ```");
        }
    }

}