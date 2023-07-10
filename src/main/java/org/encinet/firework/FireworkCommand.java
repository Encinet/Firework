package org.encinet.firework;

import net.kyori.adventure.text.Component;
import org.bukkit.command.TabExecutor;
import org.encinet.firework.files.ConfigManager;
import org.encinet.firework.util.PermissionUtil;
import org.encinet.firework.util.FireworkUtil;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FireworkCommand implements TabExecutor {
    private final Firework plugin;

    public FireworkCommand(Firework plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        ConfigManager config = plugin.getConfigManager();
        if (args.length != 0) {
            String options = args[0].toLowerCase();
            switch (options) {
                case "reload" -> {
                    if (PermissionUtil.has(sender, "firework.reload")) {
                        plugin.getLocationDataManager().reload();
                        plugin.getConfigManager().loadConfig();
                        sender.sendMessage(config.message_reload);
                    } else {
                        sender.sendMessage(config.message_no_permission);
                    }
                }
                case "unset" -> {
                    if (sender instanceof Player player) {
                        if (PermissionUtil.has(player, "firework.unset")) {
                            plugin.getLocationDataManager().deleteLocation(player.getLocation());
                            player.sendMessage(config.message_unset);
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_BOTTLE_THROW, 1.0f, 1.0f);
                        } else {
                            sender.sendMessage(config.message_no_permission);
                        }
                    } else {
                        sender.sendMessage(config.message_non_player);
                    }
                }
                case "set" -> {
                    if (sender instanceof Player player) {
                        if (PermissionUtil.has(player, "firework.set")) {
                            plugin.getLocationDataManager().addLocation(player.getLocation());
                            player.sendMessage(config.message_set);
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                        } else {
                            sender.sendMessage(config.message_no_permission);
                        }
                    } else {
                        sender.sendMessage(config.message_non_player);
                    }
                }
                case "start" -> {
                    if (PermissionUtil.has(sender, "firework.start")) {
                        int seconds;
                        if (args.length == 1) {
                            seconds = plugin.getConfigManager().default_seconds;
                        } else {
                            try {
                                seconds = Integer.parseInt(args[1]);
                                if (seconds <= 0) {
                                    seconds = plugin.getConfigManager().default_seconds;
                                }
                            } catch (NumberFormatException exception) {
                                seconds = plugin.getConfigManager().default_seconds;
                            }
                        }

                        int finalSeconds = seconds;
                        new BukkitRunnable() {
                            int sec = finalSeconds;

                            @Override
                            public void run() {
                                if (sec <= 0) {
                                    this.cancel();
                                    return;
                                }

                                for (Location location : plugin.getLocationDataManager().firework_locations) {
                                    FireworkUtil.spawnFirework(location, ThreadLocalRandom.current().nextInt(0, 2));
                                }

                                sec--;
                            }
                        }.runTaskTimer(plugin, 20L, 20L);
                        sender.sendMessage(MessageFormat.format(config.message_start, seconds));
                    } else {
                        sender.sendMessage(config.message_no_permission);
                    }
                }
                case "gun" -> {
                    if (sender instanceof Player player) {
                        if (PermissionUtil.has(sender, "firework.gun")) {
                            ItemStack rocket_gun = new ItemStack(Material.GOLDEN_HORSE_ARMOR);
                            ItemMeta rocket_gun_meta = rocket_gun.getItemMeta();
                            rocket_gun_meta.displayName(Component.text(plugin.getConfigManager().rocket_gun_name));
                            rocket_gun_meta.lore(plugin.getConfigManager().rocket_gun_lore);
                            rocket_gun.setItemMeta(rocket_gun_meta);
                            player.getInventory().addItem(rocket_gun);

                            player.sendMessage(config.message_gun);
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                        } else {
                            sender.sendMessage(config.message_no_permission);
                        }
                    } else {
                        sender.sendMessage(config.message_non_player);
                    }
                }
                default -> sender.sendMessage(config.message_help);
            }
        } else {
            sender.sendMessage(config.message_help);
        }
        return true;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tabs = new ArrayList<>();
        if (args.length == 1) {
            if (PermissionUtil.has(sender, "firework.reload")) tabs.add("reload");
            if (PermissionUtil.has(sender, "firework.set")) tabs.add("set");
            if (PermissionUtil.has(sender, "firework.unset")) tabs.add("unset");
            if (PermissionUtil.has(sender, "firework.start")) tabs.add("start");
            if (PermissionUtil.has(sender, "firework.gun")) tabs.add("gun");
        }
        return tabs;
    }
}
