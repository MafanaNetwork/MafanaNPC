package me.tahacheji.mafana.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.tahacheji.mafana.MafanaNPC;
import me.tahacheji.mafana.data.MafanaCitizens;
import me.tahacheji.mafana.data.MafanaNPCPlayer;
import me.tahacheji.mafana.data.MafanaStillNPC;
import me.tahacheji.mafana.data.MafanaTask;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

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

    private static final String HASH_ALGORITHM = "SHA-256";

    // Method to convert a string to a UUID
    public UUID stringToUUID(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert the hash bytes to a UUID
            long mostSigBits = 0;
            long leastSigBits = 0;
            for (int i = 0; i < 8; i++) {
                mostSigBits = (mostSigBits << 8) | (hashBytes[i] & 0xff);
            }
            for (int i = 8; i < 16; i++) {
                leastSigBits = (leastSigBits << 8) | (hashBytes[i] & 0xff);
            }

            return new UUID(mostSigBits, leastSigBits);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // Handle the exception appropriately in your code
        }
    }

    public String compressMafanaNPCPlayer(List<MafanaNPCPlayer> mafanaNPCPlayers) {
        Gson gson = new Gson();
        return gson.toJson(mafanaNPCPlayers);
    }

    public List<MafanaNPCPlayer> decompressMafanaNPCPlayer(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<MafanaNPCPlayer>>() {}.getType());
    }

}
