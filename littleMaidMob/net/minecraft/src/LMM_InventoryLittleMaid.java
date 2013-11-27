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
	 * 最大インベントリ数
	 * The maximum number of inventory
	 */
	public static final int maxInventorySize = 18;
	/**
	 * オーナー
	 * Owner
	 */
	public LMM_EntityLittleMaid entityLittleMaid;
	/**
	 * スロット変更チェック用
	 * Slot change check
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
		// 一応, Once
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
		// 身に着けているアーマーの防御力の合算, Combined defense of the armor wears
		// 頭部以外, Except for the head
		ItemStack lis = armorInventory[3];
		armorInventory[3] = null;
		// int li = super.getTotalArmorValue() * 20 / 17;
		int li = super.getTotalArmorValue();
		// 兜分の補正, Correction of helmet worth
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
		// 装備アーマーに対するダメージ, The damage to the equipment Armor
		// 頭部は除外, Head exclusion
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
				// アックスの攻撃力を補正, The correction attack power of Axe
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
		// インベントリをブチマケロ！, The Buchimakero inventory!
		Explosion lexp = null;
		if (detonator) {
			// Mobによる破壊の是非, All means of destruction by Mob
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
						// 爆薬ぶちまけ, The dumped explosives
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
	 * 頭部の追加アイテムを返す。
	 * I return additional items of the head.
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
		// 指定されたアイテムIDの物を持っていれば返す, And return if you have a thing of the item ID specified
		for (int j = 0; j < mainInventory.length; j++) {
			if (mainInventory[j] != null && mainInventory[j].itemID == itemid) {
				return j;
			}
		}

		return -1;
	}

	protected int getInventorySlotContainItem(Class itemClass) {
		// 指定されたアイテムクラスの物を持っていれば返す, And return if you have a thing of the items specified class
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
		// とダメージ値, Damage value
		for (int i = 0; i < mainInventory.length; i++) {
			if (mainInventory[i] != null && mainInventory[i].itemID == itemid
					&& mainInventory[i].getItemDamage() == damege) {
				return i;
			}
		}

		return -1;
	}

	protected ItemStack getInventorySlotContainItemStack(int itemid) {
		// いらんかも？, Irankamo?
		int j = getInventorySlotContainItem(itemid);
		return j > -1 ? mainInventory[j] : null;
	}

	protected ItemStack getInventorySlotContainItemStackAndDamege(int itemid,
			int damege) {
		// いらんかも？ Irankamo?
		int j = getInventorySlotContainItemAndDamage(itemid, damege);
		return j > -1 ? mainInventory[j] : null;
	}

	public int getInventorySlotContainItemFood() {
		// インベントリの最初の食料を返す, I return the first food of the inventory
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
		// 調理可能アイテムを返す, I return the item can cook
		for (int i = 0; i < mainInventory.length; i++) {
			if (isItemSmelting(i) && i != currentItem) {
				ItemStack mi = mainInventory[i];
				if (mi.getMaxDamage() > 0 && mi.getItemDamage() == 0) {
					// 修復レシピ対策, Repair measures recipe
					continue;
				}
				// レシピ対応品, Recipes counterparts
				return i;
			}
		}
		return -1;
	}

	public int getInventorySlotContainItemPotion(boolean flag, int potionID, boolean isUndead) {
		// インベントリの最初のポーションを返す, I return a portion of the first inventory
		// flag = true: 攻撃・デバフ系, Attack-debuff system
		// false: 回復・補助系, Recovery and auxiliary system
		// potionID: 要求ポーションのID, 要求ポーションのID
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
		// 燃えるアイテムか?, The item burn?
		return index > -1 && isItemBurned(getStackInSlot(index));
	}

	public static boolean isItemBurned(ItemStack pItemstack) {
		return (pItemstack != null && 
				TileEntityFurnace.getItemBurnTime(pItemstack) > 0);
	}

	public boolean isItemSmelting(int index) {
		// 燃えるアイテムか?, The item burn?
		return isItemSmelting(getStackInSlot(index));
	}

	public static boolean isItemSmelting(ItemStack pItemstack) {
		return (pItemstack != null && MMM_Helper.getSmeltingResult(pItemstack) != null);
	}

	public boolean isItemExplord(int index) {
		// 爆発物？, Explosives?
		return (index >= 0) && isItemExplord(getStackInSlot(index));
	}

	public static boolean isItemExplord(ItemStack pItemstack) {
		if (pItemstack == null)
			return false;
		Item li = pItemstack.getItem();
		return (pItemstack != null &&
				li instanceof ItemBlock && Block.blocksList[li.itemID].blockMaterial == Material.tnt);
	}

	// インベントリの転送関連, Transport-related inventory
	public boolean isChanged(int pIndex) {
		// 変化があったかの判定, Determined whether there was a change
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
		// 処理済みのチェック, Check the processed
		ItemStack lis = getStackInSlot(pIndex);
		prevItems[pIndex] = (lis == null ? null : lis.copy());
	}

	public void clearChanged() {
		// 強制リロード用、ダミーを登録して強制的に一周させる
		// It is round, forced to register for forced reload, the dummy
		ItemStack lis = new ItemStack(Item.sugar);
		for (int li = 0; li < prevItems.length; li++) {
			prevItems[li] = lis;
		}
	}
}
