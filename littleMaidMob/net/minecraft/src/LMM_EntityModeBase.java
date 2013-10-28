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
 * LMM�p�Ǝ�AI�����Ɏg�p�B
 * ���̌p���N���X��AI�����Ƃ��ēn�����Ƃ��ł���B
 * �܂��AAI�����I�𒆂͓���̊֐��������đI�𒆂̃N���X�݂̂����������B
 * �C���X�^���X�����鎖�ɂ�胍�[�J���ϐ���ێ��B
 */
public abstract class LMM_EntityModeBase {

	public final LMM_EntityLittleMaid owner;


	/**
	 * ����
	 */
	public LMM_EntityModeBase(LMM_EntityLittleMaid pEntity) {
		owner = pEntity;
	}

	public int fpriority;
	/**
	 * �D�揇�ʁB
	 * �ԍ����Ⴂ�ق�����ɏ��������B
	 * ���񌅂�00�̂��̂̓V�X�e���\��B
	 */
	public abstract int priority();

	/**
	 * �N�����̏���B
	 */
	public void init() {
	}

	/**
	 * Entity�����̎��s��
	 */
	public void initEntity() {
	}

	/**
	 * ���[�h�̒ǉ��B
	 */
	public abstract void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting);

	/**
	 * �Ǝ��f�[�^�ۑ��p�B
	 */
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
	}
	/**
	 * �Ǝ��f�[�^�Ǎ��p�B
	 */
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
	}

	/**
	 * renderSpecial�̒ǉ������p�B
	 */
	public void showSpecial(LMM_RenderLittleMaid prenderlittlemaid, double px, double py, double pz) {
	}

	/**
	 * �T�[�o�[���݂̖̂��������B
	 * AI�����̌�̕�ɌĂ΂��B
	 */
	public void updateAITick(int pMode) {
	}

	/**
	 * ���������B
	 * ���̏����̑O�ɌĂ΂��
	 */
	public void onUpdate(int pMode) {
	}

	/**
	 * ���̂ւ�̏����͎኱���Ԃ������Ă��ǂ��B
	 * ���̃A�C�e�����g�p���������B
	 * �⊮�����ɐ�񂶂Ď��s�����A���̑��蔻����S�����������B
	 */
	public boolean preInteract(EntityPlayer pentityplayer, ItemStack pitemstack) {
		return false;
	}
	/**
	 * ���̂ւ�̏����͎኱���Ԃ������Ă��ǂ��B
	 * ���̃A�C�e�����g�p���������B
	 */
	public boolean interact(EntityPlayer pentityplayer, ItemStack pitemstack) {
		return false;
	}

	/**
	 * �����Ń��[�h�`�F���W�������B
	 */
	public boolean changeMode(EntityPlayer pentityplayer) {
		return false;
	}

	/**
	 * ���[�h�`�F���W���̐ݒ菈���̖{�́B
	 * �������ɏ����������Ȃ��ƃ��[�h���ɂ��������Ȃ邩���H
	 */
	public boolean setMode(int pMode) {
		return false;
	}

	/**
	 * �g�p�A�C�e���̑I���B
	 * �߂�l�̓X���b�g�ԍ�
	 */
	public int getNextEquipItem(int pMode) {
		// ���I��
		return -1;
	}
	
	/**
	 * �A�C�e�����ۂ̔��莮�B
	 * �E���ɍs���A�C�e���̔���B
	 */
	public boolean checkItemStack(ItemStack pItemStack) {
		// ���ΏۃA�C�e���̐ݒ�Ȃ�
		return false;
	}

	/**
	 * �U�����菈���B
	 * ����ȍU������͂����Ŏ����B
	 */
	public boolean attackEntityAsMob(int pMode, Entity pEntity) {
		// ����U���̐ݒ�Ȃ�
		return false;
	}

	/**
	 * �u���b�N�̃`�F�b�N��������邩�ǂ����B
	 * ���莮�̂ǂ�����g����������őI���B
	 */
	public boolean isSearchBlock() {
		return false;
	}

	/**
	 * isSearchBlock=false�̂Ƃ��ɔ��肳���B
	 */
	public boolean shouldBlock(int pMode) {
		return false;
	}

	/**
	 * �T�����߂��u���b�N�ł��邩�B
	 * true��Ԃ��ƌ����I���B
	 */
	public boolean checkBlock(int pMode, int px, int py, int pz) {
		return false;
	}

	/**
	 * �����͈͂ɍ��G�Ώۂ��Ȃ������B
	 */
	public boolean overlooksBlock(int pMode) {
		return false;
	}
//	@Deprecated
//	public TileEntity overlooksBlock(int pMode) {
//		return null;
//	}

	/**
	 * ���E�����𒴂������̏���
	 */
	public void farrangeBlock() {
		owner.getNavigator().clearPathEntity();
	}

	/**
	 * �L��˒������𒴂������̏���
	 */
	public boolean outrangeBlock(int pMode, int pX, int pY, int pZ) {
		return owner.getNavigator().tryMoveToXYZ(pX, pY, pZ, 1.0F);
	}
	public boolean outrangeBlock(int pMode) {
		return outrangeBlock(pMode, owner.maidTile[0], owner.maidTile[1], owner.maidTile[2]);
	}

	/**
	 * �˒������ɓ��������s�����B
	 * �߂�l��true�̎��͏I�������ɓ���p��
	 */
	public boolean executeBlock(int pMode, int px, int py, int pz) {
		return false;
	}
	public boolean executeBlock(int pMode) {
		return executeBlock(pMode, owner.maidTile[0], owner.maidTile[1], owner.maidTile[2]);
	}

	/**
	 * AI���s���ɌĂ΂��B
	 */
	public void startBlock(int pMode) {
	}

	/**
	 * AI�I�����ɌĂ΂��B
	 */
	public void resetBlock(int pMode) {
	}

	/**
	 * �p��������s�����ɌĂ΂��B
	 */
	public void updateBlock() {
	}


	/**
	 * �Ǝ����G�����̎g�p�L��
	 */
	public boolean isSearchEntity() {
		return false;
	}

	/**
	 * �Ǝ����G����
	 */
	public boolean checkEntity(int pMode, Entity pEntity) {
		return false;
	}

	/**
	 * ���������p
	 */
	public int colorMultiplier(float pLight, float pPartialTicks) {
		return 0;
	}
	
	/**
	 * ��_�����̏����P�B
	 * 0�ȏ��Ԃ��Ə�����������B
	 * 1:false�Ō��̏������I������B
	 * 2:true�Ō��̏������I������B
	 */
	public float attackEntityFrom(DamageSource par1DamageSource, float par2) {
		return 0;
	}
	/**
	 * ��_�����̏����Q�B
	 * true��Ԃ��Ə�����������B
	 */
	public boolean damageEntity(int pMode, DamageSource par1DamageSource, float par2) {
		return false;
	}

	/**
	 * �������g���Ă���Tile�Ȃ�True��Ԃ��B
	 */
	public boolean isUsingTile(TileEntity pTile) {
		return false;
	}

	/**
	 * �����Ă�Tile��Ԃ��B
	 */
	public List<TileEntity> getTiles() {
		return null;
	}

	/**
	 * do1:�����蔻��̃`�F�b�N
	 * do2:�펞�u���N����A���ߔ���������蔻��������B
	 */
	protected boolean canBlockBeSeen(int pX, int pY, int pZ, boolean toTop, boolean do1, boolean do2) {
		// �u���b�N�̉�����
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
			// �ڐG�u���b�N���w�肵�����̂Ȃ��
			if (movingobjectposition.blockX == pX && 
					movingobjectposition.blockY == pY &&
					movingobjectposition.blockZ == pZ) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ��Ƃ̋������B
	 * @param pIndex
	 * 0:minRange;
	 * 1:maxRange;
	 * @return
	 */
	public double getRangeToMaster(int pIndex) {
		return pIndex == 0 ? 36D : pIndex == 1 ? 25D : 0D;
	}

	/**
	 * �U����Ƀ^�[�Q�b�g���Đݒ肳���邩�̎w��B
	 * @param pTarget
	 * @return
	 */
	public boolean isChangeTartget(Entity pTarget) {
		return !owner.isBloodsuck();
	}

}
