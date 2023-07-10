package org.encinet.firework.util;

import org.bukkit.command.CommandSender;

public class PermissionUtil {
    public static boolean has(CommandSender sender, String permission) {
        return sender.hasPermission(permission) || sender.hasPermission("firework.admin");
    }
}
