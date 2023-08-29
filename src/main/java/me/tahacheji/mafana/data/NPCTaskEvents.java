package me.tahacheji.mafana.data;

import me.tahacheji.mafana.MafanaNPC;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface NPCTaskEvents {


    default boolean onArrivalLocation(MafanaTask mafanaTask, MafanaCitizens npc, Location v1) {
        return false;
    }
    default boolean onArrivalPlayer(MafanaTask mafanaTask, MafanaCitizens npc, Player v1) {
        return false;
    }

    default boolean onArrivalNPC(MafanaTask mafanaTask, MafanaCitizens npc, MafanaCitizens v1) {
        return false;
    }

    default boolean onArrivalBlock(MafanaTask mafanaTask, MafanaCitizens npc, Material v1, Block block) {
        return false;
    }

}
