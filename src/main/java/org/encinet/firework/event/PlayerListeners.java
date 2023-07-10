package org.encinet.firework.event;

import org.bukkit.Material;
import org.encinet.firework.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.encinet.firework.util.FireworkUtil;

public class PlayerListeners implements Listener {
    private final Firework plugin;

    public PlayerListeners(Firework plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() == null) {
                return;
            }

            if ((event.getItem().getType() == Material.GOLDEN_HORSE_ARMOR)
                && (event.getItem().getItemMeta().getDisplayName().equals(plugin.getConfigManager().rocket_gun_name))) {
                FireworkUtil.spawnFirework(event.getPlayer().getLocation(), 1);
            }
        }
    }
}
