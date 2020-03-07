package io.github.catericka.supermonster.util;

import io.github.catericka.supermonster.SuperMonster;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Config {
    private static File configFile;

    private static ConfigurationLoader<CommentedConfigurationNode> configLoader;

    private static ConfigurationNode configNode;

    public static void load() {
        SuperMonster.getLogger().info("加载配置: " + configFile.toString());
        configLoader = HoconConfigurationLoader
                .builder()
                .setFile(configFile)
                .build();
        if (!configFile.exists()) {
            SuperMonster.getLogger().warn("配置文件不存在!");
            SuperMonster.getLogger().warn("创建配置文件: " + configFile.toString());
            try {
                configFile.createNewFile();
                configNode = configLoader.load(ConfigurationOptions.defaults());
                setDefaultConfig();
                configLoader.save(configNode);
            } catch (IOException e) {
                SuperMonster.getLogger().error("创建配置失败.");
                e.printStackTrace();
            }
        }
        try {
            configNode = configLoader.load(ConfigurationOptions.defaults());
        } catch (IOException e) {
            SuperMonster.getLogger().warn("加载配置失败.");
            e.printStackTrace();
        }
    }

    public static void reload() {
        load();
    }

    public static void save() {
        try {
            configLoader.save(configNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setConfigFile(File configFile) {
        Config.configFile = configFile;
    }

    public static ConfigurationNode getConfigNode() {
        return configNode;
    }

    public static void setDefaultConfig() {
        List<String> worldList = new ArrayList<>();
        worldList.add("world");
        configNode.getNode("EnableWorlds").setValue(worldList);

        configNode.getNode("SuperCharge", "enable").setValue(true);
        configNode.getNode("SuperCharge", "coolDown").setValue(2);
        configNode.getNode("SuperCharge", "superChargeDistance").setValue(1.5D);
        configNode.getNode("SuperCharge", "dayRadio").setValue(16.0D);
        configNode.getNode("SuperCharge", "nightRadio").setValue(64.0D);

        configNode.getNode("MonsterAttributeControl", "zombie", "enable").setValue(true);
        configNode.getNode("MonsterAttributeControl", "zombie", "DisplayName", "enable").setValue(true);
        configNode.getNode("MonsterAttributeControl", "zombie", "DisplayName", "name").setValue("可怕僵尸先生");
        configNode.getNode("MonsterAttributeControl", "zombie", "Healthy").setValue(30);
        configNode.getNode("MonsterAttributeControl", "zombie", "AttackDamage").setValue(6);
        Map<String, Integer> potionEffectList = new HashMap<>();
        potionEffectList.put("SPEED", 1);
        configNode.getNode("MonsterAttributeControl", "zombie", "PotionEffect").setValue(potionEffectList);

        configNode.getNode("MonsterAttributeControl", "skeleton", "enable").setValue(true);
        configNode.getNode("MonsterAttributeControl", "skeleton", "DisplayName", "enable").setValue(true);
        configNode.getNode("MonsterAttributeControl", "skeleton", "DisplayName", "name").setValue("可怕骷髅先生");
        configNode.getNode("MonsterAttributeControl", "skeleton", "Healthy").setValue(30);
        configNode.getNode("MonsterAttributeControl", "skeleton", "AttackDamage").setValue(6);
        potionEffectList = new HashMap<>();
        configNode.getNode("MonsterAttributeControl", "zombie", "PotionEffect").setValue(potionEffectList);

        configNode.getNode("MonsterAttributeControl", "arrow", "enable").setValue(true);
        configNode.getNode("MonsterAttributeControl", "skeleton", "AttackDamage").setValue(3);

        configNode.getNode("MonsterSpawnControl", "enable").setValue(true);
        configNode.getNode("MonsterSpawnControl", "QueueMultiSpawn", "enable").setValue(true);
        configNode.getNode("MonsterSpawnControl", "QueueMultiSpawn", "count").setValue(3);

        configNode.getNode("CreeperDeathExplode", "enable").setValue(true);
        configNode.getNode("CreeperDeathExplode", "radius").setValue(2);
        configNode.getNode("CreeperDeathExplode", "shouldBreakBlocks").setValue(true);
        configNode.getNode("CreeperDeathExplode", "canCauseFire").setValue(false);
        configNode.getNode("CreeperDeathExplode", "shouldPlaySmoke").setValue(true);
        configNode.getNode("CreeperDeathExplode", "shouldDamageEntities").setValue(true);

        configNode.getNode("SkeletonDefense", "enable").setValue(true);

        configNode.getNode("SkeletonCombineShot", "enable").setValue(true);
    }

    public static boolean isWorldEnable(String worldName) {
        List<? extends ConfigurationNode> worlds = configNode.getNode("EnableWorlds").getChildrenList();
        for (ConfigurationNode world :
                worlds) {
            return Objects.equals(world.getString(), worldName);
        }
        return false;
    }
}
