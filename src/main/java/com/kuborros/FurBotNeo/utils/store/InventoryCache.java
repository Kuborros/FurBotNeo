package com.kuborros.FurBotNeo.utils.store;


import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public interface InventoryCache {

    //Get Inventory of specific user
    //Default method to convert jda objects (that we should not keep around) to strings
    default MemberInventory getInventory(Member member, Guild guild) {
        return getinventory(member.getId(), guild.getId());
    }

    MemberInventory getinventory(String member, String guild);

    //Wipe cache, and force reload of inventories
    void purge();
}
