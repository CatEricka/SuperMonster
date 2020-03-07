package io.github.catericka.supermonster;

import com.google.inject.Inject;
import io.github.catericka.supermonster.monsters.*;
import io.github.catericka.supermonster.util.Config;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.io.File;

@Plugin(
        id = "supermonster",
        name = "SuperMonster",
        description = "SuperMonster clone from bukkit.",
        authors = {
                "Ayaya"
        }
)
public class SuperMonster {

    private static Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private File defaultConfig;

    private static Object ClassInstance;

    @Inject
    private void setLogger(Logger logger) {
        SuperMonster.logger = logger;
    }

    public static Logger getLogger() {
        return logger;
    }

    /**
     * 加载配置
     */
    @Listener
    public void onPreInitialization(GamePreInitializationEvent event) {
        logger.info("SuperMonster初始化.");
        ClassInstance = this;
        Config.setConfigFile(defaultConfig);
        Config.load();
        logger.info("SuperMonster初始化结束.");
    }

    /**
     * 注册监听事件并初始化所有类
     */
    @Listener
    public void onInitialization(GameInitializationEvent event) {
        CreeperExplode.init();
        MonsterAttack.init();
        MonsterAttributeControl.init();
        MonsterSpawnControl.init();
        SkeletonDefense.init();
        SkeletonCombineShot.init();
        PerMonsterTask.init();
    }

    /**
     * 注册命令
     */
    @Listener
    public void onServerStarting(GameStartingServerEvent event) {
        CommandSpec superMonsterCommandSpec = CommandSpec.builder()
                .description(Text.of("Super Monster 插件"))
                .permission("supermonster.admin")
                .executor((src, args) -> {
                    src.sendMessage(Text.of("/supermonster reload 重新加载配置"));
                    src.sendMessage(Text.of("或使用缩写/sm reload"));
                    return CommandResult.success();
                })
                .child(CommandSpec.builder()
                                .description(Text.of("重新加载Super Monster插件配置"))
                                .permission("supermonster.admin")
                                .executor((src, args) -> {
                                    src.sendMessage(Text.of("正在重新加载配置..."));
                                    configReload();
                                    return CommandResult.success();
                                })
                                .build(),
                        "reload"
                )
                .build();
        Sponge.getCommandManager().register(
                this, superMonsterCommandSpec,
                "supermonster", "sm");
    }

    /**
     * 加载结束.
     */
    @Listener
    public void onServerStarted(GameStartedServerEvent event) {
        logger.info("SuperMonster加载结束.");
    }

    /**
     * Reload
     */
    @Listener
    public void onReload(GameReloadEvent event) {
        configReload();
    }

    /**
     * 重载配置
     */
    public void configReload() {
        Config.reload();
    }

    /**
     * 获取插件实例
     */
    public static Object getInstance() {
        return ClassInstance;
    }

}
