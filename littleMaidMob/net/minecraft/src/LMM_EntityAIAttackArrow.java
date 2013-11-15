package net.minecraft.src;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LMM_EntityAIAttackArrow extends EntityAIBase implements LMM_IEntityAI {

	protected boolean fEnable;
	
	protected LMM_EntityLittleMaid fMaid;
	protected LMM_EntityLittleMaidAvatar fAvatar;
	protected LMM_InventoryLittleMaid fInventory;
	protected LMM_SwingStatus swingState;
	protected World worldObj;
	protected EntityLivingBase fTarget;
	protected int fForget;

	
	public LMM_EntityAIAttackArrow(LMM_EntityLittleMaid pEntityLittleMaid) {
		fMaid = pEntityLittleMaid;
		fAvatar = pEntityLittleMaid.maidAvatar;
		fInventory = pEntityLittleMaid.maidInventory;
		swingState = pEntityLittleMaid.getSwingStatusDominant();
		worldObj = pEntityLittleMaid.worldObj;
		fEnable = false;
		setMutexBits(3);
	}
	
	@Override
	public boolean shouldExecute() {
		EntityLivingBase entityliving = fMaid.getAttackTarget();
		
		if (!fEnable || entityliving == null || entityliving.isDead) {
			fMaid.setAttackTarget(null);
			fMaid.setTarget(null);
			if (entityliving != null) {
				fMaid.getNavigator().clearPathEntity();
			}
			fTarget = null;
			return false;
		} else {
			fTarget = entityliving;
			return true;
		}
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		fMaid.playSound(fMaid.isBloodsuck() ? LMM_EnumSound.findTarget_B : LMM_EnumSound.findTarget_N, false);
		swingState = fMaid.getSwingStatusDominant();
	}

	@Override
	public boolean continueExecuting() {
		return shouldExecute() || (fTarget != null && !fMaid.getNavigator().noPath());
	}

	@Override
	public void resetTask() {
		fTarget = null;
	}

	@Override
	public void updateTask() {
		double lrange = 225D;
		double ldist = fMaid.getDistanceSqToEntity(fTarget);
		boolean lsee = fMaid.getEntitySenses().canSee(fTarget);
		
		// ・ｽ・ｽ・ｽE・ｽﾌ外・ｽﾉ出・ｽ・ｽ・ｽ・ｽ・ｽ闔橸ｿｽﾔで飽・ｽ・ｽ・ｽ・ｽ
		if (lsee) {
			fForget = 0;
		} else {
			fForget++;
		}
		
		// ・ｽU・ｽ・ｽ・ｽﾎ象ゑｿｽ・ｽ・ｽ・ｽ・ｽ
		fMaid.getLookHelper().setLookPositionWithEntity(fTarget, 30F, 30F);
		
		if (ldist < lrange) {
			// ・ｽL・ｽ・ｽﾋ抵ｿｽ・ｽ・ｽ
			double atx = fTarget.posX - fMaid.posX;
			double aty = fTarget.posY - fMaid.posY;
			double atz = fTarget.posZ - fMaid.posZ;
			if (fTarget.isEntityAlive()) {
				ItemStack litemstack = fMaid.getCurrentEquippedItem();
				// ・ｽG・ｽﾆのベ・ｽN・ｽg・ｽ・ｽ
				double atl = atx * atx + aty * aty + atz * atz;
				double il = -1D;
				double milsq = 10D;
				Entity masterEntity = fMaid.getMaidMasterEntity();
				if (masterEntity != null && !fMaid.isPlaying()) {
					// ・ｽ・ｽﾆのベ・ｽN・ｽg・ｽ・ｽ
					double amx = masterEntity.posX - fMaid.posX;
					double amy = masterEntity.posY - fMaid.posY;//-2D
					double amz = masterEntity.posZ - fMaid.posZ;
					
					// ・ｽ・ｽ・ｽﾌ値・ｽ・ｽ・ｽO・ｽ`・ｽP・ｽﾈゑｿｽ^・ｽ[・ｽQ・ｽb・ｽg・ｽﾆの間に主が・ｽ・ｽ・ｽ・ｽ
					il = (amx * atx + amy * aty + amz * atz) / atl;
					
					// ・ｽﾋ撰ｿｽx・ｽN・ｽg・ｽ・ｽ・ｽﾆ趣ｿｽﾆの撰ｿｽ・ｽ・ｽ・ｽx・ｽN・ｽg・ｽ・ｽ
					double mix = (fMaid.posX + il * atx) - masterEntity.posX;
					double miy = (fMaid.posY + il * aty) - masterEntity.posY;// + 2D;
					double miz = (fMaid.posZ + il * atz) - masterEntity.posZ;
					// ・ｽﾋ撰ｿｽ・ｽ・ｽﾆの具ｿｽ・ｽ・ｽ
					milsq = mix * mix + miy * miy + miz * miz;
//					mod_LMM_littleMaidMob.Debug("il:%f, milsq:%f", il, milsq);
				}
				
				if (litemstack != null && !(litemstack.getItem() instanceof ItemFood) && !fMaid.weaponReload) {
					int lastentityid = worldObj.loadedEntityList.size();
					int itemcount = litemstack.stackSize;
					fMaid.mstatAimeBow = true;
					fAvatar.getValueVectorFire(atx, aty, atz, atl);
					// ・ｽ_・ｽC・ｽ・ｽ・ｽA・ｽ・ｽ・ｽw・ｽ・ｽ・ｽ・ｽ・ｽﾈら味・ｽ・ｽﾖの鯉ｿｽﾋゑｿｽ・ｽC・ｽ・ｽ・ｽ・ｽ・ｽy・ｽ・ｽ
					boolean lcanattack = true;
					boolean ldotarget = false;
					double tpr = Math.sqrt(atl);
					Entity lentity = MMM_Helper.getRayTraceEntity(fMaid.maidAvatar, tpr + 1.0F, 1.0F, 1.0F);
					int helmid = !fMaid.isMaskedMaid() ? 0 : fInventory.armorInventory[3].getItem().itemID;
					if (helmid == Item.helmetDiamond.itemID || helmid == Item.helmetGold.itemID) {
						// ・ｽﾋ撰ｿｽﾌ確・ｽF
						if (lentity != null && fMaid.getIFF(lentity)) {
							lcanattack = false;
//							mod_LMM_littleMaidMob.Debug("ID:%d-friendly fire to ID:%d.", fMaid.entityId, lentity.entityId);
						}
					}
					if (lentity == fTarget) {
						ldotarget = true;
					}
					lcanattack &= (milsq > 3D || il < 0D);
					lcanattack &= ldotarget;
					// ・ｽ・ｽ・ｽﾚ難ｿｽ
					if (!lcanattack) {
						// ・ｽﾋ鯉ｿｽ・ｽﾊ置・ｽ・ｽ・ｽm・ｽﾛゑｿｽ・ｽ・ｽ
						double tpx = fMaid.posX;
						double tpy = fMaid.posY;
						double tpz = fMaid.posZ;
//						double tpr = Math.sqrt(atl) * 0.5D;
						tpr = tpr * 0.5D;
						if (fMaid.isBloodsuck()) {
							// ・ｽ・ｽ・ｽ・ｽ・ｽ
							tpx += (atz / tpr);
							tpz -= (atx / tpr);
						} else {
							// ・ｽE・ｽ・ｽ・ｽ
							tpx -= (atz / tpr);
							tpz += (atx / tpr);
						}
						fMaid.getNavigator().tryMoveToXYZ(tpx, tpy, tpz, 1.0F);
					}
					else if (lsee & ldist < 100) {
						fMaid.getNavigator().clearPathEntity();
//						mod_LMM_littleMaidMob.Debug("Shooting Range.");
					}
					
					lcanattack &= lsee;
//            		mod_littleMaidMob.Debug(String.format("id:%d at:%d", entityId, attackTime));
					if (((fMaid.weaponFullAuto && !lcanattack) || (lcanattack && fMaid.getSwingStatusDominant().canAttack())) && fAvatar.isItemTrigger) {
						// ・ｽV・ｽ・ｽ・ｽ[・ｽg
						// ・ｽt・ｽ・ｽ・ｽI・ｽ[・ｽg・ｽ・ｽ・ｽ・ｽﾍ射鯉ｿｽ・ｽ・ｽ~
						mod_LMM_littleMaidMob.Debug("id:%d shoot.", fMaid.entityId);
						fAvatar.stopUsingItem();
						fMaid.setSwing(30, LMM_EnumSound.shoot);
					} else {
						// ・ｽ`・ｽ・ｽ・ｽ[・ｽW
						if (litemstack.getMaxItemUseDuration() > 500) {
//                			mod_littleMaidMob.Debug(String.format("non reload.%b", isMaskedMaid));
							// ・ｽ・ｽ・ｽ・ｽ・ｽ[・ｽh・ｽ・ｽ・ｽ・ｽ・ｽﾌ通常兵・ｽ・ｽ
							if (!fAvatar.isUsingItemLittleMaid()) {
								// ・ｽ\・ｽ・ｽ
								if (!fMaid.weaponFullAuto || lcanattack) {
									// ・ｽt・ｽ・ｽ・ｽI・ｽ[・ｽg・ｽ・ｽ・ｽ・ｽ・ｽﾌ場合・ｽﾍ射撰ｿｽm・ｽF
									int at = ((helmid == Item.helmetIron.itemID) || (helmid == Item.helmetDiamond.itemID)) ? 26 : 16;
									if (swingState.attackTime < at) {
										fMaid.setSwing(at, LMM_EnumSound.sighting);
										litemstack = litemstack.useItemRightClick(worldObj, fAvatar);
										mod_LMM_littleMaidMob.Debug("id:%d redygun.", fMaid.entityId);
									}
								} else {
									mod_LMM_littleMaidMob.Debug(String.format("ID:%d-friendly fire FullAuto.", fMaid.entityId));
								}
							}
						} 
						else if (litemstack.getMaxItemUseDuration() == 0) {
							// ・ｽﾊ常投・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ
							if (swingState.canAttack() && !fAvatar.isUsingItem()) {
								if (lcanattack) {
									litemstack = litemstack.useItemRightClick(worldObj, fAvatar);
									// ・ｽﾓ図・ｽI・ｽﾉシ・ｽ・ｽ・ｽ[・ｽg・ｽX・ｽp・ｽ・ｽ・ｽﾅ会ｿｽ・ｽ・ｽ・ｽﾂゑｿｽ謔､・ｽﾉゑｿｽ・ｽﾄゑｿｽ・ｽ・ｽ
									fMaid.mstatAimeBow = false;
									fMaid.setSwing(10, (litemstack.stackSize == itemcount) ? LMM_EnumSound.shoot_burst : LMM_EnumSound.Null);
									mod_LMM_littleMaidMob.Debug(String.format("id:%d throw weapon.(%d:%f:%f)", fMaid.entityId, swingState.attackTime, fMaid.rotationYaw, fMaid.rotationYawHead));
								} else {
									mod_LMM_littleMaidMob.Debug(String.format("ID:%d-friendly fire throw weapon.", fMaid.entityId));
								}
							}
						} else {
							// ・ｽ・ｽ・ｽ・ｽ・ｽ[・ｽh・ｽL・ｽ・ｽﾌ難ｿｽ・ｽ齦ｺ・ｽ・ｽ
							if (!fAvatar.isUsingItemLittleMaid()) {
								litemstack = litemstack.useItemRightClick(worldObj, fAvatar);
								mod_LMM_littleMaidMob.Debug(String.format("%d reload.", fMaid.entityId));
							}
							// ・ｽ・ｽ・ｽ・ｽ・ｽ[・ｽh・ｽI・ｽ・ｽ・ｽﾜで具ｿｽ・ｽ・ｽ・ｽI・ｽﾉ構・ｽ・ｽ・ｽ・ｽ
							swingState.attackTime = 5;
						}
					}
//            		maidAvatarEntity.setValueRotation();
					fAvatar.setValueVector();
					// ・ｽA・ｽC・ｽe・ｽ・ｽ・ｽ・ｽ・ｽS・ｽ・ｽ・ｽﾈゑｿｽ・ｽ・ｽ
					if (litemstack.stackSize <= 0) {
						fMaid.destroyCurrentEquippedItem();
						fMaid.getNextEquipItem();
					} else {
						fInventory.setInventoryCurrentSlotContents(litemstack);
					}
					
					// ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽEntity・ｽ・ｽ・ｽ`・ｽF・ｽb・ｽN・ｽ・ｽ・ｽ・ｽmaidAvatarEntity・ｽ・ｽ・ｽ・ｽ・ｽﾈゑｿｽ・ｽ・ｽ・ｽ・ｽ・ｽm・ｽF
					List<Entity> newentitys = worldObj.loadedEntityList.subList(lastentityid, worldObj.loadedEntityList.size());
					boolean shootingflag = false;
					if (newentitys != null && newentitys.size() > 0) {
						mod_LMM_littleMaidMob.Debug(String.format("new FO entity %d", newentitys.size()));
						for (Entity te : newentitys) {
							if (te.isDead) {
								shootingflag = true;
								continue;
							}
							try {
								// ・ｽ・ｽ・ｽﾄ体の趣ｿｽ・ｽu・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ
								Field fd[] = te.getClass().getDeclaredFields();
//                				mod_littleMaidMob.Debug(String.format("%s, %d", e.getClass().getName(), fd.length));
								for (Field ff : fd) {
									// ・ｽﾏ撰ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽAvatar・ｽﾆ難ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽﾆ置・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ
									ff.setAccessible(true);
									Object eo = ff.get(te);
									if (eo.equals(fAvatar)) {
										ff.set(te, this);
										mod_LMM_littleMaidMob.Debug("Replace FO Owner.");
									}
								}
							}
							catch (Exception exception) {
							}
						}
					}
					// ・ｽ・ｽﾉ厄ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽﾄゑｿｽ・ｽ・ｽ・ｽ鼾・ｿｽﾌ擾ｿｽ・ｽ・ｽ
					if (shootingflag) {
						for (Object obj : worldObj.loadedEntityList) {
							if (obj instanceof EntityCreature && !(obj instanceof LMM_EntityLittleMaid)) {
								EntityCreature ecr = (EntityCreature)obj;
								if (ecr.getEntityToAttack() == fAvatar) {
									ecr.setTarget(fMaid);
								}
							}
						}
					}
				}
			}
		} else {
			// ・ｽL・ｽ・ｽﾋ抵ｿｽ・ｽO
			if (fMaid.getNavigator().noPath()) {
				fMaid.getNavigator().tryMoveToEntityLiving(fTarget, 1.0);
			}
			if (fMaid.getNavigator().noPath()) {
				mod_LMM_littleMaidMob.Debug("id:%d Target renge out.", fMaid.entityId);
				fMaid.setAttackTarget(null);
			}
			if (fMaid.weaponFullAuto && fAvatar.isItemTrigger) {
				fAvatar.stopUsingItem();
			} else {
				fAvatar.clearItemInUse();
			}
			
		}
		
	}

	@Override
	public void setEnable(boolean pFlag) {
		fEnable = pFlag;
	}

	@Override
	public boolean getEnable() {
		return fEnable;
	}

}
