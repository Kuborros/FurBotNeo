package com.kuborros.FurBotNeo.commands.LastFmCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class LastFmCmd extends LastFmCommand {

    public LastFmCmd() {

        Command[] children = { //All Lastfm commands go here OwO
                new LastFmUserInfoComm()


        };
        this.name = "lastfm";
        this.help = "Last.fm Commands";
        this.children = children;
        this.guildOnly = true;
        this.category = new Category("Last.fm");
    }

    @Override
    protected void doCommand(CommandEvent event) {
        //Print help here
    }
}
