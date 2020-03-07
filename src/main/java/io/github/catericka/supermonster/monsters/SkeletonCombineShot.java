package io.github.catericka.supermonster.monsters;

import com.flowpowered.math.vector.Vector3d;
import io.github.catericka.supermonster.SuperMonster;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.entity.living.monster.Skeleton;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.projectile.arrow.Arrow;
import org.spongepowered.api.scheduler.Task;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


public class SkeletonCombineShot {
    private static HashMap<Skeleton, Integer> AimTimes = new HashMap<>();
    private static Task task;

    public static void init() {
        task = Task.builder()
                .execute(SkeletonCombineShot.AimTimes::clear)
                .delay(20L, TimeUnit.SECONDS)
                .interval(20L, TimeUnit.SECONDS)
                .name("Auto clean SkeletonCombineShot AimTimes")
                .submit(SuperMonster.getInstance());
    }

    public static void SecondCombineShot(Monster attacker) {
        if (attacker instanceof Skeleton) {
            Skeleton skeleton = (Skeleton) attacker;
            AimTimes.putIfAbsent(skeleton, 0);
            skeleton.getTarget().ifPresent(target -> {
                if (target instanceof Player) {
                    combineShot(skeleton);
                }
            });
        }
    }

    private static void combineShot(Skeleton skeleton) {
        AimTimes.put(skeleton, AimTimes.get(skeleton) + 1);
        if (AimTimes.get(skeleton) >= 7) {
            if (AimTimes.get(skeleton) == 7) {
                SuperMonster.getLogger().debug("午时已到!");
            }
            Optional<Entity> target = skeleton.getTarget();
            //only shot when target is a player
            if (target.map(t -> t instanceof Player).orElse(false)) {
                Vector3d vec = (new Vector3d(
                        target.get().getLocation().getX() - skeleton.getLocation().getX(),
                        target.get().getLocation().getY() - skeleton.getLocation().getY(),
                        target.get().getLocation().getZ() - skeleton.getLocation().getZ()))
                        .normalize().mul(1.8D);
                skeleton.launchProjectile(Arrow.class, vec);
            }
        }
    }
}


