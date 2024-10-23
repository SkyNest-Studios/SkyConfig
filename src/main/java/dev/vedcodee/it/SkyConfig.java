package dev.vedcodee.it;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

@Getter
public abstract class SkyConfig {

    private final File configDir;
    private final File configFile;
    private final String fileName;
    private final FileConfiguration configuration;

    public SkyConfig(File configDir, String name) {
        this.configDir = configDir;
        this.fileName = name + ".yml";

        if (!configDir.exists()) configDir.mkdirs();
        this.configFile = new File(configDir, fileName);

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.configuration = new YamlConfiguration();

        reload();
    }


    // Setters
    public void set(String patch, Object content, boolean force) {
        if(isSet(patch) && !force) return;
        configuration.set(patch, content);
    }

    public void set(String patch, Object content) {
        set(patch, content, true);
    }

    public void setDefault(String patch, Object content) {
        set(patch, content, false);
        save();
    }

    public void setLocation(String patch, Location location) {
        StringBuilder toString = new StringBuilder()
                .append(location.getWorld().getName()).append(";;")
                .append(location.getX()).append(";;")
                .append(location.getY()).append(";;")
                .append(location.getZ()).append(";;")
                .append(location.getYaw()).append(";;")
                .append(location.getPitch());

        set(patch, toString.toString());
    }

    // Getters
    public Object get(String patch) {
        return configuration.get(patch);
    }

    public String getString(String patch) {
        return configuration.getString(patch);
    }

    public ConfigurationSection getSelection(String patch) {
        return configuration.getConfigurationSection(patch);
    }

    public int getInt(String patch) {
        return configuration.getInt(patch);
    }

    public boolean getBool(String patch) {
        return configuration.getBoolean(patch);
    }

    public Location getLocation(String patch) {
        String loc = getString(patch);
        if(loc == null) return null;
        String[] split = loc.split(";;");
        World world = Bukkit.getWorld(split[0]);
        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);
        float yaw = Float.parseFloat(split[4]);
        float pitch = Float.parseFloat(split[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }


    // Utils
    public boolean isSet(String patch) {
        return configuration.isSet(patch);
    }

    public void reload() {
        try {
            configuration.load(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            configuration.save(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
