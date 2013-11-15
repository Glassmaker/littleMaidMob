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
	 * ・ｽﾃ・佚･ﾆ辰ﾆ停愴遅ﾆ停愴暖ﾆ椎・ｽ窶・
	 */
	public static final int maxInventorySize = 18;
	/**
	 * ﾆ棚・ｽ[ﾆ段・ｽ[
	 */
	public LMM_EntityLittleMaid entityLittleMaid;
	/**
	 * ﾆ湛ﾆ抵ｿｽﾆ鍛ﾆ暖窶｢ﾃ擾ｿｽXﾆ蛋ﾆ巽ﾆ鍛ﾆ誰窶廃
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
		// ﾋ・ｪ窶ｰﾅｾ
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
		// ・ｽg窶堙俄吮ｦ窶堋ｯ窶堙・堋｢窶堙ｩﾆ但・ｽ[ﾆ筑・ｽ[窶堙娯塗ﾅ津､窶氾坂堙鯉ｿｽ窶｡ﾅｽZ
		// 窶慊ｪ窶｢窶斃・闇O
		ItemStack lis = armorInventory[3];
		armorInventory[3] = null;
		// int li = super.getTotalArmorValue() * 20 / 17;
		int li = super.getTotalArmorValue();
		// ﾅ窶｢窶｢ﾂｪ窶堙娯｢ﾃ｢・ｽﾂｳ
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
		// 窶倪｢窶敕ｵﾆ但・ｽ[ﾆ筑・ｽ[窶堙俄佚寂堋ｷ窶堙ｩﾆ胆ﾆ抵ｿｽ・ｽ[ﾆ淡
		// 窶慊ｪ窶｢窶昶堙搾ｿｽﾅ毒O
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
				// ﾆ但ﾆ鍛ﾆ誰ﾆ湛窶堙鯉ｿｽUﾅ停壺氾坂堙ｰ窶｢ﾃ｢・ｽﾂｳ
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
		// ﾆ辰ﾆ停愴遅ﾆ停愴暖ﾆ椎窶堙ｰﾆ置ﾆ蛋ﾆ筑ﾆ単ﾆ抵ｿｽ・ｽI
		Explosion lexp = null;
		if (detonator) {
			// Mob窶堙俄堙ｦ窶堙ｩ窶挧窶ｰﾃｳ窶堙鯉ｿｽﾂ･窶敕ｱ
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
						// 窶敘｡窶禿ｲ窶堙披堋ｿ窶堙懌堋ｯ
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
	 * 窶慊ｪ窶｢窶昶堙娯凖・ｰﾃ・但ﾆ辰ﾆ弾ﾆ停ぎ窶堙ｰ窶｢ﾃ披堋ｷ・ｽB
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
		// ﾅｽw窶凖ｨ窶堋ｳ窶堙ｪ窶堋ｽﾆ但ﾆ辰ﾆ弾ﾆ停ぎID窶堙娯｢ﾂｨ窶堙ｰﾅｽ・ｽ窶堙≫堙・堋｢窶堙ｪ窶堙寂｢ﾃ披堋ｷ
		for (int j = 0; j < mainInventory.length; j++) {
			if (mainInventory[j] != null && mainInventory[j].itemID == itemid) {
				return j;
			}
		}

		return -1;
	}

	protected int getInventorySlotContainItem(Class itemClass) {
		// ﾅｽw窶凖ｨ窶堋ｳ窶堙ｪ窶堋ｽﾆ但ﾆ辰ﾆ弾ﾆ停ぎﾆ誰ﾆ停ｰﾆ湛窶堙娯｢ﾂｨ窶堙ｰﾅｽ・ｽ窶堙≫堙・堋｢窶堙ｪ窶堙寂｢ﾃ披堋ｷ
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
		// 窶堙・胆ﾆ抵ｿｽ・ｽ[ﾆ淡窶冤
		for (int i = 0; i < mainInventory.length; i++) {
			if (mainInventory[i] != null && mainInventory[i].itemID == itemid
					&& mainInventory[i].getItemDamage() == damege) {
				return i;
			}
		}

		return -1;
	}

	protected ItemStack getInventorySlotContainItemStack(int itemid) {
		// 窶堋｢窶堙ｧ窶堙ｱ窶堋ｩ窶堙・ｽH
		int j = getInventorySlotContainItem(itemid);
		return j > -1 ? mainInventory[j] : null;
	}

	protected ItemStack getInventorySlotContainItemStackAndDamege(int itemid,
			int damege) {
		// 窶堋｢窶堙ｧ窶堙ｱ窶堋ｩ窶堙・ｽH
		int j = getInventorySlotContainItemAndDamage(itemid, damege);
		return j > -1 ? mainInventory[j] : null;
	}

	public int getInventorySlotContainItemFood() {
		// ﾆ辰ﾆ停愴遅ﾆ停愴暖ﾆ椎窶堙鯉ｿｽﾃ・ｿｽ窶ｰ窶堙鯉ｿｽH窶板ｿ窶堙ｰ窶｢ﾃ披堋ｷ
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
		// 窶卍ｲ窶費ｿｽ窶ｰﾃや拿ﾆ但ﾆ辰ﾆ弾ﾆ停ぎ窶堙ｰ窶｢ﾃ披堋ｷ
		for (int i = 0; i < mainInventory.length; i++) {
			if (isItemSmelting(i) && i != currentItem) {
				ItemStack mi = mainInventory[i];
				if (mi.getMaxDamage() > 0 && mi.getItemDamage() == 0) {
					// ・ｽC窶｢ﾅ独椎槌歎ﾆ痴窶佚趣ｿｽﾃｴ
					continue;
				}
				// ﾆ椎槌歎ﾆ痴窶佚寂ｰﾅｾ窶｢i
				return i;
			}
		}
		return -1;
	}

	public int getInventorySlotContainItemPotion(boolean flag, int potionID, boolean isUndead) {
		// ﾆ辰ﾆ停愴遅ﾆ停愴暖ﾆ椎窶堙鯉ｿｽﾃ・ｿｽ窶ｰ窶堙姑竹・ｽ[ﾆ歎ﾆ停｡ﾆ停懌堙ｰ窶｢ﾃ披堋ｷ
		// flag = true: ・ｽUﾅ停夲ｿｽEﾆ断ﾆ弛ﾆ稚ﾅ地・ｽA false: 窶ｰﾃｱ窶｢ﾅ難ｿｽE窶｢ﾃ｢・ｽ窶｢ﾅ地
		// potionID: 窶牌窶ｹ・ｽﾆ竹・ｽ[ﾆ歎ﾆ停｡ﾆ停懌堙栗D
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
		// 窶抒窶堋ｦ窶堙ｩﾆ但ﾆ辰ﾆ弾ﾆ停ぎ窶堋ｩ?
		return index > -1 && isItemBurned(getStackInSlot(index));
	}

	public static boolean isItemBurned(ItemStack pItemstack) {
		return (pItemstack != null && 
				TileEntityFurnace.getItemBurnTime(pItemstack) > 0);
	}

	public boolean isItemSmelting(int index) {
		// 窶抒窶堋ｦ窶堙ｩﾆ但ﾆ辰ﾆ弾ﾆ停ぎ窶堋ｩ?
		return isItemSmelting(getStackInSlot(index));
	}

	public static boolean isItemSmelting(ItemStack pItemstack) {
		return (pItemstack != null && MMM_Helper.getSmeltingResult(pItemstack) != null);
	}

	public boolean isItemExplord(int index) {
		// 窶敘｡窶敖ｭ窶｢ﾂｨ・ｽH
		return (index >= 0) && isItemExplord(getStackInSlot(index));
	}

	public static boolean isItemExplord(ItemStack pItemstack) {
		if (pItemstack == null)
			return false;
		Item li = pItemstack.getItem();
		return (pItemstack != null &&
				li instanceof ItemBlock && Block.blocksList[li.itemID].blockMaterial == Material.tnt);
	}

	// ﾆ辰ﾆ停愴遅ﾆ停愴暖ﾆ椎窶堙娯彎窶倪版ﾃ麺廣
	public boolean isChanged(int pIndex) {
		// 窶｢ﾃ鞘ｰﾂｻ窶堋ｪ窶堋窶堙≫堋ｽ窶堋ｩ窶堙娯敖ｻ窶凖ｨ
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
		// ・ｽﾋ・費ｿｽ・ｽﾃ鞘堙昶堙姑蛋ﾆ巽ﾆ鍛ﾆ誰
		ItemStack lis = getStackInSlot(pIndex);
		prevItems[pIndex] = (lis == null ? null : lis.copy());
	}

	public void clearChanged() {
		// 窶ｹﾂｭ・ｽﾂｧﾆ椎ﾆ抵ｿｽ・ｽ[ﾆ檀窶廃・ｽAﾆ胆ﾆ蓄・ｽ[窶堙ｰ窶徙ﾋ弯窶堋ｵ窶堙・ｹﾂｭ・ｽﾂｧ窶廬窶堙架・ｪﾅｽﾃｼ窶堋ｳ窶堋ｹ窶堙ｩ
		ItemStack lis = new ItemStack(Item.sugar);
		for (int li = 0; li < prevItems.length; li++) {
			prevItems[li] = lis;
		}
	}
}
