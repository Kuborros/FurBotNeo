
package com.kuborros.FurBotNeo.commands.MusicCommands;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

@CommandInfo(
        name = "MusicSkip",
        description = "Skips currently played track."
)
@Author("Kuborros")
public class MusicSkipCmd extends MusicCommand {

    private final EventWaiter waiter;

    public MusicSkipCmd(EventWaiter waiter) {
        this.name = "skip";
        this.help = "Skips currently played track";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK};
        this.category = new Category("Music");
        this.waiter = waiter;
    }

    @Override
    public void doCommand(CommandEvent event) {

        if (isIdle(guild)) {
            return;
        }
        if (isDJ) {
            if (skipTrack(guild)) {
                event.reply(sendGenericEmbed("Skipped track!", "", ":fast_forward:"));
            }
        } else {
            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle(String.format("Voting to skip track: %s", getPlayer(guild).getPlayingTrack().getInfo().title))
                    .setColor(Color.BLUE)
                    .setDescription("");

            awaitResponse(event.getTextChannel().sendMessage(builder.build()).complete());
        }
    }

    private void awaitResponse(Message message) {

        message.clearReactions().complete();
        message.addReaction(OKAY).complete();
        message.addReaction(NO).complete();

        VoiceChannel vchan = message.getGuild().getSelfMember().getVoiceState().getChannel();
        List<Member> listening = vchan.getMembers();

        waiter.waitForEvent(MessageReactionAddEvent.class,
                event -> checkReaction(event, message, listening),
                event -> handleMessageReactionAddAction(event, message, listening, listening.size()),
                5, TimeUnit.MINUTES, () -> message.clearReactions().queue());
    }

    private boolean checkReaction(MessageReactionAddEvent event, Message message, List<Member> listening) {
        if (event.getMessageIdLong() != message.getIdLong())
            return false;
        switch (event.getReactionEmote().getName()) {
            case OKAY:
            case NO:
                return listening.contains(event.getMember());
            default:
                return false;
        }
    }

    private void handleMessageReactionAddAction(MessageReactionAddEvent event, Message message, List<Member> listening, int count) {


        if (event.getReaction().getReactionEmote().getName().equals(NO)) {
            listening.remove(event.getMember());
        }
        if (event.getReaction().getReactionEmote().getName().equals(OKAY)) {

        }
    }


}