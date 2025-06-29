package me.davide.permissionSync.Commands;

import me.davide.permissionSync.PermissionSync;
import me.davide.permissionSync.api.Group;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Permission implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
        if (cmd.getName().equalsIgnoreCase("permission")) {
            if (args[0].equalsIgnoreCase("group")) {
                switch (args[1]) {
                    case "list" -> {
                        List<String> list = new ArrayList<>();

                        list.add("§aGroups:");
                        PermissionSync.groups.forEach(g -> list.add(g.getName()));

                        if (list.size() <= 1) {
                            sender.sendMessage("§cNon ci sono gruppi!");
                            return true;
                        }

                        list.forEach(sender::sendMessage);
                    }
                    case "create" -> new Group(args[2]);
                    case "delete" -> PermissionSync.groups.remove(Group.get(args[2]));
                    case "perm" -> {
                        switch (args[2]) {
                            case "add" -> Group.get(args[3]).addPerm(args[4], true);
                            case "remove" -> Group.get(args[3]).removePerm(args[4]);
                            default -> {
                                sender.sendMessage("§cInserisci un comando valido");
                                return true;
                            }
                        }
                    }
                    case "user" -> {
                        Player player;
                        if (Bukkit.getPlayer(args[4]) != null)
                            player = Bukkit.getPlayer(args[4]);
                        else {
                            sender.sendMessage("§cInserisci un player valido");
                            return true;
                        }

                        if (player == null) {
                            sender.sendMessage("§cinserisci un player valido");
                            return true;
                        }

                        switch (args[2]) {

                            case "add" -> Group.get(args[3]).addPlayer(player, true);
                            case "remove" -> Group.get(args[3]).removePlayer(player);
                            default -> {
                                sender.sendMessage("§cInserisci un comando valido");
                                return true;
                            }
                        }
                    }
                    default -> {
                        sender.sendMessage("§cInserisci un comando valido");
                        return true;
                    }
                }
            } else if (args[0].equalsIgnoreCase("user")) {
                Player player;
                if (Bukkit.getPlayer(args[2]) != null)
                    player = Bukkit.getPlayer(args[2]);
                else {
                    sender.sendMessage("§cInserisci un player valido");
                    return true;
                }

                if (player == null) {
                    sender.sendMessage("§cInserisci un player valido");
                    return true;
                }

                switch (args[1]) {
                    case "groups" -> {
                        List<String> list = new ArrayList<>();

                        list.add("§aGroups" + player.getName() + " is in:");
                        Group.getByPlayer(player.getUniqueId()).forEach(g -> list.add(g.getName()));

                        if (list.size() <= 1) {
                            sender.sendMessage("§cQuesto player non è in nessun gruppo");
                            return true;
                        }

                        list.forEach(sender::sendMessage);
                    }
                    case "perms" ->  {
                        List<String> list = new ArrayList<>();

                        list.add("§aGroups" + player.getName() + " is in:");
                        Group.getByPlayer(player.getUniqueId()).forEach(g -> list.addAll(g.getPerms()));

                        if (list.size() <= 1) {
                            sender.sendMessage("§cQuesto player non ha nessun permesso");
                            return true;
                        }

                        list.forEach(sender::sendMessage);
                    }
                    default -> {
                        sender.sendMessage("§cInserisci un comando valido");
                        return true;
                    }
                }
            } else {
                sender.sendMessage("§cInserisci un comando valido");
                return true;
            }
        }
        else return true;

        return false;
    }
}
