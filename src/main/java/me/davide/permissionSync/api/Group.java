package me.davide.permissionSync.api;

import me.davide.permissionSync.PermissionSync;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class Group {
    private String name;
    private final List<UUID> players = new ArrayList<>();
    private final List<String> playerNames = new ArrayList<>();
    private final List<String> perms = new ArrayList<>();

    private final PermissionSync plugin = PermissionSync.getPlugin(PermissionSync.class);

    public Group(String name) {
        this.name = name;

        PermissionSync.groups.add(this);
        plugin.saveConfig();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Player> getPlayers() {
        List<Player> playerList = new ArrayList<>();

        for (UUID uuid : players)
            playerList.add(Bukkit.getPlayer(uuid));

        return playerList;
    }

    public void addPlayer(Player player, boolean save) {
        players.add(player.getUniqueId());
        players.forEach(p -> perms.forEach(s -> PermissionSync.permsMap.get(p).setPermission(s, true)));

        if (save) {
            players.forEach(uuid -> playerNames.add(Bukkit.getPlayer(uuid).getName()));
            plugin.getConfig().set(name + ".players", playerNames);
            plugin.saveConfig();
        }
    }

    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
        perms.forEach(s -> PermissionSync.permsMap.get(player.getUniqueId()).unsetPermission(s));

        plugin.getConfig().set(name + ".players", "");
        plugin.saveConfig();
    }

    public List<String> getPerms() {
        return perms;
    }

    public void addPerm(String permission, boolean save) {
        perms.add(permission);
        players.forEach(p -> perms.forEach(s -> PermissionSync.permsMap.get(p).setPermission(s, true)));

        if (save) {
            plugin.getConfig().set(name + ".perms", perms);
            plugin.saveConfig();
        }
    }

    public void removePerm(String permission) {
        perms.remove(permission);
        players.forEach(p -> perms.forEach(s -> PermissionSync.permsMap.get(p).unsetPermission(s)));

        plugin.getConfig().set(name + ".perms", "");
        plugin.saveConfig();
    }
    
    public static Group get(String name) {
        AtomicReference<Group> group = new AtomicReference<>();

        PermissionSync.groups.forEach(g -> {
            if (g.getName().equals(name))
                group.set(g);
        });

        return group.get();
    }

    public static List<Group> getAll() {
        return PermissionSync.groups;
    }

    public static List<Group> getByPlayer (UUID player) {
        List<Group> playerGroups = new ArrayList<>();

        for (Group g : PermissionSync.groups)
            for (UUID uuid : g.players)
                if (uuid == player)
                    playerGroups.add(g);

        return playerGroups;
    }
}
