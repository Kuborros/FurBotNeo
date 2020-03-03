package com.kuborros.FurBotNeo.utils.store;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Optional;

import static com.kuborros.FurBotNeo.BotMain.cfg;

public class JShopInventory {

    static final Logger LOG = LoggerFactory.getLogger("ShopILoader");
    private static final File ITEMSFILE = new File("items.json");

    JSONObject itemInventory;
    JSONObject roleInventory;


    public JShopInventory() {

        Optional<JSONObject> fileOpt = Optional.ofNullable(loadOrCreateFile());
        if (fileOpt.isEmpty()) {
            //We failed to load item definition table - it results in store breakage, but remaining features should remain operational (as we already correctly loaded config file!)
            LOG.error("Failed to load items.json! Store feature cannot function correctly without it, and is now forcefully disabled.");
            //As such, we forcefully disable the store features in this case
            cfg.setShopEnabled(false);
            //While technically we could use demoItems.json as fallback, it would cause issues when modified file was in use before it got damaged. It is safer to fully kill the feature in this case.
            return;
        }
        //If we got here it means file at least exists.
        itemInventory = fileOpt.get().getJSONObject("items");
        roleInventory = fileOpt.get().getJSONObject("roles");

    }

    @Nullable
    private JSONObject loadOrCreateFile() {
        if (ITEMSFILE.canRead()) {
            try {
                String content = new String(Files.readAllBytes(ITEMSFILE.toPath()));
                return new JSONObject(content);
            } catch (IOException e) {
                LOG.error("Loading items file failed: ", e);
                return null;
            }
        } else {
            LOG.info("Copying example items file...");
            try (InputStream def = getClass().getClassLoader().getResourceAsStream("demoItems.json")) {
                assert def != null;
                FileUtils.copyInputStreamToFile(def, ITEMSFILE);
                //Now that file is created we should be able to load it, or get more concrete error
                return loadOrCreateFile();
            } catch (IOException e) {
                LOG.error("... and failed! ", e);
                return null;
            }
        }
    }

    public JSONObject getItemInventory() {
        return itemInventory;
    }

    public JSONObject getRoleInventory() {
        return roleInventory;
    }

    public void reloadItemsFile() throws IOException {
        String content = new String(Files.readAllBytes(ITEMSFILE.toPath()));
        JSONObject items = new JSONObject(content);
        itemInventory = items.getJSONObject("items");
        roleInventory = items.getJSONObject("roles");
    }

}
