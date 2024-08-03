package me.viann.vsShop;

import me.viann.vsShop.commands.ShopCommand;
import me.viann.vsShop.managers.ShopGUI;
import me.viann.vsShop.managers.ShopManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class VsShop extends JavaPlugin {

    private static Economy economy = null;
    private ShopManager shopManager;

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().severe("Vault dependency not found! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Initialize ShopManager
        shopManager = new ShopManager(this);

        // Register the shop command
        ShopCommand shopCommand = new ShopCommand(shopManager, this);
        this.getCommand("shop").setExecutor(shopCommand);
        this.getCommand("shop").setTabCompleter(shopCommand);

        // Register the event listener for inventory interactions
        getServer().getPluginManager().registerEvents(new ShopGUI(this, shopManager), this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public static Economy getEconomy() {
        return economy;
    }

    public ShopManager getShopManager() {
        return shopManager;
    }
}

