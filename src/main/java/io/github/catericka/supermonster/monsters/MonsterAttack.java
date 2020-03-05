package io.github.catericka.supermonster.monsters;

import io.github.catericka.supermonster.SuperMonster;
import io.github.catericka.supermonster.util.Config;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.monster.Monster;
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

        //SuperMonster.getLogger().debug("DamageEntityEventTarget: " + event.getTargetEntity().toString());
        //SuperMonster.getLogger().debug("  EntityDamageSource: " + source.getSource().getType().getName());

        if (!Config.isEnableWorld(source.getSource().getWorld().getName())) {
            return;
        }

        if (source instanceof IndirectEntityDamageSource) {
            IndirectEntityDamageSource indirectSource = (IndirectEntityDamageSource) source;
            if (!(indirectSource.getIndirectSource() instanceof Monster)) {
                return;
            }
        }

        if (Config.getConfigNode().getNode("MonsterAttributeControl", source.getSource().getType().getName(), "enable")
                .getBoolean(false)) {
            event.setBaseDamage(Config.getConfigNode()
                    .getNode("MonsterAttributeControl", source.getSource().getType().getName(), "AttackDamage")
                    .getDouble());
            //SuperMonster.getLogger().debug("  damage: " + Config.getConfigNode().getNode("MonsterAttributeControl", source.getSource().getType().getName(), "AttackDamage").getDouble());
        }

    }
}


