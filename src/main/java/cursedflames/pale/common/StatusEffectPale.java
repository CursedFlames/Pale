package cursedflames.pale.common;

import cursedflames.pale.Pale;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public class StatusEffectPale extends StatusEffect {
	public static final Identifier ID = new Identifier(Pale.MODID, "pale");
	public static final StatusEffectPale PALE =
			new StatusEffectPale(StatusEffectType.HARMFUL, 0xffb340);

	public StatusEffectPale(StatusEffectType type, int color) {
		super(type, color);
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (entity.world.isDay() && !entity.isFireImmune() && !entity.isWet()) {
			BlockPos pos = new BlockPos(entity.getPos());
			// Not sure if this check is necessary, but vanilla does it for mob burning
			if (entity.getVehicle() instanceof BoatEntity) {
				pos = pos.up();
			}
			if (entity.world.isSkyVisible(pos)) {
				float damage = (entity.world.getBiome(pos).getPrecipitation()== Biome.Precipitation.NONE) ? 2 : 1;
				entity.damage(DamageSource.ON_FIRE, damage);
			}
		}
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return (duration % (40 * Math.max(1, 4-amplifier))) == 0;
	}
}
