package coda.toacrisp.client;

import coda.toacrisp.ToACrisp;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class WModelLayers {

    public static final ModelLayerLocation WYVERN = create("fly");

    private static ModelLayerLocation create(String name) {
        return new ModelLayerLocation(new ResourceLocation(ToACrisp.MOD_ID, name), "main");
    }
}
