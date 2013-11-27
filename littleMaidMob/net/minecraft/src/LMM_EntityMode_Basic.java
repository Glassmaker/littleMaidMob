package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;

public class LMM_EntityMode_Basic extends LMM_EntityModeBlockBase {

	public static final int mmode_Wild		= 0x0000;
	public static final int mmode_Escorter	= 0x0001;
	
	private IInventory myInventory;
	private IInventory myChest;
	private List<IInventory> fusedTiles;
	private boolean isWorking;
	private double lastdistance;
	private int maidSearchCount;

	
	/**
	 * Wild, Escorter 
	 */
	public LMM_EntityMode_Basic(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
		fusedTiles = new ArrayList<IInventory>();
//		myTile = null;
	}

	@Override
	public int priority() {
		// TODO Auto-generated method stub
		return 9000;
	}
	
	@Override
	public void init() {
		ModLoader.addLocalization("littleMaidMob.mode.Strike", "Strike");
		ModLoader.addLocalization("littleMaidMob.mode.Wait", "Wait");
		ModLoader.addLocalization("littleMaidMob.mode.Wild", "Wild");
		ModLoader.addLocalization("littleMaidMob.mode.Wild", "ja_JP", "野生種");
		ModLoader.addLocalization("littleMaidMob.mode.Escorter", "Escorter");
		ModLoader.addLocalization("littleMaidMob.mode.Escorter", "ja_JP", "従者");
		ModLoader.addLocalization("littleMaidMob.mode.F-Escorter", "Freedom");
		ModLoader.addLocalization("littleMaidMob.mode.D-Escorter", "D-Escorter");
		ModLoader.addLocalization("littleMaidMob.mode.T-Escorter", "Tracer");
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		// Wild
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = new EntityAITasks(null);
		ltasks[1] = new EntityAITasks(null);

		ltasks[0].addTask(1, owner.aiSwiming);
		ltasks[0].addTask(2, owner.aiAttack);
		ltasks[0].addTask(3, owner.aiPanic);
		ltasks[0].addTask(4, owner.aiBegMove);
		ltasks[0].addTask(4, owner.aiBeg);
		ltasks[0].addTask(5, owner.aiRestrictRain);
		ltasks[0].addTask(6, owner.aiFreeRain);
//        ltasks[0].addTask(4, new EntityAIMoveIndoors(this));
//		ltasks[0].addTask(7, owner.aiCloseDoor);
//		ltasks[0].addTask(8, owner.aiOpenDoor);
		ltasks[0].addTask(9, owner.aiCollectItem);
		ltasks[0].addTask(10, new EntityAILeapAtTarget(owner, 0.3F));
		ltasks[0].addTask(11, owner.aiWander);
		ltasks[0].addTask(12, new EntityAIWatchClosest2(owner, EntityLivingBase.class, 10F, 0.02F));
		ltasks[0].addTask(13, new EntityAIWatchClosest2(owner, net.minecraft.src.LMM_EntityLittleMaid.class, 10F, 0.02F));
		ltasks[0].addTask(13, new EntityAIWatchClosest2(owner, EntityPlayer.class, 10F, 0.02F));
		ltasks[0].addTask(13, new EntityAILookIdle(owner));

		ltasks[1].addTask(1, new LMM_EntityAIHurtByTarget(owner, false));

		owner.addMaidMode(ltasks, "Wild", mmode_Wild);

		// Escorter:0x0001
		ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = pDefaultTargeting;
		owner.addMaidMode(ltasks, "Escorter", mmode_Escorter);
		
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		// 強制的に割り当てる, I forcibly allocating
		owner.setMaidMode("Escorter");
		return true;
	}
	
	@Override
	public boolean setMode(int pMode) {
		switch (pMode) {
		case mmode_Wild :
			owner.setFreedom(true);
//			owner.aiWander.setEnable(true);
			return true;
		case mmode_Escorter :
			owner.aiAvoidPlayer.setEnable(false);
			for (int li = 0; li < owner.mstatSwingStatus.length; li++) {
				owner.setEquipItem(li, -1);
			}
			return true;
		}
//		owner.getNavigator().clearPathEntity()
		return false;
	}
	
	@Override
	public int getNextEquipItem(int pMode) {
		return pMode == mmode_Wild ? 0 : -1;
	}
	
	@Override
	public boolean checkItemStack(ItemStack pItemStack) {
		return true;
	}

	@Override
	public boolean isSearchBlock() {
		if (owner.getMaidModeInt() == mmode_Escorter && owner.isFreedom() &&
				owner.maidInventory.getFirstEmptyStack() == -1) {
			// 対象をまだ見つけていないときは検索を行う。
			// To conduct a search when it is not yet found the target.
			fDistance = 100F;
			return myInventory == null;
		}
//		clearMy();
//		fusedTiles.clear();
		return false;
	}

	@Override
	public boolean shouldBlock(int pMode) {
		return myInventory instanceof TileEntity;
	}

	@Override
	public boolean checkBlock(int pMode, int px, int py, int pz) {
		TileEntity ltile = owner.worldObj.getBlockTileEntity(px, py, pz);
		if (!(ltile instanceof IInventory)) {
			return false;
		}
		if (((IInventory)ltile).getSizeInventory() < 18) {
			// インベントリのサイズが１８以下なら対象としない。
			// It does not cover the size of your inventory if 18 or less.
			return false;
		}
		
		// 世界のメイドから, Made from the world's
		if (checkWorldMaid(ltile)) return false;
		// 使用済みチェック, Use MySpace
		if (fusedTiles.contains(ltile)) {
			// 既に通り過ぎた場所よッ！, Tsu'll place you have already passed!
			return false;
		}
		
		double ldis = owner.getDistanceTilePosSq(ltile);
		if (fDistance > ldis) {
			myInventory = (IInventory)ltile;
			fDistance = ldis;
		}
		return false;
	}

	@Override
	public boolean overlooksBlock(int pMode) {
		// チェストカートの検索, Search the chest Cart
		List<Entity> list = owner.worldObj.getEntitiesWithinAABB(IInventory.class, owner.boundingBox.expand(8D, 2D, 8D));
		double cartl = 256D;
		for (Entity lentity : list) {
			if (!fusedTiles.contains(lentity)) {
				if (((IInventory)lentity).getSizeInventory() < 18) {
					// インベントリが一定サイズ以下はスキップ, Inventory is below a certain size Skip
					continue;
				}
				double lr = lentity.getDistanceSqToEntity(owner);
				// 見える位置にある最も近い調べていないカートチェスト
				// Cart chest you have not studied the closest in a position to be able to see
				
				if (fDistance > lr && owner.getEntitySenses().canSee(lentity)) {
					myInventory = (IInventory)lentity;
					fDistance = lr;
				}
			}
		}
		lastdistance = -1;
		myChest = null;
		maidSearchCount = 0;
		if (myInventory instanceof TileEntity) {
			owner.setTilePos((TileEntity)myInventory);
			return myInventory != null;
		} else {
			owner.setTarget((Entity)myInventory);
			return false;
		}
//		return myInventory != null;
	}

	@Override
	public void startBlock(int pMode) {
	}

	@Override
	public void resetBlock(int pMode) {
		clearMy();
//		fusedTiles.clear();
	}

	protected void clearMy() {
		myInventory = null;
		if (myChest != null) {
			myChest.closeChest();
			myChest = null;
		}
		owner.clearTilePos();
		owner.setTarget(null);
	}

	@Override
	public boolean executeBlock(int pMode, int px, int py, int pz) {
//		isMaidChaseWait = true;
		if (myInventory instanceof TileEntityChest) {
			// ブロック系のチェスト, Chest of block-based
			TileEntityChest lchest = (TileEntityChest)myInventory;
			if (!lchest.isInvalid()) {
				// 使用直前に可視判定, Visualization decision just prior to use
				if (MMM_Helper.canBlockBeSeen(owner, lchest.xCoord, lchest.yCoord, lchest.zCoord, false, true, false)) {
					if (myChest == null) {
						getChest();
						if (myChest != null) {
							myChest.openChest();
						} else {
							// 開かないチェスト, Chest does not open
							myInventory = null;
						}
					}
					// チェストに収納, I put away the chest
					owner.setWorking(true);
					putChest();
					return true;
				} else {
					// 見失った, Lost sight
					clearMy();
				}
			} else {
				// Tileの消失, Disappearance of the Tile
				clearMy();
			}
		} else {
			// 想定外のインベントリ, Inventory of unexpected
			if (myInventory != null) {
				fusedTiles.add(myInventory);
			}
			clearMy();
		}
		return false;
	}

	@Override
	public boolean outrangeBlock(int pMode, int pX, int pY, int pZ) {
		// チェストまでのパスを作る, I make the path to the chest
		boolean lf = false;
		if (!owner.isMaidWaitEx()) {
			double distance;
			if (myInventory instanceof TileEntity) {
				distance = owner.getDistanceTilePos();
				if (distance == lastdistance) {
					// TODO:現状無意味, Current situation meaningless
					// 移動が固まらないように乱数加速
					// Random number accelerated movement so as not to harden
					mod_LMM_littleMaidMob.Debug("Assert.");
					//XXX: experimental
					//owner.updateWanderPath();
					owner.onLivingUpdate();
					lf = true;
				} else {
					lf = MMM_Helper.setPathToTile(owner, (TileEntity)myInventory, false);
				}
			} else {
				distance = 0;
			}
			lastdistance = distance;
			// レンジ外のチェストは閉じる, Chest out of range Close
			if (myChest != null) {
				myChest.closeChest();
				myChest = null;
			}
		}
		return lf;
	}

	@Override
	public void farrangeBlock() {
		super.farrangeBlock();
		clearMy();
	}


	protected boolean getChest() {
		// チェストを獲得, Earn chest
		if (myInventory == null) {
			return false;
		}
		// 検索済みにスタック, Stack a tracked
		fusedTiles.add(myInventory);
		if (myInventory instanceof TileEntityChest) {
			TileEntityChest lchest = (TileEntityChest)myInventory;
			if (!lchest.adjacentChestChecked) {
				lchest.checkForAdjacentChests();
			}
			fusedTiles.add(lchest.adjacentChestXNeg);
			fusedTiles.add(lchest.adjacentChestXPos);
			fusedTiles.add(lchest.adjacentChestZNeg);
			fusedTiles.add(lchest.adjacentChestZPosition);
		}
		
		TileEntity ltile = (TileEntity)myInventory;
		Block lblock = Block.blocksList[owner.worldObj.getBlockId(ltile.xCoord, ltile.yCoord, ltile.zCoord)];
		myChest = myInventory;
		if (lblock instanceof BlockChest) {
			myChest = ((BlockChest)lblock).getInventory(owner.worldObj, ltile.xCoord, ltile.yCoord, ltile.zCoord);
		}
		
		return myChest != null;
	}

	protected void putChest() {
		// チェストに近接, The proximity to the chest
		if (owner.getSwingStatusDominant().canAttack() && myChest != null) {
			// 砂糖、時計、被っているヘルム以外のアイテムを突っ込む
			// I shove the helm of items other than sugar, watch, suffer
			ItemStack is;
			mod_LMM_littleMaidMob.Debug(String.format("getChest:%d", maidSearchCount));
			while ((is = owner.maidInventory.getStackInSlot(maidSearchCount)) == null && maidSearchCount < owner.maidInventory.mainInventory.length) {
				maidSearchCount++;
			}
			if (is != null && !(
					   is.getItem().itemID == Item.sugar.itemID
					|| is.getItem().itemID == Item.pocketSundial.itemID
					|| (is == owner.maidInventory.armorItemInSlot(3))
//					|| (is.getItem() instanceof ItemArmor && ((ItemArmor)is.getItem()).armorType == 0)
				))
			{
//				mod_littleMaidMob.Debug("getchest2.");
				boolean f = false;
				for (int j = 0; j < myChest.getSizeInventory() && is.stackSize > 0; j++)
				{
					ItemStack isc = myChest.getStackInSlot(j);
					if (isc == null)
					{
//						mod_littleMaidMob.Debug(String.format("%s -> NULL", is.getItemName()));
						myChest.setInventorySlotContents(j, is.copy());
						is.stackSize = 0;
						f = true;
						break;
					}
					else if (isc.isStackable() && isc.isItemEqual(is))
					{
//						mod_littleMaidMob.Debug(String.format("%s -> %s", is.getItemName(), isc.getItemName()));
						f = true;
						isc.stackSize += is.stackSize;
						if (isc.stackSize > isc.getMaxStackSize())
						{
							is.stackSize = isc.stackSize - isc.getMaxStackSize();
							isc.stackSize = isc.getMaxStackSize();
						} else {
							is.stackSize = 0; 
							break;
						}
					}
				}
				if (is.stackSize <= 0) {
					owner.maidInventory.setInventorySlotContents(maidSearchCount, null);
				}
				if (f) {
					owner.playSound("random.pop");
					owner.setSwing(2, LMM_EnumSound.Null);
				}
			}
//			mod_littleMaidMob.Debug(String.format("getchest3:%d", maidSearchCount));
			if (++maidSearchCount >= owner.maidInventory.mainInventory.length) {
				// 検索済みの対象をスタック, Stack the subject of the search has been
//				serchedChest.add(myChest);
				clearMy();
				lastdistance = 0D;
				mod_LMM_littleMaidMob.Debug("endChest.");
				// 空きができたら捜索終了, Search ends when you are free
				if (owner.maidInventory.getFirstEmptyStack() > -1) {
					mod_LMM_littleMaidMob.Debug("Search clear.");
					fusedTiles.clear();
				}
			}
		}
	}

	@Override
	public boolean attackEntityAsMob(int pMode, Entity pEntity) {
		if (pEntity == myInventory) {
			// チェスト付カートとか, Such as chest with cart
			Entity lentity = (Entity)myInventory;
			if (!lentity.isDead) {
				if (owner.getDistanceSqToEntity(lentity) < 5D)	{
					owner.getNavigator().clearPathEntity();
					if (myChest == null) {
						myChest = (IInventory)lentity;
						fusedTiles.add(myChest);
						myChest.openChest();
					}
					if (myChest != null) {
						owner.getLookHelper().setLookPositionWithEntity(lentity, 30F, 40F);
					}
					// チェストに収納, I put away the chest
					putChest();
				} else {
					// チェストまでのパスを作る, I make the path to the chest
					if (!owner.isMaidWaitEx()) {
						double distance = owner.getDistanceSqToEntity(lentity);
						if (distance == lastdistance) {
							// TODO: 現状無意味, Current situation meaningless
							mod_LMM_littleMaidMob.Debug("Assert.");
							//XXX: exprimental 
							//owner.updateWanderPath();
							owner.onLivingUpdate();
						} else {
							owner.getNavigator().tryMoveToXYZ(lentity.posX, lentity.posY, lentity.posZ, 1.0F);
						}
						lastdistance = distance;
//						mod_littleMaidMob.Debug(String.format("Rerute:%b", hasPath()));
						if (myChest != null) {
							myChest.closeChest();
							myChest = null;
						}
					}
				}
			} else {
				// Entityの死亡, Death of Entity
				clearMy();
			}
			return true;
		} else {
			// ターゲットが変わってる？, Target is changed?
			clearMy();
		}
		return true;
	}

	@Override
	public boolean isChangeTartget(Entity pTarget) {
		if (pTarget instanceof IInventory) {
			return false;
		}
		return super.isChangeTartget(pTarget);
	}

	@Override
	public boolean preInteract(EntityPlayer pentityplayer, ItemStack pitemstack) {
		// しゃがみ時は処理無効,Processing disabled when squatting
		if (pentityplayer.isSneaking()) {
			return false;
		}
		if (owner.isContract()) {
			// 契約状態, Agreement state
			if (owner.isEntityAlive() && owner.isMaidContractOwner(pentityplayer)) {
				if (pitemstack != null) {
					// 追加分の処理, Processing an additional portion of
					owner.setPathToEntity(null);
					if (owner.isRemainsContract()) {
						if (pitemstack.getItem() instanceof ItemAppleGold) {
							// ゴールデンアッポー, Golden Apple
							if(!owner.worldObj.isRemote) {
								//XXX: experimenting
								//((ItemAppleGold)pitemstack.getItem()).onFoodEaten(pitemstack, owner.worldObj, owner.maidAvatar);
								((ItemAppleGold)pitemstack.getItem()).onEaten(pitemstack, owner.worldObj, owner.maidAvatar);
							}
							MMM_Helper.decPlayerInventory(pentityplayer, -1, 1);
							return true;
						}
						else if (pitemstack.getItem() instanceof ItemBucketMilk && !owner.getActivePotionEffects().isEmpty()) {
							// 牛乳に相談だ, The Consultation on milk
							if(!owner.worldObj.isRemote) {
								owner.clearActivePotions();
							}
							MMM_Helper.decPlayerInventory(pentityplayer, -1, 1);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
