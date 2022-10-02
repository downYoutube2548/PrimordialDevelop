package com.develop.primordial.primordialdevelop;

import com.Teenkung123.primordial.PranaRegeneration;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.develop.primordial.primordialdevelop.CommandManager.DefaultCommands.ReloadCommand;
import com.develop.primordial.primordialdevelop.CommandManager.MainCommand;
import com.develop.primordial.primordialdevelop.CommandManager.SubCommandManager;
import com.develop.primordial.primordialdevelop.YamlLoader.JoinEvent;
import com.develop.primordial.primordialdevelop.YamlLoader.LeaveEvent;
import com.develop.primordial.primordialdevelop.YamlLoader.LoadHashFile;
import com.develop.primordial.primordialdevelop.YamlLoader.SaveHashFile;
import com.downYoutube2548.primordial.DailyQuest.DailyQuestInventory;
import com.downYoutube2548.primordial.DailyQuest.QuestCommand;
import com.downYoutube2548.primordial.DamageHandle.*;
import com.downYoutube2548.primordial.MMOCore.ClassInventory.ClassInventoryManager;
import com.downYoutube2548.primordial.MMOCore.ClassInventory.Inventories.CharacterUpgradeConfirm;
import com.downYoutube2548.primordial.MMOCore.ClassInventory.Inventories.ClassCharacterUpgradeInventory;
import com.downYoutube2548.primordial.MMOCore.ClassInventory.Inventories.ClassLevelUpgradeInventory;
import com.downYoutube2548.primordial.MMOCore.ClassInventory.Inventories.ClassManageInventory;
import com.downYoutube2548.primordial.MMOCore.SubCommands.GuiCommand;
import com.downYoutube2548.primordial.MMOCore.SubCommands.MMOCoreCommand;
import com.downYoutube2548.primordial.MMOItems.ItemStatAPI.CommandWhenPickUp;
import com.downYoutube2548.primordial.PlaceholderRegister;
import com.google.common.io.ByteStreams;
import net.Indyuce.mmoitems.MMOItems;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public final class PrimordialDevelop extends JavaPlugin {

    public static YamlConfiguration configManager;

    private static PrimordialDevelop Instance;
    public static SubCommandManager subCommandManager;
    public static ClassInventoryManager classInventoryManager;
    public static ProtocolManager protocolManager;
    public static Economy economy = null;
    public static LuckPerms luckPerms;

    public static boolean debug;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Instance = this;

        getCommand("primordial").setExecutor(new MainCommand());
        getCommand("character").setExecutor(new com.develop.primordial.primordialdevelop.CommandManager.CharacterCommand());

        MMOItems.plugin.getStats().register(new CommandWhenPickUp());
        Bukkit.getServer().getPluginManager().registerEvents(new FireDamage(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new FallDamage(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new LightningDamage(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ContactDamage(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PoisonDamage(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new WitherDamage(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new HotFloorDamage(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new JoinEvent(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new LeaveEvent(this), this);

        loadConfig(this);
        makePlayerDataDir();

        protocolManager = ProtocolLibrary.getProtocolManager();

        classInventoryManager = new ClassInventoryManager();

        Bukkit.getServer().getPluginManager().registerEvents(classInventoryManager, this);


        new PlaceholderRegister().register();
        Bukkit.getPluginManager().registerEvents(new PranaRegeneration(), this);
        Bukkit.getPluginManager().registerEvents(new JoinEvent(this), this);

        subCommandManager = new SubCommandManager();

        subCommandManager.registerSubCommand(new MMOCoreCommand(this));
        subCommandManager.registerSubCommand(new QuestCommand());
        subCommandManager.registerSubCommand(new GuiCommand());
        subCommandManager.registerSubCommand(new ReloadCommand(this));
        classInventoryManager.registerClassInventory(new ClassManageInventory());
        classInventoryManager.registerClassInventory(new ClassLevelUpgradeInventory());
        classInventoryManager.registerClassInventory(new ClassCharacterUpgradeInventory());
        classInventoryManager.registerClassInventory(new CharacterUpgradeConfirm());
        classInventoryManager.registerClassInventory(new DailyQuestInventory());

        debug = configManager.getBoolean("debug");

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();

        }
        setupEconomy();

        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                        LoadHashFile.loadHashFile(PrimordialDevelop.Files, PrimordialDevelop.PlayerData, getDataFolder() + "/PlayerData", player.getUniqueId().toString());
                }
            });
        }

    }

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        economy = rsp.getProvider();
    }

    private static File loadResource(Plugin plugin, String resource) {
        File folder = plugin.getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (InputStream in = plugin.getResource(resource);
                     OutputStream out = new FileOutputStream(resourceFile)) {
                    ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) {

        }
        return resourceFile;
    }

    public static void loadConfig(Plugin plugin) {
        configManager = YamlConfiguration.loadConfiguration(loadResource(plugin, "config.yml"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                SaveHashFile.saveHashFile(PrimordialDevelop.Files, PrimordialDevelop.PlayerData, PrimordialDevelop.PlayerData.get(player.getUniqueId().toString()), PrimordialDevelop.Files.get(player.getUniqueId().toString()));
                PrimordialDevelop.Files.remove(player.getUniqueId().toString());
                PrimordialDevelop.PlayerData.remove(player.getUniqueId().toString());
            }
        }
    }

    public static PrimordialDevelop getInstance() { return Instance; }

    public void makePlayerDataDir() {
        File DataFolder = new File(getDataFolder()+"/PlayerData/");
        if (!DataFolder.exists()) DataFolder.mkdir();
    }
    public static HashMap<String, File> Files = new HashMap<>();
    public static HashMap<String, YamlConfiguration> PlayerData = new HashMap<>();
}
