package net.minecraft.src;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;

public class LMM_InventoryLittleMaid extends InventoryPlayer {

	/**
	 * �Å‘åƒCƒ“ƒxƒ“ƒgƒŠ�”
	 */
	public static final int maxInventorySize = 18;
	/**
	 * ƒI�[ƒi�[
	 */
	public LMM_EntityLittleMaid entityLittleMaid;
	/**
	 * ƒXƒ�ƒbƒg•Ï�Xƒ`ƒFƒbƒN—p
	 */
	public ItemStack prevItems[];
	
	protected Random rand = new Random();

	public LMM_InventoryLittleMaid(LMM_EntityLittleMaid par1EntityLittleMaid) {
		super(par1EntityLittleMaid.maidAvatar);

		entityLittleMaid = par1EntityLittleMaid;
		mainInventory = new ItemStack[maxInventorySize];
		prevItems = new ItemStack[mainInventory.length + armorInventory.length];
	}

	@Override
	public void readFromNBT(NBTTagList par1nbtTagList) {
		mainInventory = new ItemStack[maxInventorySize];
		armorInventory = new ItemStack[4];

		for (int i = 0; i < par1nbtTagList.tagCount(); i++) {
			NBTTagCompound nbttagcompound = (NBTTagCompound) par1nbtTagList
					.tagAt(i);
			int j = nbttagcompound.getByte("Slot") & 0xff;
			ItemStack itemstack = ItemStack
					.loadItemStackFromNBT(nbttagcompound);

			if (itemstack == null) {
				continue;
			}

			if (j >= 0 && j < mainInventory.length) {
				mainInventory[j] = itemstack;
			}

			if (j >= 100 && j < armorInventory.length + 100) {
				armorInventory[j - 100] = itemstack;
			}
		}
	}

	@Override
	public String getInvName() {
		return "InsideSkirt";
	}

	@Override
	public int getSizeInventory() {
		// ˆê‰ž
		return mainInventory.length + armorInventory.length;
	}

	@Override
	public void openChest() {
		entityLittleMaid.onGuiOpened();
	}

	@Override
	public void closeChest() {
		entityLittleMaid.onGuiClosed();
	}

	@Override
	public void decrementAnimations() {
		for (int li = 0; li < this.mainInventory.length; ++li) {
			if (this.mainInventory[li] != null) {
				this.mainInventory[li].updateAnimation(this.player.worldObj,
						entityLittleMaid, li, this.currentItem == li);
			}
		}
	}

	@Override
	public int getTotalArmorValue() {
		// �g‚É’…‚¯‚Ä‚¢‚éƒA�[ƒ}�[‚Ì–hŒä—Í‚Ì�‡ŽZ
		// “ª•”ˆÈŠO
		ItemStack lis = armorInventory[3];
		armorInventory[3] = null;
		// int li = super.getTotalArmorValue() * 20 / 17;
		int li = super.getTotalArmorValue();
		// Š••ª‚Ì•â�³
		for (int lj = 0; lj < armorInventory.length; lj++) {
			if (armorInventory[lj] != null
					&& armorInventory[lj].getItem() instanceof ItemArmor) {
				li++;
			}
		}
		armorInventory[3] = lis;
		return li;
	}

	@Override
	public void damageArmor(float pDamage) {
		// ‘•”õƒA�[ƒ}�[‚É‘Î‚·‚éƒ_ƒ��[ƒW
		// “ª•”‚Í�œŠO
		ItemStack lis = armorInventory[3];
		armorInventory[3] = null;
		super.damageArmor(pDamage);
		armorInventory[3] = lis;
	}
/*
	@Override
	public int getDamageVsEntity(Entity entity) {
		return getDamageVsEntity(entity, currentItem);
	}

	public int getDamageVsEntity(Entity entity, int index) {
		if (index < 0 || index >= getSizeInventory()) return 1;
		ItemStack itemstack = getStackInSlot(index);
		if (itemstack != null) {
			if (itemstack.getItem() instanceof ItemAxe) {
				// ƒAƒbƒNƒX‚Ì�UŒ‚—Í‚ð•â�³
				return itemstack.getDamageVsEntity(entity) * 3 / 2 + 1;

			} else {
				return itemstack.getDamageVsEntity(entity);
			}
		} else {
			return 1;
		}
	}
*/
	public void dropAllItems(boolean detonator) {
		// ƒCƒ“ƒxƒ“ƒgƒŠ‚ðƒuƒ`ƒ}ƒPƒ��I
		Explosion lexp = null;
		if (detonator) {
			// Mob‚É‚æ‚é”j‰ó‚Ì�¥”ñ
			lexp = new Explosion(entityLittleMaid.worldObj, entityLittleMaid,
					entityLittleMaid.posX, entityLittleMaid.posY, entityLittleMaid.posZ, 3F);
			lexp.isFlaming = false;
			lexp.isSmoking = entityLittleMaid.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
		}
		
		armorInventory[3] = null;
		for (int i = 0; i < getSizeInventory(); i++) {
			ItemStack it = getStackInSlot(i);
			if (it != null) {
				if (detonator && isItemExplord(i)) {
					int j = it.getItem().itemID;
					for (int l = 0; l < it.stackSize; l++) {
						// ”š–ò‚Ô‚¿‚Ü‚¯
						((BlockTNT) Block.blocksList[j]).onBlockDestroyedByExplosion(
								entityLittleMaid.worldObj,
								MathHelper.floor_double(entityLittleMaid.posX)
								+ rand.nextInt(7) - 3,
								MathHelper.floor_double(entityLittleMaid.posY)
								+ rand.nextInt(7) - 3,
								MathHelper.floor_double(entityLittleMaid.posZ)
								+ rand.nextInt(7) - 3, lexp);
					}
				} else {
					entityLittleMaid.entityDropItem(it, 0F);
				}
			}
			setInventorySlotContents(i, null);
		}
		if (detonator) {
			lexp.doExplosionA();
			lexp.doExplosionB(true);
		}
	}

	@Override
	public void dropAllItems() {
		dropAllItems(false);
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (entityLittleMaid.isDead) {
			return false;
		}
		return entityplayer.getDistanceSqToEntity(entityLittleMaid) <= 64D;
	}

	@Override
	public ItemStack getCurrentItem() {
		if (currentItem >= 0 && currentItem < mainInventory.length) {
			return mainInventory[currentItem];
		} else {
			return null;
		}
	}

	@Override
	public boolean addItemStackToInventory(ItemStack par1ItemStack) {
		onInventoryChanged();
		return super.addItemStackToInventory(par1ItemStack);
	}

	/**
	 * “ª•”‚Ì’Ç‰ÁƒAƒCƒeƒ€‚ð•Ô‚·�B
	 */
	public ItemStack getHeadMount() {
		return mainInventory[mainInventory.length - 1];
	}

	public void setInventoryCurrentSlotContents(ItemStack itemstack) {
		if (currentItem > -1) {
			setInventorySlotContents(currentItem, itemstack);
		}
	}

	protected int getInventorySlotContainItem(int itemid) {
		// Žw’è‚³‚ê‚½ƒAƒCƒeƒ€ID‚Ì•¨‚ðŽ�‚Á‚Ä‚¢‚ê‚Î•Ô‚·
		for (int j = 0; j < mainInventory.length; j++) {
			if (mainInventory[j] != null && mainInventory[j].itemID == itemid) {
				return j;
			}
		}

		return -1;
	}

	protected int getInventorySlotContainItem(Class itemClass) {
		// Žw’è‚³‚ê‚½ƒAƒCƒeƒ€ƒNƒ‰ƒX‚Ì•¨‚ðŽ�‚Á‚Ä‚¢‚ê‚Î•Ô‚·
		for (int j = 0; j < mainInventory.length; j++) {
			// if (mainInventory[j] != null &&
			// mainInventory[j].getItem().getClass().isAssignableFrom(itemClass))
			// {
			if (mainInventory[j] != null
					&& itemClass.isAssignableFrom(mainInventory[j].getItem().getClass())) {
				return j;
			}
		}

		return -1;
	}

	protected int getInventorySlotContainItemAndDamage(int itemid, int damege) {
		// ‚Æƒ_ƒ��[ƒW’l
		for (int i = 0; i < mainInventory.length; i++) {
			if (mainInventory[i] != null && mainInventory[i].itemID == itemid
					&& mainInventory[i].getItemDamage() == damege) {
				return i;
			}
		}

		return -1;
	}

	protected ItemStack getInventorySlotContainItemStack(int itemid) {
		// ‚¢‚ç‚ñ‚©‚à�H
		int j = getInventorySlotContainItem(itemid);
		return j > -1 ? mainInventory[j] : null;
	}

	protected ItemStack getInventorySlotContainItemStackAndDamege(int itemid,
			int damege) {
		// ‚¢‚ç‚ñ‚©‚à�H
		int j = getInventorySlotContainItemAndDamage(itemid, damege);
		return j > -1 ? mainInventory[j] : null;
	}

	public int getInventorySlotContainItemFood() {
		// ƒCƒ“ƒxƒ“ƒgƒŠ‚Ì�Å�‰‚Ì�H—¿‚ð•Ô‚·
		for (int j = 0; j < mainInventory.length; j++) {
			ItemStack mi = mainInventory[j];
			if (mi != null && mi.getItem() instanceof ItemFood) {
				if (((ItemFood) mi.getItem()).getHealAmount() > 0) {
					return j;
				}
			}
		}
		return -1;
	}

	public int getSmeltingItem() {
		// ’²—�‰Â”\ƒAƒCƒeƒ€‚ð•Ô‚·
		for (int i = 0; i < mainInventory.length; i++) {
			if (isItemSmelting(i) && i != currentItem) {
				ItemStack mi = mainInventory[i];
				if (mi.getMaxDamage() > 0 && mi.getItemDamage() == 0) {
					// �C•œƒŒƒVƒs‘Î�ô
					continue;
				}
				// ƒŒƒVƒs‘Î‰ž•i
				return i;
			}
		}
		return -1;
	}

	public int getInventorySlotContainItemPotion(boolean flag, int potionID, boolean isUndead) {
		// ƒCƒ“ƒxƒ“ƒgƒŠ‚Ì�Å�‰‚Ìƒ|�[ƒVƒ‡ƒ“‚ð•Ô‚·
		// flag = true: �UŒ‚�EƒfƒoƒtŒn�A false: ‰ñ•œ�E•â�•Œn
		// potionID: —v‹�ƒ|�[ƒVƒ‡ƒ“‚ÌID
		for (int j = 0; j < mainInventory.length; j++) {
			if (mainInventory[j] != null
					&& mainInventory[j].getItem() instanceof ItemPotion) {
				ItemStack is = mainInventory[j];
				List list = ((ItemPotion) is.getItem()).getEffects(is);
				nextPotion: if (list != null) {
					PotionEffect potioneffect;
					for (Iterator iterator = list.iterator(); iterator
							.hasNext();) {
						potioneffect = (PotionEffect) iterator.next();
						if (potioneffect.getPotionID() == potionID) break;
						if (potioneffect.getPotionID() == Potion.heal.id) {
							if ((!flag && isUndead) || (flag && !isUndead)) {
								break nextPotion;
							}
						} else if (potioneffect.getPotionID() == Potion.harm.id) {
							if ((flag && isUndead) || (!flag && !isUndead)) {
								break nextPotion;
							}
						} else if (Potion.potionTypes[potioneffect.getPotionID()].isBadEffect() != flag) {
							break nextPotion;
						}
					}
					return j;
				}
			}
		}
		return -1;
	}

	public int getFirstEmptyStack() {
		for (int i = 0; i < mainInventory.length; i++) {
			if (mainInventory[i] == null) {
				return i;
			}
		}

		return -1;
	}

	public boolean isItemBurned(int index) {
		// ”R‚¦‚éƒAƒCƒeƒ€‚©?
		return index > -1 && isItemBurned(getStackInSlot(index));
	}

	public static boolean isItemBurned(ItemStack pItemstack) {
		return (pItemstack != null && 
				TileEntityFurnace.getItemBurnTime(pItemstack) > 0);
	}

	public boolean isItemSmelting(int index) {
		// ”R‚¦‚éƒAƒCƒeƒ€‚©?
		return isItemSmelting(getStackInSlot(index));
	}

	public static boolean isItemSmelting(ItemStack pItemstack) {
		return (pItemstack != null && MMM_Helper.getSmeltingResult(pItemstack) != null);
	}

	public boolean isItemExplord(int index) {
		// ”š”­•¨�H
		return (index >= 0) && isItemExplord(getStackInSlot(index));
	}

	public static boolean isItemExplord(ItemStack pItemstack) {
		if (pItemstack == null)
			return false;
		Item li = pItemstack.getItem();
		return (pItemstack != null &&
				li instanceof ItemBlock && Block.blocksList[li.itemID].blockMaterial == Material.tnt);
	}

	// ƒCƒ“ƒxƒ“ƒgƒŠ‚Ì“]‘—ŠÖ˜A
	public boolean isChanged(int pIndex) {
		// •Ï‰»‚ª‚ ‚Á‚½‚©‚Ì”»’è
		ItemStack lis = getStackInSlot(pIndex);
		return !ItemStack.areItemStacksEqual(lis, prevItems[pIndex]);
		// return (lis == null || prevItems[pIndex] == null) ?
		// (prevItems[pIndex] != lis) : !ItemStack.areItemStacksEqual(lis,
		// prevItems[pIndex]);
		// return prevItems[pIndex] != getStackInSlot(pIndex);
	}

	public void setChanged(int pIndex) {
		prevItems[pIndex] = new ItemStack(Item.sugar);
	}

	public void resetChanged(int pIndex) {
		// �ˆ—��Ï‚Ý‚Ìƒ`ƒFƒbƒN
		ItemStack lis = getStackInSlot(pIndex);
		prevItems[pIndex] = (lis == null ? null : lis.copy());
	}

	public void clearChanged() {
		// ‹­�§ƒŠƒ��[ƒh—p�Aƒ_ƒ~�[‚ð“o˜^‚µ‚Ä‹­�§“I‚ÉˆêŽü‚³‚¹‚é
		ItemStack lis = new ItemStack(Item.sugar);
		for (int li = 0; li < prevItems.length; li++) {
			prevItems[li] = lis;
		}
	}
}
