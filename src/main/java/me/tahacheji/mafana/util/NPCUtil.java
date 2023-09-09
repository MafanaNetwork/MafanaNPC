package me.tahacheji.mafana.util;

import me.tahacheji.mafana.MafanaNPC;
import me.tahacheji.mafana.data.MafanaCitizens;
import me.tahacheji.mafana.data.MafanaStillNPC;
import me.tahacheji.mafana.data.MafanaTask;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

public class NPCUtil {


    public boolean isNPC(Entity entity) {
        return entity.hasMetadata("NPC");
    }

    public MafanaCitizens getMafanaCitizens(NPC npc) {
        for(MafanaCitizens mafanaCitizens : MafanaNPC.getInstance().getMafanaCitizens()) {
            if(mafanaCitizens.getNpc().getUniqueId().toString().equalsIgnoreCase(npc.getUniqueId().toString())) {
                return mafanaCitizens;
            }
        }
        return null;
    }

    public MafanaStillNPC getMafanaStillNPC(NPC npc) {
        for(MafanaStillNPC mafanaNPC : MafanaNPC.getInstance().getMafanaStillNPCS()) {
            if(mafanaNPC.getNpc().getUniqueId().toString().equalsIgnoreCase(npc.getUniqueId().toString())) {
                return mafanaNPC;
            }
        }
        return null;
    }

    public void addCoolDown(MafanaTask mafanaTask) {
        MafanaNPC.getInstance().getTaskCoolDown().add(mafanaTask);
        Bukkit.getScheduler().runTaskLater(MafanaNPC.getInstance(), () -> {
            MafanaNPC.getInstance().getTaskCoolDown().remove(mafanaTask);
        }, 20L * mafanaTask.getCoolDown());
    }
}
