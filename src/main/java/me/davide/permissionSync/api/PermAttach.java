package me.davide.permissionSync.api;

import me.davide.permissionSync.PermissionSync;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class PermAttach extends PermissionAttachment implements Listener {
    private static final PermissionSync plugin = PermissionSync.getPlugin(PermissionSync.class);
    private static final HashMap<UUID, PermissionAttachment> permMap = new HashMap<>();

    public PermAttach(Plugin plugin, Permissible permissible) {
        super(plugin, permissible);
    }

    public static void set(Player player, String permissionName, boolean permissionValue) {
        PermissionAttachment attachment = player.addAttachment(plugin);

        attachment.setPermission(permissionName, permissionValue);
        permMap.put(player.getUniqueId(), attachment);
    }

    public static void unset(Player player) {
        player.removeAttachment(permMap.get(player.getUniqueId()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        player.removeAttachment(permMap.get(player.getUniqueId()));
    }
}
