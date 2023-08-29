package me.tahacheji.mafana.command;

import me.tahacheji.mafana.MafanaNPC;
import me.tahacheji.mafana.data.MafanaCitizens;
import me.tahacheji.mafana.npc.Mafana;
import me.tahacheji.mafana.util.NPCUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class TestCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase("testnpc")) {
            Player player = (Player) sender;
            if (args[0].equalsIgnoreCase("spawn")) {

                MafanaCitizens mafanaCitizens = new MafanaCitizens("test");
                MafanaNPC.getInstance().getMafanaCitizensList().add(mafanaCitizens);
                String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                mafanaCitizens.spawnNPC(player.getLocation(), message);

            }
            if(args[0].equalsIgnoreCase("mafana")) {
                new Mafana();
            }
            if (args[0].equalsIgnoreCase("runto")) {
                for (Entity npc : player.getNearbyEntities(5, 5, 5)) {
                    if (new NPCUtil().isNPC(npc)) {
                        NPC x = CitizensAPI.getNPCRegistry().getNPC(npc);
                        MafanaCitizens mafanaCitizens = new NPCUtil().getMafanaCitizens(x);
                        mafanaCitizens.runTo(player.getLocation());
                    }
                }
            }
            if (args[0].equalsIgnoreCase("sneakto")) {
                for (Entity npc : player.getNearbyEntities(5, 5, 5)) {
                    if (new NPCUtil().isNPC(npc)) {
                        NPC x = CitizensAPI.getNPCRegistry().getNPC(npc);
                        MafanaCitizens mafanaCitizens = new NPCUtil().getMafanaCitizens(x);
                        mafanaCitizens.sneakTo(player.getLocation());
                    }
                }
            }
        }
        return false;
    }
}
