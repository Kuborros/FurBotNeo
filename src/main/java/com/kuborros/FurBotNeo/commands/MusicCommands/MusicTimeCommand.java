package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;

public class MusicTimeCommand extends MusicCommand {

    public MusicTimeCommand() {
        this.name = "seek";
        this.arguments = "<time>";
        this.help = "Skips to specified time in song";
        this.guildOnly = true;
        this.category = new Category("Music");
    }

    @Override
    public void doCommand(CommandEvent event) {

        if (isIdle(guild)) {
            event.replyWarning("No music is being played at the moment!");
            return;
        }

        String val = event.getArgs().toUpperCase().trim();
        boolean min = false;
        if (val.endsWith("M")) {
            min = true;
            val = val.substring(0, val.length() - 1).trim();
        } else if (val.endsWith("S")) {
            val = val.substring(0, val.length() - 1).trim();
        } else {
            val = val.trim();
        }
        int seconds;
        try {
            seconds = (min ? 60 : 1) * Integer.parseInt(val);
            Long milis = (long) (seconds * 1000);
            if (getPlayer(guild).getPlayingTrack().getDuration() <= milis) {
                event.replyWarning("This track is not long enough to skip that far!");
                return;
            }
            getTrackManager(guild).skipToTime(milis);
            event.reply("Skipping to: " + seconds + "s!");
        } catch (NumberFormatException ex) {
            event.replyWarning("Hm. I can't seem to get a number from that.");
        }
    }

}
