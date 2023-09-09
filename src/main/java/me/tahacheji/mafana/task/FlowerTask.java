package me.tahacheji.mafana.task;

import me.tahacheji.mafana.MafanaNPC;
import me.tahacheji.mafana.data.MafanaCitizens;
import me.tahacheji.mafana.data.MafanaTask;
import me.tahacheji.mafana.util.NPCUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class FlowerTask extends MafanaTask {
    private final Material targetBlockType;

    public FlowerTask(Material targetBlockType) {
        super(targetBlockType);
        this.targetBlockType = targetBlockType;
        setCoolDown(30);
        setUntilWalk(10);
    }

    @Override
    public boolean onArrivalBlock(MafanaTask mafanaTask, MafanaCitizens npc, Material material, Block block) {
        if (material == targetBlockType) {
            //npc.talkNPC("Hello poppy", 5);
            npc.runTo(block.getLocation());
            npc.lookAtLocation(block.getLocation(), false);
            npc.sneakNPC();

            Bukkit.getScheduler().runTaskLater(MafanaNPC.getInstance(), () -> {
                npc.unSneakNPC();
            }, 20 * 5);
            new NPCUtil().addCoolDown(this);
            return true;
        }
        return false;
    }

}
