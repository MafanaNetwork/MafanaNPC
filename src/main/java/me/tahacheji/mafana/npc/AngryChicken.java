package me.tahacheji.mafana.npc;

import me.tahacheji.mafana.data.MafanaNPCEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class AngryChicken {


    public void spawnEntity(Player player) {
       MafanaNPCEntity mafanaNPCEntity = new MafanaNPCEntity("AngryChicken", EntityType.CHICKEN);
       mafanaNPCEntity.setHostel("players");
       mafanaNPCEntity.spawnNPC(player.getLocation());
    }
}
