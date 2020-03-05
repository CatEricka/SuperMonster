package io.github.catericka.supermonster.monsters;

import io.github.catericka.supermonster.SuperMonster;
import io.github.catericka.supermonster.util.Config;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class MonsterAttributeControl {
    public static void init() {
        Sponge.getEventManager().registerListeners(SuperMonster.getInstance(), new MonsterAttributeControl());
    }

    @Listener
    public void SpawnControl(SpawnEntityEvent event) {
        event.getEntities().forEach(entity -> {
            if (!Config.isEnableWorld(entity.getWorld().getName()) || !(entity instanceof Living)) {
                return;
            }

            if (Config.getConfigNode()
                    .getNode("MonsterAttributeControl", entity.getType().getName(), "enable")
                    .getBoolean(false)) {
                //SuperMonster.getLogger().debug(entity.toString() + " spawned.");

                //set max health
                if (entity.supports(Keys.MAX_HEALTH)) {
                    entity.offer(Keys.MAX_HEALTH, Config.getConfigNode()
                            .getNode("MonsterAttributeControl", entity.getType().getName(), "Healthy")
                            .getDouble(20));
                    //SuperMonster.getLogger().debug("  MAX_HEALTH of " + entity.getType().getName() + " set to " + Config.getConfigNode().getNode("MonsterAttributeControl", entity.getType().getName(), "Healthy").getDouble(20));
                } else {
                    SuperMonster.getLogger().warn(entity.getType().getName() + " NOT SUPPORT MAX_HEALTH!");
                }

                //set health to max health
                if (entity.supports(Keys.HEALTH)) {
                    //SuperMonster.getLogger().debug("  " + entity.getType().getName() + " SUPPORT HEALTH");
                    Optional<Double> maxHealth = entity.get(Keys.MAX_HEALTH);
                    maxHealth.ifPresent(h -> entity.offer(Keys.HEALTH, h));
                } else {
                    SuperMonster.getLogger().warn(entity.getType().getName() + " NOT SUPPORT HEALTH!");
                }

                //set potion effects
                if (entity.supports(Keys.POTION_EFFECTS)) {
                    //SuperMonster.getLogger().debug("  " + entity.getType().getName() + " SUPPORT POTION_EFFECTS");

                    List<PotionEffect> potionEffectList = new ArrayList<>();
                    //if entity have other POTION_EFFECTS
                    entity.get(Keys.POTION_EFFECTS).ifPresent(potionEffectList::addAll);

                    Config.getConfigNode()
                            .getNode("MonsterAttributeControl", entity.getType().getName(), "PotionEffect")
                            .getChildrenMap()
                            .forEach((k, v) -> {
                                String effect = k.toString();
                                int level = v.getInt(1);
                                //SuperMonster.getLogger().debug("  " + entity.getType().getName() + " potion effect set to " + effect.toString() + " " + level);
                                Sponge.getRegistry().getType(PotionEffectType.class, effect)
                                        .ifPresent(effectType ->
                                                potionEffectList.add(PotionEffect.builder()
                                                        .potionType(effectType)
                                                        .amplifier(level)
                                                        .duration(259980)
                                                        .particles(true)
                                                        .build()));
                            });
                    entity.offer(Keys.POTION_EFFECTS, potionEffectList);
                } else {
                    SuperMonster.getLogger().warn(entity.getType().getName() + " NOT SUPPORT POTION_EFFECTS!");
                }

                //display name
                if (Config.getConfigNode()
                        .getNode("MonsterAttributeControl", entity.getType().getName(), "DisplayName", "enable")
                        .getBoolean(false) && entity.supports(Keys.DISPLAY_NAME)) {
                    entity.offer(Keys.DISPLAY_NAME,
                            Text.of(Config.getConfigNode()
                                    .getNode("MonsterAttributeControl", entity.getType().getName(), "DisplayName", "name")
                                    .getString("")));
                }
            }

        });
    }
}


