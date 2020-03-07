package io.github.catericka.supermonster.monsters;

import io.github.catericka.supermonster.SuperMonster;
import io.github.catericka.supermonster.util.Config;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.concurrent.TimeUnit;


public class PerMonsterTask {
    private static Task task;
    private static int superChargeElapsedSeconds = 0;
    private static boolean canCharge = false;

    public static void init() {
        task = Task.builder()
                .execute(PerMonsterTask::run)
                .delay(1, TimeUnit.SECONDS)
                .interval(1, TimeUnit.SECONDS)
                .submit(SuperMonster.getInstance());
    }

    public static void run() {
        superChargeElapsedSeconds++;
        //SuperMonster.getLogger().debug(superChargeElapsedSeconds + "->" + Config.getConfigNode().getNode("SuperCharge", "coolDown").getInt(2));
        if (superChargeElapsedSeconds >= Config.getConfigNode().getNode("SuperCharge", "coolDown").getInt(2)) {
            canCharge = true;
            //SuperMonster.getLogger().debug("Charge active.");
            superChargeElapsedSeconds=0;
        }
        Config
                .getConfigNode()
                .getNode("EnableWorlds")
                .getChildrenList().forEach(worldName -> getWorldByName(worldName.getString())
                .ifPresent(worlds -> worlds.getEntities().forEach(attacker -> {
                            if (attacker instanceof Monster) {
                                if (canCharge) {
                                    runCharge((Monster) attacker);
                                }
                                runSecondCombineShot((Monster) attacker);
                            }
                        })
                ));
        canCharge = false;
    }

    private static void runCharge(Monster attacker) {
        if (Config.getConfigNode().getNode("SuperCharge", "enable").getBoolean(true)) {
            SuperCharge.Charge(attacker);
        }
    }

    private static void runSecondCombineShot(Monster attacker) {
        if (Config.getConfigNode().getNode("SkeletonCombineShot", "enable").getBoolean(true)) {
            SkeletonCombineShot.SecondCombineShot(attacker);
        }
    }

    private static Optional<World> getWorldByName(String worldName) {
        return Optional.ofNullable(worldName).flatMap(Sponge.getServer()::getWorld);
    }
}


