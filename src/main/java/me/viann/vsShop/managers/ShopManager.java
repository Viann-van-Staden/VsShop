package me.viann.vsShop.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;
import java.util.Set;

public class ShopManager {

    private final JavaPlugin plugin;
    private FileConfiguration shopsConfig;

    public ShopManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadShopsConfig();
    }

    private void loadShopsConfig() {
        // Ensure the plugin data folder exists
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        // Save the default shops.yml file if it doesn't exist
        File shopsConfigFile = new File(plugin.getDataFolder(), "shops.yml");
        if (!shopsConfigFile.exists()) {
            plugin.saveResource("shops.yml", false);
        }

        // Load the shops configuration
        shopsConfig = YamlConfiguration.loadConfiguration(shopsConfigFile);
    }

    public Set<String> getAvailableShops() {
        return shopsConfig.getConfigurationSection("shops").getKeys(false);
    }

    public Map<String, Object> getShopItems(String shopName) {
        return shopsConfig.getConfigurationSection("shops." + shopName).getValues(false);
    }

    public boolean shopExists(String shopName) {
        return shopsConfig.contains("shops." + shopName);
    }
}
