package me.tahacheji.mafana.event;

import me.tahacheji.mafana.data.MafanaStillNPC;
import me.tahacheji.mafana.util.ConvoTrait;
import me.tahacheji.mafana.util.NPCUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.concurrent.CompletableFuture;

public class PlayerTalk implements Listener {


    @EventHandler
    public void chat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        for(Entity entity : player.getNearbyEntities(6, 6,6)) {
            if(new NPCUtil().isNPC(entity)) {
                NPC x = CitizensAPI.getNPCRegistry().getNPC(entity);
                MafanaStillNPC mafanaStillNPC = new NPCUtil().getMafanaStillNPC(x);
                if(mafanaStillNPC != null) {
                    mafanaStillNPC.talkNextToNPC(e.getMessage(), player, mafanaStillNPC);
                }
            }
        }
    }

    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent e) {
        for (NPC npc : CitizensAPI.getNPCRegistry().sorted()) {
            if (npc.getEntity() == null || npc.getTraitNullable(ConvoTrait.class) == null){
                continue;
            }

            var trait = npc.getTraitNullable(ConvoTrait.class);
            var talkingTo = trait.getTalkingTo();

            if (talkingTo == null || !talkingTo.equals(e.getPlayer())){
                continue;
            }

            if (npc.getEntity().getLocation().distance(e.getPlayer().getLocation()) > 20){
                trait.stopConversation();
            }else{
                //get what the player typed in chat
                trait.addMessage(e.getMessage());

                CompletableFuture.runAsync(() -> {
                    trait.getResponse(talkingTo, e.getMessage());
                });
                e.setCancelled(true);
            }

        }

    }
}
