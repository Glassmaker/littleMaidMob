package net.minecraft.src;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public class LMM_EntityAIRestrictRain extends EntityAIBase implements
		LMM_IEntityAI {

	protected EntityLiving theEntity;
	protected boolean isEnable;

	public LMM_EntityAIRestrictRain(EntityLiving par1EntityLiving) {
		theEntity = par1EntityLiving;
		isEnable = false;
	}

	@Override
	public boolean shouldExecute() {
		return isEnable && theEntity.worldObj.isRaining();
	}

	@Override
	public void startExecuting() {
		theEntity.getNavigator().setAvoidSun(true);
	}

	@Override
	public void resetTask() {
		theEntity.getNavigator().setAvoidSun(false);
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
