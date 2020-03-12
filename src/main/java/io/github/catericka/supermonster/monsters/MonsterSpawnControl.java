package io.github.catericka.supermonster.monsters;

import io.github.catericka.supermonster.SuperMonster;
import io.github.catericka.supermonster.event.QueueMultiSpawn;
import io.github.catericka.supermonster.util.Config;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Hostile;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class MonsterSpawnControl {
    private int spawnCount;
    private boolean isSpawning;

    public static void init() {
        Sponge.getEventManager().registerListeners(SuperMonster.getInstance(), new MonsterSpawnControl());
    }

    @Listener
    public void SpawnControl(SpawnEntityEvent event) {
        event.getEntities().forEach(entity -> {
            if (event.getCause().containsType(QueueMultiSpawn.class)) {
                return;
            }
            if (!Config.isWorldEnable(entity.getWorld().getName()) || !(entity instanceof Hostile)) {
                return;
            }
            //SuperMonster.getLogger().debug(entity.getType().getName() + " instanceof Monster");

            if (Config.getConfigNode().getNode("MonsterSpawnControl", "enable").getBoolean(false) &&
                    Config.getConfigNode().getNode("MonsterSpawnControl", "QueueMultiSpawn", "enable").getBoolean(false)) {
                spawnCount++;
                if (spawnCount >= Config.getConfigNode().getNode("MonsterSpawnControl", "QueueMultiSpawn", "count").getInt(3)) {
                    int spawnTimes = Config.getConfigNode().getNode("MonsterSpawnControl", "QueueMultiSpawn", "count").getInt(3);

                    Location<World> spawnLocation = entity.getLocation();
                    for (int last = 0; last < spawnTimes - 1; last++) {
                        try (CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
                            Entity copiedEntity = spawnLocation.getExtent().createEntityNaturally(entity.getType(), spawnLocation.getPosition());
                            frame.pushCause(new QueueMultiSpawn());
                            frame.addContext(EventContextKeys.SPAWN_TYPE, SpawnTypes.PLUGIN);
                            spawnLocation.getExtent().spawnEntity(copiedEntity);
                        }
                    }

                    spawnCount = 0;
                } else {
                    event.setCancelled(true);
                }
            }
        });
    }
}


