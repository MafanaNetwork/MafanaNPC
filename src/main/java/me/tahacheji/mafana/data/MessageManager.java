package me.tahacheji.mafana.data;

import me.tahacheji.mafana.MafanaNPC;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class MessageManager {

    private final List<ArmorStand> armorStandList = new ArrayList<>();
    private static final int MAX_ARMOR_STANDS = 5;
    private static final double DISTANCE_BETWEEN_MESSAGES = 0.3;

    public void sendMessage(NPCMessage... messages) {
        long totalDelay = 0L;
        for (NPCMessage npcMessage : messages) {
            long initialDelay = totalDelay;
            new BukkitRunnable() {
                @Override
                public void run() {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < armorStandList.size(); i++) {
                                ArmorStand stand = armorStandList.get(i);
                                Location newLocation = stand.getLocation().add(0, DISTANCE_BETWEEN_MESSAGES + .2, 0);
                                stand.teleport(newLocation);
                            }
                            ArmorStand armorStand = spawnMessageArmorStand(npcMessage.getNpc(), npcMessage.getMessage());
                            for(Player player : npcMessage.getNpc().getEntity().getLocation().getNearbyPlayers(5)) {
                                player.sendMessage(npcMessage.getNpc().getName() + ": " + npcMessage.getMessage());
                            }
                            armorStandList.add(0, armorStand); // Add at the beginning of the list
                            if (armorStandList.size() > MAX_ARMOR_STANDS) {
                                ArmorStand oldestArmorStand = armorStandList.remove(armorStandList.size() - 1);
                                if (oldestArmorStand != null) {
                                    oldestArmorStand.remove();
                                }
                            }
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    armorStand.remove();
                                    armorStandList.remove(armorStand);
                                }
                            }.runTaskLater(MafanaNPC.getInstance(), 20L * npcMessage.getMessageDuration());
                        }
                    }.runTask(MafanaNPC.getInstance());
                }
            }.runTaskLater(MafanaNPC.getInstance(), 20L * initialDelay);
            totalDelay += npcMessage.getUntilNext(); // Add the next delay to the total
        }
    }

    public void sendMessage(Sound sound, int radius, float volume, float pitch, NPCMessage... messages) {
        long totalDelay = 0L;
        for (NPCMessage npcMessage : messages) {
            long initialDelay = totalDelay;
            new BukkitRunnable() {
                @Override
                public void run() {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < armorStandList.size(); i++) {
                                ArmorStand stand = armorStandList.get(i);
                                Location newLocation = stand.getLocation().add(0, DISTANCE_BETWEEN_MESSAGES + .2, 0);
                                stand.teleport(newLocation);
                            }
                            ArmorStand armorStand = spawnMessageArmorStand(npcMessage.getNpc(), npcMessage.getMessage());
                            for(Player player : npcMessage.getNpc().getEntity().getLocation().getNearbyPlayers(radius)) {
                                player.playSound(player.getLocation(), sound, volume, pitch);
                                player.sendMessage(npcMessage.getNpc().getName() + ": " + npcMessage.getMessage());
                            }
                            armorStandList.add(0, armorStand); // Add at the beginning of the list
                            if (armorStandList.size() > MAX_ARMOR_STANDS) {
                                ArmorStand oldestArmorStand = armorStandList.remove(armorStandList.size() - 1);
                                if (oldestArmorStand != null) {
                                    oldestArmorStand.remove();
                                }
                            }
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    armorStand.remove();
                                    armorStandList.remove(armorStand);
                                }
                            }.runTaskLater(MafanaNPC.getInstance(), 20L * npcMessage.getMessageDuration());
                        }
                    }.runTask(MafanaNPC.getInstance());
                }
            }.runTaskLater(MafanaNPC.getInstance(), 20L * initialDelay);
            totalDelay += npcMessage.getUntilNext(); // Add the next delay to the total
        }
    }

    public void sendMessage(Sound sound, int radius, NPCMessage... messages) {
        long totalDelay = 0L;
        for (NPCMessage npcMessage : messages) {
            long initialDelay = totalDelay;
            new BukkitRunnable() {
                @Override
                public void run() {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < armorStandList.size(); i++) {
                                ArmorStand stand = armorStandList.get(i);
                                Location newLocation = stand.getLocation().add(0, DISTANCE_BETWEEN_MESSAGES + .2, 0);
                                stand.teleport(newLocation);
                            }
                            ArmorStand armorStand = spawnMessageArmorStand(npcMessage.getNpc(), npcMessage.getMessage());
                            for(Player player : npcMessage.getNpc().getEntity().getLocation().getNearbyPlayers(radius)) {
                                player.playSound(player.getLocation(), sound, 1, 1);
                                player.sendMessage(npcMessage.getNpc().getName() + ": " + npcMessage.getMessage());
                            }
                            armorStandList.add(0, armorStand); // Add at the beginning of the list
                            if (armorStandList.size() > MAX_ARMOR_STANDS) {
                                ArmorStand oldestArmorStand = armorStandList.remove(armorStandList.size() - 1);
                                if (oldestArmorStand != null) {
                                    oldestArmorStand.remove();
                                }
                            }
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    armorStand.remove();
                                    armorStandList.remove(armorStand);
                                }
                            }.runTaskLater(MafanaNPC.getInstance(), 20L * npcMessage.getMessageDuration());
                        }
                    }.runTask(MafanaNPC.getInstance());
                }
            }.runTaskLater(MafanaNPC.getInstance(), 20L * initialDelay);
            totalDelay += npcMessage.getUntilNext(); // Add the next delay to the total
        }
    }

        private ArmorStand spawnMessageArmorStand (NPC player, String message){
            World world = player.getEntity().getWorld();
            Location location = player.getEntity().getLocation().clone().add(0, 0, 0);

            ArmorStand armorStand = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setInvulnerable(true);
            armorStand.setCustomNameVisible(true);
            armorStand.setCustomName(message);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isSpawned()) {
                        armorStand.remove();
                        armorStandList.remove(armorStand);
                        cancel();
                        return;
                    }

                    Location newLocation = player.getEntity().getLocation().clone().add(0, armorStandList.indexOf(armorStand) * DISTANCE_BETWEEN_MESSAGES + .5, 0);
                    armorStand.teleport(newLocation);
                }
            }.runTaskTimer(MafanaNPC.getInstance(), 0, 0);

            return armorStand;
        }

    }

