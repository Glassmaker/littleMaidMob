package net.minecraft.src;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;

public class LMM_EntityMode_Playing extends LMM_EntityModeBase {

	public static final int mmode_Playing	= 0x00ff;

	public static final int mpr_NULL = 0;
	public static final int mpr_QuickShooter = 0x0010;
	public static final int mpr_StockShooter = 0x0020;
	
	public int fcounter;
	
	protected Random rand = new Random();

	public LMM_EntityMode_Playing(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
		fcounter = 0;
	}

	@Override
	public int priority() {
		return 900;
	}

	@Override
	public void init() {
		ModLoader.addLocalization("littleMaidMob.mode.Playing", "Playing");
		// ModLoader.addLocalization("littleMaidMob.mode.T-Playing", "Playing");
		// ModLoader.addLocalization("littleMaidMob.mode.F-Playing", "Playing");
		// ModLoader.addLocalization("littleMaidMob.mode.D-Playing", "Playing");
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		// Playing:0x00ff
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = pDefaultTargeting;
//		ltasks[1] = new EntityAITasks(owner.aiProfiler);
		
//		ltasks[1].addTask(3, new LMM_EntityAIHurtByTarget(owner, true));
//		ltasks[1].addTask(4, new LMM_EntityAINearestAttackableTarget(owner, EntityLiving.class, 16F, 0, true));
		
		owner.addMaidMode(ltasks, "Playing", mmode_Playing);
		
	}

	protected boolean checkSnows(int x, int y, int z) {
		// ・ｽ・ｽ閧ｪ・ｽ痰ｩ・ｽH
		boolean f = true;
		f &= owner.worldObj.getBlockId(x, y, z) == Block.snow.blockID;
		f &= owner.worldObj.getBlockId(x + 1, y, z) == Block.snow.blockID;
		f &= owner.worldObj.getBlockId(x - 1, y, z) == Block.snow.blockID;
		f &= owner.worldObj.getBlockId(x, y, z + 1) == Block.snow.blockID;
		f &= owner.worldObj.getBlockId(x, y, z - 1) == Block.snow.blockID;
		
		return f;
	}

	protected boolean movePlaying() {
		//
		int x = MathHelper.floor_double(owner.posX);
		int y = MathHelper.floor_double(owner.posY);
		int z = MathHelper.floor_double(owner.posZ);
		PathEntity pe = null;
		
		// CW・ｽ・ｽ・ｽﾉ鯉ｿｽ・ｽ・ｽ・ｽﾌ茨ｿｽ・ｽ・ｽL・ｽ・ｽ・ｽ・ｽ 
		loop_search:
			for (int a = 2; a < 18 && pe == null; a += 2) {
				x--;
				z--;
				for (int b = 0; b < a; b++) {
					// N
					for (int c = 0; c < 4; c++) {
						if (checkSnows(x, y, z)) {
							pe = owner.worldObj.getEntityPathToXYZ(owner, x, y - 1, z, 10F, true, false, false, true);
							if (pe != null) {
								break loop_search;
							}
						}
						if (c == 0) x++;
						if (c == 1) z++;
						if (c == 2) x--;
						if (c == 3) z--;
					}
				}
			}
		
		if (pe != null) {
			owner.getNavigator().setPath(pe, 1.0F);
			mod_LMM_littleMaidMob.Debug("Find Snow Area-%d:%d, %d, %d.", owner.entityId, x, y, z);
			return true;
		} else {
			return false;
		}
	}

	protected void playingSnowWar() {
		switch (fcounter) {
		case 0:
			// ・ｽL・ｽ・ｽﾊ全・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ
			owner.setSitting(false);
			owner.setSneaking(false);
			if (!owner.getNextEquipItem()) {
				owner.setAttackTarget(null);
				
				owner.getNavigator().clearPathEntity();
				fcounter = 1;
			} else if (owner.getAttackTarget() == null) {
				// ・ｽ・ｽ・ｽC・ｽh・ｽﾆプ・ｽ・ｽ・ｽ[・ｽ・ｽ・ｽ[・ｽi・ｽ・ｽ・ｽ・ｽ・ｽﾊ）・ｽ・ｽ・ｽ^・ｽ[・ｽQ・ｽb・ｽg・ｽ・ｽ
				List<Entity> list = owner.worldObj.getEntitiesWithinAABBExcludingEntity(owner, owner.boundingBox.expand(16D, 4D, 16D));
				for (Entity e : list) {
					if (e != null && (e instanceof EntityPlayer || e instanceof LMM_EntityLittleMaid)) {
						if (rand.nextBoolean()) {
							owner.setAttackTarget((EntityLivingBase)e);
							break;
						}
					}
				}
			}
			break;
		case 1:
			// ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ
			owner.setAttackTarget(null);
			if (owner.getNavigator().noPath()) {
				fcounter = 2;
			}
			break;
		
		case 2:
			// ・ｽ瘡ｴ・ｽ・ｽT・ｽ・ｽ
			if (owner.getAttackTarget() == null && owner.getNavigator().noPath()) {
				if (movePlaying()) {
					fcounter = 3;
				} else {
					owner.setPlayingRole(mpr_NULL);
					fcounter = 0;
				}
			} else {
				owner.setAttackTarget(null);
			}
//			isMaidChaseWait = true;
			break;
		case 3:
			// ・ｽ瘡ｴ・ｽﾖ難ｿｽ・ｽ・ｽ
			if (owner.getNavigator().noPath()) {
				if (checkSnows(
						MathHelper.floor_double(owner.posX),
						MathHelper.floor_double(owner.posY),
						MathHelper.floor_double(owner.posZ))) {
//					owner.isMaidChaseWait = true;
					owner.attackTime = 30;
					if (owner.getPlayingRole() == mpr_QuickShooter) {
						fcounter = 8;
					} else {
						fcounter = 4;
					}
				} else {
					// ・ｽﾄ鯉ｿｽ・ｽ・ｽ
					fcounter = 2;
				}
			}
			break;
		case 4:
		case 5:
		case 6:
		case 7:
			// ・ｽ・ｽ・ｽ・ｽ・ｽ[・ｽh
			if (owner.attackTime <= 0) {
				if (owner.maidInventory.addItemStackToInventory(new ItemStack(Item.snowball))) {
					owner.playSound("random.pop");
					if (owner.getPlayingRole() == mpr_StockShooter) {
						owner.setSwing(5, LMM_EnumSound.collect_snow);
						fcounter = 0;
					} else {
						owner.setSwing(30, LMM_EnumSound.collect_snow);
						fcounter++;
					}
				} else {
					owner.setPlayingRole(mpr_NULL);
					fcounter = 0;
				}
			}
//			owner.isMaidChaseWait = true;
			owner.setJumping(false);
			owner.getNavigator().clearPathEntity();
			owner.getLookHelper().setLookPosition(
					MathHelper.floor_double(owner.posX), 
					MathHelper.floor_double(owner.posY - 1D), 
					MathHelper.floor_double(owner.posZ), 
					30F, 40F);
			owner.setSitting(true);
			break;
		case 8:
			// ・ｽ・ｽ・ｽ・ｽ・ｽ[・ｽh
//			isMaidChaseWait = true;
			if (owner.attackTime <= 0) {
				if (owner.maidInventory.addItemStackToInventory(new ItemStack(Item.snowball))) {
					owner.setSwing(5, LMM_EnumSound.collect_snow);
					owner.playSound("random.pop");
					fcounter = 0;
				} else {
					owner.setPlayingRole(mpr_NULL);
					fcounter = 0;
				}
			}
//			isMaidChaseWait = true;
			owner.setSneaking(true);
			owner.getLookHelper().setLookPosition(
					MathHelper.floor_double(owner.posX), 
					MathHelper.floor_double(owner.posY - 1D), 
					MathHelper.floor_double(owner.posZ), 
					30F, 40F);
			break;
		}
		
	}


	@Override
	public void updateAITick(int pMode) {
		if (owner.isFreedom()) {
			// ・ｽ・ｽ・ｽR・ｽs・ｽ・ｽ・ｽ・ｽ・ｽﾌ固体は虎趣ｿｽ眈々・ｽﾆ鯉ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽB
			if (owner.worldObj.isDaytime()) {
				// ・ｽ・ｽ・ｽﾔのゑｿｽ・ｽV・ｽ・ｽ
				
				// ・ｽ瘡ｴ・ｽ・ｽ・ｽ・ｽ
				if (!owner.isPlaying()) {
					// TODO:・ｽ・ｽ・ｽV・ｽﾑ費ｿｽ・ｽ・ｽ
					int xx = MathHelper.floor_double(owner.posX);
					int yy = MathHelper.floor_double(owner.posY);
					int zz = MathHelper.floor_double(owner.posZ);
					
					// 3x3・ｽ・ｽ・ｽ・ｽﾌ包ｿｽ・ｽ・ｽ・ｽﾈらお・ｽV・ｽﾑ費ｿｽ・ｽ閧ｪ・ｽ・ｽ・ｽ・ｽ
					boolean f = true;
					for (int z = -1; z < 2; z++) {
						for (int x = -1; x < 2; x++) {
							f &= owner.worldObj.getBlockId(xx + x, yy, zz + z) == Block.snow.blockID;
						}
					}
					int lpr = rand.nextInt(100) - 97;
					lpr = (f && lpr > 0) ? (lpr == 1 ? mpr_QuickShooter : mpr_StockShooter) : 0;
					owner.setPlayingRole(lpr);
					fcounter = 0;
					if (f) {
						// mod_littleMaidMob.Debug(String.format("playRole-%d:%d", entityId, playingRole));
					}
					
				} else if (owner.getPlayingRole() >= 0x8000) {
					// ・ｽ・ｽﾌ包ｿｽ・ｽI・ｽ・ｽ
					owner.setPlayingRole(mpr_NULL);
					fcounter = 0;
				} else {
					// ・ｽ・ｽ・ｽV・ｽﾑの趣ｿｽ・ｽs・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽﾉ擾ｿｽ・ｽ・ｽ・ｽH
					if (owner.getPlayingRole() == mpr_QuickShooter || 
							owner.getPlayingRole() == mpr_StockShooter) {
						playingSnowWar();
					}
					
				}
				
			} else {
				// ・ｽ・ｽﾌゑｿｽ・ｽV・ｽ・ｽ
				if (!owner.isPlaying()) {
					// ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ
					
				} else if (owner.getPlayingRole() < 0x8000) {
					// ・ｽ・ｽ・ｽﾌ包ｿｽ・ｽI・ｽ・ｽ
					owner.setPlayingRole(mpr_NULL);
					fcounter = 0;
					
				} else {
					// ・ｽ・ｽ・ｽV・ｽﾑの趣ｿｽ・ｽs・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽﾉ擾ｿｽ・ｽ・ｽ・ｽH
					
				}
			}
			
			// ・ｽ`・ｽF・ｽX・ｽg・ｽ・ｽ・ｽ・ｽ
			if (owner.getAttackTarget() == null
					&& owner.maidInventory.getFirstEmptyStack() == -1) {
				
			}
		}
	}

	@Override
	public float attackEntityFrom(DamageSource par1DamageSource, float par2) {
		if (par1DamageSource.getSourceOfDamage() instanceof EntitySnowball) {
			// ・ｽ・ｽ・ｽV・ｽﾑ費ｿｽ・ｽ・ｽp・ｽA・ｽ・ｽﾊゑｿｽ・ｽﾇゑｿｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ
			owner.maidDamegeSound = LMM_EnumSound.hurt_snow;
			if (!owner.isContract() || owner.isFreedom()) {
				owner.setPlayingRole(mpr_QuickShooter);
				owner.setMaidWait(false);
				owner.setMaidWaitCount(0);
				mod_LMM_littleMaidMob.Debug("playingMode Enable.");
			}
		}
		return 0F;
	}

	@Override
	public boolean setMode(int pMode) {
		switch (pMode) {
		case mmode_Playing :
			owner.aiAttack.setEnable(false);
			owner.aiShooting.setEnable(true);
			owner.setBloodsuck(false);
			return true;
		}
		
		return false;
	}

	@Override
	public int getNextEquipItem(int pMode) {
		ItemStack litemstack = null;
		if (owner.getPlayingRole() != 0) {
			for (int li = 0; li < owner.maidInventory.maxInventorySize; li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack == null) continue;
				
				// ・ｽ瘠・
				if (litemstack.getItem() instanceof ItemSnowball) {
					return li;
				}
			}
		}
		return -1;
	}

}
