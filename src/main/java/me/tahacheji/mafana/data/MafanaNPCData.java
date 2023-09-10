package me.tahacheji.mafana.data;

import me.TahaCheji.mysqlData.MySQL;
import me.TahaCheji.mysqlData.MysqlValue;
import me.TahaCheji.mysqlData.SQLGetter;
import me.tahacheji.mafana.util.NPCUtil;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        if(sqlGetter.exists(mafanaStillNPC.getNpcUUID())) {
            List<String> s = getPlayers(mafanaStillNPC);
            s.add(player.getUniqueId().toString());
            setPlayer(mafanaStillNPC, s);
        }
    }

    public boolean existPlayer(MafanaStillNPC mafanaStillNPC, Player player) {
        if(sqlGetter.exists(mafanaStillNPC.getNpcUUID())) {
            for(String s : getPlayers(mafanaStillNPC)) {
                if(s.equalsIgnoreCase(player.getUniqueId().toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setPlayer(MafanaStillNPC mafanaStillNPC, List<String> s) {
        sqlGetter.setString(new MysqlValue("PLAYERS", mafanaStillNPC.getNpcUUID(), new NPCUtil().encryptList(s)));
    }

    public List<String> getPlayers(MafanaStillNPC mafanaStillNPC) {
        return new NPCUtil().decryptToList(sqlGetter.getString(mafanaStillNPC.getNpcUUID(), new MysqlValue("PLAYERS")));
    }

    @Override
    public void connect() {
        super.connect();
        if (this.isConnected()) sqlGetter.createTable("player_npc_talk",
                new MysqlValue("NPCNAME", ""),
                new MysqlValue("PLAYERS", ""));
    }
}
