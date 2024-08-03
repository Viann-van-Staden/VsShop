package me.viann.vsShop.managers;

import me.viann.vsShop.VsShop;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Map;

public class ShopGUI implements Listener {

    private final JavaPlugin plugin;
    private final ShopManager shopManager;

    public ShopGUI(JavaPlugin plugin, ShopManager shopManager) {
        this.plugin = plugin;
        this.shopManager = shopManager;
    }

    public static void openShop(Player player, String shopName, ShopManager shopManager) {
        Map<String, Object> items = shopManager.getShopItems(shopName);
        Inventory shopInventory = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Shop: " + shopName);

        for (Map.Entry<String, Object> entry : items.entrySet()) {
            String itemName = entry.getKey();
            int price = (int) entry.getValue();
            Material material = Material.getMaterial(itemName);

            if (material != null) {
                ItemStack itemStack = new ItemStack(material);
                ItemMeta meta = itemStack.getItemMeta();
                meta.setLore(Arrays.asList(ChatColor.GOLD + "Price: " + ChatColor.WHITE + price + " coins"));
                itemStack.setItemMeta(meta);
                shopInventory.addItem(itemStack);
            }
        }

        player.openInventory(shopInventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().startsWith(ChatColor.GREEN + "Shop: ")) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) return;

            Player player = (Player) event.getWhoClicked();
            ItemStack itemStack = event.getCurrentItem();
            ItemMeta meta = itemStack.getItemMeta();
            int price = Integer.parseInt(ChatColor.stripColor(meta.getLore().get(0)).split(" ")[1]);

            Economy economy = VsShop.getEconomy();
            if (economy.getBalance(player) >= price) {
                economy.withdrawPlayer(player, price);
                player.getInventory().addItem(itemStack);
                player.sendMessage(ChatColor.GREEN + "You purchased " + itemStack.getType() + " for " + price + " coins.");
            } else {
                player.sendMessage(ChatColor.RED + "You don't have enough coins to purchase this item.");
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        // Handle any cleanup if necessary
    }
}

