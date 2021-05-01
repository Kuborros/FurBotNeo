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

    public JSONObject getRoleInventory() {
        return roleInventory;
    }

    public void reloadItemsFile() throws IOException {
        String content = new String(Files.readAllBytes(ITEMSFILE.toPath()));
        JSONObject items = new JSONObject(content);
        roleInventory = items.getJSONObject("roles");
    }

}
