package me.tahacheji.mafana.data;

import me.TahaCheji.mysqlData.MySQL;
import me.TahaCheji.mysqlData.MysqlValue;
import me.TahaCheji.mysqlData.SQLGetter;
import me.tahacheji.mafana.util.NPCUtil;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MafanaNPCData extends MySQL {
    SQLGetter sqlGetter = new SQLGetter(this);

    public MafanaNPCData() {
        super("localhost", "3306", "51190", "51190", "26c58bbe8e");
    }

    public void addNPC(MafanaStillNPC mafanaStillNPC) {
        if(!sqlGetter.exists(mafanaStillNPC.getNpcUUID())) {
            UUID uuid = mafanaStillNPC.getNpcUUID();
            sqlGetter.setString(new MysqlValue("NPCNAME", uuid, mafanaStillNPC.getName()));
            sqlGetter.setString(new MysqlValue("PLAYERS", uuid, ""));
        }
    }

    public void addPlayer(MafanaStillNPC mafanaStillNPC, Player player) {
        if (sqlGetter.exists(mafanaStillNPC.getNpcUUID())) {
            List<MafanaNPCPlayer> s = new ArrayList<>(getPlayers(mafanaStillNPC)); // Create a new ArrayList from the unmodifiable list
            s.add(new MafanaNPCPlayer(player.getUniqueId(), "0"));
            setPlayer(mafanaStillNPC, s);
        }
    }

    public void setPlayerValue(MafanaStillNPC mafanaStillNPC, Player player, String value) {
        if(sqlGetter.exists(mafanaStillNPC.getNpcUUID())) {
            if(existPlayer(mafanaStillNPC, player)) {
                MafanaNPCPlayer mafanaNPCPlayer = getPlayer(mafanaStillNPC, player);
                if(mafanaNPCPlayer != null) {
                    List<MafanaNPCPlayer> npcPlayers = getPlayers(mafanaStillNPC);
                    npcPlayers.remove(mafanaNPCPlayer);
                    npcPlayers.add(new MafanaNPCPlayer(player.getUniqueId(), value));
                    setPlayer(mafanaStillNPC, npcPlayers);
                }
            }
        }
    }

    public boolean existPlayer(MafanaStillNPC mafanaStillNPC, Player player) {
        if(sqlGetter.exists(mafanaStillNPC.getNpcUUID())) {
            for(MafanaNPCPlayer mafanaNPCPlayer : getPlayers(mafanaStillNPC)) {
                if(mafanaNPCPlayer.getPlayer().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    public MafanaNPCPlayer getPlayer(MafanaStillNPC mafanaStillNPC, Player player) {
        for(MafanaNPCPlayer mafanaNPCPlayer : getPlayers(mafanaStillNPC)) {
            if(mafanaNPCPlayer.getPlayer().toString().equalsIgnoreCase(player.getUniqueId().toString())) {
                return mafanaNPCPlayer;
            }
        }
        return null;
    }

    public void setPlayer(MafanaStillNPC mafanaStillNPC, List<MafanaNPCPlayer> s) {
        sqlGetter.setString(new MysqlValue("PLAYERS", mafanaStillNPC.getNpcUUID(), new NPCUtil().compressMafanaNPCPlayer(s)));
    }

    public List<MafanaNPCPlayer> getPlayers(MafanaStillNPC mafanaStillNPC) {
        return new NPCUtil().decompressMafanaNPCPlayer(sqlGetter.getString(mafanaStillNPC.getNpcUUID(), new MysqlValue("PLAYERS")));
    }

    @Override
    public void connect() {
        super.connect();
        if (this.isConnected()) sqlGetter.createTable("player_npc_talk",
                new MysqlValue("NPCNAME", ""),
                new MysqlValue("PLAYERS", ""));
    }
}
