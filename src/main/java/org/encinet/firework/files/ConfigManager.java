package org.encinet.firework.files;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.encinet.firework.Firework;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private final Firework plugin;
    public int default_seconds;
    public String rocket_gun_name;
    public List<Component> rocket_gun_lore;

    public String message_reload;
    public String message_set;
    public String message_unset;
    public String message_start;
    public String message_gun;
    public String message_help;
    public String message_no_permission;
    public String message_non_player;

    public ConfigManager(Firework plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        loadConfig();
    }

    public void loadConfig() {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        default_seconds = config.getInt("default-seconds", 60);
        rocket_gun_name = color(config.getString("firework-rocket.name", "&cFirework rocket"));

        rocket_gun_lore = new ArrayList<>();
        List<String> rocket_gun_lore_string = config.getStringList("firework-rocket.lore");
        rocket_gun_lore_string.forEach((value) -> rocket_gun_lore.add(Component.text(color(value))));
        if (rocket_gun_lore.size() == 0) {
            rocket_gun_lore.add(Component.text(color("&eRight-click &7to fire a firework!")));
        }

        message_reload = color(config.getString("message.reload", "&6&lFirework &8» &aPlugin reloaded successfully"));
        message_set = color(config.getString("message.set", "&6&lFirework &8» &aFirework location added successfully"));
        message_unset = color(config.getString("message.unset", "&6&lFirework &8» &aFirework location removed successfully"));
        message_start = color(config.getString("message.start", "&6&lFirework &8» &eThe fireworks show has started for &c{0} seconds&e!"));
        message_gun = color(config.getString("message.gun", "&6&lFirework &8» &aFirework gun has been added to your inventory."));
        message_help = color(config.getString("message.help", "---------- &6&lFirework ----------\n" +
                                                        "&c/firework reload &7- Reload config.yml and data.yml file\n" +
                                                        "&c/firework start [time in seconds] &7- Start firework event\n" +
                                                        "&c/firework set &7- Set location for firework rocket\n" +
                                                        "&c/firework unset &7- Unset location for firework rocket\n" +
                                                        "&c/firework gun &7- Get firework gun"));
        message_no_permission = color(config.getString("message.no-permission", "&cYou cannot do this."));
        message_non_player = color(config.getString("message.non-player"));
    }
    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
