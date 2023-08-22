package me.tahacheji.mafana;

import me.tahacheji.mafana.command.TestCommand;
import me.tahacheji.mafana.data.MafanaCitizens;
import me.tahacheji.mafana.data.MessageManager;
import me.tahacheji.mafana.event.PlayerBreakBlock;
import me.tahacheji.mafana.event.PlayerJoin;
import me.tahacheji.mafana.event.PlayerTalk;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class MafanaNPC extends JavaPlugin {

    private static MafanaNPC mafanaNPC;
    private List<MafanaCitizens> mafanaCitizensList = new ArrayList<>();
    private MessageManager messageManager;

    @Override
    public void onEnable() {
        mafanaNPC = this;
        messageManager = new MessageManager();
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerBreakBlock(), this);
        getServer().getPluginManager().registerEvents(new PlayerTalk(), this);
        getCommand("testnpc").setExecutor(new TestCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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
