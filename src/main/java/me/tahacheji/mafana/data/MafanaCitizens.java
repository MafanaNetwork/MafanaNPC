package me.tahacheji.mafana.data;

import me.tahacheji.mafana.MafanaNPC;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class MafanaCitizens {

    private final String name;
    private final NPC npc;

    private final UUID npcUUID;


    public MafanaCitizens(String name) {
        this.name = name;
        this.npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, name);
        this.npcUUID = npc.getUniqueId();
    }

    public void spawnNPC(Location location) {
        npc.spawn(location);
    }

    public void despawnNPC() {
        npc.despawn();
    }

    public void walkTo(Location location) {
        Navigator navigator = npc.getNavigator();
        navigator.setTarget(location);
        npc.data().set(NPC.Metadata.PATHFINDER_OPEN_DOORS, true);
    }

    public void runTo(Location location) {
        Navigator navigator = npc.getNavigator();
        navigator.setTarget(location);
        navigator.getLocalParameters().speedModifier(2); // Increase the speed for running
        npc.data().set(NPC.Metadata.PATHFINDER_OPEN_DOORS, true);
    }

    public void sneakTo(Location location) {
        Navigator navigator = npc.getNavigator();
        navigator.setTarget(location);
        npc.data().set(NPC.Metadata.SNEAKING, true);
    }

    public void sneakNPC() {
        npc.data().set(NPC.Metadata.SNEAKING, true);
    }

    public void unSneakNPC() {
        npc.data().set(NPC.Metadata.SNEAKING, false);
    }

    public void talkNPC(String message, int time) {
        MafanaNPC.getInstance().getMessageManager().sendMessage(npc, message, time);
    }

    public void swingMainHand() {
        Player player = ((Player) npc.getEntity());
        player.swingMainHand();
    }

    public void swingOffHand() {
        Player player = ((Player) npc.getEntity());
        player.swingOffHand();
    }

    public void openDoorAnimation(Location doorLocation) {
        Block block = doorLocation.getBlock();

        if (block.getType() == Material.DARK_OAK_DOOR || block.getType() == Material.ACACIA_DOOR
                || block.getType() == Material.OAK_DOOR || block.getType() == Material.BIRCH_DOOR
                || block.getType() == Material.JUNGLE_DOOR || block.getType() == Material.CHERRY_DOOR
                || block.getType() == Material.IRON_DOOR) {
            Bukkit.getScheduler().runTask(MafanaNPC.getInstance(), () -> {
                Door door = (Door) block.getBlockData();
                door.setOpen(true);
                block.setBlockData(door);
            });

            lookAtLocation(doorLocation, false);
        }
    }

    public void openChestAnimation(Location chestLocation) {
        Block block = chestLocation.getBlock();

        if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) {
            Bukkit.getScheduler().runTask(MafanaNPC.getInstance(), () -> {
                Chest chest = (Chest) block.getState();
                chest.open();
            });

            lookAtLocation(chestLocation, false);
        }
    }

    public void openTrapDoorAnimation(Location trapDoorLocation) {
        Block block = trapDoorLocation.getBlock();
        Material blockType = block.getType();

        if (blockType == Material.ACACIA_TRAPDOOR ||
                blockType == Material.BIRCH_TRAPDOOR ||
                blockType == Material.DARK_OAK_TRAPDOOR ||
                blockType == Material.JUNGLE_TRAPDOOR ||
                blockType == Material.OAK_TRAPDOOR ||
                blockType == Material.SPRUCE_TRAPDOOR) {

            Bukkit.getScheduler().runTask(MafanaNPC.getInstance(), () -> {
                TrapDoor trapDoor = (TrapDoor) block.getBlockData();
                trapDoor.setOpen(true);
                block.setBlockData(trapDoor);
            });

            lookAtLocation(trapDoorLocation, false);
        }
    }

    public void goOnLadder(Location location) {
        Navigator navigator = npc.getNavigator();
        navigator.setTarget(location);
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

    public void nodYes() {
        double currentPitch = ((Player) npc.getEntity()).getLocation().getPitch();
        double newPitch = currentPitch + 10.0; // Adjust the pitch change as needed

        Bukkit.getScheduler().runTask(MafanaNPC.getInstance(), () -> {
            Location newLocation = npc.getEntity().getLocation().clone();
            newLocation.setPitch((float) newPitch);
            npc.getEntity().teleport(newLocation);
        });
    }


    public void nodNo() {
        double currentYaw = ((Player) npc.getEntity()).getLocation().getYaw();
        double newYaw = currentYaw + 10.0; // Adjust the yaw change as needed

        Bukkit.getScheduler().runTask(MafanaNPC.getInstance(), () -> {
            ((Player) npc.getEntity()).getLocation().setYaw((float) newYaw);
        });
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


    public Entity getEntity() {
        return npc.getEntity();
    }

    public Player getPlayerNPC() {
        return (Player) getEntity();
    }
    public NPC getNpc() {
        return npc;
    }

    public String getName() {
        return name;
    }

    public UUID getNpcUUID() {
        return npcUUID;
    }
}
