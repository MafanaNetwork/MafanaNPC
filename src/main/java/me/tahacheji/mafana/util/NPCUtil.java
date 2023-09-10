package me.tahacheji.mafana.util;

import me.tahacheji.mafana.MafanaNPC;
import me.tahacheji.mafana.data.MafanaCitizens;
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

    private final String ALGORITHM = "AES";
    private final String CIPHER_INSTANCE = "AES/ECB/PKCS5Padding";
    private final Key SECRET_KEY;

    public NPCUtil() {
        SECRET_KEY = getSecretKey("mafana");
    }


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

    private Key getSecretKey(String keyString) {
        // Generate a secret key from the provided keyString
        byte[] keyBytes = Arrays.copyOf(keyString.getBytes(StandardCharsets.UTF_8), 16); // AES keys are 128 bits (16 bytes)
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public String encryptList(List<String> list) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);

            String serializedList = String.join(",", list);
            byte[] encryptedBytes = cipher.doFinal(serializedList.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            return null;
        }
    }

    public List<String> decryptToList(String encryptedString) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);

            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedString);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            String decryptedString = new String(decryptedBytes, StandardCharsets.UTF_8);

            return Arrays.asList(decryptedString.split(","));
        } catch (Exception e) {
            return new ArrayList<>(); // Return an empty list if decryption fails
        }
    }
}
