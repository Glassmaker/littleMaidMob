package net.minecraft.src;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * LMM・ｽp・ｽﾆ趣ｿｽAI・ｽ・ｽ・ｽ・ｽ・ｽﾉ使・ｽp・ｽB
 * ・ｽ・ｽ・ｽﾌ継・ｽ・ｽ・ｽN・ｽ・ｽ・ｽX・ｽ・ｽAI・ｽ・ｽ・ｽ・ｽ・ｽﾆゑｿｽ・ｽﾄ渡・ｽ・ｽ・ｽ・ｽ・ｽﾆゑｿｽ・ｽﾅゑｿｽ・ｽ・ｽB
 * ・ｽﾜゑｿｽ・ｽAAI・ｽ・ｽ・ｽ・ｽ・ｽI・ｽ・ﾍ難ｿｽ・ｽ・ｽﾌ関撰ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽﾄ選・ｽ・ﾌク・ｽ・ｽ・ｽX・ｽﾌみゑｿｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽB
 * ・ｽC・ｽ・ｽ・ｽX・ｽ^・ｽ・ｽ・ｽX・ｽ・ｽ・ｽ・ｽ・ｽ骼厄ｿｽﾉゑｿｽ閭搾ｿｽ[・ｽJ・ｽ・ｽ・ｽﾏ撰ｿｽ・ｽ・ｽﾛ趣ｿｽ・ｽB
 */
public abstract class LMM_EntityModeBase {

	public final LMM_EntityLittleMaid owner;


	/**
	 * ・ｽ・ｽ・ｽ・ｽ
	 */
	public LMM_EntityModeBase(LMM_EntityLittleMaid pEntity) {
		owner = pEntity;
	}

	public int fpriority;
	/**
	 * ・ｽD・ｽ謠・ｿｽﾊ。
	 * ・ｽﾔ搾ｿｽ・ｽ・ｽ・ｽ痰｢・ｽﾙゑｿｽ・ｽ・ｽ・ｽ・ｽﾉ擾ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽB
	 * ・ｽ・ｽ・ｽ・・ｽ00・ｽﾌゑｿｽ・ｽﾌはシ・ｽX・ｽe・ｽ・ｽ・ｽ\・ｽ・ｽB
	 */
	public abstract int priority();

	/**
	 * ・ｽN・ｽ・ｽ・ｽ・ｽ・ｽﾌ擾ｿｽ・ｽ・ｽB
	 */
	public void init() {
	}

	/**
	 * Entity・ｽ・ｽ・ｽ・ｽ・ｽﾌ趣ｿｽ・ｽs・ｽ・ｽ
	 */
	public void initEntity() {
	}

	/**
	 * ・ｽ・ｽ・ｽ[・ｽh・ｽﾌ追会ｿｽ・ｽB
	 */
	public abstract void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting);

	/**
	 * ・ｽﾆ趣ｿｽ・ｽf・ｽ[・ｽ^・ｽﾛ托ｿｽ・ｽp・ｽB
	 */
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
	}
	/**
	 * ・ｽﾆ趣ｿｽ・ｽf・ｽ[・ｽ^・ｽﾇ搾ｿｽ・ｽp・ｽB
	 */
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
	}

	/**
	 * renderSpecial・ｽﾌ追会ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽp・ｽB
	 */
	public void showSpecial(LMM_RenderLittleMaid prenderlittlemaid, double px, double py, double pz) {
	}

	/**
	 * ・ｽT・ｽ[・ｽo・ｽ[・ｽ・ｽ・ｽﾌみの厄ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽB
	 * AI・ｽ・ｽ・ｽ・ｽ・ｽﾌ鯉ｿｽﾌ包ｿｽﾉ呼ばゑｿｽ・ｽB
	 */
	public void updateAITick(int pMode) {
	}

	/**
	 * ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽB
	 * ・ｽ・ｽ・ｽﾌ擾ｿｽ・ｽ・ｽ・ｽﾌ前・ｽﾉ呼ばゑｿｽ・ｽ
	 */
	public void onUpdate(int pMode) {
	}

	/**
	 * ・ｽ・ｽ・ｽﾌへゑｿｽﾌ擾ｿｽ・ｽ・ｽ・ｽﾍ若干・ｽ・ｽ・ｽﾔゑｿｽ・ｽ・ｽ・ｽ・ｽ・ｽﾄゑｿｽ・ｽﾇゑｿｽ・ｽB
	 * ・ｽ・ｽ・ｽﾌア・ｽC・ｽe・ｽ・ｽ・ｽ・ｽ・ｽg・ｽp・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽB
	 * ・ｽ竓ｮ・ｽ・ｽ・ｽ・ｽ・ｽﾉ撰ｿｽｶて趣ｿｽ・ｽs・ｽ・ｽ・ｽ・ｽ・ｽA・ｽ・ｽ・ｽﾌ托ｿｽ・ｽ阡ｻ・ｽ・ｽ・ｽ・ｽS・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽB
	 */
	public boolean preInteract(EntityPlayer pentityplayer, ItemStack pitemstack) {
		return false;
	}
	/**
	 * ・ｽ・ｽ・ｽﾌへゑｿｽﾌ擾ｿｽ・ｽ・ｽ・ｽﾍ若干・ｽ・ｽ・ｽﾔゑｿｽ・ｽ・ｽ・ｽ・ｽ・ｽﾄゑｿｽ・ｽﾇゑｿｽ・ｽB
	 * ・ｽ・ｽ・ｽﾌア・ｽC・ｽe・ｽ・ｽ・ｽ・ｽ・ｽg・ｽp・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽB
	 */
	public boolean interact(EntityPlayer pentityplayer, ItemStack pitemstack) {
		return false;
	}

	/**
	 * ・ｽ・ｽ・ｽ・ｽ・ｽﾅ・ｿｽ・ｽ[・ｽh・ｽ`・ｽF・ｽ・ｽ・ｽW・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽB
	 */
	public boolean changeMode(EntityPlayer pentityplayer) {
		return false;
	}

	/**
	 * ・ｽ・ｽ・ｽ[・ｽh・ｽ`・ｽF・ｽ・ｽ・ｽW・ｽ・ｽ・ｽﾌ設定処・ｽ・ｽ・ｽﾌ本・ｽﾌ。
	 * ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽﾉ擾ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽﾈゑｿｽ・ｽﾆ・ｿｽ・ｽ[・ｽh・ｽ・ｽ・ｽﾉゑｿｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽﾈるか・ｽ・ｽ・ｽH
	 */
	public boolean setMode(int pMode) {
		return false;
	}

	/**
	 * ・ｽg・ｽp・ｽA・ｽC・ｽe・ｽ・ｽ・ｽﾌ選・ｽ・ｽ・ｽB
	 * ・ｽﾟゑｿｽl・ｽﾍス・ｽ・ｽ・ｽb・ｽg・ｽﾔ搾ｿｽ
	 */
	public int getNextEquipItem(int pMode) {
		// ・ｽ・ｽ・ｽI・ｽ・ｽ
		return -1;
	}
	
	/**
	 * ・ｽA・ｽC・ｽe・ｽ・ｽ・ｽ・ｽ・ｽﾂ否の費ｿｽ・ｽ闔ｮ・ｽB
	 * ・ｽE・ｽ・ｽ・ｽﾉ行・ｽ・ｽ・ｽA・ｽC・ｽe・ｽ・ｽ・ｽﾌ費ｿｽ・ｽ・ｽB
	 */
	public boolean checkItemStack(ItemStack pItemStack) {
		// ・ｽ・ｽ・ｽﾎ象ア・ｽC・ｽe・ｽ・ｽ・ｽﾌ設抵ｿｽﾈゑｿｽ
		return false;
	}

	/**
	 * ・ｽU・ｽ・ｽ・ｽ・ｽ・ｽ闖茨ｿｽ・ｽ・ｽB
	 * ・ｽ・ｽ・ｽ・ｽﾈ攻・ｽ・ｽ・ｽ・ｽ・ｽ・ｽﾍゑｿｽ・ｽ・ｽ・ｽﾅ趣ｿｽ・ｽ・ｽ・ｽB
	 */
	public boolean attackEntityAsMob(int pMode, Entity pEntity) {
		// ・ｽ・ｽ・ｽ・ｽU・ｽ・ｽ・ｽﾌ設抵ｿｽﾈゑｿｽ
		return false;
	}

	/**
	 * ・ｽu・ｽ・ｽ・ｽb・ｽN・ｽﾌチ・ｽF・ｽb・ｽN・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ驍ｩ・ｽﾇゑｿｽ・ｽ・ｽ・ｽB
	 * ・ｽ・ｽ・ｽ闔ｮ・ｽﾌどゑｿｽ・ｽ・ｽ・ｽ・ｽg・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽﾅ選・ｽ・ｽ・ｽB
	 */
	public boolean isSearchBlock() {
		return false;
	}

	/**
	 * isSearchBlock=false・ｽﾌとゑｿｽ・ｽﾉ費ｿｽ・ｽ閧ｳ・ｽ・ｽ・ｽB
	 */
	public boolean shouldBlock(int pMode) {
		return false;
	}

	/**
	 * ・ｽT・ｽ・ｽ・ｽ・ｽ・ｽﾟゑｿｽ・ｽu・ｽ・ｽ・ｽb・ｽN・ｽﾅゑｿｽ・ｽ驍ｩ・ｽB
	 * true・ｽ・ｽﾔゑｿｽ・ｽﾆ鯉ｿｽ・ｽ・ｽ・ｽI・ｽ・ｽ・ｽB
	 */
	public boolean checkBlock(int pMode, int px, int py, int pz) {
		return false;
	}

	/**
	 * ・ｽ・ｽ・ｽ・ｽ・ｽﾍ囲に搾ｿｽ・ｽG・ｽﾎ象ゑｿｽ・ｽﾈゑｿｽ・ｽ・ｽ・ｽ・ｽ・ｽB
	 */
	public boolean overlooksBlock(int pMode) {
		return false;
	}
//	@Deprecated
//	public TileEntity overlooksBlock(int pMode) {
//		return null;
//	}

	/**
	 * ・ｽ・ｽ・ｽE・ｽ・ｽ・ｽ・ｽ・ｽｴゑｿｽ・ｽ・ｽ・ｽ・ｽ・ｽﾌ擾ｿｽ・ｽ・ｽ
	 */
	public void farrangeBlock() {
		owner.getNavigator().clearPathEntity();
	}

	/**
	 * ・ｽL・ｽ・ｽﾋ抵ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽｴゑｿｽ・ｽ・ｽ・ｽ・ｽ・ｽﾌ擾ｿｽ・ｽ・ｽ
	 */
	public boolean outrangeBlock(int pMode, int pX, int pY, int pZ) {
		return owner.getNavigator().tryMoveToXYZ(pX, pY, pZ, 1.0F);
	}
	public boolean outrangeBlock(int pMode) {
		return outrangeBlock(pMode, owner.maidTile[0], owner.maidTile[1], owner.maidTile[2]);
	}

	/**
	 * ・ｽﾋ抵ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽﾉ難ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽs・ｽ・ｽ・ｽ・ｽ・ｽB
	 * ・ｽﾟゑｿｽl・ｽ・ｽtrue・ｽﾌ趣ｿｽ・ｽﾍ終・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽﾉ難ｿｽ・ｽ・ｽp・ｽ・ｽ
	 */
	public boolean executeBlock(int pMode, int px, int py, int pz) {
		return false;
	}
	public boolean executeBlock(int pMode) {
		return executeBlock(pMode, owner.maidTile[0], owner.maidTile[1], owner.maidTile[2]);
	}

	/**
	 * AI・ｽ・ｽ・ｽs・ｽ・ｽ・ｽﾉ呼ばゑｿｽ・ｽB
	 */
	public void startBlock(int pMode) {
	}

	/**
	 * AI・ｽI・ｽ・ｽ・ｽ・ｽ・ｽﾉ呼ばゑｿｽ・ｽB
	 */
	public void resetBlock(int pMode) {
	}

	/**
	 * ・ｽp・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽs・ｽ・ｽ・ｽ・ｽ・ｽﾉ呼ばゑｿｽ・ｽB
	 */
	public void updateBlock() {
	}


	/**
	 * ・ｽﾆ趣ｿｽ・ｽ・ｽ・ｽG・ｽ・ｽ・ｽ・ｽ・ｽﾌ使・ｽp・ｽL・ｽ・ｽ
	 */
	public boolean isSearchEntity() {
		return false;
	}

	/**
	 * ・ｽﾆ趣ｿｽ・ｽ・ｽ・ｽG・ｽ・ｽ・ｽ・ｽ
	 */
	public boolean checkEntity(int pMode, Entity pEntity) {
		return false;
	}

	/**
	 * ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽp
	 */
	public int colorMultiplier(float pLight, float pPartialTicks) {
		return 0;
	}
	
	/**
	 * ・ｽ・ｽ_・ｽ・ｽ・ｽ・ｽ・ｽﾌ擾ｿｽ・ｽ・ｽ・ｽP・ｽB
	 * 0・ｽﾈ擾ｿｽ・ｽﾔゑｿｽ・ｽﾆ擾ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽB
	 * 1:false・ｽﾅ鯉ｿｽ・ｽﾌ擾ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽI・ｽ・ｽ・ｽ・ｽ・ｽ・ｽB
	 * 2:true・ｽﾅ鯉ｿｽ・ｽﾌ擾ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽI・ｽ・ｽ・ｽ・ｽ・ｽ・ｽB
	 */
	public float attackEntityFrom(DamageSource par1DamageSource, float par2) {
		return 0;
	}
	/**
	 * ・ｽ・ｽ_・ｽ・ｽ・ｽ・ｽ・ｽﾌ擾ｿｽ・ｽ・ｽ・ｽQ・ｽB
	 * true・ｽ・ｽﾔゑｿｽ・ｽﾆ擾ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽB
	 */
	public boolean damageEntity(int pMode, DamageSource par1DamageSource, float par2) {
		return false;
	}

	/**
	 * ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽg・ｽ・ｽ・ｽﾄゑｿｽ・ｽ・ｽTile・ｽﾈゑｿｽTrue・ｽ・ｽﾔゑｿｽ・ｽB
	 */
	public boolean isUsingTile(TileEntity pTile) {
		return false;
	}

	/**
	 * ・ｽ・ｽ・ｽ・ｽ・ｽﾄゑｿｽTile・ｽ・ｽﾔゑｿｽ・ｽB
	 */
	public List<TileEntity> getTiles() {
		return null;
	}

	/**
	 * do1:・ｽ・ｽ・ｽ・ｽ・ｽ阡ｻ・ｽ・ｽﾌチ・ｽF・ｽb・ｽN
	 * do2:・ｽ寬橸ｿｽu・ｽ・ｽ・ｽN・ｽ・ｽ・ｽ・ｽA・ｽ・ｽ・ｽﾟ費ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ阡ｻ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽB
	 */
	protected boolean canBlockBeSeen(int pX, int pY, int pZ, boolean toTop, boolean do1, boolean do2) {
		// ・ｽu・ｽ・ｽ・ｽb・ｽN・ｽﾌ可趣ｿｽ・ｽ・ｽ・ｽ・ｽ
		World worldObj = owner.worldObj;
		Block lblock = Block.blocksList[worldObj.getBlockId(pX, pY, pZ)];
		if (lblock == null) {
			mod_LMM_littleMaidMob.Debug("block-null: %d, %d, %d", pX, pY, pZ);
			return false;
		}
		lblock.setBlockBoundsBasedOnState(worldObj, pX, pY, pZ);
		
		Vec3 vec3do = Vec3.createVectorHelper(owner.posX, owner.posY + owner.getEyeHeight(), owner.posZ);
		Vec3 vec3dt = Vec3.createVectorHelper((double)pX + 0.5D, (double)pY + ((lblock.getBlockBoundsMaxY() + lblock.getBlockBoundsMinY()) * (toTop ? 0.9D : 0.5D)), (double)pZ + 0.5D);
		MovingObjectPosition movingobjectposition = worldObj.rayTraceBlocks_do_do(vec3do, vec3dt, do1, do2);
		
		if (movingobjectposition != null && movingobjectposition.typeOfHit == EnumMovingObjectType.TILE) {
			// ・ｽﾚ触・ｽu・ｽ・ｽ・ｽb・ｽN・ｽ・ｽ・ｽw・ｽ閧ｵ・ｽ・ｽ・ｽ・ｽ・ｽﾌなゑｿｽ・ｽ
			if (movingobjectposition.blockX == pX && 
					movingobjectposition.blockY == pY &&
					movingobjectposition.blockZ == pZ) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ・ｽ・ｽﾆの具ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽB
	 * @param pIndex
	 * 0:minRange;
	 * 1:maxRange;
	 * @return
	 */
	public double getRangeToMaster(int pIndex) {
		return pIndex == 0 ? 36D : pIndex == 1 ? 25D : 0D;
	}

	/**
	 * ・ｽU・ｽ・ｽ・ｽ・ｽﾉタ・ｽ[・ｽQ・ｽb・ｽg・ｽ・ｽ・ｽﾄ設定さ・ｽ・ｽ・ｽ驍ｩ・ｽﾌ指・ｽ・ｽB
	 * @param pTarget
	 * @return
	 */
	public boolean isChangeTartget(Entity pTarget) {
		return !owner.isBloodsuck();
	}

}
