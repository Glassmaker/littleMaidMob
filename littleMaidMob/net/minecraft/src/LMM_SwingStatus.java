package net.minecraft.src;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;

public class LMM_SwingStatus {

	public int index;
	public int lastIndex;
	public boolean isSwingInProgress;
	public float swingProgress;
	public float prevSwingProgress;
	public int swingProgressInt;
	public float onGround;
	public int attackTime;
//	public int usingCount;
	public int itemInUseCount;
	protected ItemStack itemInUse;
	protected Random rand = new Random();



	public LMM_SwingStatus() {
		index = lastIndex = -1;
		isSwingInProgress = false;
		swingProgress = prevSwingProgress = 0.0F;
		onGround = 0F;
		attackTime = 0;
		itemInUseCount = 0;
		itemInUse = null;
	}

	/**
	 * TODO:�”’l‚Ì�X�V—p�AonEntityUpdate“à‚ÅŒÄ‚ÔŽ–:‚¢‚ç‚ñ‚©�H
	 */
	public void onEntityUpdate(LMM_EntityLittleMaid pEntity) {
		prevSwingProgress = swingProgress;
	}

	/**
	 * �”’l‚Ì�X�V—p�AonUpdate“à‚ÅŒÄ‚ÔŽ–
	 */
	public void onUpdate(LMM_EntityLittleMaid pEntity) {
		prevSwingProgress = swingProgress;
		if (attackTime > 0) {
			attackTime--;
		}
		
		// ˜r�U‚è
		int li = pEntity.getSwingSpeedModifier();
		if (isSwingInProgress) {
			swingProgressInt++;
			if(swingProgressInt >= li) {
				swingProgressInt = 0;
				isSwingInProgress = false;
			}
		} else {
			swingProgressInt = 0;
		}
		swingProgress = (float)swingProgressInt / (float)li;
		
		if (isUsingItem()) {
			ItemStack itemstack = pEntity.maidInventory.getStackInSlot(index);
			Entity lrentity = pEntity.worldObj.isRemote ? null : pEntity;
			
			if (itemstack != itemInUse) {
				clearItemInUse(lrentity);
			} else {
				if (itemInUseCount <= 25 && itemInUseCount % 4 == 0) {
					// �H‚×‚©‚·‚Æ‚©
					updateItemUse(pEntity, 5);
				}
				if (--itemInUseCount <= 0 && lrentity != null) {
					onItemUseFinish(pEntity.maidAvatar);
				}
			}
		}
	}

	/**
	 * ‘I‘ð’†‚ÌƒXƒ�ƒbƒg”Ô�†‚ð�Ý’è
	 */
	public void setSlotIndex(int pIndex) {
		index = pIndex;
		lastIndex = -2;
	}

	/**
	 * ‘I‘ð’†‚ÌƒCƒ“ƒxƒ“ƒgƒŠ“àƒAƒCƒeƒ€ƒXƒ^ƒbƒN‚ð•Ô‚·
	 */
	public ItemStack getItemStack(LMM_EntityLittleMaid pEntity) {
		if (index > -1) {
			return pEntity.maidInventory.getStackInSlot(index);
		} else {
			return null;
		}
	}

	public boolean canAttack() {
		return attackTime <= 0;
	}



// ˜r�U‚èŠÖŒW


	public float getSwingProgress(float ltime) {
		float lf = swingProgress - prevSwingProgress;
		
		if (lf < 0.0F) {
			++lf;
		}
		
		return onGround = prevSwingProgress + lf * ltime;
	}

	public boolean setSwinging() {
		if (!isSwingInProgress || swingProgressInt < 0) {
			swingProgressInt = -1;
			isSwingInProgress = true;
			return true;
		}
		return false;
	}


	/**
	 * •Ï�X‚ª‚ ‚é‚©‚Ç‚¤‚©‚ð•Ô‚µ�Aƒtƒ‰ƒO‚ðƒNƒŠƒA‚·‚é�B
	 */
	public boolean checkChanged() {
		boolean lflag = index != lastIndex;
		lastIndex = index;
		return lflag;
	}

// ƒAƒCƒeƒ€‚ÌŽg—p‚ÉŠÖ‚í‚éŠÖ�”ŒQ

	public ItemStack getItemInUse() {
		return itemInUse;
	}

	public int getItemInUseCount() {
		return itemInUseCount;
	}

	public boolean isUsingItem() {
		return itemInUse != null;
	}

	public int getItemInUseDuration() {
		return isUsingItem() ? itemInUse.getMaxItemUseDuration() - itemInUseCount : 0;
	}

	/**
	 * 
	 * @param pEntity
	 * ƒT�[ƒo�[‚ÌŽž‚ÍEntity‚ð�Ý’è‚·‚é�B
	 */
	public void stopUsingItem(Entity pEntity) {
		if (itemInUse != null && pEntity instanceof EntityPlayer) {
			itemInUse.onPlayerStoppedUsing(pEntity.worldObj, (EntityPlayer)pEntity, itemInUseCount);
		}
		
		clearItemInUse(pEntity);
	}

	/**
	 * 
	 * @param pEntity
	 * ƒT�[ƒo�[‚ÌŽž‚ÍEntity‚ð�Ý’è‚·‚é�B
	 */
	public void clearItemInUse(Entity pEntity) {
		itemInUse = null;
		itemInUseCount = 0;
		
		if (pEntity != null) {
			pEntity.setEating(false);
		}
	}

	public boolean isBlocking() {
		return isUsingItem() && Item.itemsList[this.itemInUse.itemID].getItemUseAction(itemInUse) == EnumAction.block;
	}

	/**
	 * 
	 * @param par1ItemStack
	 * @param par2
	 * @param pEntity
	 * ƒT�[ƒo�[‚ÌŽž‚ÍEntity‚ð�Ý’è‚·‚é�B
	 */
	public void setItemInUse(ItemStack par1ItemStack, int par2, Entity pEntity) {
		if (par1ItemStack != itemInUse) {
			itemInUse = par1ItemStack;
			itemInUseCount = par2;
			
			if (pEntity != null) {
				pEntity.setEating(true);
			}
		}
	}

	protected void updateItemUse(Entity pEntity, int par2) {
		if (itemInUse.getItemUseAction() == EnumAction.drink) {
			pEntity.playSound("random.drink", 0.5F, pEntity.worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}
		
		if (itemInUse.getItemUseAction() == EnumAction.eat) {
			for (int var3 = 0; var3 < par2; ++var3) {
				Vec3 var4 = pEntity.worldObj.getWorldVec3Pool().getVecFromPool(((double)rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
				var4.rotateAroundX(-pEntity.rotationPitch * (float)Math.PI / 180.0F);
				var4.rotateAroundY(-pEntity.rotationYaw * (float)Math.PI / 180.0F);
				Vec3 var5 = pEntity.worldObj.getWorldVec3Pool().getVecFromPool(((double)rand.nextFloat() - 0.5D) * 0.3D, (double)(-rand.nextFloat()) * 0.6D - 0.3D, 0.6D);
				var5.rotateAroundX(-pEntity.rotationPitch * (float)Math.PI / 180.0F);
				var5.rotateAroundY(-pEntity.rotationYaw * (float)Math.PI / 180.0F);
				var5 = var5.addVector(pEntity.posX, pEntity.posY + (double)pEntity.getEyeHeight(), pEntity.posZ);
				pEntity.worldObj.spawnParticle("iconcrack_" + itemInUse.getItem().itemID, var5.xCoord, var5.yCoord, var5.zCoord, var4.xCoord, var4.yCoord + 0.05D, var4.zCoord);
			}
			
			pEntity.playSound("random.eat", 0.5F + 0.5F * (float)rand.nextInt(2), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
		}
	}

	protected void onItemUseFinish(EntityPlayer pEntityPlayer) {
		if (this.itemInUse != null) {
			this.updateItemUse(pEntityPlayer, 16);
			int var1 = this.itemInUse.stackSize;
			ItemStack var2 = itemInUse.onFoodEaten(pEntityPlayer.worldObj, pEntityPlayer);
			
			if (var2 != this.itemInUse || var2 != null && var2.stackSize != var1) {
				if (var2.stackSize == 0) {
					pEntityPlayer.inventory.setInventorySlotContents(index, null);
				} else {
					pEntityPlayer.inventory.setInventorySlotContents(index, var2);
				}
			}
			
			clearItemInUse(pEntityPlayer);
		}
	}

}
