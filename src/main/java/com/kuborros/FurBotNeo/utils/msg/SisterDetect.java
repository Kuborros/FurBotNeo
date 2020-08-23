package com.kuborros.FurBotNeo.utils.msg;

import net.dv8tion.jda.api.entities.Guild;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class SisterDetect {

    private static final Properties versionInfo = new Properties();
    private static String version, buildDate, arch, os;

    public SisterDetect() {

        version = "0";
        buildDate = "0";
        arch = System.getProperty("os.arch");
        os = System.getProperty("os.type");
        try {
            versionInfo.load(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("version.info")));
            version = versionInfo.getProperty("version");
            buildDate = versionInfo.getProperty("build.date");
        } catch (IOException ignored) {
        }
    }

    public String prepareDetectionMsg(Guild guild) {


        return null;
    }


}

