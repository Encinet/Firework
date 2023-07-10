package org.encinet.firework.util;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.concurrent.ThreadLocalRandom;

public class FireworkUtil {
    public static void spawnFirework(Location location, int power) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        final FireworkEffect.Builder builder = FireworkEffect.builder();
        builder.withColor(randomColor(random));

        final FireworkEffect.Type[] type = FireworkEffect.Type.values();
        builder.with(type[random.nextInt(type.length)]);

        if (random.nextInt(3) == 0) {
            builder.withTrail();
        }
        if (random.nextInt(2) == 0) {
            builder.withFade(randomColor(random));
        }
        if (random.nextInt(3) == 0) {
            builder.withFlicker();
        }

        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);

        final FireworkMeta fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.clearEffects();
        fireworkMeta.addEffect(builder.build());
        fireworkMeta.setPower(power);
        firework.setFireworkMeta(fireworkMeta);
    }

    private static Color randomColor(ThreadLocalRandom random) {
        return Color.fromRGB(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }
}
