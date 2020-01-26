package com.kuborros.FurBotNeo.utils.store;

import java.util.List;

import static com.kuborros.FurBotNeo.BotMain.cfg;
import static com.kuborros.FurBotNeo.BotMain.db;

public class MemberInventory {

    String memberId, guildId;
    List<String> ownedItems, ownedRoles;
    int balance, level;
    boolean VIP;

    //Initialises empty inventory
    public MemberInventory(String memberId, String guildId) {
        this.memberId = memberId;
        this.guildId = guildId;
        if (cfg.isDebugMode()) {
            //If in debug mode, give all new users unholy amount of tokens for testing
            this.balance = Integer.MAX_VALUE / 2;
            //Also give high level to test high level token costs
            this.level = 9001;
            this.VIP = true;
        } else {
            this.balance = 0;
            this.level = 0;
            this.VIP = false;
        }
    }

    //Initialise inventory with existing data
    public MemberInventory(String memberId, String guildId, int balance, int level, List<String> ownedItems, List<String> ownedRoles, boolean vip) {
        this.memberId = memberId;
        this.guildId = guildId;
        this.balance = balance;
        this.level = level;
        this.ownedItems = ownedItems;
        this.ownedRoles = ownedRoles;
        this.VIP = vip;
    }

    public void sync() {
        db.memberSetInventory(this);
    }

    public String getMemberId() {
        return memberId;
    }

    public String getGuildId() {
        return guildId;
    }

    public List<String> getOwnedItems() {
        return ownedItems;
    }

    public List<String> getOwnedRoles() {
        return ownedRoles;
    }

    public int getBalance() {
        return balance;
    }

    public int getLevel() {
        return level;
    }

    public boolean isVIP() {
        return VIP;
    }
}
