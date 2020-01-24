package com.kuborros.FurBotNeo.utils.store;

import java.util.ArrayList;

import static com.kuborros.FurBotNeo.BotMain.cfg;

public class MemberInventory {

    String memberId, guildId;
    ArrayList<String> ownedItems, ownedRoles;
    int balance, level;

    //Initialises empty inventory
    public MemberInventory(String memberId, String guildId) {
        this.memberId = memberId;
        this.guildId = guildId;
        if (cfg.isDebugMode()) {
            //If in debug mode, give all new users unholy amount of tokens for testing
            this.balance = Integer.MAX_VALUE / 2;
            //Also give high level to test high level token costs
            this.level = 9001;
        } else {
            this.balance = 0;
            this.level = 0;
        }
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
