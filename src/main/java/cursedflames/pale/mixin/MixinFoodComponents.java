package cursedflames.pale.mixin;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.FoodComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FoodComponents.class)
public class MixinFoodComponents {
	// Remove the vanilla hunger status effect from rotten flesh
	@Redirect(method = "<clinit>",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/item/FoodComponent$Builder;statusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;F)Lnet/minecraft/item/FoodComponent$Builder;",
					ordinal = 11))
	private static FoodComponent.Builder onRottenFleshStatusEffect(FoodComponent.Builder builder,
			StatusEffectInstance effectInstance, float chance) {
		return builder;
	}
}
