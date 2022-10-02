package com.develop.primordial.primordialdevelop.YamlLoader;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class SaveHashFile {
    public static void saveHashFile(HashMap<String, File> HashFile, HashMap<String, YamlConfiguration> HashConfig, YamlConfiguration configuration, File file) {
        try {
            configuration.save(file);
        } catch (IOException e) {
            System.out.println("§cError! Can't save file.");
            return;
        }
        HashFile.remove(file.getName().replace(".yml", ""));
        HashConfig.remove(file.getName().replace(".yml", ""));
    }
}
