package me.davide.permissionSync.Commands;

import me.davide.permissionSync.PermissionSync;
import me.davide.permissionSync.api.Group;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Permission implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
        if (cmd.getName().equalsIgnoreCase("permission")) {
            if (args[0].equalsIgnoreCase("group")) {
                switch (args[1]) {
                    case "list" -> Group.getAll().forEach(g -> sender.sendMessage(g.getName()));
                    case "create" -> new Group(args[2]);
                    case "delete" -> PermissionSync.groups.remove(Group.get(args[2]));
                    case "perm" -> {
                        switch (args[2]) {
                            case "add" -> Group.get(args[3]).addPerm(args[4], true);
                            case "remove" -> Group.get(args[3]).removePerm(args[4]);
                            default -> {
                                sender.sendMessage("§cEnter a valid command");
                                return true;
                            }
                        }
                    }
                    case "user" -> {
                        switch (args[2]) {
                            case "add" -> Group.get(args[3]).addPlayer(Bukkit.getPlayer(args[4]), true);
                            case "remove" -> Group.get(args[3]).removePlayer(Bukkit.getPlayer(args[4]));
                            default -> {
                                sender.sendMessage("§cEnter a valid command");
                                return true;
                            }
                        }
                    }
                    default -> {
                        sender.sendMessage("§cEnter a valid command");
                        return true;
                    }
                }
            } else if (args[0].equalsIgnoreCase("user")) {
                Player player;
                if (Bukkit.getPlayer(args[2]) != null)
                    player = Bukkit.getPlayer(args[2]);
                else {
                    sender.sendMessage("§cEnter a valid player name");
                    return true;
                }

                assert player != null;
                switch (args[1]) {
                    case "groups" -> Group.getByPlayer(player.getUniqueId()).forEach(g -> sender.sendMessage(g.getName()));
                    case "perms" -> Group.getByPlayer(player.getUniqueId()).forEach(g -> g.getPerms().forEach(sender::sendMessage));
                    default -> {
                        sender.sendMessage("§cEnter a valid command");
                        return true;
                    }
                }
            } else {
                sender.sendMessage("§cEnter a valid command");
                return true;
            }
        }
        else return true;

        return false;
    }
}
