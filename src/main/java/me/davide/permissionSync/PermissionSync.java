package me.davide.permissionSync;

import me.davide.permissionSync.Commands.Permission;
import me.davide.permissionSync.api.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class PermissionSync extends JavaPlugin {
    public static List<Group> groups = new ArrayList<>();
    public static HashMap<UUID, PermissionAttachment> permsMap = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("permission").setExecutor(new Permission());

        for (Player player : Bukkit.getOnlinePlayers())
            permsMap.put(player.getUniqueId(), player.addAttachment(this));

        loadDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Player player : Bukkit.getOnlinePlayers())
            player.removeAttachment(permsMap.get(player.getUniqueId()));
    }

    private void loadDefaultConfig() {
        for (String key : getConfig().getKeys(false)) {
            Group group = new Group(key);

            for (String perm : getConfig().getList(key + ".perms").toArray(new String[]{})) {
                group.addPerm(perm, false);
            }

            for (String playerName : getConfig().getList(key + ".players").toArray(new String[]{}))
                group.addPlayer(Bukkit.getPlayer(playerName), false);
        }
    }
}
