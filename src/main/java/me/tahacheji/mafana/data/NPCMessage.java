package me.tahacheji.mafana.data;

import net.citizensnpcs.api.npc.NPC;

public class NPCMessage {


    private final NPC npc;
    private final String message;
    private final int messageDuration;
    private final int untilNext;

    public NPCMessage(NPC npc, String message, int messageDuration, int untilNext) {
        this.npc = npc;
        this.message = message;
        this.messageDuration = messageDuration;
        this.untilNext = untilNext;
    }


    public NPC getNpc() {
        return npc;
    }

    public int getMessageDuration() {
        return messageDuration;
    }

    public int getUntilNext() {
        return untilNext;
    }

    public String getMessage() {
        return message;
    }
}
