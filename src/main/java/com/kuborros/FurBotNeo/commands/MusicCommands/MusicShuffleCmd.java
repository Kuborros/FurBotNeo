
package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.Permission;

@CommandInfo(
        name = "MusicShuffle",
        description = "Randomises playlist order."
)
@Author("Kuborros")
public class MusicShuffleCmd extends MusicCommand {

    public MusicShuffleCmd() {
        this.name = "shuffle";
        this.help = "Shuffles the playlist";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK};
        this.category = new Category("Music");
    }

    @Override
    public void doCommand(CommandEvent event) {
        if (isIdle(guild)) {
            event.reply(sendFailEmbed("There is no queue for me to shuffle!", ""));
            return;
        }

        getTrackManager(guild).shuffleQueue();
        event.getTextChannel().sendMessage(sendGenericEmbed("Shuffled queue!", "For better, or for worse~", ":twisted_rightwards_arrows:")).queue();
    }
}