package me.tahacheji.mafana.util;

import me.tahacheji.mafana.MafanaNPC;
import me.tahacheji.mafana.data.MafanaCitizens;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Entity;

public class NPCUtil {


    public boolean isNPC(Entity entity) {
        return entity.hasMetadata("NPC");
    }

    public MafanaCitizens getMafanaCitizens(NPC npc) {
        for(MafanaCitizens mafanaCitizens : MafanaNPC.getInstance().getMafanaCitizensList()) {
            if(mafanaCitizens.getNpc().getUniqueId().toString().equalsIgnoreCase(npc.getUniqueId().toString())) {
                return mafanaCitizens;
            }
        }
        return null;
    }
}
