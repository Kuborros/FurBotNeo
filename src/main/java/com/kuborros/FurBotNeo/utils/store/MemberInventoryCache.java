package com.kuborros.FurBotNeo.utils.store;

public interface MemberInventoryCache {

    //Interface meant to provide easy way to change cache backend
    MemberInventory getInventory(String id);

    default MemberInventory getInventory(String memberId, String guildId) {
        //Wrapper for more easily obtainable values
        return getInventory(memberId + ',' + guildId);
    }

    void setInventory(MemberInventory inventory);

    void expireInventory(String id);

    default void expireInventory(MemberInventory inventory) {
        expireInventory(inventory.uId);
    }

}
