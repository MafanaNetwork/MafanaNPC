package me.tahacheji.mafana.event;

import io.papermc.paper.event.block.BlockBreakBlockEvent;
import me.tahacheji.mafana.data.MafanaCitizens;
import me.tahacheji.mafana.util.NPCUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerBreakBlock implements Listener {


    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        for (Entity npc : player.getNearbyEntities(15, 15, 15)) {
            if (new NPCUtil().isNPC(npc)) {
                NPC x = CitizensAPI.getNPCRegistry().getNPC(npc);
                MafanaCitizens mafanaCitizens = new NPCUtil().getMafanaCitizens(x);
                mafanaCitizens.lookAtLocation(event.getBlock().getLocation(), false);
            }
        }
    }
}
