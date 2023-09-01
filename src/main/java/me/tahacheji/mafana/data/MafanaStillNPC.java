package me.tahacheji.mafana.data;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MafanaStillNPC {

    private final String name;

    private final NPC npc;

    private final UUID npcUUID;

    private Location defaultPointLocation;

    private List<NPCMessage> npcDialog = new ArrayList<>();

    public MafanaStillNPC(String name) {
        this.name = name;
        this.npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
        this.npcUUID = npc.getUniqueId();
    }

    public void spawnNPC(Location location) {
        this.defaultPointLocation = location;
        npc.spawn(location);
    }

    public void setNPCSkin(String value, String texture) {
        SkinTrait skinTrait = npc.getTrait(SkinTrait.class);
        skinTrait.setTexture(value, texture);
    }

    public void setNPCSkinPlayer(Player player) {
        SkinTrait skinTrait = npc.getTrait(SkinTrait.class);
        skinTrait.setSkinName(player.getName());
    }

    public void despawnNPC() {
        npc.despawn();
    }

    public void sneakNPC() {
        npc.data().set(NPC.Metadata.SNEAKING, true);
    }

    public void unSneakNPC() {
        npc.data().set(NPC.Metadata.SNEAKING, false);
    }

    public void talkNPC(NPCMessage... npcMessages) {
        me.tahacheji.mafana.MafanaNPC.getInstance().getMessageManager().sendMessage(npcMessages);
    }

    public void swingMainHand() {
        Player player = ((Player) npc.getEntity());
        player.swingMainHand();
    }

    public void swingOffHand() {
        Player player = ((Player) npc.getEntity());
        player.swingOffHand();
    }

    public void facePlayer(Player player) {
        npc.faceLocation(player.getLocation());
    }

    public void holdItem(ItemStack itemStack) {
        npc.getTrait(Equipment.class).set(Equipment.EquipmentSlot.HAND, itemStack);
    }

    public void wearArmor(List<ItemStack> armor) {
        Equipment equipment = npc.getTrait(Equipment.class);
        for (int i = 0; i < armor.size(); i++) {
            equipment.set(Equipment.EquipmentSlot.values()[i], armor.get(i));
        }
    }

    public void nodYes(long animationSpeed) {
        Location npcLocation = npc.getEntity().getLocation();
        double currentPitch = npcLocation.getPitch();
        double movePitch = 25.0;
        Location newLocation = npcLocation.clone();
        newLocation.setPitch((float) (currentPitch - movePitch));
        npc.teleport(newLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
        Bukkit.getScheduler().runTaskLater(me.tahacheji.mafana.MafanaNPC.getInstance(), () -> {
            Location downLocation = npcLocation.clone();
            downLocation.setPitch((float) (currentPitch + movePitch));
            npc.teleport(downLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
        }, animationSpeed);
        Bukkit.getScheduler().runTaskLater(me.tahacheji.mafana.MafanaNPC.getInstance(), () -> {
            Location originalLocation = npcLocation.clone();
            originalLocation.setPitch((float) currentPitch);
            npc.teleport(originalLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
        }, animationSpeed * 2);
    }

    public void nodNo(long animationSpeed) {
        Location npcLocation = npc.getEntity().getLocation();
        double currentYaw = npcLocation.getYaw();
        double movePitch = 25.0;
        Location newLocation = npcLocation.clone();
        newLocation.setYaw((float) (currentYaw - movePitch));
        npc.teleport(newLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
        Bukkit.getScheduler().runTaskLater(me.tahacheji.mafana.MafanaNPC.getInstance(), () -> {
            Location rightLocation = npcLocation.clone();
            rightLocation.setYaw((float) (currentYaw + movePitch));
            npc.teleport(rightLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
        }, animationSpeed);
        Bukkit.getScheduler().runTaskLater(me.tahacheji.mafana.MafanaNPC.getInstance(), () -> {
            Location originalLocation = npcLocation.clone();
            originalLocation.setYaw((float) currentYaw);
            npc.teleport(originalLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
        }, animationSpeed * 2);
    }

    public void nodMultipleTimes(boolean yesAnimation, int nodCount, long delayBetweenNods, int animationSpeed) {
        long totalDelay = 0L;
        for (int i = 0; i < nodCount; i++) {
            Bukkit.getScheduler().runTaskLater(me.tahacheji.mafana.MafanaNPC.getInstance(), () -> {
                if (yesAnimation) {
                    nodYes(animationSpeed);
                } else {
                    nodNo(animationSpeed);
                }
            }, totalDelay);
            totalDelay += delayBetweenNods;
        }
    }


    public void lookAtLocation(Location location, boolean lookUp) {
        double deltaX = location.getX() - npc.getEntity().getLocation().getX();
        double deltaY = location.getY() - npc.getEntity().getLocation().getY();
        double deltaZ = location.getZ() - npc.getEntity().getLocation().getZ();

        double distanceXZ = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        double distanceY = Math.sqrt(distanceXZ * distanceXZ + deltaY * deltaY);

        double yaw = Math.toDegrees(Math.atan2(deltaX, deltaZ));
        double pitch = Math.toDegrees(Math.atan2(deltaY, distanceXZ)) * (lookUp ? -1 : 1);
        npc.getEntity().teleport(npc.getEntity().getLocation().setDirection(location.toVector().subtract(npc.getEntity().getLocation().toVector())));
        ((Player) npc.getEntity()).getLocation().setYaw((float) yaw);
        ((Player) npc.getEntity()).getLocation().setPitch((float) pitch);
    }

    public void addNPCDialog(NPCMessage s) {
        getNpcDialog().add(s);
    }

    public void addNPCDialog(NPCMessage... s) {
        getNpcDialog().addAll(List.of(s));
    }

    public NPCMessage getRandomNpcDialog() {
        if (npcDialog.isEmpty()) {
            return null;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(npcDialog.size());

        return npcDialog.get(randomIndex);
    }

    public List<NPCMessage> getNpcDialog() {
        return npcDialog;
    }

    public NPC getNpc() {
        return npc;
    }

    public UUID getNpcUUID() {
        return npcUUID;
    }

    public String getName() {
        return name;
    }

    public Location getDefaultPointLocation() {
        return defaultPointLocation;
    }
}
