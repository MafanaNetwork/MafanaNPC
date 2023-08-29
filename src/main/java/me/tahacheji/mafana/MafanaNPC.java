package me.tahacheji.mafana;

import me.tahacheji.mafana.command.TestCommand;
import me.tahacheji.mafana.data.MafanaTask;
import me.tahacheji.mafana.util.ConvoTrait;
import me.tahacheji.mafana.data.MafanaCitizens;
import me.tahacheji.mafana.data.MessageManager;
import me.tahacheji.mafana.event.PlayerBreakBlock;
import me.tahacheji.mafana.event.PlayerJoin;
import me.tahacheji.mafana.event.PlayerTalk;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class MafanaNPC extends JavaPlugin {

    private static MafanaNPC mafanaNPC;
    private List<MafanaCitizens> mafanaCitizensList = new ArrayList<>();
    private MessageManager messageManager;

    private List<MafanaTask> taskCoolDown = new ArrayList<>();

    @Override
    public void onEnable() {
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(ConvoTrait.class));
        mafanaNPC = this;
        messageManager = new MessageManager();
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerBreakBlock(), this);
        getServer().getPluginManager().registerEvents(new PlayerTalk(), this);
        getCommand("testnpc").setExecutor(new TestCommand());

    }

    public List<MafanaTask> getTaskCoolDown() {
        return taskCoolDown;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public List<MafanaCitizens> getMafanaCitizensList() {
        return mafanaCitizensList;
    }

    public static MafanaNPC getInstance() {
        return mafanaNPC;
    }
}
