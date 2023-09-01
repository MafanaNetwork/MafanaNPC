package me.tahacheji.mafana.event;

import me.tahacheji.mafana.data.MafanaStillNPC;
import me.tahacheji.mafana.util.NPCUtil;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerRightClick implements Listener {


    @EventHandler
    public void rightClickNPC(NPCRightClickEvent event) {
        NPC npc = event.getNPC();
        MafanaStillNPC mafanaStillNPC = new NPCUtil().getMafanaStillNPC(npc);
        if(mafanaStillNPC != null) {
            mafanaStillNPC.talkNPC(mafanaStillNPC.getRandomNpcDialog());
        }
    }

}
