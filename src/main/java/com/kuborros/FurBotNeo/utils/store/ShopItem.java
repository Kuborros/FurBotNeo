/*
 * Copyright © 2020 Kuborros (kuborros@users.noreply.github.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kuborros.FurBotNeo.utils.store;

import java.awt.*;

public class ShopItem {

    Color color;

    final String dbName;
    final String itemName;
    final int value;
    String url;

    public ShopItem(String dbName, String itemName, String data, int value) {
        this.dbName = dbName;
        this.itemName = itemName;
        this.value = value;
        this.url = "";
        this.color = Color.decode(data);
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
}
