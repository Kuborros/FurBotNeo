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
                        return db.memberGetInventory(key);
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

