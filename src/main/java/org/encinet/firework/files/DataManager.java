package org.encinet.firework.files;

import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.encinet.firework.Firework;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DataManager {
    private final Firework plugin;
    private final FileConfiguration config;
    private final File config_file;
    
    public final ArrayList<Location> firework_locations = new ArrayList<>();

    public DataManager(Firework plugin) {
        this.plugin = plugin;
        config_file = new File(plugin.getDataFolder(), "data.yml");
        if (!config_file.exists()) {
            config_file.getParentFile().mkdir();
            plugin.saveResource("data.yml", false);
        }
        config = new YamlConfiguration();
        reloadConfigFile();

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            Bukkit.getLogger().info("[Firework] Loading firework locations...");
            try {
                reload();
            } catch (NullPointerException ignored) {
            }
        }, 20L);
    }

    public void addLocation(Location location) {
        Location format = new Location(location.getWorld(),
                location.getBlockX() + 0.5, location.getBlockY(), location.getBlockZ() + 0.5);
        firework_locations.add(format);
        save();
    }

    public void save() {
        List<Map<String, Object>> data = new ArrayList<>();
        for (Location location : firework_locations) {
            Map<String, Object> info = new HashMap<>();
            info.put("world", location.getWorld().getName());
            info.put("x", location.getX());
            info.put("y", location.getY());
            info.put("z", location.getZ());
            data.add(info);
        }
        config.set("data", data);
        try {
            config.save(new File(plugin.getDataFolder(), "data.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteLocation(Location location) {
        for (Location loc : firework_locations) {
            if (loc.getBlockX() == location.getBlockX()
                && loc.getBlockY() == location.getBlockY()
                && loc.getBlockZ() == location.getBlockZ()
                && loc.getWorld().getName().equalsIgnoreCase(location.getWorld().getName())) {
                firework_locations.remove(loc);
                break;
            }
        }
    }

    public void onDisable() {
        save();
        firework_locations.clear();
    }

    public void reload() {
        firework_locations.clear();
        reloadConfigFile();
        @NotNull List<Map<?, ?>> data = config.getMapList("data");
        for (Map<?, ?> map : data) {
            String worldName = (String) map.get("world");
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                Location location = new Location(world,
                        (double) map.get("x"), (double) map.get("y"), (double) map.get("z"));
                firework_locations.add(location);
            }
        }
        Bukkit.getLogger().info("[Firework] Fireworks locations loaded successfully!");
    }

    public void reloadConfigFile() {
        try {
            config.load(config_file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
