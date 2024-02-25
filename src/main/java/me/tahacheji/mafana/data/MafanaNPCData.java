package me.tahacheji.mafana.data;
import me.tahacheji.mafana.util.NPCUtil;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MafanaNPCData extends MySQL {
    SQLGetter sqlGetter = new SQLGetter(this);

    public MafanaNPCData() {
        super("162.254.145.231", "3306", "51252", "51252", "346a1ef0fc");}

    public CompletableFuture<Void> addNPC(MafanaStillNPC mafanaStillNPC) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!sqlGetter.existsAsync(mafanaStillNPC.getNpcUUID()).join()) {
                    UUID uuid = mafanaStillNPC.getNpcUUID();
                    sqlGetter.setStringAsync(new DatabaseValue("NPCNAME", uuid, mafanaStillNPC.getName())).join();
                    sqlGetter.setStringAsync(new DatabaseValue("PLAYERS", uuid, "")).join();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }


    public CompletableFuture<Void> addPlayer(MafanaStillNPC mafanaStillNPC, Player player) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (sqlGetter.existsAsync(mafanaStillNPC.getNpcUUID()).join()) {
                    List<MafanaNPCPlayer> s = getPlayers(mafanaStillNPC).join();
                    if (s == null) {
                        s = new ArrayList<>();
                    }
                    s.add(new MafanaNPCPlayer(player.getUniqueId(), "0"));
                    setPlayer(mafanaStillNPC, s).join();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }


    public CompletableFuture<Void> setPlayerValue(MafanaStillNPC mafanaStillNPC, Player player, String value) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (sqlGetter.existsAsync(mafanaStillNPC.getNpcUUID()).join()) {
                    if (existPlayer(mafanaStillNPC, player).join()) {
                        List<MafanaNPCPlayer> npcPlayers = getPlayers(mafanaStillNPC).join();
                        if (npcPlayers == null) {
                            npcPlayers = new ArrayList<>();
                        }
                        MafanaNPCPlayer mafanaNPCPlayer = getPlayer(mafanaStillNPC, player).join();
                        if (mafanaNPCPlayer != null) {
                            npcPlayers.remove(mafanaNPCPlayer);
                            npcPlayers.add(new MafanaNPCPlayer(player.getUniqueId(), value));
                            setPlayer(mafanaStillNPC, npcPlayers).join();
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }


    public CompletableFuture<Boolean> existPlayer(MafanaStillNPC mafanaStillNPC, Player player) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (sqlGetter.existsAsync(mafanaStillNPC.getNpcUUID()).join()) {
                    List<MafanaNPCPlayer> players = getPlayers(mafanaStillNPC).join();
                    if (players != null) {
                        for (MafanaNPCPlayer mafanaNPCPlayer : players) {
                            if (mafanaNPCPlayer.getPlayer().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
                                return true;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return false;
        });
    }


    public CompletableFuture<MafanaNPCPlayer> getPlayer(MafanaStillNPC mafanaStillNPC, Player player) {
        CompletableFuture<MafanaNPCPlayer> x = new CompletableFuture<>();
        getPlayers(mafanaStillNPC).thenAcceptAsync(players -> {
            if (players != null) {
                for (MafanaNPCPlayer mafanaNPCPlayer : players) {
                    if (mafanaNPCPlayer.getPlayer().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
                        x.complete(mafanaNPCPlayer);
                    }
                }
            }
        });
        return x;
    }

    public CompletableFuture<Void> setPlayer(MafanaStillNPC mafanaStillNPC, List<MafanaNPCPlayer> s) {
        if (s != null) {
            return sqlGetter.setStringAsync(new DatabaseValue("PLAYERS", mafanaStillNPC.getNpcUUID(), new NPCUtil().compressMafanaNPCPlayer(s)));
        }
        return null;
    }

    public CompletableFuture<List<MafanaNPCPlayer>> getPlayers(MafanaStillNPC mafanaStillNPC) {
        CompletableFuture<List<MafanaNPCPlayer>> x = new CompletableFuture<>();
        sqlGetter.getStringAsync(mafanaStillNPC.getNpcUUID(), new DatabaseValue("PLAYERS")).thenAcceptAsync(string -> {
            if (string != null) {
                x.complete(new NPCUtil().decompressMafanaNPCPlayer(string));
            }
        });
        return x;
    }
    public void connect() {
        sqlGetter.createTable("player_npc_talk",
                new DatabaseValue("NPCNAME", ""),
                new DatabaseValue("PLAYERS", ""));
    }
}
