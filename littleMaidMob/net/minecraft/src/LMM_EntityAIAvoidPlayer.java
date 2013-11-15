package net.minecraft.src;

import java.util.List;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.Vec3;

public class LMM_EntityAIAvoidPlayer extends EntityAIBase implements
		LMM_IEntityAI {

	/** The entity we are attached to */
	protected LMM_EntityLittleMaid theMaid;
	protected EntityPlayer theMaster;
	protected float speedNormal;
	protected PathEntity avoidPath;
	/** The PathNavigate of our entity */
	protected PathNavigate entityPathNavigate;
	protected boolean isEnable;

	public boolean isActive;
	public int minDist;

	public LMM_EntityAIAvoidPlayer(LMM_EntityLittleMaid pEntityLittleMaid,
			float pSpeed, int pMinDist) {
		theMaid = pEntityLittleMaid;
		speedNormal = pSpeed;
		entityPathNavigate = pEntityLittleMaid.getNavigator();
		isActive = false;
		isEnable = false;
		minDist = pMinDist;
		setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (!isEnable || !isActive || !theMaid.isContract()) {
			isActive = false;
			return false;
		}

		theMaster = theMaid.mstatMasterEntity;

		// ・ｽﾎ象は鯉ｿｽ・ｽ・ｽ・ｽ驍ｩ・ｽH・ｽﾄゑｿｽ・ｽ・ｽ・ｽ黷｢・ｽ・ｽﾈゑｿｽ・ｽﾋ？
		if (!theMaid.getEntitySenses().canSee(theMaster)) {
			return false;
		}

		// ・ｽﾚ難ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ
		Vec3 vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(
				theMaid, minDist, 7, Vec3.createVectorHelper(theMaster.posX,
						theMaster.posY, theMaster.posZ));

		// ・ｽﾚ難ｿｽ・ｽ謔ｪ・ｽ・ｽ・ｽ・ｽ
		if (vec3d == null) {
			return false;
		}
		// ・ｽﾚ難ｿｽ・ｽ・ｽﾌ具ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽﾟゑｿｽ
		if (theMaster.getDistanceSq(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord) < theMaid.mstatMasterDistanceSq) {
			return false;
		}
		
		avoidPath = entityPathNavigate.getPathToXYZ(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
		
		if (avoidPath == null) {
			return false;
		}

		return avoidPath.isDestinationSame(vec3d);
	}

	@Override
	public boolean continueExecuting() {
		return !entityPathNavigate.noPath() && theMaid.getDistanceSqToEntity(theMaster) < 144D;
	}

	@Override
	public void startExecuting() {
		entityPathNavigate.setPath(avoidPath, speedNormal);
	}

	@Override
	public void resetTask() {
		isActive = false;
	}

	public void setActive() {
		// ・ｽ・ｽ・ｽ・ｽJ・ｽn
		isActive = true;
	}

	// ・ｽ・ｽ・ｽs・ｽﾂ能・ｽt・ｽ・ｽ・ｽO
	@Override
	public void setEnable(boolean pFlag) {
		isEnable = pFlag;
	}

	@Override
	public boolean getEnable() {
		return isEnable;
	}

}
