package net.minecraft.src;

import java.util.Collection;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet18Animation;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatBase;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;


public class LMM_EntityLittleMaidAvatar extends EntityPlayer {

	public LMM_EntityLittleMaid avatar;
	/** 窶堋｢窶堙ｧ窶堙ｱ・ｽH **/
	public boolean isItemTrigger;
	/** 窶堋｢窶堙ｧ窶堙ｱ・ｽH **/
	public boolean isItemReload;
	/** 窶堋｢窶堙ｧ窶堙ｱ・ｽH **/
	private boolean isItemPreReload;
	private double appendX;
	private double appendY;
	private double appendZ;

	
	public LMM_EntityLittleMaidAvatar(World par1World, LMM_EntityLittleMaid par2EntityLittleMaid) {
		super(par1World, "");
		
		// ・ｽ窶ｰﾅﾃｺ・ｽﾃ昶凖ｨ
		avatar = par2EntityLittleMaid;
		dataWatcher = avatar.getDataWatcher();
		
		inventory = avatar.maidInventory;
		inventory.player = this;
	}

	@Override
	protected void applyEntityAttributes() {
		// ・ｽ窶ｰﾅﾃｺ・ｽﾃ昶凖ｨﾅｽE窶堋ｵ
		// ・ｽ窶ｰﾅﾃｺ・ｽﾃ昶凖ｨ窶冤窶堙哉胆ﾆ蓄・ｽ[窶堙会ｿｽﾃ昶凖ｨ窶堋ｳ窶堙ｪ窶堙ｩ・ｽB
		super.applyEntityAttributes();
//		this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.13000000417232513D);

	}

	@Override
	public float getEyeHeight() {
		return avatar.getEyeHeight();
	}

	@Override
	protected String getHurtSound() {
		return null;
	}

	@Override
	protected String getDeathSound() {
		return null;
	}

	@Override
	public boolean canCommandSenderUseCommand(int var1, String var2) {
		return false;
	}

	@Override
	public void addStat(StatBase par1StatBase, int par2) {}

	@Override
	public void addScore(int par1) {}

	@Override
	public void onUpdate() {
//		posX = avatar.posX;
		EntityPlayer lep = avatar.getMaidMasterEntity();
		entityId = avatar.entityId;
		
		if (lep != null) {
			capabilities.isCreativeMode = lep.capabilities.isCreativeMode;
		}
		
		if (xpCooldown > 0) {
			xpCooldown--;
		}
		avatar.experienceValue = experienceTotal;
		
		
	}

	@Override
	public void onItemPickup(Entity entity, int i) {
		// ﾆ但ﾆ辰ﾆ弾ﾆ停ぎ窶ｰﾃｱﾅｽﾃｻ窶堙姑竪ﾆ稚ﾆ巽ﾆ誰ﾆ暖
		if (worldObj.isRemote) {
			// Client
			LMM_Client.onItemPickup(this, entity, i);
		} else {
			super.onItemPickup(entity, i);
		}
	}

	@Override
	public void onCriticalHit(Entity par1Entity) {
		if (worldObj.isRemote) {
			// Client
			LMM_Client.onCriticalHit(this, par1Entity);
		} else {
			((WorldServer)worldObj).getEntityTracker().sendPacketToAllAssociatedPlayers(avatar, new Packet18Animation(par1Entity, 6));
		}
	}

	@Override
	public void onEnchantmentCritical(Entity par1Entity) {
		if (worldObj.isRemote) {
			LMM_Client.onEnchantmentCritical(this, par1Entity);
		} else {
			((WorldServer)worldObj).getEntityTracker().sendPacketToAllAssociatedPlayers(avatar, new Packet18Animation(par1Entity, 7));
		}
	}

	@Override
	public void attackTargetEntityWithCurrentItem(Entity par1Entity) {
		// TODO:
		float ll = 0;
		if (par1Entity instanceof EntityLivingBase) {
			ll = ((EntityLivingBase)par1Entity).getHealth();
		}
		super.attackTargetEntityWithCurrentItem(par1Entity);
		if (par1Entity instanceof EntityLivingBase) {
			((EntityLivingBase)par1Entity).setRevengeTarget(avatar);
		}
		if (par1Entity instanceof EntityCreature) {
			((EntityCreature)par1Entity).setTarget(avatar);
		}
		if (ll > 0) {
			mod_LMM_littleMaidMob.Debug(String.format("ID:%d Given Damege:%f", avatar.entityId, ll - ((EntityLivingBase)par1Entity).getHealth()));
		}
	}

	@Override
	public ItemStack getCurrentEquippedItem() {
		return avatar.getCurrentEquippedItem();
	}

	@Override
	public ItemStack getCurrentArmor(int par1) {
		return avatar.func_130225_q(par1);
	}

	@Override
	public ItemStack getCurrentItemOrArmor(int par1) {
		return avatar.getCurrentItemOrArmor(par1);
	}

	@Override
	public ItemStack[] getLastActiveItems() {
		return avatar.getLastActiveItems();
	}
/*
	@Override
	protected void alertWolves(EntityLivingBase par1EntityLiving, boolean par2) {
		// 窶堋ｱ窶堋ｱ窶堙ｰ・ｽﾃ昶凖ｨ窶堋ｵ窶堋ｿ窶堙｡窶堋､窶堙・凖奇ｿｽﾃｭ窶堙・堙坂堙岩堙ｩ窶堙帚版ｽ窶堋ｿ窶堋ｷ窶堙ｩ
	}
*/
	@Override
	public void destroyCurrentEquippedItem() {
		// ﾆ但ﾆ辰ﾆ弾ﾆ停ぎ窶堋ｪ窶ｰﾃｳ窶堙ｪ窶堋ｽ窶堙娯堙・ｽﾅｸ窶堙娯倪｢窶敕ｵ窶堙ｰ窶露窶佚ｰ
		// TODO:窶僊窶堋ｵ・ｽAForge窶懌┐窶堙・致ﾆ椎抵ｿｽ[ﾆ停橸ｿｽ[ﾆ辰ﾆ遅ﾆ停愴暖窶堙ｰ・ｽﾃ昶凖ｨ窶堋ｵ窶堙・堋｢窶堙ｩ窶堙窶堙娯堋ｾ窶堙・堙岩堙ｩ窶堙帚版ｽ窶堋ｿ窶堋ｷ窶堙ｩ窶堙娯堙・ｿｽA窶ｰﾂｽ窶堙ｧ窶堋ｩ窶堙娯佚趣ｿｽﾃｴ窶堋ｪ窶｢K窶牌・ｽB
//		super.destroyCurrentEquippedItem();
		inventory.setInventorySlotContents(inventory.currentItem, (ItemStack)null);
		avatar.getNextEquipItem();
	}

	@Override
	public void onKillEntity(EntityLivingBase entityliving) {
		avatar.onKillEntity(entityliving);
	}

	protected Entity getEntityServer() {
		return worldObj.isRemote ? null : this;
	}

	// Itemﾅｽg窶廃ﾅﾃ麺廣

	public int getItemInUseDuration(int pIndex) {
		return avatar.getSwingStatus(pIndex).getItemInUseDuration();
	}
	@Deprecated
	@Override
	public int getItemInUseDuration() {
		return getItemInUseDuration(avatar.maidDominantArm);
	}

	public ItemStack getItemInUse(int pIndex) {
		return avatar.getSwingStatus(pIndex).getItemInUse();
	}
	@Deprecated
	@Override
	public ItemStack getItemInUse() {
		return getItemInUse(avatar.maidDominantArm);
	}

	public int getItemInUseCount(int pIndex) {
		return avatar.getSwingStatus(pIndex).getItemInUseCount();
	}
	@Deprecated
	@Override
	public int getItemInUseCount() {
		return getItemInUseCount(avatar.maidDominantArm);
	}

	public boolean isUsingItem(int pIndex) {
		return avatar.getSwingStatus(pIndex).isUsingItem();
	}
	@Deprecated
	@Override
	public boolean isUsingItem() {
		return isUsingItem(avatar.maidDominantArm);
	}
	public boolean isUsingItemLittleMaid() {
		return super.isUsingItem() | isItemTrigger;
	}

	public void clearItemInUse(int pIndex) {
		isItemTrigger = false;
		isItemReload = isItemPreReload = false;
		avatar.getSwingStatus(pIndex).clearItemInUse(getEntityServer());
	}
	@Deprecated
	@Override
	public void clearItemInUse() {
//		super.clearItemInUse();
		isItemTrigger = false;
		isItemReload = isItemPreReload = false;
		clearItemInUse(avatar.maidDominantArm);
	}

	public void stopUsingItem(int pIndex) {
		isItemTrigger = false;
		isItemReload = isItemPreReload = false;
		avatar.getSwingStatus(pIndex).stopUsingItem(getEntityServer());
	}
	@Deprecated
	@Override
	public void stopUsingItem() {
//		super.stopUsingItem();
		isItemTrigger = false;
		isItemReload = isItemPreReload = false;
		stopUsingItem(avatar.maidDominantArm);
	}

	public void setItemInUse(int pIndex, ItemStack itemstack, int i) {
		isItemTrigger = true;
		isItemReload = isItemPreReload;
		avatar.getSwingStatus(pIndex).setItemInUse(itemstack, i, getEntityServer());
	}
	@Deprecated
	@Override
	public void setItemInUse(ItemStack itemstack, int i) {
//		super.setItemInUse(itemstack, i);
		isItemTrigger = true;
		isItemReload = isItemPreReload;
		setItemInUse(avatar.maidDominantArm, itemstack, i);
	}

	@Override
	public void setEating(boolean par1) {
		avatar.setEating(par1);
	}

	@Override
	public boolean isEating() {
		return avatar.isEating();
	}

	@Override
	public void setAir(int par1) {
		avatar.setAir(par1);
	}

	@Override
	public int getAir() {
		return avatar.getAir();
	}

	@Override
	public void setFire(int par1) {
		avatar.setFire(par1);
	}

	@Override
	public boolean isBurning() {
		return avatar.isBurning();
	}

	@Override
	protected void setFlag(int par1, boolean par2) {
		//XXX: experimenting
		//avatar.setFlag(par1, par2);
	}

	@Override
	public boolean isBlocking() {
		return avatar.isBlocking();
	}

	@Override
	public void playSound(String par1Str, float par2, float par3) {
		avatar.playSound(par1Str, par2, par3);
	}

	@Override
	public ChunkCoordinates getPlayerCoordinates() {
		return null;
	}

	@Override
	public void sendChatToPlayer(ChatMessageComponent var1) {
		// ﾆ蛋ﾆ槌槌鍛ﾆ暖ﾆ抵ｿｽﾆ鍛ﾆ短・ｽ[ﾆ淡窶堙最ｽg窶堙ｭ窶堙遺堋｢・ｽB
	}

	// 窶｢s窶牌・ｽH

	@Override
	protected void setHideCape(int par1, boolean par2) {}

	@Override
	protected boolean getHideCape(int par1) {
		return false;
	}

	@Override
	public void setScore(int par1) {}

	@Override
	public int getScore() {
		return 0;
	}

	public void func_110149_m(float par1) {
		avatar.func_110149_m(par1);
	}

	public float func_110139_bj() {
		return avatar.func_110139_bj();
	}

	/**
	 * 窶伉ｮ・ｽﾂｫ窶冤ﾆ椎ﾆ湛ﾆ暖窶堙ｰﾅｽﾃｦ窶慊ｾ
	 */
	public BaseAttributeMap func_110140_aT() {
//		return super.func_110140_aT();
		return avatar == null ? super.getAttributeMap() : avatar.getAttributeMap();
	}

	@Override
	public void addPotionEffect(PotionEffect par1PotionEffect) {
		avatar.addPotionEffect(par1PotionEffect);
	}

	@Override
	public PotionEffect getActivePotionEffect(Potion par1Potion) {
		return avatar.getActivePotionEffect(par1Potion);
	}

	@Override
	public Collection getActivePotionEffects() {
		return avatar.getActivePotionEffects();
	}

	@Override
	public void clearActivePotions() {
		avatar.clearActivePotions();
	}

	@Override
	protected void onChangedPotionEffect(PotionEffect par1PotionEffect, boolean par2) {
		avatar.onChangedPotionEffect(par1PotionEffect, par2);
	}

	public void getValue() {
		// EntityLittleMaid窶堋ｩ窶堙ｧ窶冤窶堙ｰﾆ坦ﾆ痴・ｽ[
		setPosition(avatar.posX, avatar.posY, avatar.posZ);
		prevPosX = avatar.prevPosX;
		prevPosY = avatar.prevPosY;
		prevPosZ = avatar.prevPosZ;
		rotationPitch = avatar.rotationPitch;
		rotationYaw = avatar.rotationYaw;
		prevRotationPitch = avatar.prevRotationPitch;
		prevRotationYaw = avatar.prevRotationYaw;
		yOffset = avatar.yOffset;
		renderYawOffset = avatar.renderYawOffset;
		prevRenderYawOffset = avatar.prevRenderYawOffset;
		rotationYawHead = avatar.rotationYawHead;
		attackTime = avatar.attackTime;
	}

	public void getValueVector(double atx, double aty, double atz, double atl) {
		// EntityLittleMaid窶堋ｩ窶堙ｧ窶冤窶堙ｰﾆ坦ﾆ痴・ｽ[
		double l = MathHelper.sqrt_double(atl);
		appendX = atx / l;
		appendY = aty / l;
		appendZ = atz / l;
		posX = avatar.posX + appendX;
		posY = avatar.posY + appendY;
		posZ = avatar.posZ + appendZ;
		prevPosX = avatar.prevPosX + appendX;
		prevPosY = avatar.prevPosY + appendY;
		prevPosZ = avatar.prevPosZ + appendZ;
		rotationPitch		= avatar.rotationPitch;
		prevRotationPitch	= avatar.prevRotationPitch;
		rotationYaw			= avatar.rotationYaw;
		prevRotationYaw		= avatar.prevRotationYaw;
		renderYawOffset		= avatar.renderYawOffset;
		prevRenderYawOffset	= avatar.prevRenderYawOffset;
		rotationYawHead		= avatar.rotationYawHead;
		prevRotationYawHead	= avatar.prevRotationYawHead;
		yOffset = avatar.yOffset;
		motionX = avatar.motionX;
		motionY = avatar.motionY;
		motionZ = avatar.motionZ;
		isSwingInProgress = avatar.getSwinging();
	}

	/**
	 * ﾅｽﾃ暁停塲ﾃ・ｿｽﾂｧ窶廃・ｽArotation窶堙ｰ窶慊ｪ窶堙会ｿｽ窶｡窶堙ｭ窶堋ｹ窶堙ｩ
	 */
	public void getValueVectorFire(double atx, double aty, double atz, double atl) {
		// EntityLittleMaid窶堋ｩ窶堙ｧ窶冤窶堙ｰﾆ坦ﾆ痴・ｽ[
		double l = MathHelper.sqrt_double(atl);
		appendX = atx / l;
		appendY = aty / l;
		appendZ = atz / l;
		posX = avatar.posX + appendX;
		posY = avatar.posY + appendY;
		posZ = avatar.posZ + appendZ;
		prevPosX = avatar.prevPosX + appendX;
		prevPosY = avatar.prevPosY + appendY;
		prevPosZ = avatar.prevPosZ + appendZ;
		rotationPitch		= updateDirection(avatar.rotationPitch);
		prevRotationPitch	= updateDirection(avatar.prevRotationPitch);
		rotationYaw			= updateDirection(avatar.rotationYawHead);
		prevRotationYaw		= updateDirection(avatar.prevRotationYawHead);
		renderYawOffset		= updateDirection(avatar.renderYawOffset);
		prevRenderYawOffset	= updateDirection(avatar.prevRenderYawOffset);
		rotationYawHead		= updateDirection(avatar.rotationYawHead);
		prevRotationYawHead	= updateDirection(avatar.prevRotationYawHead);
		yOffset = avatar.yOffset;
		motionX = avatar.motionX;
		motionY = avatar.motionY;
		motionZ = avatar.motionZ;
		isSwingInProgress = avatar.getSwinging();
	}

	protected float updateDirection(float pValue) {
		pValue %= 360F;
		if (pValue < 0) pValue += 360F;
		return pValue;
	}


	public void setValue() {
		// EntityLittleMiad窶堙問冤窶堙ｰﾆ坦ﾆ痴・ｽ[
		avatar.setPosition(posX, posY, posZ);
		avatar.prevPosX = prevPosX;
		avatar.prevPosY = prevPosY;
		avatar.prevPosZ = prevPosZ;
		avatar.rotationPitch = rotationPitch;
		avatar.rotationYaw = rotationYaw;
		avatar.prevRotationPitch = prevRotationPitch;
		avatar.prevRotationYaw = prevRotationYaw;
		avatar.yOffset = yOffset;
		avatar.renderYawOffset = renderYawOffset;
		avatar.prevRenderYawOffset = prevRenderYawOffset;
		avatar.getSwingStatusDominant().attackTime = avatar.attackTime = attackTime;
	}

	public void setValueRotation() {
		// EntityLittleMiad窶堙問冤窶堙ｰﾆ坦ﾆ痴・ｽ[
		avatar.rotationPitch = rotationPitch;
		avatar.rotationYaw = rotationYaw;
		avatar.prevRotationPitch = prevRotationPitch;
		avatar.prevRotationYaw = prevRotationYaw;
		avatar.renderYawOffset = renderYawOffset;
		avatar.prevRenderYawOffset = prevRenderYawOffset;
		avatar.motionX = motionX;
		avatar.motionY = motionY;
		avatar.motionZ = motionZ;
		if (isSwingInProgress) avatar.setSwinging(LMM_EnumSound.Null);
		
	}

	public void setValueVector() {
		// EntityLittleMiad窶堙問冤窶堙ｰﾆ坦ﾆ痴・ｽ[
		avatar.posX = posX - appendX;
		avatar.posY = posY - appendY;
		avatar.posZ = posZ - appendZ;
		avatar.prevPosX = prevPosX - appendX;
		avatar.prevPosY = prevPosY - appendY;
		avatar.prevPosZ = prevPosZ - appendZ;
		avatar.rotationPitch	 = rotationPitch;
		avatar.prevRotationPitch = prevRotationPitch;
//		avatar.rotationYaw			= rotationYaw;
//		avatar.prevRotationYaw		= prevRotationYaw;
//		avatar.renderYawOffset		= renderYawOffset;
//		avatar.prevRenderYawOffset	= prevRenderYawOffset;
		avatar.motionX = motionX;
		avatar.motionY = motionY;
		avatar.motionZ = motionZ;
		if (isSwingInProgress) avatar.setSwinging(LMM_EnumSound.Null);
	}

}
