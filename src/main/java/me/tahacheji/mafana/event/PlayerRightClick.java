package me.tahacheji.mafana.event;

import me.tahacheji.mafana.data.MafanaStillNPC;
import me.tahacheji.mafana.util.NPCUtil;
import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
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
            mafanaStillNPC.rightClickNPC(event, mafanaStillNPC, event.getClicker());
        }
    }

    @EventHandler
    public void leftClick(NPCLeftClickEvent event) {
        NPC npc = event.getNPC();
        MafanaStillNPC mafanaStillNPC = new NPCUtil().getMafanaStillNPC(npc);
        if(mafanaStillNPC != null) {
            mafanaStillNPC.leftClickNPC(event, mafanaStillNPC, event.getClicker());
        }
    }

    @EventHandler
    public void click(NPCClickEvent event) {
        NPC npc = event.getNPC();
        MafanaStillNPC mafanaStillNPC = new NPCUtil().getMafanaStillNPC(npc);
        if(mafanaStillNPC != null) {
            mafanaStillNPC.talkNPC(mafanaStillNPC.getRandomNpcDialog());
            mafanaStillNPC.clickNPC(event, mafanaStillNPC, event.getClicker());
        }
    }

}
