package cursedflames.pale.mixin;

import cursedflames.pale.common.StatusEffectPale;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponents;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
	@Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

	@Shadow public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

	private MixinLivingEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	// Target after vanilla effects are applied, but before the stack size is decremented.
	@Inject(method = "eatFood(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/LivingEntity;applyFoodEffects(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)V",
					ordinal = 0,
					shift = At.Shift.AFTER))
	private void onEatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
		if (stack.isEmpty() || stack.getItem().getFoodComponent() != FoodComponents.ROTTEN_FLESH) {
			return;
		}

		int baseDuration = 3*60*20;
		int durationIncrement = 10*20;

		StatusEffectInstance currentEffect = this.getStatusEffect(StatusEffectPale.PALE);
		if (currentEffect == null) {
			this.addStatusEffect(new StatusEffectInstance(StatusEffectPale.PALE, baseDuration, 0, false, false, true));
			return;
		}
		// Extend current effect, and amplify effect if below max level
		int level = currentEffect.getAmplifier()+1;
		if (level > 3) {
			level = 3;
			this.playSound(SoundEvents.ENTITY_ZOMBIE_AMBIENT, 0.5f, 0.8f);
		}
		int time = Math.max(currentEffect.getDuration() + durationIncrement, baseDuration);
		currentEffect.upgrade(new StatusEffectInstance(StatusEffectPale.PALE, time, level, false, false, true));
	}
}
