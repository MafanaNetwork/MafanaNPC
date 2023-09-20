package me.tahacheji.mafana.data.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.tahacheji.mafana.data.MafanaNPCPlayer;

import java.io.IOException;
import java.util.UUID;

public class MafanaNPCPlayerTypeAdapter extends TypeAdapter<MafanaNPCPlayer> {

    @Override
    public void write(JsonWriter out, MafanaNPCPlayer npcPlayer) throws IOException {
        out.beginObject();
        out.name("player").value(npcPlayer.getPlayer().toString());
        out.name("value").value(npcPlayer.getValue());
        out.endObject();
    }

    @Override
    public MafanaNPCPlayer read(JsonReader in) throws IOException {
        UUID playerUUID = null;
        String value = null;

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "player":
                    playerUUID = UUID.fromString(in.nextString());
                    break;
                case "value":
                    value = in.nextString();
                    break;
                default:
                    in.skipValue(); // Skip any unknown fields
                    break;
            }
        }
        in.endObject();

        if (playerUUID != null && value != null) {
            return new MafanaNPCPlayer(playerUUID, value);
        } else {
            return null; // Handle invalid or missing data
        }
    }
}
