package com.kuborros.FurBotNeo.utils.audio;

import net.dv8tion.jda.api.entities.Member;

public class RequesterInfo {
    private final String name;
    private final String id;

    RequesterInfo(Member author) {
        name = author.getEffectiveName();
        id = author.getId();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
