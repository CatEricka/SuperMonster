package io.github.catericka.supermonster.monsters;

import com.flowpowered.math.vector.Vector3d;
import io.github.catericka.supermonster.SuperMonster;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.entity.living.monster.Skeleton;
import org.spongepowered.api.entity.projectile.arrow.Arrow;
import org.spongepowered.api.scheduler.Task;

import java.util.HashMap;


public class SkeletonCombieShot {
    private static HashMap<Skeleton, Integer> AimTimes = new HashMap<>();
    private static Task task;

    public static void init() {
        task = Task.builder()
                .execute(() -> SkeletonCombieShot.AimTimes.clear())
                .delayTicks(400L)
                .intervalTicks(400L)
                .name("Auto clean SkeletonCombieShot AimTimes")
                .submit(SuperMonster.getInstance());
    }

    public static void SecondCombieShot(Monster attacker) {
        if (attacker instanceof Skeleton) {
            Skeleton skeleton = (Skeleton) attacker;
            AimTimes.putIfAbsent(skeleton, 0);
            skeleton.getTarget().ifPresent(target -> {
                AimTimes.put(skeleton, AimTimes.get(skeleton) + 1);
                if (AimTimes.get(skeleton) >= 7) {
                    if (AimTimes.get(skeleton) == 7) {
                        SuperMonster.getLogger().debug("午时已到!");
                    }
                    Vector3d vec = (new Vector3d(
                            target.getLocation().getX() - skeleton.getLocation().getX(),
                            target.getLocation().getY() - skeleton.getLocation().getY(),
                            target.getLocation().getZ() - skeleton.getLocation().getZ()))
                            .normalize().mul(1.8D);
                    skeleton.launchProjectile(Arrow.class, vec);
                }
            });
        }
    }
}


