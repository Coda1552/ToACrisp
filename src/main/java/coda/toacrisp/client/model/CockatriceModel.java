package coda.toacrisp.client.model;

import coda.toacrisp.ToACrisp;
import coda.toacrisp.common.entities.CockatriceEntity;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class CockatriceModel extends AnimatedGeoModel<CockatriceEntity> {

    @Override
    public ResourceLocation getModelResource(CockatriceEntity object) {
        return new ResourceLocation(ToACrisp.MOD_ID, "geo/cockatrice.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CockatriceEntity object) {
        return new ResourceLocation(ToACrisp.MOD_ID, "textures/entity/cockatrice.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CockatriceEntity animatable) {
        return new ResourceLocation(ToACrisp.MOD_ID, "animations/cockatrice.animations.json");
    }

    @Override
    public void setLivingAnimations(CockatriceEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");
        IBone root = this.getAnimationProcessor().getBone("bone");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));

        if (entity.isBaby()) {
            root.setScaleX(0.5F);
            root.setScaleY(0.5F);
            root.setScaleZ(0.5F);
            root.setPositionY(-0.1F);
            head.setScaleX(1.4F);
            head.setScaleY(1.4F);
            head.setScaleZ(1.4F);
        }
        else {
            root.setPositionY(-0.1F);
        }
    }
}
