package io.github.catericka.supermonster.monsters;

import com.flowpowered.math.vector.Vector3d;
import io.github.catericka.supermonster.util.Config;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.monster.Monster;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;


public class SuperCharge {

    public static void Charge(Monster attacker) {
        if (attacker.getType().equals(EntityTypes.ZOMBIE) || attacker.getType().equals(EntityTypes.PIG_ZOMBIE)) {
            Optional<Player> targetPlayer = getTargetPlayer(attacker);
            if (targetPlayer.isPresent()) {
                //SuperMonster.getLogger().debug("SuperCharge Target: " + targetPlayer.get().toString());
                Location<World> L = getTargetLocation(attacker, targetPlayer.get());
                Vector3d vec = (new Vector3d(
                        L.getX() - attacker.getLocation().getX(),
                        Config.getConfigNode().getNode("SuperCharge", "superChargeDistance").getDouble(1.5D),
                        L.getZ() - attacker.getLocation().getZ()))
                        .normalize().mul(1.0D);
                attacker.setVelocity(vec);
            }
        }
    }

    public static Location<World> getTargetLocation(Monster attacker, Living entity) {
        return new Location<>(entity.getWorld(), entity.getLocation().getX(), attacker.getLocation().getY(), entity.getLocation().getZ());
    }

    public static Optional<Player> getTargetPlayer(Monster thisMob) {
        double radio;
        if ((thisMob.getWorld().getProperties().getWorldTime() % 24000L) < 12000L) {
            radio = Config.getConfigNode().getNode("SuperCharge", "dayRadio").getDouble(16);
        } else {
            radio = Config.getConfigNode().getNode("SuperCharge", "nightRadio").getDouble(64);
        }

        for (Entity entity : thisMob.getNearbyEntities(radio)) {
            if (entity instanceof Player) {
                Player player = (Player) entity;
                Optional<GameMode> gameMode = player.get(Keys.GAME_MODE);
                if (gameMode.map(m -> m.equals(GameModes.SURVIVAL) || m.equals(GameModes.ADVENTURE)).orElse(false)) {
                    return Optional.of(player);
                }
            }
        }
        return Optional.empty();
    }
}


