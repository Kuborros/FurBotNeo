package com.kuborros.FurBotNeo.utils.store;

import java.util.ArrayList;

public class MemberInventory {

    String memberId, guildId;
    ArrayList<String> ownedItems, ownedRoles;
    int balance, level;

    //Initialises empty inventory
    public MemberInventory(String memberId, String guildId) {
        this.memberId = memberId;
        this.guildId = guildId;
        this.balance = 0;
        this.level = 0;
    }

    //Initialise inventory with existing data
    public MemberInventory(String memberId, String guildId, int balance, int level, ArrayList<String> ownedItems, ArrayList<String> ownedRoles) {
        this.memberId = memberId;
        this.guildId = guildId;
        this.balance = balance;
        this.level = level;
        this.ownedItems = ownedItems;
        this.ownedRoles = ownedRoles;
    }
}
