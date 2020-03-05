package io.github.catericka.supermonster.monsters;


import io.github.catericka.supermonster.SuperMonster;
import io.github.catericka.supermonster.util.Config;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.world.explosion.Explosion;

public class CreeperExplode {

    public static void init() {
        Sponge.getEventManager().registerListeners(SuperMonster.getInstance(), new CreeperExplode());
    }

    @Listener
    public void DeathExplode(DestructEntityEvent.Death event) {
        Living entity = event.getTargetEntity();
        if (!Config.isEnableWorld(entity.getWorld().getName())) {
            return;
        }
        if (Config.getConfigNode().getNode("CreeperDeathExplode", "enable").getBoolean(true) && entity.getType() == EntityTypes.CREEPER) {
            entity.getWorld().triggerExplosion(Explosion.builder()
                    .location(entity.getLocation())
                    .radius(Config.getConfigNode().getNode("CreeperDeathExplode", "radius").getFloat(2))
                    .shouldBreakBlocks(Config.getConfigNode().getNode("CreeperDeathExplode", "shouldBreakBlocks").getBoolean(true))
                    .canCauseFire(Config.getConfigNode().getNode("CreeperDeathExplode", "canCauseFire").getBoolean(false))
                    .shouldPlaySmoke(Config.getConfigNode().getNode("CreeperDeathExplode", "shouldPlaySmoke").getBoolean(true))
                    .shouldDamageEntities(Config.getConfigNode().getNode("CreeperDeathExplode", "shouldDamageEntities").getBoolean(true))
                    .build());
        }
    }
}


