package com.kuborros.FurBotNeo.utils.store;

import java.awt.*;

public class ShopItem {

    Color color;

    final String dbName;
    final String itemName;
    final ItemType type;
    final int value;
    String url;

    public ShopItem(String dbName, String itemName, String data, int value, ItemType type) {
        this.dbName = dbName;
        this.itemName = itemName;
        this.value = value;
        if (type == ItemType.ITEM) {
            this.url = data;
            this.color = Color.BLACK;
        } else if (type == ItemType.ROLE) {
            this.url = "";
            this.color = Color.decode(data);
        }
        this.type = type;
    }

    public Color getColor() {
        return color;
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

    public ItemType getType() {
        return type;
    }

    public enum ItemType {ITEM, ROLE}
}
