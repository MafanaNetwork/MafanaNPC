package me.tahacheji.mafana.data;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class MafanaTask implements NPCTaskEvents{

    private Material block;
    private Location location;
    private Player player;

    private NPC mafanaCitizens;

    private int coolDown;

    private int untilWalk;

    public MafanaTask(Material block, Location location, Player player) {
        this.block = block;
        this.location = location;
        this.player = player;
    }

    public MafanaTask(NPC mafanaCitizens) {
        this.mafanaCitizens = mafanaCitizens;
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

    public void setUntilWalk(int untilWalk) {
        this.untilWalk = untilWalk;
    }

    public int getUntilWalk() {
        return untilWalk;
    }

    public int getCoolDown() {
        return coolDown;
    }

    public Material getBlock() {
        return block;
    }

    public NPC getMafanaCitizens() {
        return mafanaCitizens;
    }
}
