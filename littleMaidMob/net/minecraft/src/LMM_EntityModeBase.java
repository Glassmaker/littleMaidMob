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
 * LMM用独自AI処理に使用
 * この継承クラスをAI処理として渡すことができる。
 * また、AI処理選択中は特定の関数を除いて選択中のクラスのみが処理される。
 * インスタンス化する事によりローカル変数を保持。
 * Used for LMM for its own AI processing
 * It can be passed as AI processing the inherited class.
 * In addition, only the selected class is processed, except for the function of specific AI processing selected.
 * To keep a local variable by instantiating.
 */
public abstract class LMM_EntityModeBase {

	public final LMM_EntityLittleMaid owner;


	/**
	 * 初期化
	 * Initialization
	 */
	public LMM_EntityModeBase(LMM_EntityLittleMaid pEntity) {
		owner = pEntity;
	}

	public int fpriority;
	/**
	 * 優先順位。
	 * 番号が若いほうが先に処理される。
	 * 下二桁が00のものはシステム予約。
	 * Priorities.
	 * More young number is processed first.
	 * The last two digits are reserved for the system of 00 things.
	 */
	public abstract int priority();

	/**
	 * 起動時の初期化。
	 * Initialization at startup.
	 */
	public void init() {
	}

	/**
	 * Entity初期化時の実行部
	 * Execution of the Entity initialization
	 */
	public void initEntity() {
	}

	/**
	 * モードの追加。
	 * Add mode.
	 */
	public abstract void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting);

	/**
	 * 独自データ保存用。
	 * Own data storage.
	 */
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
	}
	/**
	 * 独自データ読込用。
	 * Own data for reading.
	 */
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
	}

	/**
	 * renderSpecialの追加実装用。
	 * Additional implementation for renderSpecial.
	 */
	public void showSpecial(LMM_RenderLittleMaid prenderlittlemaid, double px, double py, double pz) {
	}

	/**
	 * サーバー側のみの毎時処理。
	 * AI処理の後の方に呼ばれる。
	 * Hourly processing server-side only.
	 * Called in later in the AI process.
	 */
	public void updateAITick(int pMode) {
	}

	/**
	 * 毎時処理。
	 * 他の処理の前に呼ばれる
	 * Per hour processing.
	 * It is called before processing other
	 */
	public void onUpdate(int pMode) {
	}

	/**
	 * このへんの処理は若干時間かかっても良し。
	 * 他のアイテムを使用したい時。
	 * 補完処理に先んじて実行される、その代わり判定も全部自分持ち。
	 * Processing of this neighborhood is good even if it takes some time.
	 * If you want to use other items.
	 * Is carried out ahead of the completion process, but instead also determined their possession all.
	 */
	public boolean preInteract(EntityPlayer pentityplayer, ItemStack pitemstack) {
		return false;
	}
	/**
	 * このへんの処理は若干時間かかっても良し。
	 * 他のアイテムを使用したい時。
	 * Processing of this neighborhood is good even if it takes some time.
	 * If you want to use other items.
	 */
	public boolean interact(EntityPlayer pentityplayer, ItemStack pitemstack) {
		return false;
	}

	/**
	 * 砂糖でモードチェンジした時。
	 * When you change mode in sugar.
	 */
	public boolean changeMode(EntityPlayer pentityplayer) {
		return false;
	}

	/**
	 * モードチェンジ時の設定処理の本体。
	 * こっちに処理を書かないとロード時におかしくなるかも？
	 * The body of the process of setting the mode change time.
	 * The cause odd at load time if you do not write a treatment over here?
	 */
	public boolean setMode(int pMode) {
		return false;
	}

	/**
	 * 使用アイテムの選択。
	 * 戻り値はスロット番号
	 * Selection of the item.
	 * The return value is the slot number
	 */
	public int getNextEquipItem(int pMode) {
		// 未選択, Not selected
		return -1;
	}
	
	/**
	 * アイテム回収可否の判定式。
	 * 拾いに行くアイテムの判定。
	 * Discriminants item recovery propriety.
	 * Judgment of the items that go to pick up.
	 */
	public boolean checkItemStack(ItemStack pItemStack) {
		// 回収対象アイテムの設定なし, No set of recalled items
		return false;
	}

	/**
	 * 攻撃判定処理。
	 * 特殊な攻撃動作はここで実装。
	 * Attack determination process.
	 * Special attack operation implemented here.
	 */
	public boolean attackEntityAsMob(int pMode, Entity pEntity) {
		// 特殊攻撃の設定なし, No set of special attacks
		return false;
	}

	/**
	 * ブロックのチェック判定をするかどうか。
	 * 判定式のどちらを使うかをこれで選択。
	 * You if you want to check the decision of block.
	 * Can be selected by this use either of the judgment formula.
	 */
	public boolean isSearchBlock() {
		return false;
	}

	/**
	 * isSearchBlock=falseのときに判定される。
	 * it is determined at the time of isSearchBlock = false.
	 */
	public boolean shouldBlock(int pMode) {
		return false;
	}

	/**
	 * 探し求めたブロックであるか。
	 * trueを返すと検索終了。
	 * Or a block sought.
	 * Search and end return true.
	 */
	public boolean checkBlock(int pMode, int px, int py, int pz) {
		return false;
	}

	/**
	 * 検索範囲に索敵対象がなかった。
	 * There was no searching for the enemy target in the search range.
	 */
	public boolean overlooksBlock(int pMode) {
		return false;
	}
//	@Deprecated
//	public TileEntity overlooksBlock(int pMode) {
//		return null;
//	}

	/**
	 * 限界距離を超えた時の処理
	 * Process when it exceeds the limit distance
	 */
	public void farrangeBlock() {
		owner.getNavigator().clearPathEntity();
	}

	/**
	 * 有効射程距離を超えた時の処理
	 * Process when it exceeds the effective firing range
	 */
	public boolean outrangeBlock(int pMode, int pX, int pY, int pZ) {
		return owner.getNavigator().tryMoveToXYZ(pX, pY, pZ, 1.0F);
	}
	public boolean outrangeBlock(int pMode) {
		return outrangeBlock(pMode, owner.maidTile[0], owner.maidTile[1], owner.maidTile[2]);
	}

	/**
	 * 射程距離に入ったら実行される。
	 * 戻り値がtrueの時は終了せずに動作継続
	 * It is executed Once inside the firing range.
	 * The return value is to continue operation without end when the true
	 */
	public boolean executeBlock(int pMode, int px, int py, int pz) {
		return false;
	}
	public boolean executeBlock(int pMode) {
		return executeBlock(pMode, owner.maidTile[0], owner.maidTile[1], owner.maidTile[2]);
	}

	/**
	 * AI実行時に呼ばれる。
	 * Referred to AI runtime.
	 */
	public void startBlock(int pMode) {
	}

	/**
	 * AI終了時に呼ばれる。
	 * The called AI end.
	 */
	public void resetBlock(int pMode) {
	}

	/**
	 * 継続判定を行う時に呼ばれる。
	 * It is called when performing the continuation decision.
	 */
	public void updateBlock() {
	}


	/**
	 * 独自索敵処理の使用有無
	 * Use or non-use of its own search operation processing
	 */
	public boolean isSearchEntity() {
		return false;
	}

	/**
	 * 独自索敵処理
	 * Own search operation processing
	 */
	public boolean checkEntity(int pMode, Entity pEntity) {
		return false;
	}

	/**
	 * 発光処理用
	 * Emission processing
	 */
	public int colorMultiplier(float pLight, float pPartialTicks) {
		return 0;
	}
	
	/**
	 * 被ダメ時の処理１。
	 * 0以上を返すと処理を乗っ取る。
	 * 1:falseで元の処理を終了する。
	 * 2:trueで元の処理を終了する。
	 * Processing one of the bad time.
	 * Take over the process and returns 0 or more.
	 * 1: I will end the processing of original false.
	 * 2: I will end the processing of original true.
	 */
	public float attackEntityFrom(DamageSource par1DamageSource, float par2) {
		return 0;
	}
	/**
	 * 被ダメ時の処理２。
	 * trueを返すと処理を乗っ取る。
	 * Processing two of the bad time.
	 * take over the process and return true.
	 */
	public boolean damageEntity(int pMode, DamageSource par1DamageSource, float par2) {
		return false;
	}

	/**
	 * 自分が使っているTileならTrueを返す。
	 * Returns True if Tile which you are using.
	 */
	public boolean isUsingTile(TileEntity pTile) {
		return false;
	}

	/**
	 * 持ってるTileを返す。
	 * I return the Tile have.
	 */
	public List<TileEntity> getTiles() {
		return null;
	}

	/**
	 * do1:当たり判定のチェック
	 * do2:常時ブロク判定、透過判定も当たり判定も無視。
	 * do1: Check per decision
	 * do2: Brok always determines, hit also determined transparent decision or ignored.
	 */
	protected boolean canBlockBeSeen(int pX, int pY, int pZ, boolean toTop, boolean do1, boolean do2) {
		// ブロックの可視判定, Visible decision of block
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
			// 接触ブロックが指定したものならば, If those specified contact block
			if (movingobjectposition.blockX == pX && 
					movingobjectposition.blockY == pY &&
					movingobjectposition.blockZ == pZ) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 主との距離感。
	 * Sense of distance with the Master.
	 * @param pIndex
	 * 0:minRange;
	 * 1:maxRange;
	 * @return
	 */
	public double getRangeToMaster(int pIndex) {
		return pIndex == 0 ? 36D : pIndex == 1 ? 25D : 0D;
	}

	/**
	 * 攻撃後にターゲットを再設定させるかの指定。
	 * Specify whether to reset the target after the attack.
	 * @param pTarget
	 * @return
	 */
	public boolean isChangeTartget(Entity pTarget) {
		return !owner.isBloodsuck();
	}

}
