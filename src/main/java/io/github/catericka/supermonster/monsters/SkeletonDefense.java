package io.github.catericka.supermonster.monsters;

import com.flowpowered.math.vector.Vector3d;
import io.github.catericka.supermonster.SuperMonster;
import io.github.catericka.supermonster.util.Config;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.cause.First;

public class SkeletonDefense {
    public static void init() {
        Sponge.getEventManager().registerListeners(SuperMonster.getInstance(), new SkeletonDefense());
    }

    @Listener
    public void DefenseAway(DamageEntityEvent event, @First EntityDamageSource source) {
        Entity entity = event.getTargetEntity();
        Entity damager = source.getSource();
        if (!Config.isWorldEnable(entity.getWorld().getName()) || !Config.getConfigNode()
                .getNode("SkeletonDefense", "enable").getBoolean(true)) {
            return;
        }

        if (entity.getType().equals(EntityTypes.SKELETON) && damager.getType().equals(EntityTypes.PLAYER)) {
            Vector3d vec = (new Vector3d(
                    entity.getLocation().getX() - damager.getLocation().getX(),
                    0.01D,
                    entity.getLocation().getZ() - damager.getLocation().getZ()))
                    .normalize().mul(1.8D);
            entity.setVelocity(vec);
        }
    }
}


