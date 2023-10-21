package osbourn.holdyourbreath.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import osbourn.holdyourbreath.HoldYourBreathClient;
import osbourn.holdyourbreath.HoldYourBreathConfig;

@Mixin(LivingEntity.class)
abstract class AirMixinClient extends Entity {
    public AirMixinClient(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * This method is for display only. The actual logic of when the player takes drowning damage is calculated on the
     * server side. See the similarly named method in AirMixin.
     */
    @ModifyExpressionValue(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;canBreatheInWater()Z"))
    public boolean onlyReduceAirIfDrowning(boolean originalReturnValue) {
        if (HoldYourBreathConfig.breathHoldingEnabled) {
            if ((Object)this instanceof PlayerEntity player) {
                if (!HoldYourBreathClient.isDrowning) {
                    return true;
                }
            }
        }
        return originalReturnValue;
    }
}
