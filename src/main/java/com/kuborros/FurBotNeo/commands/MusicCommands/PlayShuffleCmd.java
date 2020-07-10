
package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.Permission;

@CommandInfo(
        name = "MusicPlayShuffle",
        description = "Same as MusicPlay, but queue gets randomised."
)
@Author("Kuborros")
public class PlayShuffleCmd extends MusicCommand {

    public PlayShuffleCmd() {
        this.name = "playshuffle";
        this.arguments = "<title|URL>";
        this.help = "Adds song to playlist then shuffles it";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK};
        this.category = new Category("Music");
    }

    @Override
    public void doCommand(CommandEvent event) {

        if (isDJ) {
            this.input = (input != null && input.startsWith("http")) ? input : "ytsearch: " + input;

            if (event.getArgs().isEmpty()) {
                event.reply(sendFailEmbed("Please include a valid search.", "\"Valid\" means supported url, or a search term (that will be searched on youtube)"));
            } else {
                loadTrack(input, event.getMember(), event.getMessage());
                getTrackManager(guild).shuffleQueue();
                if (getPlayer(guild).isPaused())
                    getPlayer(guild).setPaused(false);
            }
        } else {
            event.reply(sendFailEmbed("Only DJs can shuffle the playlist while adding a new track!", "With that power comes responsibility!"));
        }
    }
}