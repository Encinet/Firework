package org.encinet.firework;

import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.encinet.firework.files.ConfigManager;
import org.encinet.firework.event.PlayerListeners;
import org.encinet.firework.files.DataManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Firework extends JavaPlugin {
    private DataManager dataManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        dataManager = new DataManager(this);

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListeners(this), this);

        PluginCommand command = getCommand("firework");
        if (command != null) {
            command.setExecutor(new FireworkCommand(this));
        }
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        if (dataManager != null) {
            dataManager.onDisable();
        }
    }

    public DataManager getLocationDataManager() {
        return dataManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
