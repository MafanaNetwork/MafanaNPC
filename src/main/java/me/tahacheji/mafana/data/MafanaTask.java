package me.tahacheji.mafana.data;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class MafanaTask implements NPCTaskEvents{

    private Material block;
    private Location location;
    private Player player;

    private int coolDown;

    public MafanaTask(Material block, Location location, Player player) {
        this.block = block;
        this.location = location;
        this.player = player;
    }

    public MafanaTask(Player player) {
        this.player = player;
    }

    public MafanaTask(Location location) {
        this.location = location;
    }

    public MafanaTask(Material block) {
        this.block = block;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getLocation() {
        return location;
    }

    public void setCoolDown(int coolDown) {
        this.coolDown = coolDown;
    }

    public int getCoolDown() {
        return coolDown;
    }

    public Material getBlock() {
        return block;
    }
}