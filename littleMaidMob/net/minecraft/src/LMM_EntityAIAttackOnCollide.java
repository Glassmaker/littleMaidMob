package net.minecraft.src;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LMM_EntityAIAttackOnCollide extends EntityAIBase implements LMM_IEntityAI {

	protected boolean fEnable;

	protected World worldObj;
	protected LMM_EntityLittleMaid theMaid;
	protected Entity entityTarget;
	protected float moveSpeed;
	protected boolean isReroute;
	protected PathEntity pathToTarget;
	protected int rerouteTimer;
	protected double attackRange;

	public boolean isGuard;


	public LMM_EntityAIAttackOnCollide(LMM_EntityLittleMaid par1EntityLittleMaid, float par2, boolean par3) {
		theMaid = par1EntityLittleMaid;
		worldObj = par1EntityLittleMaid.worldObj;
		moveSpeed = par2;
		isReroute = par3;
		isGuard = false;
		setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if (!fEnable) {
			return false;
		}
		Entity lentity = theMaid.getAttackTarget();
		if (lentity == null) {
			lentity = theMaid.getEntityToAttack();
			if (lentity == null) {
				return false;
			}
		}
		
		entityTarget = lentity;
		pathToTarget = theMaid.getNavigator().getPathToXYZ(entityTarget.posX, entityTarget.posY, entityTarget.posZ);
//		pathToTarget = theMaid.getNavigator().getPathToEntityLiving(entityTarget);
		attackRange = (double)theMaid.width + (double)entityTarget.width + 0.4D;
		attackRange *= attackRange;
		
		if ((pathToTarget != null) || (theMaid.getDistanceSq(entityTarget.posX, entityTarget.boundingBox.minY, entityTarget.posZ) <= attackRange)) {
			return true;
		} else {
			theMaid.setAttackTarget(null);
			theMaid.setTarget(null);
			return false;
		}
		
	}

	@Override
	public void startExecuting() {
		theMaid.getNavigator().setPath(pathToTarget, moveSpeed);
		rerouteTimer = 0;
		theMaid.playSound(theMaid.isBloodsuck() ? LMM_EnumSound.findTarget_B : LMM_EnumSound.findTarget_N, false);
		theMaid.maidAvatar.stopUsingItem();
	}

	@Override
	public boolean continueExecuting() {
		Entity lentity = theMaid.getAttackTarget();
		if (lentity == null) {
			lentity = theMaid.getEntityToAttack();
		}
		if (lentity == null || entityTarget != lentity) {
			return false;
		}
		
		if (entityTarget.isDead) {
			theMaid.setAttackTarget(null);
			theMaid.setTarget(null);
			theMaid.getNavigator().clearPathEntity();
			return false;
		}
		
		if (!entityTarget.isEntityAlive()) {
			return false;
		}
		
		if (!isReroute) {
			return !theMaid.getNavigator().noPath();
		}
		
//		return theMaid.isWithinHomeDistance(MathHelper.floor_double(entityTarget.posX), MathHelper.floor_double(entityTarget.posY), MathHelper.floor_double(entityTarget.posZ));
		return theMaid.func_110176_b(MathHelper.floor_double(entityTarget.posX), MathHelper.floor_double(entityTarget.posY), MathHelper.floor_double(entityTarget.posZ));
	}

	@Override
	public void resetTask() {
		entityTarget = null;
//		theMaid.getNavigator().clearPathEntity();
		theMaid.maidAvatar.stopUsingItem();
	}

	@Override
	public void updateTask() {
		theMaid.getLookHelper().setLookPositionWithEntity(entityTarget, 30F, 30F);
		
//		if ((isReroute || theMaid.getEntitySenses().canSee(entityTarget)) && --rerouteTimer <= 0) {
//			// �����[�g
//			rerouteTimer = 4 + theMaid.getRNG().nextInt(7);
//			theMaid.getNavigator().tryMoveToXYZ(entityTarget.posX, entityTarget.posY, entityTarget.posZ, moveSpeed);
//		}
		if (--rerouteTimer <= 0) {
			if (isReroute) {
				// �����[�g
				rerouteTimer = 4 + theMaid.getRNG().nextInt(7);
				theMaid.getNavigator().tryMoveToXYZ(entityTarget.posX, entityTarget.posY, entityTarget.posZ, moveSpeed);
			}
			if (theMaid.getEntitySenses().canSee(entityTarget)) {
				// �����[�g
				rerouteTimer = 4 + theMaid.getRNG().nextInt(7);
				theMaid.getNavigator().tryMoveToXYZ(entityTarget.posX, entityTarget.posY, entityTarget.posZ, moveSpeed);
			} else {
				theMaid.setAttackTarget(null);
				theMaid.setTarget(null);
			}
		}
		
		if (theMaid.getDistanceSq(entityTarget.posX, entityTarget.boundingBox.minY, entityTarget.posZ) > attackRange) {
			if (isGuard && theMaid.isMaskedMaid()) {
				EntityLivingBase lel = null;
				if (entityTarget instanceof EntityCreature) {
					lel = ((EntityCreature)entityTarget).getAttackTarget();
				}
				else if (entityTarget instanceof EntityLivingBase) {
					lel = ((EntityLivingBase)entityTarget).getAITarget();
				}
				if (lel == theMaid) {
					ItemStack li = theMaid.getCurrentEquippedItem();
					if (li != null && li.getItemUseAction() == EnumAction.block) {
						li.useItemRightClick(worldObj, theMaid.maidAvatar);
					}
				}
			}
			return;
		}
		
		if (!theMaid.getSwingStatusDominant().canAttack()) {
			return;
		} else {
			// ���ʂ���110�x���U���͈�
			double tdx = entityTarget.posX - theMaid.posX;
			double tdz = entityTarget.posZ - theMaid.posZ;
			double vdx = -Math.sin(theMaid.renderYawOffset * 3.1415926535897932384626433832795F / 180F);
			double vdz = Math.cos(theMaid.renderYawOffset * 3.1415926535897932384626433832795F / 180F);
			double ld = (tdx * vdx + tdz * vdz) / (Math.sqrt(tdx * tdx + tdz * tdz) * Math.sqrt(vdx * vdx + vdz * vdz));
//	        System.out.println(theMaid.renderYawOffset + ", " + ld);
			if (ld < -0.35D) {
				return;
			}
			
			// �U��
			theMaid.attackEntityAsMob(entityTarget);
			if (theMaid.getActiveModeClass().isChangeTartget(entityTarget)) {
				// �Ώۂ��Đݒ肳����
				theMaid.setAttackTarget(null);
				theMaid.setTarget(null);
				theMaid.getNavigator().clearPathEntity();
			}
			return;
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
