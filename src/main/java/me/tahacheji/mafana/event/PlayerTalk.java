package me.tahacheji.mafana.event;

import me.tahacheji.mafana.data.MafanaCitizens;
import me.tahacheji.mafana.util.ConvoTrait;
import me.tahacheji.mafana.util.NPCUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.concurrent.CompletableFuture;

public class PlayerTalk implements Listener {


    /*
    @EventHandler
    public void chat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        for (Entity npc : player.getNearbyEntities(15, 15, 15)) {
            if (new NPCUtil().isNPC(npc)) {
                NPC x = CitizensAPI.getNPCRegistry().getNPC(npc);
                MafanaCitizens mafanaCitizens = new NPCUtil().getMafanaCitizens(x);
                if(event.getMessage().equalsIgnoreCase("yes")) {
                    mafanaCitizens.nodYes(5L);
                }
                if(event.getMessage().equalsIgnoreCase("no")) {
                    mafanaCitizens.nodNo(5L);
                }
                if(event.getMessage().equalsIgnoreCase("lot")) {
                    mafanaCitizens.nodMultipleTimes(true, 5, 20L, 5);
                }
            }
        }
    }
     */

    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent e) {
        for (NPC npc : CitizensAPI.getNPCRegistry().sorted()) {
            if (npc.getEntity() == null || npc.getTraitNullable(ConvoTrait.class) == null){
                continue;
            }

            var trait = npc.getTraitNullable(ConvoTrait.class);
            var talkingTo = trait.getTalkingTo();

            //See if the NPC is talking to the player
            if (talkingTo == null || !talkingTo.equals(e.getPlayer())){
                continue;
            }

            //If the player is talking to the NPC but is not within 20 blocks, stop the conversation
            if (npc.getEntity().getLocation().distance(e.getPlayer().getLocation()) > 20){
                trait.stopConversation();
            }else{
                //get what the player typed in chat
                trait.addMessage(e.getMessage());

                CompletableFuture.runAsync(() -> {
                    //trait.getResponse(talkingTo, e.getMessage());
                });
                e.setCancelled(true);
            }

        }

    }
}
