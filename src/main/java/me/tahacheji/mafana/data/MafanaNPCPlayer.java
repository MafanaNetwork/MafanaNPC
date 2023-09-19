package me.tahacheji.mafana.data;

import org.bukkit.entity.Player;

import java.util.UUID;

public class MafanaNPCPlayer {

    private final UUID player;
    private final String value;

    public MafanaNPCPlayer(UUID player, String value) {
        this.player = player;
        this.value = value;
    }

    public UUID getPlayer() {
        return player;
    }

    public String getValue() {
        return value;
    }
}
