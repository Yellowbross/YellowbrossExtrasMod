package com.yellowbrossproductions.yellowbrossextras.client.render.layer;

import java.util.Map;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HeadItemLayer<T extends LivingEntity, M extends EntityModel<T> & CustomHeadedModel> extends RenderLayer<T, M> {
   private final float scaleX;
   private final float scaleY;
   private final float scaleZ;
   private final Map<SkullBlock.Type, SkullModelBase> skullModels;
   private final ItemInHandRenderer itemInHandRenderer;

   public HeadItemLayer(RenderLayerParent<T, M> layerParent, EntityModelSet entityModelSet, ItemInHandRenderer itemInHandRenderer) {
      this(layerParent, entityModelSet, 1.0F, 1.0F, 1.0F, itemInHandRenderer);
   }

    public HeadItemLayer(RenderLayerParent<T, M> layerParent, EntityModelSet entityModelSet, ItemInHandRenderer itemInHandRenderer, float scale) {
        this(layerParent, entityModelSet, scale, scale, scale, itemInHandRenderer);
    }

   public HeadItemLayer(RenderLayerParent<T, M> layerParent, EntityModelSet entityModelSet, float scaleX, float scaleY, float scaleZ, ItemInHandRenderer itemInHandRenderer) {
      super(layerParent);
      this.scaleX = scaleX;
      this.scaleY = scaleY;
      this.scaleZ = scaleZ;
      this.skullModels = SkullBlockRenderer.createSkullRenderers(entityModelSet);
      this.itemInHandRenderer = itemInHandRenderer;
   }

   public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
      ItemStack itemstack = pLivingEntity.getItemBySlot(EquipmentSlot.HEAD);
      if (!itemstack.isEmpty()) {
         Item item = itemstack.getItem();
         pPoseStack.pushPose();
         pPoseStack.scale(this.scaleX, this.scaleY, this.scaleZ);
         boolean flag = pLivingEntity instanceof Villager || pLivingEntity instanceof ZombieVillager;
         if (pLivingEntity.isBaby() && !(pLivingEntity instanceof Villager)) {
            float f = 2.0F;
            float f1 = 1.4F;
            pPoseStack.translate(0.0D, 0.03125D, 0.0D);
            pPoseStack.scale(0.7F, 0.7F, 0.7F);
            pPoseStack.translate(0.0D, 1.0D, 0.0D);
         }

         this.getParentModel().translateToHead(pPoseStack);
         if (item instanceof BlockItem && ((BlockItem)item).getBlock() instanceof AbstractSkullBlock) {
            float f2 = 1.1875F;
            pPoseStack.scale(1.1875F, -1.1875F, -1.1875F);
            if (flag) {
               pPoseStack.translate(0.0D, 0.0625D, 0.0D);
            }

            GameProfile gameprofile = null;
            if (itemstack.hasTag()) {
               CompoundTag compoundtag = itemstack.getTag();
               if (compoundtag.contains("SkullOwner", 10)) {
                  gameprofile = NbtUtils.readGameProfile(compoundtag.getCompound("SkullOwner"));
               }
            }

            pPoseStack.translate(-0.5D, 0.0D, -0.5D);
            SkullBlock.Type skullblock$type = ((AbstractSkullBlock)((BlockItem)item).getBlock()).getType();
            SkullModelBase skullmodelbase = this.skullModels.get(skullblock$type);
            RenderType rendertype = SkullBlockRenderer.getRenderType(skullblock$type, gameprofile);
            SkullBlockRenderer.renderSkull((Direction)null, 180.0F, pLimbSwing, pPoseStack, pBuffer, pPackedLight, skullmodelbase, rendertype);
         } else if (!(item instanceof ArmorItem) || ((ArmorItem)item).getSlot() != EquipmentSlot.HEAD) {
            translateToHead(pPoseStack, flag);
            this.itemInHandRenderer.renderItem(pLivingEntity, itemstack, ItemTransforms.TransformType.HEAD, false, pPoseStack, pBuffer, pPackedLight);
         }

         pPoseStack.popPose();
      }
   }

   public static void translateToHead(PoseStack pPoseStack, boolean b) {
      float f = 0.625F;
      pPoseStack.translate(0.0D, -0.25D, 0.0D);
      pPoseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
      pPoseStack.scale(0.625F, -0.625F, -0.625F);
      if (b) {
         pPoseStack.translate(0.0D, 0.1875D, 0.0D);
      }

   }
}