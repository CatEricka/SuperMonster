package io.github.catericka.supermonster.monsters;

import io.github.catericka.supermonster.SuperMonster;
import io.github.catericka.supermonster.util.Config;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.scheduler.Task;

import java.util.Optional;


public class PerMonsterTask {
    private static Task task;
    private static int SuperChargeElapsedSeconds = 0;
    private static boolean canCharge = false;

    public static void init() {
        task = Task.builder()
                .execute(PerMonsterTask::run)
                .delayTicks(20L)
                .intervalTicks(20L)
                .submit(SuperMonster.getInstance());
    }

    public static void run() {
        SuperChargeElapsedSeconds++;
        if (SuperChargeElapsedSeconds >= Config.getConfigNode().getNode("SuperCharge", "coolDown").getInt(2)) {
            canCharge = true;
            SuperChargeElapsedSeconds = 0;
        }
        Config.getConfigNode().getNode("EnableWorlds").getChildrenList().forEach(worldNameNode -> {
            Sponge.getServer().getWorld(Optional.ofNullable(worldNameNode.getString()).orElse("")).ifPresent(value -> {
                value.getEntities().forEach(attacker -> {
                    if (attacker instanceof Monster) {
                        if (Config.getConfigNode().getNode("SuperCharge", "enable").getBoolean(true) && canCharge) {
                            //SuperMonster.getLogger().debug("Charge active.");
                            SuperCharge.Charge((Monster) attacker);
                        }
                        if (Config.getConfigNode().getNode("SkeletonCombieShot", "enable").getBoolean(true)) {
                            SkeletonCombieShot.SecondCombieShot((Monster) attacker);
                        }
                    }
                });
            });
        });
        canCharge = false;
    }
}


