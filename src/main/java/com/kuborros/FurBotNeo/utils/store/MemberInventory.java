package com.kuborros.FurBotNeo.utils.store;

import java.util.ArrayList;

import static com.kuborros.FurBotNeo.BotMain.cfg;
import static com.kuborros.FurBotNeo.BotMain.db;

public class MemberInventory {

    final String memberId;
    final String guildId;
    final String uId;
    ArrayList<String> ownedItems, ownedRoles;
    int balance, level;
    boolean VIP, banned;
    String currentItem, currentRole;

    //Initialises empty inventory
    public MemberInventory(String memberId, String guildId) {
        this.memberId = memberId;
        this.guildId = guildId;
        this.uId = memberId + "," + guildId;
        this.banned = false;
        this.ownedRoles = new ArrayList<>();
        this.ownedItems = new ArrayList<>();
        this.currentItem = "";
        this.currentRole = "";
        if (cfg.isDebugMode()) {
            //If in debug mode, give all new users unholy amount of tokens for testing
            this.balance = Integer.MAX_VALUE / 2;
            //Also give high level to test high level token costs
            this.level = 9001;
            this.VIP = true;
        } else {
            this.balance = 0;
            this.level = 0;
            //If buying vip is disabled, we make everyone vip to not lock features out
            this.VIP = !cfg.isBuyVipEnabled();
        }
    }

    //Initialise inventory with existing data
    public MemberInventory(String memberId, String guildId, int balance, int level, ArrayList<String> ownedItems, ArrayList<String> ownedRoles, String currItem, String currRole, boolean vip, boolean banned) {
        this.memberId = memberId;
        this.guildId = guildId;
        this.uId = memberId + "," + guildId;
        this.balance = balance;
        this.level = level;
        this.ownedItems = ownedItems;
        this.ownedRoles = ownedRoles;
        this.currentItem = currItem;
        this.currentRole = currRole;
        this.banned = banned;
        if (cfg.isBuyVipEnabled()) this.VIP = vip;
        else this.VIP = true;
    }

    public void sync() {
        db.memberSetInventory(this);
    }

    //All add/remove methods return MemberInventory for easy chaining
    public MemberInventory addToInventory(String item) {
        ownedItems.add(item);
        return this;
    }

    public MemberInventory addToRoles(String item) {
        ownedRoles.add(item);
        return this;
    }

    public MemberInventory removeFromInventory(String item) {
        ownedItems.remove(item);
        return this;
    }

    public MemberInventory removeFromRoles(String item) {
        ownedRoles.remove(item);
        return this;
    }

    public MemberInventory addTokens(int amount) {
        balance += amount;
        return this;
    }

    public MemberInventory spendTokens(int amount) {
        balance -= amount;
        return this;
    }

    public MemberInventory addLevel() {
        level++;
        return this;
    }

    public MemberInventory addLevel(int levels) {
        level += levels;
        return this;
    }

    public MemberInventory setBotBan(boolean beaned) {
        this.banned = beaned;
        return this;
    }

    public MemberInventory setVIP(boolean vip) {
        this.VIP = vip;
        return this;
    }

    public String getCurrentItem() {
        return currentItem;
    }

    public MemberInventory setCurrentItem(String item) {
        this.currentItem = item;
        return this;
    }

    public MemberInventory setCurrentRole(String role) {
        this.currentRole = role;
        return this;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getGuildId() {
        return guildId;
    }

    public String getCurrentRole() {
        return currentRole;
    }

    public ArrayList<String> getOwnedItems() {
        return ownedItems;
    }

    public ArrayList<String> getOwnedRoles() {
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

    public boolean isBanned() {
        //Owner is never banned, even if db says so, to avoid permament lockout
        if (memberId.equals(cfg.getOwnerId())) return false;
        return banned;
    }
}
