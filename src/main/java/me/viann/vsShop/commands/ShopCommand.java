package me.viann.vsShop.commands;

import me.viann.vsShop.managers.ShopGUI;
import me.viann.vsShop.managers.ShopManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ShopCommand implements CommandExecutor, TabCompleter {

    private final ShopManager shopManager;
    private final JavaPlugin plugin;

    public ShopCommand(ShopManager shopManager, JavaPlugin plugin) {
        this.shopManager = shopManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("shop")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length == 0) {
                    player.sendMessage(ChatColor.YELLOW + "Available shops: " + String.join(", ", shopManager.getAvailableShops()));
                    return true;
                } else if (args.length == 1) {
                    String shopName = args[0].toLowerCase();
                    if (shopManager.shopExists(shopName)) {
                        ShopGUI.openShop(player, shopName, shopManager);
                        return true;
                    } else {
                        player.sendMessage(ChatColor.RED + "Shop not found: " + shopName);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("shop")) {
            if (args.length == 1) {
                Set<String> shopNames = shopManager.getAvailableShops();
                List<String> completions = new ArrayList<>();
                StringUtil.copyPartialMatches(args[0], shopNames, completions);
                return completions;
            }
        }
        return null;
    }
}

