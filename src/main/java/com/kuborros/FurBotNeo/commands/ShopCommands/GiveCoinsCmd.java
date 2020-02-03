package com.kuborros.FurBotNeo.commands.ShopCommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.kuborros.FurBotNeo.utils.store.MemberInventory;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

import static com.kuborros.FurBotNeo.BotMain.inventoryCache;


public class GiveCoinsCmd extends ShopCommand {

    public GiveCoinsCmd() {
        this.name = "give";
        this.help = "Give your tokens out to someone!";
        this.guildOnly = true;
        this.botPermissions = new Permission[]{Permission.MESSAGE_EMBED_LINKS};
        this.category = new Command.Category("Shop");
    }

    @Override
    protected void doCommand(CommandEvent event) {

        if (event.getMessage().getMentionedMembers().isEmpty() || event.getArgs().isEmpty()) {
            event.reply("Needs input");
            return;
        }
        Member member = event.getMessage().getMentionedMembers().get(0);
        MemberInventory reveiverInv = inventoryCache.getInventory(member.getId(), event.getGuild().getId());
        int amount = 0;
        int ownerBalance = inventory.getBalance();
        try {
            amount = Integer.parseInt(event.getArgs().replaceFirst(member.getAsMention(), ""));
        } catch (Exception e) {
            event.reply(errorResponseEmbed("You wrote nonsense", e));
            return;
        }
        if (ownerBalance < amount) {
            event.reply("Ur too poor");
            return;
        }

        inventoryCache.setInventory(reveiverInv.addTokens(amount));
        inventoryCache.setInventory(inventory.spendTokens(amount));

        event.reply("You did it lol"); //Embed here
    }
}
