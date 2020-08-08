package cursedflames.pale;

import cursedflames.pale.common.StatusEffectPale;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.registry.Registry;

public class Pale implements ModInitializer {
	public static final String MODID = "pale";

	@Override
	public void onInitialize() {
		Registry.register(Registry.STATUS_EFFECT, StatusEffectPale.ID, StatusEffectPale.PALE);
	}
}
