package me.tahacheji.mafana.npc;

import me.tahacheji.mafana.MafanaNPC;
import me.tahacheji.mafana.data.MafanaCitizens;
import me.tahacheji.mafana.task.FlowerTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;

public class Mafana extends MafanaCitizens {
    public Mafana() {
        super("Mafana");
        spawnNPC(new Location(Bukkit.getWorld("world"), 5, 121, -0.5), "Dumb guy");

        setBoundaryX(new Location(Bukkit.getWorld("world"), 3, 120, -3));
        setBoundaryY(new Location(Bukkit.getWorld("world"), 21, 120, 12));

        setFlowerLocation(getBlockLocationsWithinBoundary(Material.POPPY));

        ArrayList<Location> points = new ArrayList<>();
        points.add(new Location(Bukkit.getWorld("world"), 14, 120, 2));
        points.add(new Location(Bukkit.getWorld("world"), 7, 120, 9));
        points.add(new Location(Bukkit.getWorld("world"), 19, 120, 11));
        setPointLocation(points);

        setTaskEvents(new FlowerTask(Material.POPPY));

        startCitizen();
        MafanaNPC.getInstance().getMafanaCitizens().add(this);
    }
}
