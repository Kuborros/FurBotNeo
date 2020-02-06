package com.kuborros.FurBotNeo.commands.ShopCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.kuborros.FurBotNeo.utils.store.MemberInventory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;

import static com.kuborros.FurBotNeo.BotMain.cfg;
import static com.kuborros.FurBotNeo.BotMain.inventoryCache;


@CommandInfo(
        name = "give",
        description = "Transfer your tokens to other user."
)
@Author("Kuborros")
public class GiveCoinsCmd extends ShopCommand {

    public GiveCoinsCmd() {
        this.name = "give";
        this.help = "Give your tokens out to someone!";
        this.arguments = "<mention> <amount>";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.category = new Command.Category("Shop");
    }

    @Override
    protected void doCommand(CommandEvent event) {

        if (event.getMessage().getMentionedMembers().isEmpty() || event.getArgs().isEmpty()) {
            event.replyWarning("For funds transfer you need to mention the recipient and specify the amount!");
            return;
        }
        Member member = event.getMessage().getMentionedMembers().get(0);
        if (member.getId().equals(event.getSelfMember().getId())) {
            event.reply("You want to give money to... me? Im flattered, but i also have to refuse~");
            return;
        }
        if (member.getId().equals(event.getMember().getId())) {
            event.reply("You want to give money to... yourself?");
            return;
        }

        MemberInventory reveiverInv = inventoryCache.getInventory(member.getId(), event.getGuild().getId());
        int amount;
        int ownerBalance = inventory.getBalance();
        try {
            amount = Integer.parseInt(event.getArgs().replaceFirst(member.getAsMention(), ""));
        } catch (Exception e) {
            if (cfg.isDebugMode()) event.reply(errorResponseEmbed("", e));
            else event.replyWarning("Are you suuuuuure you put numbers in there?");
            return;
        }
        if (ownerBalance < amount) {
            event.reply("You cannot afford this!");
            return;
        }

        inventoryCache.setInventory(reveiverInv.addTokens(amount));
        inventoryCache.setInventory(inventory.spendTokens(amount));

        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("TokenTransfer:tm:")
                .setColor(Color.ORANGE)
                .setDescription(String.format("%s just gave %d tokens to %s!", event.getMember().getEffectiveName(), amount, member.getEffectiveName()))
                .setThumbnail(member.getUser().getEffectiveAvatarUrl())
                .setFooter("Thank you for using our banking services~", event.getAuthor().getEffectiveAvatarUrl());
        event.reply(builder.build());
    }
}
