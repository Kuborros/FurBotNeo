
package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.Permission;

@CommandInfo(
        name = "MusicPlayNext",
        description = "Same as MusicPlay, but track will be forced as next in queue."
)
@Author("Kuborros")
public class PlayNextCmd extends MusicCommand {

    public PlayNextCmd() {
        this.name = "playnext";
        this.arguments = "<title|URL>";
        this.help = "Add song to playlist and makes it the next song";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK};
        this.category = new Category("Music");
    }

    @Override
    public void doCommand(CommandEvent event) {

        this.input = (input != null && input.startsWith("http")) ? input : "ytsearch: " + input;

        if (event.getArgs().isEmpty()) {
            event.reply(sendFailEmbed("Please include a valid search.", "\"Valid\" means supported url, or a search term (that will be searched on youtube)"));
        } else {
            loadTrackNext(input, event.getMember(), event.getMessage());
            if (getPlayer(guild).isPaused()) {
                getPlayer(guild).setPaused(false);
            }
        }
    }
}
