package org.example.test;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Echest extends JavaPlugin implements CommandExecutor, TabCompleter, Listener {
    private final Map<String, Map<String, Inventory>> playerChests = new HashMap<>();
    private File chestsFile;
    private FileConfiguration chestsConfig;

    @Override
    public void onEnable() {
        getCommand("echest").setExecutor(this);
        getCommand("echest").setTabCompleter(this);
        Bukkit.getPluginManager().registerEvents(this, this);
        loadChests();
    }

    @Override
    public void onDisable() {
        saveChests();
    }

    private void loadChests() {
        chestsFile = new File(getDataFolder(), "chests.yml");
        if (!chestsFile.exists()) {
            try {
                getDataFolder().mkdirs();
                chestsFile.createNewFile();
            } catch (IOException e) {
                getLogger().severe("Could not create chests.yml!");
                return;
            }
        }

        chestsConfig = YamlConfiguration.loadConfiguration(chestsFile);
        for (String playerName : chestsConfig.getKeys(false)) {
            Map<String, Inventory> chestMap = new HashMap<>();
            for (String chestName : chestsConfig.getConfigurationSection(playerName).getKeys(false)) {
                int size = chestsConfig.getInt(playerName + "." + chestName + ".size", 27);
                Inventory inventory = Bukkit.createInventory(null, size, "Virtual Chest: " + chestName);
                List<ItemStack> items = (List<ItemStack>) chestsConfig.getList(playerName + "." + chestName + ".items");
                if (items != null) {
                    inventory.setContents(items.toArray(new ItemStack[0]));
                }
                chestMap.put(chestName, inventory);
            }
            playerChests.put(playerName, chestMap);
        }
    }

    private void saveChests() {
        chestsConfig = new YamlConfiguration();
        for (Map.Entry<String, Map<String, Inventory>> playerEntry : playerChests.entrySet()) {
            String playerName = playerEntry.getKey();
            for (Map.Entry<String, Inventory> chestEntry : playerEntry.getValue().entrySet()) {
                List<ItemStack> items = Arrays.asList(chestEntry.getValue().getContents());
                chestsConfig.set(playerName + "." + chestEntry.getKey() + ".size", chestEntry.getValue().getSize());
                chestsConfig.set(playerName + "." + chestEntry.getKey() + ".items", items);
            }
        }
        try {
            chestsConfig.save(chestsFile);
        } catch (IOException e) {
            getLogger().severe("Could not save chests.yml!");
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            saveChests();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("echest.use")) {
            player.sendMessage("You do not have permission to use this command!");
            return true;
        }

        playerChests.putIfAbsent(player.getName(), new HashMap<>());

        if (args.length >= 2 && args[0].equalsIgnoreCase("create")) {
            String chestName = args[1];
            int size = (args.length == 3) ? Integer.parseInt(args[2]) : 27;
            if (size % 9 != 0 || size < 9 || size > 54) {
                player.sendMessage("Invalid chest size! Use multiples of 9 (e.g., 9, 18, 27, 36, 45, 54)");
                return true;
            }
            if (playerChests.get(player.getName()).containsKey(chestName)) {
                player.sendMessage("You already have a chest with this name!");
                return true;
            }
            Inventory newChest = Bukkit.createInventory(player, size, "Virtual Chest: " + chestName);
            playerChests.get(player.getName()).put(chestName, newChest);
            saveChests();
            player.sendMessage("Created new Virtual Chest: " + chestName);
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("load")) {
            String chestName = args[1];
            Inventory chest = playerChests.get(player.getName()).get(chestName);
            if (chest == null) {
                player.sendMessage("No chest found with this name!");
                return true;
            }
            player.openInventory(chest);
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("delete")) {
            String chestName = args[1];
            if (!playerChests.get(player.getName()).containsKey(chestName)) {
                player.sendMessage("No chest found with this name!");
                return true;
            }
            playerChests.get(player.getName()).remove(chestName);
            saveChests();
            player.sendMessage("Deleted Virtual Chest: " + chestName);
            return true;
        }

        player.sendMessage("Usage:");
        player.sendMessage("/echest create <name> [size]");
        player.sendMessage("/echest load <name>");
        player.sendMessage("/echest delete <name>");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return Collections.emptyList();
        }
        Player player = (Player) sender;
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("create");
            completions.add("load");
            completions.add("delete");
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("load") || args[0].equalsIgnoreCase("delete"))) {
            completions.addAll(playerChests.getOrDefault(player.getName(), Collections.emptyMap()).keySet());
        }
        return completions;
    }
}
