package me.tahacheji.mafana.data;

import me.tahacheji.mafana.MafanaNPC;
import me.tahacheji.mafana.util.ConvoTrait;
import me.tahacheji.mafana.util.NPCUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class MafanaCitizens {

    private final String name;
    private final NPC npc;

    private final UUID npcUUID;

    private Location defaultPointLocation;

    private Location walkingTo;
    private List<Location> pointLocation = new ArrayList<>();
    private List<Location> chestLocation = new ArrayList<>();
    private List<Location> doorLocation = new ArrayList<>();
    private List<Location> trapDoorLocation = new ArrayList<>();
    private List<Location> flowerLocation = new ArrayList<>();
    private List<Location> unexploredLocation = new ArrayList<>();

    private List<MafanaTask> taskEvents = new ArrayList<>();
    private Location boundaryX;
    private Location boundaryY;
    public final NPCRegistry registry = CitizensAPI.createAnonymousNPCRegistry(new MemoryNPCDataStore());


    public MafanaCitizens(String name) {
        this.name = name;
        this.npc = registry.createNPC(EntityType.PLAYER, ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "Citizen" + ChatColor.DARK_GRAY + "] " + name);
        this.npcUUID = npc.getUniqueId();
    }

    public void spawnNPC(Location location, String role) {
        this.defaultPointLocation = location;
        npc.spawn(location);
        npc.addTrait(new ConvoTrait(role));
    }

    public void setNPCSkin(String name, String signature, String texture) {
        npc.addTrait(SkinTrait.class);
        SkinTrait skinTrait = npc.getTrait(SkinTrait.class);
        skinTrait.setShouldUpdateSkins(true);
        skinTrait.setSkinPersistent(name, signature, texture);
        skinTrait.setShouldUpdateSkins(true);
    }

    public void setLookAtPlayer() {
        npc.addTrait(LookClose.class);
        LookClose lookClose = getNpc().getTrait(LookClose.class);
        lookClose.lookClose(true);
    }

    public void despawnNPC() {
        npc.despawn();
    }

    public void walkTo(Location location) {
        Navigator navigator = npc.getNavigator();
        navigator.setTarget(location);
        npc.data().set(NPC.Metadata.PATHFINDER_OPEN_DOORS, true);
        executeTaskEvents();
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

    public void talkNPC(NPCMessage... npcMessages) {
        MafanaNPC.getInstance().getMessageManager().sendMessage(npcMessages);
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

    public void facePlayer(Player player) {
        npc.faceLocation(player.getLocation());
    }
    public void talkNPC(Sound sound, int radius, NPCMessage... npcMessages) {
        me.tahacheji.mafana.MafanaNPC.getInstance().getMessageManager().sendMessage(sound, radius, npcMessages);
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

    public void nodYes(long animationSpeed) {
        Location npcLocation = npc.getEntity().getLocation();
        double currentPitch = npcLocation.getPitch();
        double movePitch = 25.0;
        Location newLocation = npcLocation.clone();
        newLocation.setPitch((float) (currentPitch - movePitch));
        npc.teleport(newLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
        Bukkit.getScheduler().runTaskLater(MafanaNPC.getInstance(), () -> {
            Location downLocation = npcLocation.clone();
            downLocation.setPitch((float) (currentPitch + movePitch));
            npc.teleport(downLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
        }, animationSpeed);
        Bukkit.getScheduler().runTaskLater(MafanaNPC.getInstance(), () -> {
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
        Bukkit.getScheduler().runTaskLater(MafanaNPC.getInstance(), () -> {
            Location rightLocation = npcLocation.clone();
            rightLocation.setYaw((float) (currentYaw + movePitch));
            npc.teleport(rightLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
        }, animationSpeed);
        Bukkit.getScheduler().runTaskLater(MafanaNPC.getInstance(), () -> {
            Location originalLocation = npcLocation.clone();
            originalLocation.setYaw((float) currentYaw);
            npc.teleport(originalLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
        }, animationSpeed * 2);
    }

    public void nodMultipleTimes(boolean yesAnimation, int nodCount, long delayBetweenNods, int animationSpeed) {
        long totalDelay = 0L;
        for (int i = 0; i < nodCount; i++) {
            Bukkit.getScheduler().runTaskLater(MafanaNPC.getInstance(), () -> {
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

    private boolean isWalkingTo = false;
    private BukkitTask walkTo = null;
    private MafanaTask currentTask = null;
    private long resumeWalkTime = 0;

    public void startCitizen() {
        walkToNextPoint();
    }

    public void walkToNextPoint() {
        if (isWalkingTo && walkTo != null) {
            return;
        }

        Location currentLocation = npc.getEntity().getLocation();
        Location nextPoint = getNextPointLocation();

        double distance = currentLocation.distance(nextPoint);
        double walkingSpeed = 4.3;

        long walkTimeInTicks = Math.round(distance / walkingSpeed * 20); // Convert seconds to ticks
        long totalDelay = walkTimeInTicks + (20 * 2); // Add 2 seconds (20 ticks)
        walkTo(nextPoint);
        isWalkingTo = true;
        walkTo = Bukkit.getScheduler().runTaskLater(MafanaNPC.getInstance(), () -> {
            isWalkingTo = false;
            walkToCurrentTask(); // Continue to the next point or resume the current task
        }, totalDelay);
    }

    public boolean isWalkingToNextPoint() {
        return isWalkingTo;
    }

    public void executeTaskEvents() {
        Location currentLocation = getEntity().getLocation();
        MafanaTask mafanaTask = null;
        boolean taskExecuted = false;

        for (MafanaTask taskEvent : taskEvents) {
            if(MafanaNPC.getInstance().getTaskCoolDown().contains(taskEvent)) {
                continue;
            }
            if (taskEvent.onArrivalLocation(taskEvent, this, currentLocation)) {
                taskExecuted = true;
                mafanaTask = taskEvent;
                break;
            }
        }

        if(!taskExecuted) {
            for(MafanaTask task : taskEvents) {
                for (Entity npc : npc.getEntity().getLocation().getNearbyEntities(5, 5, 5)) {
                    if (new NPCUtil().isNPC(npc)) {
                        NPC x = CitizensAPI.getNPCRegistry().getNPC(npc);
                        MafanaCitizens m = new NPCUtil().getMafanaCitizens(x);
                        if(task.onArrivalNPC(task, this, m)) {
                            taskExecuted = true;
                            mafanaTask = task;
                            break;
                        }
                    }
                }
            }
        }

        if (!taskExecuted) {
            for (Player player : getNearbyPlayers(5)) {
                if (executePlayerTask(player) != null) {
                    mafanaTask = executePlayerTask(player);
                    Objects.requireNonNull(executePlayerTask(player)).onArrivalPlayer(executePlayerTask(player), this, player);
                    taskExecuted = true;
                    break;
                }
            }
        }

        if (!taskExecuted) {
            for (Block block : getNearbyBlocks(15)) {
                if (executeBlockTask(block) != null) {
                    mafanaTask = executeBlockTask(block);
                    executeBlockTask(block).onArrivalBlock(executeBlockTask(block), this, executeBlockTask(block).getBlock(), block);
                    taskExecuted = true;
                    break;
                }
            }
        }

        if (taskExecuted) {
            if(mafanaTask != null) {
                currentTask = mafanaTask;
                resumeWalkTime = System.currentTimeMillis() + (mafanaTask.getUntilWalk() * 1000L);
            }
        }
    }

    private void walkToCurrentTask() {
        // Check if there's a task to resume
        if (currentTask != null) {
            long currentTime = System.currentTimeMillis();

            // Check if it's time to resume walking
            if (currentTime >= resumeWalkTime) {
                currentTask = null; // Reset the current task
                walkToNextPoint(); // Continue walking to the next point
            } else {
                // Schedule another attempt to resume walking
                Bukkit.getScheduler().runTaskLater(MafanaNPC.getInstance(), this::walkToCurrentTask, 20L); // Delay of 1 second
            }
        } else {
            walkToNextPoint(); // No task to resume, continue walking to the next point
        }
    }


    private List<Player> getNearbyPlayers(int radius) {
        List<Player> nearbyPlayers = new ArrayList<>();
        Location currentLocation = getEntity().getLocation();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().distance(currentLocation) <= radius) {
                nearbyPlayers.add(player);
            }
        }

        return nearbyPlayers;
    }

    private MafanaTask executePlayerTask(Player player) {
        for (MafanaTask taskEvent : taskEvents) {
            if(MafanaNPC.getInstance().getTaskCoolDown().contains(taskEvent)) {
                continue;
            }
            if (taskEvent.getPlayer() != null &&
                    taskEvent.getPlayer().equals(player) &&
                    player.getLocation().distance(getEntity().getLocation()) <= 5) {
                return taskEvent;
            }
        }
        return null;
    }

    private MafanaTask executeBlockTask(Block block) {
        for (MafanaTask taskEvent : taskEvents) {
            if(MafanaNPC.getInstance().getTaskCoolDown().contains(taskEvent)) {
                continue;
            }
            if (taskEvent.getBlock() != null &&
                    taskEvent.getBlock() == block.getType()) {
                return taskEvent;
            }
        }
        return null;
    }

    private List<Block> getNearbyBlocks(int radius) {
        List<Block> nearbyBlocks = new ArrayList<>();
        Location currentLocation = getEntity().getLocation();
        World world = currentLocation.getWorld();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block block = world.getBlockAt(currentLocation.getBlockX() + x,
                            currentLocation.getBlockY() + y,
                            currentLocation.getBlockZ() + z);
                    nearbyBlocks.add(block);
                }
            }
        }

        return nearbyBlocks;
    }

    public List<Location> getBlockLocationsWithinBoundary(Material blockType) {
        List<Location> validBlockLocations = new ArrayList<>();

        World world = boundaryX.getWorld();
        int minX = Math.min(boundaryX.getBlockX(), boundaryY.getBlockX());
        int minY = Math.min(boundaryX.getBlockY(), boundaryY.getBlockY());
        int minZ = Math.min(boundaryX.getBlockZ(), boundaryY.getBlockZ());
        int maxX = Math.max(boundaryX.getBlockX(), boundaryY.getBlockX());
        int maxY = Math.max(boundaryX.getBlockY(), boundaryY.getBlockY());
        int maxZ = Math.max(boundaryX.getBlockZ(), boundaryY.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (block.getType() == blockType) {
                        validBlockLocations.add(block.getLocation());
                    }
                }
            }
        }

        return validBlockLocations;
    }

    public List<Location> getBlockLocationsWithinBoundary(Material... blocks) {
        List<Location> validBlockLocations = new ArrayList<>();
        List<Material> blockTypes = Arrays.asList(blocks);
        World world = boundaryX.getWorld();
        int minX = Math.min(boundaryX.getBlockX(), boundaryY.getBlockX());
        int minY = Math.min(boundaryX.getBlockY(), boundaryY.getBlockY());
        int minZ = Math.min(boundaryX.getBlockZ(), boundaryY.getBlockZ());
        int maxX = Math.max(boundaryX.getBlockX(), boundaryY.getBlockX());
        int maxY = Math.max(boundaryX.getBlockY(), boundaryY.getBlockY());
        int maxZ = Math.max(boundaryX.getBlockZ(), boundaryY.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    if (blockTypes.contains(block.getType())) {
                        validBlockLocations.add(block.getLocation());
                    }
                }
            }
        }

        return validBlockLocations;
    }
    public List<MafanaTask> getTaskEvents() {
        return taskEvents;
    }

    public void setTaskEvents (MafanaTask... taskEvents) {
        this.taskEvents = List.of(taskEvents);
    }

    public List<Location> getChestLocation() {
        return chestLocation;
    }

    public List<Location> getPointLocation() {
        return pointLocation;
    }

    public List<Location> getDoorLocation() {
        return doorLocation;
    }

    public List<Location> getFlowerLocation() {
        return flowerLocation;
    }

    public List<Location> getTrapDoorLocation() {
        return trapDoorLocation;
    }

    public Location getWalkingTo() {
        return walkingTo;
    }

    public Location getBoundaryX() {
        return boundaryX;
    }

    public List<Location> getUnexploredLocation() {
        return unexploredLocation;
    }

    public Location getBoundaryY() {
        return boundaryY;
    }

    public void organizePointsByDistance() {
        Location npcLocation = npc.getEntity().getLocation();

        // Sort the pointLocation list based on distance from NPC
        pointLocation.sort(Comparator.comparingDouble(npcLocation::distance));
    }


    private int currentPointIndex = 0;

    public Location getNextPointLocation() {
        if (pointLocation.isEmpty()) {
            return defaultPointLocation; // Return default point if the list is empty
        }

        // Get the next point based on the current index
        Location nextPoint = pointLocation.get(currentPointIndex);

        // Move to the next index and cycle back to 0 if at the end
        currentPointIndex = (currentPointIndex + 1) % pointLocation.size();

        return nextPoint;
    }
    public void setWalkingTo(Location walkingTo) {
        this.walkingTo = walkingTo;
    }

    public void setPointLocation(List<Location> pointLocation) {
        this.pointLocation = pointLocation;
    }

    public void setChestLocation(List<Location> chestLocation) {
        this.chestLocation = chestLocation;
    }

    public void setDoorLocation(List<Location> doorLocation) {
        this.doorLocation = doorLocation;
    }

    public void setTrapDoorLocation(List<Location> trapDoorLocation) {
        this.trapDoorLocation = trapDoorLocation;
    }

    public void setFlowerLocation(List<Location> flowerLocation) {
        this.flowerLocation = flowerLocation;
    }

    public void setUnexploredLocation(List<Location> unexploredLocation) {
        this.unexploredLocation = unexploredLocation;
    }

    public void setBoundaryX(Location boundaryX) {
        this.boundaryX = boundaryX;
    }

    public void setBoundaryY(Location boundaryY) {
        this.boundaryY = boundaryY;
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
