package io.github.catericka.supermonster.monsters;

import io.github.catericka.supermonster.SuperMonster;
import io.github.catericka.supermonster.util.Config;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.Hostile;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.IndirectEntityDamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.cause.First;


public class MonsterAttack {
    public static void init() {
        Sponge.getEventManager().registerListeners(SuperMonster.getInstance(), new MonsterAttack());
    }

    @Listener
    public void onAttack(DamageEntityEvent event, @First EntityDamageSource source) {
        if (!Config.isWorldEnable(source.getSource().getWorld().getName())) {
            return;
        }

        //debug info
        if (Config.getConfigNode().getNode("Debug", "enableDamageInfo").getBoolean(false)) {
            SuperMonster.getLogger().info("DamageInfo: DamageEntityEventTarget -> " + event.getTargetEntity().toString());
            SuperMonster.getLogger().info("  DamageInfo: EntityDamageSource -> " + source.getSource().getType().getName());
        }

        if (source instanceof IndirectEntityDamageSource) {
            IndirectEntityDamageSource indirectSource = (IndirectEntityDamageSource) source;

            //debug info
            if (Config.getConfigNode().getNode("Debug", "enableDamageInfo").getBoolean(false)) {
                SuperMonster.getLogger().info("  DamageInfo: IndirectEntityDamageSource -> " + indirectSource.getIndirectSource().getType().getName());
            }

            if (!(indirectSource.getIndirectSource() instanceof Hostile)) {
                return;
            }
        }

        if (Config.getConfigNode().getNode("MonsterAttributeControl", source.getSource().getType().getName(), "enable")
                .getBoolean(false)) {
            event.setBaseDamage(Config.getConfigNode()
                    .getNode("MonsterAttributeControl", source.getSource().getType().getName(), "AttackDamage")
                    .getDouble());

            //debug info
            if (Config.getConfigNode().getNode("Debug", "enableDamageInfo").getBoolean(false)) {
                SuperMonster.getLogger().info("  DamageInfo: Damage -> " + Config.getConfigNode()
                        .getNode("MonsterAttributeControl", source.getSource().getType().getName(), "AttackDamage")
                        .getDouble());
            }
        }
    }
}


