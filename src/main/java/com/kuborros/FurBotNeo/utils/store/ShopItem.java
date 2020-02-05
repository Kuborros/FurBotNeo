package com.kuborros.FurBotNeo.utils.store;

public class ShopItem {

    String dbName, itemName, url;
    int value;

    public ShopItem(String dbName, String itemName, String url, int value) {
        this.dbName = dbName;
        this.itemName = itemName;
        this.value = value;
        this.url = url;
    }

    public String getDbName() {
        return dbName;
    }

    public String getItemName() {
        return itemName;
    }

    public String getUrl() {
        return url;
    }

    public int getValue() {
        return value;
    }
}
