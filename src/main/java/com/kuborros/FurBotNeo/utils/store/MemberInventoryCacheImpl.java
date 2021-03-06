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


import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.cache2k.expiry.ExpiryTimeValues;
import org.cache2k.integration.CacheLoader;

import static com.kuborros.FurBotNeo.BotMain.db;


public class MemberInventoryCacheImpl implements MemberInventoryCache {

    private static Cache<String, MemberInventory> cache;

    public MemberInventoryCacheImpl() {
        cache = new Cache2kBuilder<String, MemberInventory>() {
        }
                .name("MemberInventories")
                .loader(new CacheLoader<>() {
                    @Override
                    public MemberInventory load(final String key) {
                        String[] id = key.split(",");
                        return db.memberGetInventory(id[0], id[1]);
                    }
                })
                .eternal(true)
                .build();
    }

    @Override
    public MemberInventory getInventory(String id) {
        //Obtains value from cache, if not present it will be loaded from db
        return cache.get(id);
    }

    @Override
    public void setInventory(MemberInventory inventory) {
        //Put updated inv into cache and set it to update in separate thread to db
        //This ensures db is up to date at all times
        cache.put(inventory.uId, inventory);
        new Thread(inventory::sync).start();
    }

    @Override
    public void expireInventory(String id) {
        cache.expireAt(id, ExpiryTimeValues.NOW);
    }

}

