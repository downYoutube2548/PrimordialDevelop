package com.develop.primordial.primordialdevelop.YamlLoader;

import com.develop.primordial.primordialdevelop.PrimordialDevelop;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class LoadHashFile {

    public static void loadHashFile(HashMap<String, File> HashFile, HashMap<String, YamlConfiguration> HashConfig, String filePath, String fileName) {
        File file = HashFile.get(fileName);
        if (file == null) {
            file = new File(filePath, fileName+".yml");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("§cError! Can't load file \""+filePath+"/"+fileName+".yml\"!");
                    return;
                }
            }

            HashFile.put(fileName, file);
            HashConfig.put(fileName, YamlConfiguration.loadConfiguration(file));
        }
    }
}
