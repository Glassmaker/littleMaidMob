package net.minecraft.src;

import static net.minecraft.src.LMM_Statics.dataWatch_Absoption;
import static net.minecraft.src.LMM_Statics.dataWatch_Color;
import static net.minecraft.src.LMM_Statics.dataWatch_DominamtArm;
import static net.minecraft.src.LMM_Statics.dataWatch_ExpValue;
import static net.minecraft.src.LMM_Statics.dataWatch_Flags;
import static net.minecraft.src.LMM_Statics.dataWatch_Flags_Aimebow;
import static net.minecraft.src.LMM_Statics.dataWatch_Flags_Bloodsuck;
import static net.minecraft.src.LMM_Statics.dataWatch_Flags_Freedom;
import static net.minecraft.src.LMM_Statics.dataWatch_Flags_LooksSugar;
import static net.minecraft.src.LMM_Statics.dataWatch_Flags_OverDrive;
import static net.minecraft.src.LMM_Statics.dataWatch_Flags_Tracer;
import static net.minecraft.src.LMM_Statics.dataWatch_Flags_Wait;
import static net.minecraft.src.LMM_Statics.dataWatch_Flags_Working;
import static net.minecraft.src.LMM_Statics.dataWatch_Flags_looksWithInterest;
import static net.minecraft.src.LMM_Statics.dataWatch_Flags_looksWithInterestAXIS;
import static net.minecraft.src.LMM_Statics.dataWatch_Flags_remainsContract;
import static net.minecraft.src.LMM_Statics.dataWatch_Free;
import static net.minecraft.src.LMM_Statics.dataWatch_Gotcha;
import static net.minecraft.src.LMM_Statics.dataWatch_ItemUse;
import static net.minecraft.src.LMM_Statics.dataWatch_Mode;
import static net.minecraft.src.LMM_Statics.dataWatch_Parts;
import static net.minecraft.src.LMM_Statics.dataWatch_Texture;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITaskEntry;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeInstance;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet41EntityEffect;
import net.minecraft.network.packet.Packet42RemoveEntityEffect;
import net.minecraft.network.packet.Packet5PlayerInventory;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
 
public class LMM_EntityLittleMaid extends EntityTameable implements MMM_ITextureEntity {

	// 窶凖ｨ・ｽ窶昶堙拘tatics窶堙麺・壺慊ｮ
//	protected static final UUID maidUUID = UUID.nameUUIDFromBytes("net.minecraft.src.littleMaidMob".getBytes());
	protected static final UUID maidUUID = UUID.fromString("e2361272-644a-3028-8416-8536667f0efb");
	protected static AttributeModifier attCombatSpeed = (new AttributeModifier(maidUUID, "Combat speed boost", 0.07D, 0)).setSaved(false);
	protected static AttributeModifier attAxeAmp = (new AttributeModifier(maidUUID, "Axe Attack boost", 0.5D, 1)).setSaved(false);


	// 窶｢ﾃ擾ｿｽ窶敘陳ｸ窶堙ｧ窶堋ｵ窶堋ｽ窶堋｢窶堙遺塲ｸ
//    protected long maidContractLimit;		// ﾅ胆窶禿ｱﾅｽﾂｸﾅ津ｸ窶愿ｺ
	protected int maidContractLimit;		// ﾅ胆窶禿ｱﾅﾃｺﾅﾃ・
	protected long maidAnniversary;			// ﾅ胆窶禿ｱ窶愿ｺUID窶堙・堋ｵ窶堙・ｽg窶廃
	protected int maidDominantArm;			// 窶藩懌堋ｫﾋ徨・ｽA1Byte
	/** ﾆ弾ﾆ誰ﾆ湛ﾆ蛋ﾆ槌椎ﾃ麺廣窶堙姑断・ｽ[ﾆ耽窶堙ｰﾅﾃ・費ｿｽ **/
	public MMM_TextureData textureData;
	public Map<String, MMM_EquippedStabilizer> maidStabilizer = new HashMap<String, MMM_EquippedStabilizer>();
	
	
	public LMM_InventoryLittleMaid maidInventory;
	public LMM_EntityLittleMaidAvatar maidAvatar;
	public LMM_EntityCaps maidCaps;	// Client窶伉､窶堙娯堙・
	
	public List<LMM_EntityModeBase> maidEntityModeList;
	public Map<Integer, EntityAITasks[]> maidModeList;
	public Map<String, Integer> maidModeIndexList;
	public int maidMode;		// 2Byte
//	public int maidColor;		// 1Byte
//	public boolean maidContract;
	public boolean maidTracer;
	public boolean maidFreedom;
	public boolean maidWait;
	public int homeWorld;
	protected int maidTiles[][] = new int[9][3];
	public int maidTile[] = new int[3];
	public TileEntity maidTileEntity;
	
	// 窶慊ｮ窶廬窶堙茨ｿｽﾃｳ窶佚・
	protected EntityPlayer mstatMasterEntity;	// ﾅｽﾃ･
	protected double mstatMasterDistanceSq;		// ﾅｽﾃ･窶堙・堙娯ｹ窶披板｣・ｽAﾅ致ﾅｽZﾅ馳窶氾岩ｰﾂｻ窶廃
	protected Entity mstatgotcha;				// ﾆ抵ｿｽﾆ辰ﾆ停橸ｿｽ[ﾆ檀窶廃
	protected boolean mstatBloodsuck;
	protected boolean mstatClockMaid;
	// ﾆ筑ﾆ湛ﾆ誰窶敖ｻ窶凖ｨ
	protected int mstatMaskSelect;
	// 窶凖・ｰﾃ≫堙娯慊ｪ窶｢窶昶倪｢窶敕ｵ
	protected boolean mstatCamouflage;
	protected boolean mstatPlanter;
//	protected boolean isMaidChaseWait;
	protected int mstatWaitCount;
	protected int mstatTime;
	protected MMM_Counter maidOverDriveTime;
	protected boolean mstatFirstLook;
	protected boolean mstatLookSuger;
	protected MMM_Counter mstatWorkingCount;
	protected int mstatPlayingRole;
	protected int mstatWorkingInt;
	protected String mstatModeName;
	protected boolean mstatOpenInventory;
	// ﾋ徨・ｽU窶堙ｨ
	public LMM_SwingStatus mstatSwingStatus[]; 
	public boolean mstatAimeBow;
	// ﾅｽﾃｱﾅｽﾃｼ窶堙ｨ
	private boolean looksWithInterest;
	private boolean looksWithInterestAXIS;
	private float rotateAngleHead;			// Angle
	private float prevRotateAngleHead;		// prevAngle

	/**
	 * ﾅ津や佚娯堋ｲ窶堙・堙俄冤窶堙ｰﾆ弛ﾆ停ｰ窶堙や堋ｩ窶堋ｹ窶堙ｩ窶堙娯堙嫁ｽg窶堋､・ｽB
	 */
	public float entityIdFactor;
	
	public boolean weaponFullAuto;	// 窶倪｢窶敕ｵ窶堋ｪﾆ稚ﾆ停ｹﾆ棚・ｽ[ﾆ暖窶｢・ｽﾅﾃｭ窶堋ｩ窶堙・堋､窶堋ｩ
	public boolean weaponReload;	// 窶倪｢窶敕ｵ窶堋ｪﾆ椎ﾆ抵ｿｽ・ｽ[ﾆ檀窶堙ｰ窶梅窶堋ｵ窶堙・堋｢窶堙ｩ窶堋ｩ窶堙・堋､窶堋ｩ
	public boolean maidCamouflage;
	
	
	// 窶ｰﾂｹ・ｽﾂｺ
//	protected LMM_EnumSound maidAttackSound;
	protected LMM_EnumSound maidDamegeSound;
	protected int maidSoundInterval;
	protected float maidSoundRate;
	
	// ﾅｽﾃﾅ陳ｱ窶廃
	private int firstload = 1;
	public String statusMessage = "";
	
	
	// AI
	public EntityAITempt aiTempt;
	public LMM_EntityAIBeg aiBeg;
	public LMM_EntityAIBegMove aiBegMove;
	public EntityAIOpenDoor aiOpenDoor;
	public EntityAIRestrictOpenDoor aiCloseDoor;
	public LMM_EntityAIAvoidPlayer aiAvoidPlayer;
	public LMM_EntityAIFollowOwner aiFollow;
	public LMM_EntityAIAttackOnCollide aiAttack;
	public LMM_EntityAIAttackArrow aiShooting;
	public LMM_EntityAICollectItem aiCollectItem;
	public LMM_EntityAIRestrictRain aiRestrictRain;
	public LMM_EntityAIFleeRain aiFreeRain;
	public LMM_EntityAIWander aiWander;
	public LMM_EntityAIJumpToMaster aiJumpTo;
	public LMM_EntityAIFindBlock aiFindBlock;
	public LMM_EntityAITracerMove aiTracer;
	public EntityAISwimming aiSwiming;
	public EntityAIPanic aiPanic;
	// ActiveModeClass
	protected LMM_EntityModeBase maidActiveModeClass;
	public Profiler aiProfiler;


	public LMM_EntityLittleMaid(World par1World) {
		super(par1World);
		// ・ｽ窶ｰﾅﾃｺ・ｽﾃ昶凖ｨ
		maidInventory = new LMM_InventoryLittleMaid(this);
		if (par1World != null ) {
			maidAvatar = new LMM_EntityLittleMaidAvatar(par1World, this);
		}
		mstatOpenInventory = false;
//		isMaidChaseWait = false;
		mstatTime = 6000;
		maidOverDriveTime = new MMM_Counter(5, 300, -100);
		mstatWorkingCount = new MMM_Counter(11, 10, -10);
		
		// ﾆ停堡断ﾆ停ｹﾆ椎槌停愴胆ﾆ椎ﾆ停愴丹窶廃窶堙姑稚ﾆ停ｰﾆ丹ﾅl窶慊ｾ窶廃ﾆ蜘ﾆ停ｹﾆ恥・ｽ[ﾅﾃ厄ｿｽ窶・
		maidCaps = new LMM_EntityCaps(this);
		
		// ﾅ蛋窶佚版蛋・ｽﾂｬ・ｽﾃｪ
		textureData = new MMM_TextureData(this, maidCaps);
		textureData.setColor(12);
		MMM_TextureBox ltb[] = new MMM_TextureBox[2];
		ltb[0] = ltb[1] = MMM_TextureManager.instance.getDefaultTexture(this);
		setTexturePackName(ltb);
		
		entityIdFactor = (float)(entityId * 70);
		// ﾋ徨・ｽU窶堙ｨ
		mstatSwingStatus = new LMM_SwingStatus[] { new LMM_SwingStatus(), new LMM_SwingStatus()};
		setDominantArm(rand.nextInt(mstatSwingStatus.length));
		
		// ・ｽﾃ・ｿｽﾂｶ窶ｰﾂｹ・ｽﾂｺ
//		maidAttackSound = LMM_EnumSound.attack;
		maidDamegeSound = LMM_EnumSound.hurt;
		maidSoundInterval = 0;
		
		// 窶禿ｬ・ｽﾂｶﾅｽﾃｭ窶廃・ｽ窶ｰﾅﾃｺ窶冤・ｽﾃ昶凖ｨ
		setHealth(15F);
		
		// ﾋ・壺慊ｮ窶廃ﾆ稚ﾆ達ﾆ淡ﾆ谷ﾆ停ｹ・ｽﾃ昶凖ｨ
		getNavigator().setAvoidsWater(true);
		getNavigator().setBreakDoors(true);
		
		
		// TODO:窶堋ｱ窶堙ｪ窶堙哉弾ﾆ湛ﾆ暖
//		maidStabilizer.put("HeadTop", MMM_StabilizerManager.getStabilizer("WitchHat", "HeadTop"));
		
		
		
		// EntityMode窶堙娯凖・ｰﾃ・
		maidEntityModeList = LMM_EntityModeManager.getModeList(this);
		// ﾆ停夲ｿｽ[ﾆ檀ﾆ椎ﾆ湛ﾆ暖
		maidActiveModeClass = null;
		maidModeList = new HashMap<Integer, EntityAITasks[]>();
		maidModeIndexList = new HashMap<String, Integer>();
		initModeList();
		mstatModeName = "";
		maidMode = 65535;
		// ・ｽ窶ｰﾅﾃｺ窶ｰﾂｻﾅｽﾅｾﾅｽﾃ・ｽsﾆ坦・ｽ[ﾆ檀
		for (LMM_EntityModeBase lem : maidEntityModeList) {
			lem.initEntity();
		}
	}

	@Override
	public EntityLivingData onSpawnWithEgg(EntityLivingData par1EntityLivingData) {
		// ﾆ弾ﾆ誰ﾆ湛ﾆ蛋ﾆ槌抵ｿｽ[窶堙ｰﾆ停ｰﾆ停愴胆ﾆ停ぎ窶堙・露窶佚ｰ
		String ls;
		if (mod_LMM_littleMaidMob.cfg_defaultTexture.isEmpty()) {
			ls = MMM_TextureManager.instance.getRandomTextureString(rand);
		} else {
			ls = mod_LMM_littleMaidMob.cfg_defaultTexture;
		}
		textureData.setTextureInitServer(ls);
		mod_LMM_littleMaidMob.Debug("init-ID:%d, %s:%d", entityId, textureData.textureBox[0].textureName, textureData.getColor());
		setTexturePackIndex(textureData.getColor(), textureData.textureIndex);
		setMaidMode("Wild");
		return super.onSpawnWithEgg(par1EntityLivingData);
	}

	protected void func_110147_ax() {
		// ・ｽ窶ｰﾅﾃｺﾆ恥ﾆ停ｰﾆ抵ｿｽ・ｽ[ﾆ耽・ｽ[
		super.applyEntityAttributes();
		// 窶佚趣ｿｽﾃ嵳・壺慊ｮ窶ｰﾃや拿窶敕才・・
		getEntityAttribute(SharedMonsterAttributes.followRange).setAttribute(20.0D);
		// ﾅﾃｮ窶怒ﾋ・壺慊ｮ窶伉ｬ窶忸
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.23000000417232513D);
		// 窶｢W・ｽ竄ｬ・ｽUﾅ停壺氾坂啀
		getAttributeMap().func_111150_b(SharedMonsterAttributes.attackDamage).setAttribute(1.0D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		/*
		 * DataWatcher窶堙哉誰ﾆ停ｰﾆ辰ﾆ但ﾆ停愴暖窶堋ｩ窶堙ｧﾆ探・ｽ[ﾆ弛・ｽ[窶堙問堙坂冤窶堙ｰ窶從窶堋ｳ窶堙遺堋｢・ｽA窶從窶堋ｹ窶堙遺堋｢・ｽB
		 */
		
		// ﾅｽg窶廃窶吮ﾆ椎ﾆ湛ﾆ暖
		// 0:Flags
		// 1:Air
		// 2, 3, 4, 5,
		// 6: HP
		// 7, 8:PotionMap
		// 9: ArrowCount
		// 10: ﾅ津・猫窶督ｼ・ｽﾃ・
		// 11: 窶督ｼ窶｢t窶敖ｻ窶凖ｨ
		// 12: GrowingAge
		// 16: Tame(4), Sit(1) 
		// 17: ownerName
		
		// maidAvater窶廃EntityPlayerﾅ津敘ﾂｷ窶｢ﾃ擾ｿｽ窶・
		// 17 -> 18
		// 18 : Absoptionﾅ津ｸ窶ｰﾃ岩堙ｰﾆ誰ﾆ停ｰﾆ辰ﾆ但ﾆ停愴暖窶伉､窶堙問彎窶倪披堋ｷ窶堙ｩ窶堙娯堙嫁ｽg窶堋､
		dataWatcher.addObject(dataWatch_Absoption, Float.valueOf(0.0F));
		
		// 窶愿・ｽﾂｩ窶｢ﾂｪ
		// 19:maidColor
		dataWatcher.addObject(dataWatch_Color, Byte.valueOf((byte)0));
		// 20:窶露窶佚ｰﾆ弾ﾆ誰ﾆ湛ﾆ蛋ﾆ槌槌辰ﾆ停愴断ﾆ鍛ﾆ誰ﾆ湛
		dataWatcher.addObject(dataWatch_Texture, Integer.valueOf(0));
		// 21:ﾆ停堡断ﾆ停ｹﾆ恥・ｽ[ﾆ団窶堙娯｢\ﾅｽﾂｦﾆ稚ﾆ停ｰﾆ丹
		dataWatcher.addObject(dataWatch_Parts, Integer.valueOf(0));
		// 22:・ｽﾃｳ窶佚披労ﾋ・堡稚ﾆ停ｰﾆ丹ﾅ嘆(32Bit)・ｽA・ｽﾃ夲ｿｽﾃ冷堙拘taticsﾅｽQ・ｽﾃ・
		dataWatcher.addObject(dataWatch_Flags, Integer.valueOf(0));
		// 23:GotchaID
		dataWatcher.addObject(dataWatch_Gotcha, Integer.valueOf(0));
		// 24:ﾆ抵ｿｽﾆ辰ﾆ檀ﾆ停夲ｿｽ[ﾆ檀
		dataWatcher.addObject(dataWatch_Mode, Short.valueOf((short)0));
		// 25:窶藩懌堋ｫﾋ徨
		dataWatcher.addObject(dataWatch_DominamtArm, Byte.valueOf((byte)0));
		// 26:ﾆ但ﾆ辰ﾆ弾ﾆ停ぎ窶堙固ｽg窶廃窶敖ｻ窶凖ｨ
		dataWatcher.addObject(dataWatch_ItemUse, Integer.valueOf(0));
		// 27:窶｢ﾃ崘ｽ・ｽﾅ弛ﾅ陳ｱ窶冤
		dataWatcher.addObject(dataWatch_ExpValue, Integer.valueOf(0));
		
		// TODO:test
		// 31:ﾅｽﾂｩ窶燃窶｢ﾃ擾ｿｽ窶晢ｿｽAEntityMode窶懌┐窶堙・ｽg窶廃窶ｰﾃや拿窶堙遺｢ﾃ擾ｿｽ窶晢ｿｽB
		dataWatcher.addObject(dataWatch_Free, new Integer(0));
	}

	public void initModeList() {
		// AI
		aiBeg = new LMM_EntityAIBeg(this, 8F);
		aiBegMove = new LMM_EntityAIBegMove(this, 1.0F);
		aiOpenDoor = new EntityAIOpenDoor(this, true);
		aiCloseDoor = new EntityAIRestrictOpenDoor(this);
		aiAvoidPlayer = new LMM_EntityAIAvoidPlayer(this, 1.0F, 3);
		aiFollow = new LMM_EntityAIFollowOwner(this, 1.0F, 36D, 25D, 81D);
		aiAttack = new LMM_EntityAIAttackOnCollide(this, 1.0F, true);
		aiShooting = new LMM_EntityAIAttackArrow(this);
		aiCollectItem = new LMM_EntityAICollectItem(this, 1.0F);
		aiRestrictRain = new LMM_EntityAIRestrictRain(this);
		aiFreeRain = new LMM_EntityAIFleeRain(this, 1.0F);
		aiWander = new LMM_EntityAIWander(this, 1.0F);
		aiJumpTo = new LMM_EntityAIJumpToMaster(this);
		aiFindBlock = new LMM_EntityAIFindBlock(this);
		aiSwiming = new LMM_EntityAISwimming(this);
		aiPanic = new EntityAIPanic(this, 2.0F);
		aiTracer = new LMM_EntityAITracerMove(this);
		aiSit = new LMM_EntityAIWait(this);
		
		// TODO:窶堋ｱ窶堙ｪ窶堋｢窶堙ｧ窶堙遺堋ｭ窶堙具ｿｽH
		aiProfiler = worldObj != null && worldObj.theProfiler != null ? worldObj.theProfiler : null;

		// 窶慊ｮ・ｽﾃｬﾆ停夲ｿｽ[ﾆ檀窶廃窶堙卦asksList窶堙ｰ・ｽ窶ｰﾅﾃｺ窶ｰﾂｻ
		EntityAITasks ltasks[] = new EntityAITasks[2];
		ltasks[0] = new EntityAITasks(aiProfiler);
		ltasks[1] = new EntityAITasks(aiProfiler);
		
		// default
		ltasks[0].addTask(1, aiSwiming);
		ltasks[0].addTask(2, aiSit);
		ltasks[0].addTask(3, aiJumpTo);
		ltasks[0].addTask(4, aiFindBlock);
		ltasks[0].addTask(6, aiAttack);
		ltasks[0].addTask(7, aiShooting);
//		ltasks[0].addTask(8, aiPanic);
		ltasks[0].addTask(10, aiBeg);
		ltasks[0].addTask(11, aiBegMove);
		ltasks[0].addTask(20, aiAvoidPlayer);
		ltasks[0].addTask(21, aiFreeRain);
		ltasks[0].addTask(22, aiCollectItem);
		// ﾋ・壺慊ｮ窶廃AI
		ltasks[0].addTask(30, aiTracer);
		ltasks[0].addTask(31, aiFollow);
		ltasks[0].addTask(32, aiWander);
		ltasks[0].addTask(33, new EntityAILeapAtTarget(this, 0.3F));
		// Mutex窶堙娯ｰe窶ｹﾂｿ窶堋ｵ窶堙遺堋｢窶愿・ｽﾃｪ・ｽs窶慊ｮ
		ltasks[0].addTask(40, aiCloseDoor);
		ltasks[0].addTask(41, aiOpenDoor);
		ltasks[0].addTask(42, aiRestrictRain);
		// ﾅｽﾃｱ窶堙娯慊ｮ窶堋ｫ窶儕窶愿・
		ltasks[0].addTask(51, new EntityAIWatchClosest(this, EntityLivingBase.class, 10F));
		ltasks[0].addTask(52, new EntityAILookIdle(this));
		
		// 窶凖・ｰﾃ≫｢ﾂｪ
		for (LMM_EntityModeBase ieml : maidEntityModeList) {
			ieml.addEntityMode(ltasks[0], ltasks[1]);
		}
	}


	public void addMaidMode(EntityAITasks[] peaiTasks, String pmodeName, int pmodeIndex) {
		maidModeList.put(pmodeIndex, peaiTasks);
		maidModeIndexList.put(pmodeName, pmodeIndex);
	}


	public int getMaidModeInt() {
		return maidMode;
	}

	public String getMaidModeString() {
		if (!isContract()) {
			return getMaidModeString(maidMode);
		} else if (!isRemainsContract()) {
			return "Strike";
		} else if (isMaidWait()) {
			return "Wait";
		} else if (isPlaying()) {
			return "Playing";
		} else {
			String ls = getMaidModeString(maidMode);
			if (maidOverDriveTime.isEnable()) {
				ls = "D-" + ls;
			} else
			if (isTracer()) {
				ls = "T-" + ls;
			} else
			if (isFreedom()) {
				ls = "F-" + ls;
			}
			return ls;
		}
	}

	public String getMaidModeString(int pindex) {
		// ﾆ停夲ｿｽ[ﾆ檀窶督ｼ・ｽﾃ娯堙固l窶慊ｾ
		String ls = "";
		for (Entry<String, Integer> le : maidModeIndexList.entrySet()) {
			if (le.getValue() == pindex) {
				ls = le.getKey();
				break;
			}
		}
		return ls;
	}

	public boolean setMaidMode(String pname) {
		return setMaidMode(pname, false);
	}

	public boolean setMaidMode(String pname, boolean pplaying) {
		if (!maidModeIndexList.containsKey(pname)) {
			return false;
		}
		return setMaidMode(maidModeIndexList.get(pname), pplaying);
	}

	public boolean setMaidMode(int pindex) {
		return setMaidMode(pindex, false);
	}


	public boolean setMaidMode(int pindex, boolean pplaying) {
		// ﾆ停夲ｿｽ[ﾆ檀窶堙俄ｰﾅｾ窶堋ｶ窶堙БI窶堙ｰ・ｽﾃ倪堙ｨ窶佚問堋ｦ窶堙ｩ
		velocityChanged = true;
		if (!maidModeList.containsKey(pindex)) return false;
		if (maidMode == pindex) return true;
		
		if (pplaying) {
			
		} else {
			mstatWorkingInt = pindex;
		}
		mstatModeName = getMaidModeString(pindex);
		maidMode = pindex;
		dataWatcher.updateObject(dataWatch_Mode, (short)maidMode);
		EntityAITasks[] ltasks = maidModeList.get(pindex);
		
		// AI窶堙ｰ・ｽﾂｪ窶凖ｪ窶堋ｩ窶堙ｧ・ｽ窶倪堋ｫﾅﾂｷ窶堋ｦ窶堙ｩ
		if (ltasks.length > 0 && ltasks[0] != null) {
			setMaidModeAITasks(ltasks[0], tasks);
		} else {
			setMaidModeAITasks(null, tasks);
		}
		if (ltasks.length > 1 && ltasks[1] != null) {
			setMaidModeAITasks(ltasks[1], targetTasks);
		} else {
			setMaidModeAITasks(null, targetTasks);
		}

		// ﾆ停夲ｿｽ[ﾆ檀・ｽﾃ倪佚問堙俄ｰﾅｾ窶堋ｶ窶堋ｽ・ｽﾋ・費ｿｽﾅ地窶堙ｰﾅm窶｢ﾃ・
		maidAvatar.stopUsingItem();
		setSitting(false);
		setSneaking(false);
		setActiveModeClass(null);
		aiJumpTo.setEnable(true);
//		aiFollow.setEnable(true);
		aiAttack.setEnable(true);
		aiShooting.setEnable(false);
		aiAvoidPlayer.setEnable(true);
//		aiWander.setEnable(maidFreedom);
		setBloodsuck(false);
		clearTilePosAll();
		for (int li = 0; li < maidEntityModeList.size(); li++) {
			LMM_EntityModeBase iem = maidEntityModeList.get(li); 
			if (iem.setMode(maidMode)) {
				setActiveModeClass(iem);
				aiFollow.minDist = iem.getRangeToMaster(0);
				aiFollow.maxDist = iem.getRangeToMaster(1);
				break;
			}
		}
		getNextEquipItem();
		
		return true;
	}

	protected void setMaidModeAITasks(EntityAITasks pTasksSRC, EntityAITasks pTasksDEST) {
		// ﾅﾃｹ窶伉ｶ窶堙窟I窶堙ｰ・ｽﾃｭ・ｽﾅ凪堋ｵ窶堙・冰窶堋ｫﾅﾂｷ窶堋ｦ窶堙ｩ・ｽB
		// 窶慊ｮ・ｽﾃｬ窶堙ｰﾆ誰ﾆ椎ﾆ但
		try {
			ArrayList<EntityAITaskEntry> ltasksDoDEST = (ArrayList<EntityAITaskEntry>)ModLoader.getPrivateValue(EntityAITasks.class, pTasksDEST, 0);
			ArrayList<EntityAITaskEntry> ltasksExeDEST = (ArrayList<EntityAITaskEntry>)ModLoader.getPrivateValue(EntityAITasks.class, pTasksDEST, 1);
			
			if (pTasksSRC == null) {
				ltasksDoDEST.clear();
				ltasksExeDEST.clear();
			} else {
				ArrayList<EntityAITaskEntry> ltasksDoSRC = (ArrayList<EntityAITaskEntry>)ModLoader.getPrivateValue(EntityAITasks.class, pTasksSRC, 0);
				ArrayList<EntityAITaskEntry> ltasksExeSRC = (ArrayList<EntityAITaskEntry>)ModLoader.getPrivateValue(EntityAITasks.class, pTasksSRC, 1);
				
				Iterator iterator;
				iterator = ltasksExeDEST.iterator();
				while (iterator.hasNext()) {
					EntityAITaskEntry ltaskentory = (EntityAITaskEntry)iterator.next();
					ltaskentory.action.resetTask();
				}	
				ltasksExeDEST.clear();
				
				ltasksDoDEST.clear();
				ltasksDoDEST.addAll(ltasksDoSRC);
				// TODO: 窶督｢ﾅｽﾃ窶倪｢窶堙娯ｹ@窶拿・ｽAﾆ停夲ｿｽ[ﾆ檀ﾆ蛋ﾆ巽ﾆ停愴淡ﾅｽﾅｾ窶堙鯉ｿｽ窶ｰﾅﾃｺ窶ｰﾂｻ窶堙ｰ・ｽs窶堋､・ｽB
				for (EntityAITaskEntry ltask : ltasksDoSRC) {
					if (ltask instanceof LMM_IEntityAI) {
//						((LMM_IEntityAI)ltask).setDefaultEnable();
					}
				}
			}
		} catch (Exception s) {
		}
		
	}

	/**
	 * 窶廳窶廃窶堋ｳ窶堙ｪ窶堙・堋｢窶堙ｩﾆ停夲ｿｽ[ﾆ檀ﾆ誰ﾆ停ｰﾆ湛
	 */
	public LMM_EntityModeBase getActiveModeClass() {
		return maidActiveModeClass;
	}

	public void setActiveModeClass(LMM_EntityModeBase pEntityMode) {
		maidActiveModeClass = pEntityMode;
	}

	public boolean isActiveModeClass() {
		return maidActiveModeClass != null;
	}

	// ﾅ津ｸ窶ｰﾃ岩ｰﾂｹ窶堙鯉ｿｽﾃ昶凖ｨ
	@Override
	protected String getHurtSound() {
		playSound(maidDamegeSound, true);
		return null;
	}

	@Override
	protected String getDeathSound() {
		playSound(LMM_EnumSound.death, true);
		return null;
	}

	@Override
	protected String getLivingSound() {
		// 窶｢・ｽ窶冓窶堙鯉ｿｽﾂｺ
		LMM_EnumSound so = LMM_EnumSound.Null;
		if (getHealth() < 10)
			so = LMM_EnumSound.living_whine;
		else if (rand.nextFloat() < maidSoundRate) {
			if (mstatTime > 23500 || mstatTime < 1500) {
				so = LMM_EnumSound.living_morning;
			} else if (mstatTime < 12500) {
				if (isContract()) {
					BiomeGenBase biomegenbase = worldObj.getBiomeGenForCoords(MathHelper.floor_double(posX + 0.5D), MathHelper.floor_double(posZ + 0.5D));
					float ltemp = biomegenbase.getFloatTemperature();
					if (ltemp <= 0.15F) {
						so = LMM_EnumSound.living_cold;
					} else if (ltemp > 1.0F) {
						so = LMM_EnumSound.living_hot;
					} else {
						so = LMM_EnumSound.living_daytime;
					}
					if (worldObj.isRaining()) {
						if (biomegenbase.canSpawnLightningBolt()) {
							so = LMM_EnumSound.living_rain;
						} else if (biomegenbase.getEnableSnow()) {
							so = LMM_EnumSound.living_snow;
						}
					}
				} else {
					so = LMM_EnumSound.living_daytime;
				}
			} else {
				so = LMM_EnumSound.living_night;
			}
		}
		
		mod_LMM_littleMaidMob.Debug("id:%d LivingSound:%s", entityId, worldObj == null ? "null" : worldObj.isRemote ? "Client" : "Server");
		playLittleMaidSound(so, false);
		return null;
	}

	/**
	 * ﾅﾃ依・補ｰﾂｹ・ｽﾂｺ・ｽﾃ・ｿｽﾂｶ・ｽA窶｢W・ｽ竄ｬ窶堙娯ｰﾂｹ・ｽﾂｺ窶堙娯堙敘ｽg窶廃窶堋ｷ窶堙ｩ窶堋ｱ窶堙・ｿｽB
	 */
	public void playSound(String pname) {
		playSound(pname, 0.5F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
	}

	/**
	 * ﾆ値ﾆ鍛ﾆ暖ﾆ抵ｿｽ・ｽ[ﾆ誰窶佚寂ｰﾅｾ窶ｰﾂｹ・ｽﾂｺ・ｽﾃ・ｿｽﾂｶ
	 */
	public void playSound(LMM_EnumSound enumsound, boolean force) {
		if ((maidSoundInterval > 0 && !force) || enumsound == LMM_EnumSound.Null) return;
		maidSoundInterval = 20;
		if (worldObj.isRemote) {
			// Client
//			String lsound = LMM_SoundManager.getSoundValue(enumsound, textureName, maidColor & 0x00ff);
//			float lpitch = mod_LMM_littleMaidMob.VoiceDistortion ? (rand.nextFloat() * 0.2F) + 0.95F : 1.0F;
//			worldObj.playSound(posX, posY, posZ, lsound, getSoundVolume(), lpitch, false);
		} else {
			// Server
			mod_LMM_littleMaidMob.Debug("id:%d-%s, seps:%04x-%s", entityId, worldObj.isRemote ? "Client" : "Server",  enumsound.index, enumsound.name());
			byte[] lbuf = new byte[] {
					LMM_Statics.LMN_Client_PlaySound,
					0, 0, 0, 0,
					0, 0, 0, 0
			};
			MMM_Helper.setInt(lbuf, 5, enumsound.index);
			LMM_Net.sendToAllEClient(this, lbuf);
		}
	}

	/**
	 * 窶ｰﾂｹ・ｽﾂｺ・ｽﾃ・ｿｽﾂｶ窶廃・ｽB
	 * 窶凖奇ｿｽﾃｭ窶堙鯉ｿｽﾃ・ｿｽﾂｶ窶堙・堙哉値ﾆ鍛ﾆ暖ﾆ抵ｿｽ・ｽ[ﾆ誰窶ｰz窶堋ｵ窶堙俄堙遺堙ｩ窶堙娯堙・堋ｻ窶堙娯佚趣ｿｽﾃｴ・ｽB
	 */
	public void playLittleMaidSound(LMM_EnumSound enumsound, boolean force) {
		// 窶ｰﾂｹ・ｽﾂｺ窶堙鯉ｿｽﾃ・ｿｽﾂｶ
		if ((maidSoundInterval > 0 && !force) || enumsound == LMM_EnumSound.Null) return;
		maidSoundInterval = 20;
		if (worldObj.isRemote) {
			// Client
			String s = LMM_SoundManager.getSoundValue(enumsound, textureData.getTextureName(0), textureData.getColor());
			mod_LMM_littleMaidMob.Debug(String.format("id:%d, se:%04x-%s (%s)", entityId, enumsound.index, enumsound.name(), s));
			float lpitch = mod_LMM_littleMaidMob.cfg_VoiceDistortion ? (rand.nextFloat() * 0.2F) + 0.95F : 1.0F;
			worldObj.playSound(posX, posY, posZ, s, getSoundVolume(), lpitch, false);
		}
	}

	@Override
	public void onKillEntity(EntityLivingBase par1EntityLiving) {
		super.onKillEntity(par1EntityLiving);
		if (isBloodsuck()) {
//			mod_LMM_littleMaidMob.Debug("nice Kill.");
			playSound(LMM_EnumSound.laughter, true);
		} else {
			setTarget(null);
			setAttackTarget(null);
		}
	}

	@Override
	protected boolean canDespawn() {
		// ﾆ断ﾆ湛ﾆ竹・ｽ[ﾆ停懌敖ｻ窶凖ｨ
		return mod_LMM_littleMaidMob.cfg_canDespawn || super.canDespawn();
	}

	@Override
	public boolean getCanSpawnHere() {
		// ﾆ湛ﾆ竹・ｽ[ﾆ停懌ｰﾃや拿窶堋ｩ・ｽH
		if (mod_LMM_littleMaidMob.cfg_spawnLimit <= getMaidCount()) {
			mod_LMM_littleMaidMob.Debug("Spawn Limit.");
			return false;
		}
		int lx = MathHelper.floor_double(this.posX);
		int ly = MathHelper.floor_double(this.boundingBox.minY);
		int lz = MathHelper.floor_double(this.posZ);
		/*
		// TODO:ﾆ探・ｽ[ﾆ弛・ｽ[窶伉､窶堙・敖ｻ窶凖ｨ窶堙・堋ｫ窶堙遺堋｢窶堙娯堙・・凪督｡窶堙遺堋ｵ?
		MMM_TextureBox lbox = MMM_TextureManager.instance.getTextureBox(textureBox[0]);
		if (worldObj == null || textureModel == null  
				|| !textureBox[0].mo.getCanSpawnHere(worldObj, lx, ly, lz, this)) {
			mod_LMM_littleMaidMob.Debug(String.format("%s is can't spawn hear.", textureName));
			return false;
		}
		*/
		if (mod_LMM_littleMaidMob.cfg_Dominant) {
			// ﾆ檀ﾆ蓄ﾆ段ﾆ停愴暖
			return this.worldObj.checkNoEntityCollision(this.boundingBox) 
					&& this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty() 
					&& !this.worldObj.isAnyLiquid(this.boundingBox)
					&& this.getBlockPathWeight(lx, ly, lz) >= 0.0F;
		} else {
			return super.getCanSpawnHere();
		}
	}

	@Override
	public void setDead() {
		if (mstatgotcha != null) {
			// ﾅｽﾃｱ窶｢R窶堙ｰﾆ檀ﾆ抵ｿｽﾆ鍛ﾆ致
			EntityItem entityitem = new EntityItem(worldObj, mstatgotcha.posX, mstatgotcha.posY, mstatgotcha.posZ, new ItemStack(Item.silk));
			worldObj.spawnEntityInWorld(entityitem);
			mstatgotcha = null;
		}
		super.setDead();
	}

	/**
	 * 窶愿・堙晢ｿｽﾅｾ窶堙昶氾戸・ｦ窶愿窶堙姑抵ｿｽﾆ辰ﾆ檀窶堋ｳ窶堙ｱ窶堙鯉ｿｽ窶・
	 */
	public int getMaidCount() {
		int lj = 0;
		for (int li = 0; li < worldObj.loadedEntityList.size(); li++) {
			if (worldObj.loadedEntityList.get(li) instanceof LMM_EntityLittleMaid) {
				lj++;
			}
		}
		return lj;
	}

	@Override
	public EntityAgeable createChild(EntityAgeable var1) {
		// 窶堋ｨﾅｽq窶堋ｳ窶堙ｱ窶堙鯉ｿｽﾃ昶凖ｨ
		return null;
	}

	// ﾆ竪ﾆ稚ﾆ巽ﾆ誰ﾆ暖窶｢\ﾅｽﾂｦ
	protected void showParticleFX(String s) {
		showParticleFX(s, 1D, 1D, 1D);
	}

	protected void showParticleFX(String s, double d, double d1, double d2) {
		showParticleFX(s, d, d1, d2, 0D, 0D, 0D);
	}

	protected void showParticleFX(String s, double d, double d1, double d2, double d3, double d4, double d5 ) {
		for (int i = 0; i < 7; i++) {
			double d6 = rand.nextGaussian() * d + d3;
			double d7 = rand.nextGaussian() * d1 + d4;
			double d8 = rand.nextGaussian() * d2 + d5;
			worldObj.spawnParticle(s, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d6, d7, d8);
		}
	}

	@Override
	public void handleHealthUpdate(byte par1) {
		// worldObj.setEntityState(this, (byte))窶堙・ｽw窶凖ｨ窶堋ｳ窶堙ｪ窶堋ｽﾆ但ﾆ誰ﾆ歎ﾆ停｡ﾆ停懌堙ｰﾅｽﾃ・ｽs
		switch (par1) {
		case 10:
			// 窶｢s窶ｹ@ﾅ停┐
			showParticleFX("smoke", 0.02D, 0.02D, 0.02D);
			break;
		case 11:
			// ﾆ担ﾆ鱈ﾆ嘆ﾆ停・
			double a = getContractLimitDays() / 7D;
			double d6 = a * 0.3D;
			double d7 = a;
			double d8 = a * 0.3D;
			worldObj.spawnParticle("note", posX, posY + height + 0.1D, posZ, d6, d7, d8);
			break;
		case 12:
			// ﾅｽﾂｩ窶燃・ｽs窶慊ｮ
			showParticleFX("reddust", 0.5D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
			break;
		case 13:
			// 窶｢sﾅｽﾂｩ窶燃・ｽs窶慊ｮ
			showParticleFX("smoke", 0.02D, 0.02D, 0.02D);
			break;
		case 14:
			// ﾆ暖ﾆ椎抵ｿｽ[ﾆ探・ｽ[
			showParticleFX("explode", 0.3D, 0.3D, 0.3D, 0.0D, 0.0D, 0.0D);
			break;
			
		default:
			super.handleHealthUpdate(par1);
		}
	}

	public void func_110149_m(float par1) {
		if (par1 < 0.0F) {
			par1 = 0.0F;
		}
		
		this.getDataWatcher().updateObject(dataWatch_Absoption, Float.valueOf(par1));
	}

	public float func_110139_bj() {
		return this.getDataWatcher().getWatchableObjectFloat(dataWatch_Absoption);
	}


	public int colorMultiplier(float pLight, float pPartialTicks) {
		// 窶敖ｭﾅ津ｵ・ｽﾋ・費ｿｽ窶廃
		int lbase = 0;
		if (maidOverDriveTime.isDelay()) {
			int i;
			if (maidOverDriveTime.isEnable()) {
				i = 100;
			} else {
				i = 100 + maidOverDriveTime.getValue();
			}
			lbase = i << 24 | 0x00df0f0f;
		}
		
		if (isActiveModeClass()) {
			lbase = lbase | getActiveModeClass().colorMultiplier(pLight, pPartialTicks);
		}
		
		return lbase;
	}


	// AIﾅﾃ麺廣
	@Override
	protected boolean isAIEnabled() {
		// ・ｽVAI窶佚寂ｰﾅｾ
		return true;
	}
	
	/**
	 * 窶廨窶督｡窶｢ﾃｻﾅｽﾂｯ窶｢ﾃ・
	 */
	public boolean getIFF(Entity pEntity) {
		// 窶廨窶督｡窶｢ﾃｻﾅｽﾂｯ窶｢ﾃ・窶廨=false)
		if (pEntity == null || pEntity == mstatMasterEntity) {
			return true;
		}
		
		int tt = LMM_IFF.getIFF(getMaidMaster(), pEntity);
		switch (tt) {
		case LMM_IFF.iff_Enemy:
			return false;
		case LMM_IFF.iff_Friendry:
			return true;
		case LMM_IFF.iff_Unknown:
			if (isBloodsuck()) {
				// ﾅ椎停堙俄ｰﾃｬ窶堋ｦ窶堙・堋｢窶堙ｩﾅｽﾅｾ窶堙坂廨
				return false;
			}
			if (pEntity instanceof LMM_EntityLittleMaid) {
				// 窶堋ｨ窶之窶堙柁停夲ｿｽ[ﾆ檀窶堙姑抵ｿｽﾆ辰ﾆ檀窶堙俄堙坂廨窶佚寂堋ｵ窶堙遺堋｢
				if (((LMM_EntityLittleMaid)pEntity).mstatPlayingRole > LMM_EntityMode_Playing.mpr_NULL) {
					return true;
				}
			}
			if (pEntity instanceof EntityCreature) {
				// 窶佛ﾅｽﾃｨ窶堋ｪ窶ｰﾂｽ窶堙ｰﾆ耽・ｽ[ﾆ嘆ﾆ鍛ﾆ暖窶堙俄堋ｵ窶堙・堋｢窶堙ｩ窶堋ｩ窶堙・塚・堙懌堙ｩ
				Entity et = ((EntityCreature)pEntity).getEntityToAttack();
				if (et != null && et == mstatMasterEntity) {
					return false;
				}
				if (et == this) {
					return false;
				}
				if (et instanceof LMM_EntityLittleMaid) {
					// 窶慊ｯ窶堋ｶﾆ筑ﾆ湛ﾆ耽・ｽ[窶堙姑抵ｿｽﾆ辰ﾆ檀窶堙ｰ・ｽUﾅ停壺佚趣ｿｽﾃ帚堙・堋ｵ窶堙・堋｢窶堙ｩ
					if (((LMM_EntityLittleMaid)et).getMaidMasterEntity() == mstatMasterEntity) {
						return false;
					}
				}
			}
			return true;
			
		default :
			return false;
		}
	}

	@Override
	public boolean canAttackClass(Class par1Class) {
		// IFF窶堙鯉ｿｽﾃ昶凖ｨ・ｽAﾆ誰ﾆ停ｰﾆ湛窶突・堙娯敖ｻ窶凖ｨ窶堋ｵ窶堋ｩ窶堙・堋ｫ窶堙遺堋｢窶堙娯堙・ｽg窶堙ｭ窶堙遺堋｢・ｽB
		return true;
	}

	@Override
	public boolean attackEntityAsMob(Entity par1Entity) {
		
		// ・ｽﾂｳ・ｽﾃｭﾅｽﾅｾ窶堙坂ｰﾃｱ窶｢ﾅ凪妊・ｽﾃｦ・ｽﾋ・費ｿｽ
		if (getHealth() < 10 && !isBloodsuck() && maidInventory.hasItem(Item.sugar.itemID)) {
			return true;
		}
		
		// 窶愿・ｽﾃｪ窶堙茨ｿｽUﾅ停夲ｿｽﾋ・費ｿｽ
		if (isActiveModeClass() && getActiveModeClass().attackEntityAsMob(maidMode, par1Entity)) {
			return true;
		}
		
		// 窶｢W・ｽ竄ｬ・ｽﾋ・費ｿｽ
		setSwing(20, isBloodsuck() ? LMM_EnumSound.attack_bloodsuck : LMM_EnumSound.attack);
		maidAvatar.attackTargetEntityWithCurrentItem(par1Entity);
		return true;
	}

	@Override
	public boolean isBreedingItem(ItemStack par1ItemStack) {
		// 窶堋ｨ・ｽD窶堙昶堙坂ｰﾂｽ・ｽH
		if (isContractEX()) {
			return par1ItemStack.itemID == Item.sugar.itemID;
		} else {
			return par1ItemStack.itemID == Item.cake.itemID;
		}
	}

	
	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
		// ﾆ断・ｽ[ﾆ耽ﾆ短・ｽ[ﾆ置
		super.writeEntityToNBT(par1nbtTagCompound);
		
		par1nbtTagCompound.setTag("Inventory", maidInventory.writeToNBT(new NBTTagList()));
		par1nbtTagCompound.setString("Mode", getMaidModeString(mstatWorkingInt));
		par1nbtTagCompound.setBoolean("Wait", isMaidWait());
		par1nbtTagCompound.setBoolean("Freedom", isFreedom());
		par1nbtTagCompound.setBoolean("Tracer", isTracer());
		par1nbtTagCompound.setInteger("LimitCount", maidContractLimit);
		par1nbtTagCompound.setLong("Anniversary", maidAnniversary);
		par1nbtTagCompound.setInteger("EXP", experienceValue);
		par1nbtTagCompound.setInteger("DominantArm", maidDominantArm);
		par1nbtTagCompound.setInteger("Color", textureData.getColor());
		par1nbtTagCompound.setString("texName", textureData.getTextureName(0));
		par1nbtTagCompound.setString("texArmor", textureData.getTextureName(1));
		// HomePosition
		par1nbtTagCompound.setInteger("homeX", getHomePosition().posX);
		par1nbtTagCompound.setInteger("homeY", getHomePosition().posY);
		par1nbtTagCompound.setInteger("homeZ", getHomePosition().posZ);
		par1nbtTagCompound.setInteger("homeWorld", homeWorld);
		// Tiles
		NBTTagCompound lnbt = new NBTTagCompound();
		par1nbtTagCompound.setTag("Tiles", lnbt);
		for (int li = 0; li < maidTiles.length; li++) {
			if (maidTiles[li] != null) {
				lnbt.setIntArray(String.valueOf(li), maidTiles[li]);
			}
		}
		// 窶凖・ｰﾃ≫｢ﾂｪ
		for (int li = 0; li < maidEntityModeList.size(); li++) {
			maidEntityModeList.get(li).writeEntityToNBT(par1nbtTagCompound);
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		// ﾆ断・ｽ[ﾆ耽ﾆ抵ｿｽ・ｽ[ﾆ檀
		super.readEntityFromNBT(par1nbtTagCompound);
		
		if (par1nbtTagCompound.hasKey("ModeColor")) {
			// 窶ｹﾅ停敕・堋ｩ窶堙ｧ窶堙固恥・ｽﾂｳ
	        String s = par1nbtTagCompound.getString("Master");
	        if(s.length() > 0) {
	        	setOwner(s);
	            setContract(true);
	        }
	        NBTTagList nbttaglist = par1nbtTagCompound.getTagList("Inventory");
	        maidInventory.readFromNBT(nbttaglist);
	        // ﾆ但・ｽ[ﾆ筑・ｽ[ﾆ湛ﾆ抵ｿｽﾆ鍛ﾆ暖窶｢ﾃ擾ｿｽX窶堙俄佚寂ｰﾅｾ窶堋ｷ窶堙ｩ窶堋ｽ窶堙溪堙姑坦・ｽ[ﾆ檀
	        ItemStack[] armi = new ItemStack[4];
	        for (int i = 0; i < 4; i++) {
	        	ItemStack is = maidInventory.armorItemInSlot(i);
	        	if (is != null) {
	            	armi[3 - ((ItemArmor)is.getItem()).armorType] = is; 
	        	}
	        }
	        maidInventory.armorInventory = armi; 
	        //
	        setMaidWait(par1nbtTagCompound.getBoolean("Wait"));
	        setFreedom(par1nbtTagCompound.getBoolean("Freedom"));
	        setTracer(par1nbtTagCompound.getBoolean("Tracer"));
			textureData.textureIndex[0] = MMM_TextureManager.instance.getIndexTextureBoxServer(this, par1nbtTagCompound.getString("texName"));
			textureData.textureIndex[1] = MMM_TextureManager.instance.getIndexTextureBoxServer(this, par1nbtTagCompound.getString("texArmor"));
			textureData.textureBox[0] = MMM_TextureManager.instance.getTextureBoxServer(textureData.textureIndex[0]);
			textureData.textureBox[1] = MMM_TextureManager.instance.getTextureBoxServer(textureData.textureIndex[1]);
			byte b = par1nbtTagCompound.getByte("ModeColor");
			setColor(b & 0x0f);
	        switch ((b & 0xf0) >> 4) {
	        case 0:
	        	setMaidMode(0x0000);	// Wild
	        	break;
	        case 2:
	        	setMaidMode(0x0001);	// Escorter
	        	break;
	        case 4:
	        	setMaidMode(0x0080);	// Fencer
	        	break;
	        case 5:
	        	setMaidMode(0x0000);	// Healer
	        	break;
	        case 6:
	        	setMaidMode(0x0021);	// Cooking
	        	break;
	        case 7:
	        	setMaidMode(0x00c0);	// Bloodsucker
	        	break;
	        case 8:
	        	setMaidMode(0x0083);	// Archer
	        	break;
	        case 9:
	        	setMaidMode(0x00c3);	// Blazingstar
	        	break;
	        case 10:
	        	setMaidMode(0x0081);	// Ripper
	        	break;
	        case 11:
	        	setMaidMode(0x00c2);	// Detonator
	        	break;
	        case 12:
	        	setMaidMode(0x00c1);	// TNT-D
	        	break;
	        case 13:
	        	setMaidMode(0x0020);	// Torcher
	        	break;
	        case 15:
	        	setMaidMode(0x0000);	// Pharmacist
	        	break;
	        default :
	        	setMaidMode(0x0000);	// Wild
	        }
//	        setMaidMode((b & 0xf0) >> 4);
	        int lhx = 0;
	        int lhy = 0;
	        int lhz = 0;
	        NBTTagList nbttaglist1 = par1nbtTagCompound.getTagList("HomePosI");
	        if (nbttaglist1.tagCount() > 0) {
	        	lhx = ((NBTTagInt)nbttaglist1.tagAt(0)).data;
	        	lhy = ((NBTTagInt)nbttaglist1.tagAt(1)).data;
	        	lhz = ((NBTTagInt)nbttaglist1.tagAt(2)).data;
	        } else {
	        	lhx = MathHelper.floor_double(posX);
	        	lhy = MathHelper.floor_double(posY);
	        	lhz = MathHelper.floor_double(posZ);
	        }
			getHomePosition().set(lhx, lhy, lhz);
			long lcl = par1nbtTagCompound.getLong("Limit");
			if (isContract() && lcl == 0) {
				maidContractLimit = 24000;
			} else {
				maidContractLimit = (int)((lcl - worldObj.getWorldTime()));
			}
			maidAnniversary = par1nbtTagCompound.getLong("Anniversary");
			if (maidAnniversary == 0L && isContract()) {
				// ﾆ胆ﾆ蓄・ｽ[窶堙鯉ｿｽ窶昶冤窶堙ｰ窶愿ｼ窶堙ｪ窶堙ｩ
				maidAnniversary = worldObj.getWorldTime() - entityId;
			}
			
		} else {
			// ・ｽVﾅ耽
			mod_LMM_littleMaidMob.Debug("read." + worldObj.isRemote);
			
			maidInventory.readFromNBT(par1nbtTagCompound.getTagList("Inventory"));
			setMaidWait(par1nbtTagCompound.getBoolean("Wait"));
			setFreedom(par1nbtTagCompound.getBoolean("Freedom"));
			setTracer(par1nbtTagCompound.getBoolean("Tracer"));
			setMaidMode(par1nbtTagCompound.getString("Mode"));
			if (par1nbtTagCompound.hasKey("LimitCount")) {
				maidContractLimit = par1nbtTagCompound.getInteger("LimitCount");
			} else {
				long lcl = par1nbtTagCompound.getLong("Limit");
				if (isContract() && lcl == 0) {
					maidContractLimit = 24000;
				} else {
					maidContractLimit = (int)((lcl - worldObj.getWorldTime()));
				}
			}
			if (isContract() && maidContractLimit == 0) {
				// 窶冤窶堋ｪ窶堋ｨ窶堋ｩ窶堋ｵ窶堋｢ﾅｽﾅｾ窶堙坂啀窶愿ｺ窶｢ﾂｪ
//	        	maidContractLimit = worldObj.getWorldTime() + 24000L;
				maidContractLimit = 24000;
			}
			maidAnniversary = par1nbtTagCompound.getLong("Anniversary");
			if (maidAnniversary == 0L && isContract()) {
				// ﾆ胆ﾆ蓄・ｽ[窶堙鯉ｿｽ窶昶冤窶堙ｰ窶愿ｼ窶堙ｪ窶堙ｩ
				maidAnniversary = worldObj.getWorldTime() - entityId;
			}
			if (maidAvatar != null) {
				maidAvatar.experienceTotal = par1nbtTagCompound.getInteger("EXP");
			}
			setDominantArm(par1nbtTagCompound.getInteger("DominantArm"));
			if (mstatSwingStatus.length <= maidDominantArm) {
				maidDominantArm = 0;
			}
			textureData.textureIndex[0] = MMM_TextureManager.instance.getIndexTextureBoxServer(this, par1nbtTagCompound.getString("texName"));
			textureData.textureIndex[1] = MMM_TextureManager.instance.getIndexTextureBoxServer(this, par1nbtTagCompound.getString("texArmor"));
			textureData.textureBox[0] = MMM_TextureManager.instance.getTextureBoxServer(textureData.textureIndex[0]);
			textureData.textureBox[1] = MMM_TextureManager.instance.getTextureBoxServer(textureData.textureIndex[1]);
			textureData.setColor(par1nbtTagCompound.getInteger("Color"));
			setTexturePackIndex(textureData.color, textureData.getTextureIndex());
			
			// HomePosition
			int lhx = par1nbtTagCompound.getInteger("homeX");
			int lhy = par1nbtTagCompound.getInteger("homeY");
			int lhz = par1nbtTagCompound.getInteger("homeZ");
			getHomePosition().set(lhx, lhy, lhz);
			homeWorld = par1nbtTagCompound.getInteger("homeWorld");
			
			// Tiles
			NBTTagCompound lnbt = par1nbtTagCompound.getCompoundTag("Tiles");
			for (int li = 0; li < maidTiles.length; li++) {
				int ltile[] = lnbt.getIntArray(String.valueOf(li));
				maidTiles[li] = ltile.length > 0 ? ltile : null;
			}
			
			// 窶凖・ｰﾃ≫｢ﾂｪ
			for (int li = 0; li < maidEntityModeList.size(); li++) {
				maidEntityModeList.get(li).readEntityFromNBT(par1nbtTagCompound);
			}
		}
		onInventoryChanged();
		
		// ﾆ檀ﾆ鍛ﾆ馳ﾆ停ｹ窶佚趣ｿｽﾃｴ
		if (mod_LMM_littleMaidMob.cfg_antiDoppelganger && maidAnniversary > 0L) {
			for (int i = 0; i < worldObj.loadedEntityList.size(); i++) {
				Entity entity1 = (Entity)worldObj.loadedEntityList.get(i);
				if (!entity1.isDead && entity1 instanceof LMM_EntityLittleMaid) {
					LMM_EntityLittleMaid elm = (LMM_EntityLittleMaid)entity1;
					if (elm != this && elm.isContract() && elm.maidAnniversary == maidAnniversary
							&& elm.getMaidMaster().equalsIgnoreCase(getMaidMaster())) {
						// ・ｽV窶堋ｵ窶堋｢窶｢ﾃｻ窶堙ｰﾅｽc窶堋ｷ
						if (entityId > elm.entityId) {
							mod_LMM_littleMaidMob.Debug(String.format("Load Doppelganger ID:%d, %d" ,elm.entityId, maidAnniversary));
							elm.setDead();
						} else {
							mod_LMM_littleMaidMob.Debug(String.format("Load Doppelganger ID:%d, %d" ,entityId, maidAnniversary));
							setDead();
							break;
						}
					}
				}
			}
		} else {
			mod_LMM_littleMaidMob.Debug(String.format("Load ID:%d, MaidMaster:%s, x:%.1f, y:%.1f, z:%.1f, %d" ,entityId, getMaidMaster(), posX, posY, posZ, maidAnniversary));
		}
		
	}

	@Override
	public Icon getItemIcon(ItemStack par1ItemStack, int par2) {
		// ﾆ但ﾆ辰ﾆ弾ﾆ停ぎ窶堙娯｢\ﾅｽﾂｦ
		if (maidAvatar != null) {
			return maidAvatar.getItemIcon(par1ItemStack, par2);
		}
		
		if (par1ItemStack.getItem().requiresMultipleRenderPasses()) {
			return par1ItemStack.getItem().getIconFromDamageForRenderPass(par1ItemStack.getItemDamage(), par2);
		} else {
			return super.getItemIcon(par1ItemStack, par2);
		}
	}


	// 窶堋ｨ窶堙ｱ窶堙披堋ｨ窶堙寂堋ｯ窶堙坂督ｳ窶廨
	@Override
	public boolean canBeCollidedWith() {
		if (ridingEntity != null && ridingEntity == mstatMasterEntity) {
			ItemStack litemstack = ((EntityPlayer)mstatMasterEntity).getCurrentEquippedItem();
			return (litemstack == null) || (litemstack.itemID == Item.saddle.itemID);
		} else {
			return super.canBeCollidedWith();
		}
	}

	@Override
	public boolean canAttackWithItem() {
		if (ridingEntity != null && ridingEntity == mstatMasterEntity) {
			return false;
		} else {
			return super.canAttackWithItem();
		}
	}

	@Override
	public double getMountedYOffset() {
		// TODO:窶堋ｱ窶堋ｱ窶堙坂牌窶卍ｲ・ｽﾂｮ
		if (riddenByEntity instanceof EntityChicken) {
			return height + 0.03D;
		}
		if (riddenByEntity instanceof EntitySquid) {
			return height - 0.2D;
		}
		return super.getMountedYOffset() + 0.35D;
	}

	@Override
	public double getYOffset() {
		if(ridingEntity instanceof EntityPlayer) {
			// ﾅｽp・ｽﾂｨ・ｽﾂｧﾅ津､
//        	setSneaking(true);
//        	mstatAimeBow = true;
//        	updateAimebow();
//            return (double)(yOffset - 1.8F);
			return (double)(yOffset - 2.0F);
		}
		return (double)(yOffset - 0.25F);
	}

	@Override
	public void updateRidden() {
		// TODO:ﾆ但ﾆ鍛ﾆ致ﾆ断・ｽ[ﾆ暖ﾅｽﾅｾ窶堙家蛋ﾆ巽ﾆ鍛ﾆ誰
		++ticksExisted;
		//
		
		if(ridingEntity instanceof EntityPlayer) {
			EntityPlayer lep = (EntityPlayer)ridingEntity;
			
			// ﾆ蜘ﾆ鍛ﾆ檀ﾆ地ﾆ狸・ｽ[
			renderYawOffset = lep.renderYawOffset;
			prevRenderYawOffset = lep.prevRenderYawOffset;
			double llpx = lastTickPosX;
			double llpy = lastTickPosY;
			double llpz = lastTickPosZ;
			
			super.updateRidden();
			
			renderYawOffset = lep.renderYawOffset;
			if (((rotationYaw - renderYawOffset) % 360F) > 90F) {
				rotationYaw = renderYawOffset + 90F;
			}
			if (((rotationYaw - renderYawOffset) % 360F) < -90F) {
				rotationYaw = renderYawOffset - 90F;
			}
			if (((rotationYawHead - renderYawOffset) % 360F) > 90F) {
				rotationYawHead = renderYawOffset + 90F;
			}
			if (((rotationYawHead - renderYawOffset) % 360F) < -90F) {
				rotationYawHead = renderYawOffset - 90F;
			}
			
			double dx = Math.sin(((double)lep.renderYawOffset * Math.PI) / 180D) * 0.35D;
			double dz = Math.cos(((double)lep.renderYawOffset * Math.PI) / 180D) * 0.35D;
			setPosition(lep.posX + dx, posY, lep.posZ - dz);
			lastTickPosX = llpx;
			lastTickPosY = llpy;
			lastTickPosZ = llpz;
		} else {
			super.updateRidden();
		}
	}
	
	@Override
	public void updateRiderPosition() {
		super.updateRiderPosition();
	}

	@Override
	public float getSwingProgress(float par1) {
		for (LMM_SwingStatus lswing : mstatSwingStatus) {
			lswing.getSwingProgress(par1);
		}
		return getSwingStatusDominant().onGround;
	}

	// ﾅｽﾃｱﾅｽﾃｼ窶堙ｨ
	public void setLooksWithInterest(boolean f) {
		if (looksWithInterest != f) {
			looksWithInterest = f;
			if (numTicksToChaseTarget <= 0) {
				looksWithInterestAXIS = rand.nextBoolean();
			}
			int li = dataWatcher.getWatchableObjectInt(dataWatch_Flags);
			li = looksWithInterest ? (li | dataWatch_Flags_looksWithInterest) : (li & ~dataWatch_Flags_looksWithInterest);
			li = looksWithInterestAXIS ? (li | dataWatch_Flags_looksWithInterestAXIS) : (li & ~dataWatch_Flags_looksWithInterestAXIS);
			dataWatcher.updateObject(dataWatch_Flags, Integer.valueOf(li));
		}
	}

	public boolean getLooksWithInterest() {
		looksWithInterest = (dataWatcher.getWatchableObjectInt(dataWatch_Flags) & dataWatch_Flags_looksWithInterest) > 0;
		looksWithInterestAXIS = (dataWatcher.getWatchableObjectInt(dataWatch_Flags) & dataWatch_Flags_looksWithInterestAXIS) > 0;

		return looksWithInterest;
	}

	public float getInterestedAngle(float f) {
		return (prevRotateAngleHead + (rotateAngleHead - prevRotateAngleHead) * f) * ((looksWithInterestAXIS ? 0.08F : -0.08F) * (float)Math.PI);
	}


	// ﾆ胆ﾆ抵ｿｽ・ｽ[ﾆ淡ﾆ坦ﾆ停愴暖ﾆ抵ｿｽ・ｽ[ﾆ停ｹ
//	@Override
	public boolean isBlocking() {
		return getSwingStatusDominant().isBlocking();
//		return maidAvatar.isBlocking();
	}

	@Override
	protected void damageArmor(float pDamage) {
		maidInventory.damageArmor(pDamage);
		//XXX: experimenting
		//maidAvatar.damageArmor(pDamage);
	}

	@Override
	public int getTotalArmorValue() {
		return maidAvatar.getTotalArmorValue();
	}

	//XXX: experimenting
	/*
	@Override
	protected float applyArmorCalculations(DamageSource par1DamageSource, float par2) {
		return maidAvatar.applyArmorCalculations(par1DamageSource, par2);
	}

	@Override
	protected float applyPotionDamageCalculations(DamageSource par1DamageSource, float par2) {
		return maidAvatar.applyPotionDamageCalculations(par1DamageSource, par2);
	}*/

	@Override
	protected void damageEntity(DamageSource par1DamageSource, float par2) {
		// ﾆ胆ﾆ抵ｿｽ・ｽ[ﾆ淡ﾆ箪・ｽ[ﾆ湛窶堙俄ｰﾅｾ窶堋ｶ窶堙・ｰﾂｹ・ｽﾂｺ窶｢ﾃ擾ｿｽX
		if (par1DamageSource == DamageSource.fall) {
			maidDamegeSound = LMM_EnumSound.hurt_fall;
		}
		if(!par1DamageSource.isUnblockable() && isBlocking()) {
			// ﾆ置ﾆ抵ｿｽﾆ鍛ﾆ鱈ﾆ停愴丹
//			par2 = (1.0F + par2) * 0.5F;
			mod_LMM_littleMaidMob.Debug(String.format("Blocking success ID:%d, %f -> %f" , this.entityId, par2, (par2 = (1.0F + par2) * 0.5F)));
			maidDamegeSound = LMM_EnumSound.hurt_guard;
		}
		
		// 窶敕ｭﾆ胆ﾆ抵ｿｽ
		float llasthealth = getHealth();
		if (par2 > 0 && getActiveModeClass() != null && !getActiveModeClass().damageEntity(maidMode, par1DamageSource, par2)) {
			//XXX: experimenting
			//maidAvatar.damageEntity(par1DamageSource, par2);
			maidAvatar.attackEntityFrom(par1DamageSource, par2);
			
			// ﾆ胆ﾆ抵ｿｽ・ｽ[ﾆ淡窶堙ｰﾅｽﾃｳ窶堋ｯ窶堙ｩ窶堙・佚停ｹ@窶堙ｰ窶ｰﾃｰ・ｽﾅ・
			setMaidWait(false);
		}
		
		if (llasthealth == getHealth() && maidDamegeSound == LMM_EnumSound.hurt) {
			maidDamegeSound = LMM_EnumSound.hurt_nodamege;
		}
		mod_LMM_littleMaidMob.Debug(String.format("GetDamage ID:%d, %s, %f/ %f" , this.entityId, par1DamageSource.damageType, llasthealth - getHealth(), par2));
//		super.damageEntity(par1DamageSource, par2);
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		Entity entity = par1DamageSource.getEntity();
		
		// ﾆ胆ﾆ抵ｿｽ・ｽ[ﾆ淡ﾆ箪・ｽ[ﾆ湛窶堙ｰ窶愿≫凖ｨ窶堋ｵ窶堙・ｰﾂｹ・ｽﾂｺ窶堙鯉ｿｽﾃ昶凖ｨ
		maidDamegeSound = LMM_EnumSound.hurt;
		if (par1DamageSource == DamageSource.inFire || par1DamageSource == DamageSource.onFire || par1DamageSource == DamageSource.lava) {
			maidDamegeSound = LMM_EnumSound.hurt_fire;
		}
		for (LMM_EntityModeBase lm : maidEntityModeList) {
			float li = lm.attackEntityFrom(par1DamageSource, par2);
			if (li > 0) return li == 1 ? false : true;
		}
		
		setMaidWait(false);
		setMaidWaitCount(0);
		if (par2 > 0) {
			// 窶之窶堙鯛堙搾ｿｽI窶堙ｭ窶堙ｨ窶堋ｾ・ｽI
			setPlayingRole(0);
			getNextEquipItem();
		}
		// ﾆ嘆・ｽ[ﾆ停ぎ窶愿ｯﾋ・補忸窶堙俄堙ｦ窶堙ｩﾆ胆ﾆ抵ｿｽ・ｽ[ﾆ淡窶堙娯｢ﾃ｢・ｽﾂｳ
		if(isContract() && (entity instanceof EntityLivingBase) || (entity instanceof EntityArrow)) {
			if(worldObj.difficultySetting == 0) {
				par2 = 0;
			}
			if(worldObj.difficultySetting == 1 && par2 > 0) {
				par2 = par2 / 2 + 1;
			}
			if(worldObj.difficultySetting == 3) {
				par2 = (par2 * 3) / 2;
			}
		}
		
//		if (par2 == 0 && maidMode != mmode_Detonator) {
		if (par2 == 0) {
			// ﾆ知・ｽ[ﾆ胆ﾆ抵ｿｽ・ｽ[ﾆ淡
			if (maidDamegeSound == LMM_EnumSound.hurt) {
				maidDamegeSound = LMM_EnumSound.hurt_nodamege;
			}
			playSound(maidDamegeSound, true);
			return false;
		}
		
		if(super.attackEntityFrom(par1DamageSource, par2)) {
			//ﾅ胆窶禿ｱﾅｽﾃ停堙娯督ｼ窶楼ﾆ蛋ﾆ巽ﾆ鍛ﾆ誰窶堙哉筑ﾆ停ｹﾆ蛋窶廃
			if (isContract() && entity != null) {
				if (getIFF(entity) && !isPlaying()) {
					fleeingTick = 0;
					return true;
				}
			} else if (maidInventory.getCurrentItem() == null) {
				return true;
			}
			fleeingTick = 0;
//            entityToAttack = entity;
            /*
            if (entity != null) {
                setPathToEntity(worldObj.getPathEntityToEntity(this, entityToAttack, 16F, true, false, false, true));
            }
    		if (maidMode == mmode_Healer && entity instanceof EntityLiving) {
    			// ﾆ智・ｽ[ﾆ停ｰ・ｽ[窶堙坂禿ｲ・ｽﾃ懌堙・ｿｽUﾅ停・
    			maidInventory.currentItem = maidInventory.getInventorySlotContainItemPotion(true, 0, ((EntityLiving)entity).isEntityUndead() & isMaskedMaid);
    		}
    		*/
			return true; 
		} else {
			return false;
		}
		
		
//		return maidAvatar.attackEntityFrom(par1DamageSource, par2);
	}

	/**
	 * 窶佚趣ｿｽﾃ帚堙家竹・ｽ[ﾆ歎ﾆ停｡ﾆ停懌堙ｰﾅｽg窶堋､・ｽB
	 */
	public void usePotionTotarget(EntityLivingBase entityliving) {
		ItemStack itemstack = maidInventory.getCurrentItem();
		if (itemstack != null && itemstack.getItem() instanceof ItemPotion) {
			// ﾆ竹・ｽ[ﾆ歎ﾆ停｡ﾆ停愬津ｸ窶ｰﾃ岩堙娯敖ｭ窶慊ｮ
			itemstack.stackSize--;
			List list = ((ItemPotion)itemstack.getItem()).getEffects(itemstack);
			if (list != null) {
				PotionEffect potioneffect;
				for (Iterator iterator = list.iterator(); iterator.hasNext(); entityliving.addPotionEffect(new PotionEffect(potioneffect))) {
					potioneffect = (PotionEffect)iterator.next();
				}
			}
			if(itemstack.stackSize <= 0) {
				maidInventory.setInventoryCurrentSlotContents(null);
			}
			maidInventory.addItemStackToInventory(new ItemStack(Item.glassBottle));
		}
	}

	@Override
	protected void dropFewItems(boolean par1, int par2) {
		// ﾆ抵ｿｽﾆ辰ﾆ檀窶堋ｳ窶堙ｱ窶堙坂堋ｨ・ｽﾂｻ窶愬凪堙・坦ﾆ坦ﾆ但窶堙・｢s窶凖ｨﾅ蛋窶堙娯ｰﾂｽ窶堋ｩ窶堙・堙・堋ｫ窶堙・堙ｩ窶堙鯉ｿｽI
		int k = rand.nextInt(3 + par2);
		for(int j = 0; j <= k; j++) {
			if(rand.nextInt(30) == 0) {
				dropItem(Item.slimeBall.itemID, 1);
			}
			if(rand.nextInt(50) == 0) {
				entityDropItem(new ItemStack(Item.dyePowder.itemID, 1, 3), 0F);
			}
			dropItem(Item.sugar.itemID, 1);
		}
		
		// ﾆ辰ﾆ停愴遅ﾆ停愴暖ﾆ椎窶堙ｰﾆ置ﾆ蛋ﾆ筑ﾆ単ﾆ抵ｿｽ・ｽI
		maidInventory.dropAllItems();
	}

	@Override
	protected int getDropItemId() {
		return Item.sugar.itemID;
	}

	@Override
	protected int getExperiencePoints(EntityPlayer par1EntityPlayer) {
		return experienceValue;
	}


	@Override
	public void applyEntityCollision(Entity par1Entity) {
		// 窶｢ﾃゑｿｽﾅ・ｽﾃ夲ｿｽG窶ｰﾃｱ窶敕ｰ窶廃
		super.applyEntityCollision(par1Entity);
		
		if (par1Entity instanceof LMM_EntityLittleMaid) {
			if (((LMM_EntityLittleMaid)par1Entity).aiAvoidPlayer.isActive) {
				aiAvoidPlayer.isActive = true;
			}
		} else if (par1Entity == mstatMasterEntity) {
			aiAvoidPlayer.setActive();
		}
	}

	@Override
	protected void updateAITick() {
//		// AI窶佚寂ｰﾅｾﾅ耽窶堙坂堋ｱ窶堙≫堋ｿ窶堋ｪﾅ津・堙寂堙ｪ窶堙ｩ
//		dataWatcher.updateObject(dataWatch_Health, Integer.valueOf(getHealth()));
		
		// 窶凖・ｰﾃ≫｢ﾂｪ
		for (LMM_EntityModeBase ieml : maidEntityModeList) {
			ieml.updateAITick(getMaidModeInt());
		}
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
	}

	/**
	 * 窶凪樞倪吮佚趣ｿｽﾃｴﾆ坦ﾆ痴・ｽ[
	 */
	private boolean isBlockTranslucent(int par1, int par2, int par3) {
		return this.worldObj.isBlockNormalCube(par1, par2, par3);
	}

	/**
	 * 窶凪樞倪吮佚趣ｿｽﾃｴﾆ坦ﾆ痴・ｽ[
	 */
	@Override
	protected boolean pushOutOfBlocks(double par1, double par3, double par5) {
		// EntityPlayerSP窶堙娯堙ｰﾋ・ｸ窶堙≫卍｣窶堙≫堙・堋ｫ窶堋ｽ
		int var7 = MathHelper.floor_double(par1);
		int var8 = MathHelper.floor_double(par3);
		int var9 = MathHelper.floor_double(par5);
		double var10 = par1 - (double)var7;
		double var12 = par5 - (double)var9;
		
		boolean lflag = false;
		for (int li = 0; (float)li < height; li++) {
			lflag |= this.isBlockTranslucent(var7, var8 + li, var9);
		}
		if (lflag) {
			boolean var14 = !this.isBlockTranslucent(var7 - 1, var8, var9) && !this.isBlockTranslucent(var7 - 1, var8 + 1, var9);
			boolean var15 = !this.isBlockTranslucent(var7 + 1, var8, var9) && !this.isBlockTranslucent(var7 + 1, var8 + 1, var9);
			boolean var16 = !this.isBlockTranslucent(var7, var8, var9 - 1) && !this.isBlockTranslucent(var7, var8 + 1, var9 - 1);
			boolean var17 = !this.isBlockTranslucent(var7, var8, var9 + 1) && !this.isBlockTranslucent(var7, var8 + 1, var9 + 1);
			byte var18 = -1;
			double var19 = 9999.0D;
			
			if (var14 && var10 < var19) {
				var19 = var10;
				var18 = 0;
			}
			
			if (var15 && 1.0D - var10 < var19) {
				var19 = 1.0D - var10;
				var18 = 1;
			}
			
			if (var16 && var12 < var19) {
				var19 = var12;
				var18 = 4;
			}
			
			if (var17 && 1.0D - var12 < var19) {
				var19 = 1.0D - var12;
				var18 = 5;
			}
			
			float var21 = 0.1F;
			
			if (var18 == 0) {
				this.motionX = (double)(-var21);
			}
			
			if (var18 == 1) {
				this.motionX = (double)var21;
			}
			
			if (var18 == 4) {
				this.motionZ = (double)(-var21);
			}
			
			if (var18 == 5) {
				this.motionZ = (double)var21;
			}
			
			return !(var14 | var15 | var16 | var17);
		}
		
		return false;
	}

	@Override
	public void onLivingUpdate() {
		// 窶ｰﾃｱ窶｢ﾅ凪敖ｻ窶凖ｨ
		float lhealth = getHealth();
		if (lhealth > 0) {
			if (!worldObj.isRemote) {
				if (getSwingStatusDominant().canAttack()) {
					if (!isBloodsuck()) {
						// 窶凖奇ｿｽﾃｭﾅｽﾅｾ窶堙坂ｰﾃｱ窶｢ﾅ凪妊・ｽﾃｦ
						if (lhealth < getMaxHealth()) {
							if (maidInventory.consumeInventoryItem(Item.sugar.itemID)) {
								eatSugar(true, false);
							}
						}
					}
				}
			}
		}
		
		super.onLivingUpdate();
		
		maidInventory.decrementAnimations();
		// 窶凪樞倪吮佚趣ｿｽﾃｴ
		boolean grave = true;
		grave &= pushOutOfBlocks(posX - (double)width * 0.34999999999999998D, boundingBox.minY, posZ + (double)width * 0.34999999999999998D);
		grave &= pushOutOfBlocks(posX - (double)width * 0.34999999999999998D, boundingBox.minY, posZ - (double)width * 0.34999999999999998D);
		grave &= pushOutOfBlocks(posX + (double)width * 0.34999999999999998D, boundingBox.minY, posZ - (double)width * 0.34999999999999998D);
		grave &= pushOutOfBlocks(posX + (double)width * 0.34999999999999998D, boundingBox.minY, posZ + (double)width * 0.34999999999999998D);
		if (grave && onGround) {
			jump();
		}
		if(lhealth > 0) {
			// 窶ｹﾃ滂ｿｽﾃ塲ﾃ・ｽ窶ｹ窶堙娯凖・ｰﾃ≫堙坂堋ｱ窶堋ｱ
			// ﾆ但ﾆ辰ﾆ弾ﾆ停ぎ窶堙娯ｰﾃｱﾅｽﾃｻ
			if (!worldObj.isRemote) {
				List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(1.0D, 0.0D, 1.0D));
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						Entity entity = (Entity)list.get(i);
						if (!entity.isDead) {
							if (entity instanceof EntityArrow) {
								// 窶愿・ｽﾃｪ窶ｰﾃｱﾅｽﾃｻ
								((EntityArrow)entity).canBePickedUp = 1;
							}
							entity.onCollideWithPlayer(maidAvatar);
						}
					}
					// ﾆ但ﾆ辰ﾆ弾ﾆ停ぎ窶堋ｪﾋ・ｪ窶掖窶堙俄堙遺堙≫堙・堋｢窶堙・但ﾆ辰ﾆ弾ﾆ停ぎ窶堙家耽ﾆ嘆窶堙ｰ窶堙・堙≫堙・堋｢窶堙ｩ・ｽﾃｪ・ｽ窶｡窶堙哉耽ﾆ嘆窶堙ｰﾆ誰ﾆ椎ﾆ但
					if (entityToAttack instanceof EntityItem && maidInventory.getFirstEmptyStack() == -1) {
						setTarget(null);
					}
				}
			}
			// ﾅｽﾅｾﾅ致窶堙ｰﾅｽ・ｽ窶堙≫堙・堋｢窶堙ｩ
			// TODO:窶伉ｽ窶｢ﾂｪ窶堋ｱ窶堙娯｢ﾃ凪堙ｨ窶堙鯉ｿｽﾋ・費ｿｽ窶堙坂堋ｨ窶堋ｩ窶堋ｵ窶堋｢
			if (isContractEX() && mstatClockMaid) {
				// ﾆ嘆・ｽ[ﾆ停ぎ窶愿ﾅｽﾅｾﾅﾃ披堙会ｿｽ窶｡窶堙ｭ窶堋ｹ窶堋ｽ窶ｰﾂｹ・ｽﾂｺ窶堙鯉ｿｽﾃ・ｿｽﾂｶ
				mstatTime = (int)(worldObj.getWorldTime() % 24000);
				if (mstatMasterEntity != null) {
					boolean b = mstatMasterEntity.isPlayerSleeping();
					
					if (mstatMasterDistanceSq < 25D && getEntitySenses().canSee(mstatMasterEntity))	{
						LMM_EnumSound lsound = LMM_EnumSound.Null;
						if (mstatFirstLook && (mstatTime > 23500 || mstatTime < 1500)) {
							lsound = LMM_EnumSound.goodmorning;
							mstatFirstLook = false;
						} 
						else if (!mstatFirstLook && b) {
							lsound = LMM_EnumSound.goodnight;
							mstatFirstLook = true;
						} 
						else if (mstatFirstLook && !b) {
							mstatFirstLook = false;
						}
						
						if (lsound != LMM_EnumSound.Null) {
							playSound(lsound, true);
							setLooksWithInterest(true);
						}
					} else {
						if (!mstatFirstLook && (b || (mstatTime > 18000 && mstatTime < 23500))) {
							mstatFirstLook = true;
						}
					}
				}
			} else {
				mstatTime = 6000;
			}
			
			// TNT-D System
			maidOverDriveTime.onUpdate();
			if (maidOverDriveTime.isDelay()) {
				for (int li = 0; li < mstatSwingStatus.length; li++) {
					mstatSwingStatus[li].attackTime--;
				}
				if (maidOverDriveTime.isEnable()) {
					worldObj.spawnParticle("reddust", (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, 1.2D, 0.4D, 0.4D);
				}
				if (!worldObj.isRemote) {
					Entity lattackentity = getAttackTarget();
					if (lattackentity == null) {
						lattackentity = getEntityToAttack();
					}
					if (lattackentity != null) {
						PathEntity pe = worldObj.getPathEntityToEntity(this, lattackentity, 16F, true, false, false, true);
						
						if (pe != null) {
							pe.incrementPathIndex();
							if (!pe.isFinished()) {
								Vec3 v = pe.getPosition(this);
								setPosition(v.xCoord, v.yCoord, v.zCoord);
							}
						}
					}
				}
			}
			
			if (!worldObj.isRemote) {
				if (getSwingStatusDominant().canAttack()) {
//					mod_LMM_littleMaidMob.Debug("isRemort:" + worldObj.isRemote);
					// 窶ｰﾃｱ窶｢ﾅ・
					if (getHealth() < getMaxHealth()) {
						if (maidInventory.consumeInventoryItem(Item.sugar.itemID)) {
							eatSugar(true, false);
						}
					}
					// 窶堙や堙懌堙晢ｿｽH窶堋｢
					if (rand.nextInt(50000) == 0 && maidInventory.consumeInventoryItem(Item.sugar.itemID)) {
						eatSugar(true, false);
					}
					// ﾅ胆窶禿ｱ・ｽX・ｽV
					if (isContractEX()) {
						float f = getContractLimitDays();
						if (f <= 6 && maidInventory.consumeInventoryItem(Item.sugar.itemID)) {
							// ﾅ胆窶禿ｱ・ｽX・ｽV
							eatSugar(true, true);
						}
					}
				}
			}
		}
	}

	@Override
	public void onUpdate() {
		// Entity・ｽ窶ｰ窶ｰﾃｱ・ｽﾂｶ・ｽﾂｬﾅｽﾅｾ窶堙姑辰ﾆ停愴遅ﾆ停愴暖ﾆ椎・ｽX・ｽV窶廃
		// ﾆ探・ｽ[ﾆ弛・ｽ[窶堙娯｢ﾃｻ窶堋ｪ・ｽﾃｦ窶堙俄ｹN窶慊ｮ窶堋ｷ窶堙ｩ窶堙娯堙・誰ﾆ停ｰﾆ辰ﾆ但ﾆ停愴暖窶伉､窶堋ｪ・ｽX・ｽV窶堙ｰﾅｽﾃｳ窶堋ｯﾅｽﾃｦ窶堙ｪ窶堙遺堋｢
		if (firstload > 0) {
			// ・ｽ窶ｰ窶ｰﾃｱ・ｽX・ｽV窶廃
			// ﾆ探・ｽ[ﾆ弛・ｽ[窶堙娯｢ﾃｻ窶堋ｪ・ｽﾃｦ窶堙俄ｹN窶慊ｮ窶堋ｵ窶堙・堋｢窶堙ｩ窶堙娯堙・ｹﾂｭ・ｽﾂｧ窶愿・堙晢ｿｽﾅｾ窶堙昶堙固ｽﾃｨ・ｽ窶｡窶堋ｪ窶｢K窶牌
			if (--firstload == 0) {
				if (worldObj.isRemote) {
					LMM_Net.sendToEServer(this, new byte[] {LMM_Statics.LMN_Server_UpdateSlots, 0, 0, 0, 0});
				} else {
				}
			}
		}
		
		// 窶敕ｲ窶堙鯛慊ｹ窶ｹﾃｯ窶廃
		weaponFullAuto = false;
		weaponReload = false;
		
		// ﾅｽﾃ･窶堙固m窶戳窶堙遺堙・
		mstatMasterEntity = getMaidMasterEntity();
		if (mstatMasterEntity != null) {
			mstatMasterDistanceSq = getDistanceSqToEntity(mstatMasterEntity);
		}
		// ﾆ停堡断ﾆ停ｹﾆ探ﾆ辰ﾆ炭窶堙姑椎ﾆ但ﾆ停ｹﾆ耽ﾆ辰ﾆ停ぎ窶｢ﾃ擾ｿｽX窶猫窶堙ｨ・ｽH
		textureData.onUpdate();
		// ﾆ椎ﾆ但ﾆ停ｹﾆ耽ﾆ辰ﾆ停ぎ窶｢ﾃ鞘慊ｮ窶冤窶堙ｰﾆ但ﾆ鍛ﾆ致ﾆ断・ｽ[ﾆ暖
		if (worldObj.isRemote) {
			// ﾆ誰ﾆ停ｰﾆ辰ﾆ但ﾆ停愴暖窶伉､
			boolean lupd = false;
			lupd |= updateMaidContract();
			lupd |= updateMaidColor();
//			lupd |= updateTexturePack();
			updateTexturePack();
			if (lupd) {
				setTextureNames();
			}
			setMaidMode(dataWatcher.getWatchableObjectShort(dataWatch_Mode));
			setDominantArm(dataWatcher.getWatchableObjectByte(dataWatch_DominamtArm));
			updateMaidFlagsClient();
			updateGotcha();
		} else {
			boolean lf;
			// ﾆ探・ｽ[ﾆ弛・ｽ[窶伉､
			updateRemainsContract();
			// Overdrive
			lf = maidOverDriveTime.isEnable();
			if (getMaidFlags(dataWatch_Flags_OverDrive) != lf) {
				if (lf) {
					playSound(LMM_EnumSound.TNT_D, true);
				}
				setMaidFlags(lf, dataWatch_Flags_OverDrive);
			}
			// Working!
			lf = mstatWorkingCount.isEnable();
			if (getMaidFlags(dataWatch_Flags_Working) != lf) {
				setMaidFlags(lf, dataWatch_Flags_Working);
			}
			// ・ｽX窶堙銀堙ｩ
			if (!isContractEX() && !isFreedom()) {
				setFreedom(true);
				setMaidWait(false);
			}
			// ﾋ・壺慊ｮ窶伉ｬ窶忸窶堙娯｢ﾃ擾ｿｽX
			AttributeInstance latt = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
			// 窶伉ｮ・ｽﾂｫ窶堙ｰ窶ｰﾃｰ・ｽﾅ・
			latt.removeModifier(attCombatSpeed);
			if (isContract()) {
				if (!isFreedom() || (entityToAttack != null || getAttackTarget() != null)) {
					// 窶伉ｮ・ｽﾂｫ窶堙ｰ・ｽﾃ昶凖ｨ
					latt.applyModifier(attCombatSpeed);
				}
			}
		}
		
		// 窶愿・ｽﾂｩ・ｽﾋ・費ｿｽ窶廃窶突・ｽﾅｾ・ｽﾋ・費ｿｽ
		for (LMM_EntityModeBase leb : maidEntityModeList) {
			leb.onUpdate(maidMode);
		}
		
		
		super.onUpdate();
		// SwingUpdate
		LMM_SwingStatus lmss1 = getSwingStatusDominant();
		prevSwingProgress = maidAvatar.prevSwingProgress = lmss1.prevSwingProgress;
		swingProgress = maidAvatar.swingProgress = lmss1.swingProgress;
		swingProgressInt = maidAvatar.swingProgressInt = lmss1.swingProgressInt;
		isSwingInProgress = maidAvatar.isSwingInProgress = lmss1.isSwingInProgress;
		
		// Aveter窶堙娯突・ｽﾅｾ・ｽﾋ・費ｿｽ
		if (maidAvatar != null) {
			maidAvatar.getValue();
			maidAvatar.onUpdate();
//			maidAvatar.setValue();
		}
		
		// ﾆ谷ﾆ脱ﾆ停愴耽ﾅ地
		if (mstatWaitCount > 0) {
			if (hasPath()) {
				mstatWaitCount = 0;
			} else {
				mstatWaitCount--;
			}
		}
		if (maidSoundInterval > 0) {
			maidSoundInterval--;
		}
		
		// 窶堋ｭ窶堙鯛堋ｩ窶堋ｵ窶堋ｰ	
		prevRotateAngleHead = rotateAngleHead;
		if (getLooksWithInterest()) {
			rotateAngleHead = rotateAngleHead + (1.0F - rotateAngleHead) * 0.4F;
			numTicksToChaseTarget = 10;
		} else {
			rotateAngleHead = rotateAngleHead + (0.0F - rotateAngleHead) * 0.4F;
			if (numTicksToChaseTarget > 0) numTicksToChaseTarget--;
		}
		
		if (getAttackTarget() != null || getEntityToAttack() != null) {
			setWorking(true);
		}
		mstatWorkingCount.onUpdate();
		for (LMM_SwingStatus lmss : mstatSwingStatus) {
			lmss.onUpdate(this);
		}
		LMM_SwingStatus lmss = getSwingStatusDominant();
		prevSwingProgress = maidAvatar.prevSwingProgress = lmss.prevSwingProgress;
		swingProgress = maidAvatar.swingProgress = lmss.swingProgress;
		swingProgressInt = maidAvatar.swingProgressInt = lmss.swingProgressInt;
		isSwingInProgress = maidAvatar.isSwingInProgress = lmss.isSwingInProgress;
		
		// ﾅｽ・ｽ窶堋ｿ窶｢ﾂｨ窶堙固m窶戳
		if (maidInventory.inventoryChanged) {
			onInventoryChanged();
			maidInventory.inventoryChanged = false;
		}
		if (!worldObj.isRemote) {
			// ﾆ探・ｽ[ﾆ弛・ｽ[窶伉､・ｽﾋ・費ｿｽ
			// ﾆ辰ﾆ停愴遅ﾆ停愴暖ﾆ椎窶堙鯉ｿｽX・ｽV
//			if (!mstatOpenInventory) {
				for (int li = 0 ;li < maidInventory.getSizeInventory(); li++) {
					boolean lchange = false;
					int lselect = 0xff;
					// 窶露窶佚ｰ窶倪｢窶敕ｵ窶堋ｪ窶｢ﾃ鞘堙ｭ窶堙≫堋ｽ
					for (int lj = 0; lj < mstatSwingStatus.length; lj++) {
						lchange = mstatSwingStatus[lj].checkChanged();
						if (mstatSwingStatus[lj].index == li) {
							lselect = lj;
						}
					}
					// ﾆ辰ﾆ停愴遅ﾆ停愴暖ﾆ椎窶堙娯吮・ｽg窶堋ｪ窶｢ﾃ鞘堙ｭ窶堙≫堋ｽ
					if (lchange || maidInventory.isChanged(li)) {
						((WorldServer)worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(this, new Packet5PlayerInventory(this.entityId, (li | lselect << 8) + 5, maidInventory.getStackInSlot(li)));
						maidInventory.resetChanged(li);
						mod_LMM_littleMaidMob.Debug(String.format("ID:%d-%s - Slot(%x:%d-%d,%d) Update.", entityId, worldObj.isRemote ? "Client" : "Server", lselect, li, mstatSwingStatus[0].index, mstatSwingStatus[1].index));
					}
//				}
			}
			
			// 窶ｹ|・ｽ\窶堋ｦ
			mstatAimeBow &= !getSwingStatusDominant().canAttack();
			// ・ｽ\窶堋ｦ窶堙鯉ｿｽX・ｽV
			updateAimebow();
			
			// TODO:test
			if (dataWatcher.getWatchableObjectInt(dataWatch_ExpValue) != experienceValue) {
				dataWatcher.updateObject(dataWatch_ExpValue, Integer.valueOf(experienceValue));
			}
			
			// ﾅｽﾂｩ窶｢ﾂｪ窶堙ｦ窶堙ｨ窶佚･窶堋ｫ窶堙遺堙窶堙娯堙搾ｿｽﾃｦ窶堙≫堋ｯ窶堙遺堋｢・ｽiﾆ辰ﾆ谷・ｽﾅ凪堋ｭ・ｽj
			if (riddenByEntity != null && !(riddenByEntity instanceof EntitySquid)) {
				if (height * width < riddenByEntity.height * riddenByEntity.width) {
					if (riddenByEntity instanceof EntityLivingBase) {
						attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)riddenByEntity), 0);
					}
					riddenByEntity.mountEntity(null);
					return;
				}
			}
			
			// 窶｢竄ｬ窶倪｢窶敕ｵﾅｽﾅｾ窶堙搾ｿｽUﾅ停壺氾坂堋ｪ・ｽﾃ｣窶堋ｪ窶堙ｩ
			AttributeInstance latt = this.getEntityAttribute(SharedMonsterAttributes.attackDamage);
			// 窶伉ｮ・ｽﾂｫ窶堙ｰ窶ｰﾃｰ・ｽﾅ・
			latt.removeModifier(attAxeAmp);
			ItemStack lis = getCurrentEquippedItem();
			if (lis != null && lis.getItem() instanceof ItemAxe) {
				// 窶伉ｮ・ｽﾂｫ窶堙ｰ・ｽﾃ昶凖ｨ
				latt.applyModifier(attAxeAmp);
			}
		} else {
			// Client
			// TODO:test
			experienceValue = dataWatcher.getWatchableObjectInt(dataWatch_ExpValue);
		}
		
		// 窶｢R窶堙・ｿｽf窶况
		if(mstatgotcha != null) {
			double d = mstatgotcha.getDistanceSqToEntity(this);
			if (entityToAttack == null) {
				// ﾆ辰ﾆ停愴坦ﾆ停ぎ窶堋ｲ窶堙≫堋ｱ窶廃
				if (d > 4D) {
//                    setPathToEntity(null);
					getNavigator().clearPathEntity();
					getLookHelper().setLookPositionWithEntity(mstatgotcha, 15F, 15F);
				}
				if (d > 12.25D) {
//                    setPathToEntity(worldObj.getPathEntityToEntity(mstatgotcha, this, 16F, true, false, false, true));
					getNavigator().tryMoveToXYZ(mstatgotcha.posX, mstatgotcha.posY, mstatgotcha.posZ, 1.0F);
					getLookHelper().setLookPositionWithEntity(mstatgotcha, 15F, 15F);
				}
			}
			if (d > 25D) {
				double d1 = mstatgotcha.posX - posX;
				double d3 = mstatgotcha.posZ - posZ;
				double d5 = 0.125D / (Math.sqrt(d1 * d1 + d3 * d3) + 0.0625D);
				d1 *= d5;
				d3 *= d5;
				motionX += d1;
				motionZ += d3;
			}
			if (d > 42.25D) {
				double d2 = mstatgotcha.posX - posX;
				double d4 = mstatgotcha.posZ - posZ;
				double d6 = 0.0625D / (Math.sqrt(d2 * d2 + d4 * d4) + 0.0625D);
				d2 *= d6;
				d4 *= d6;
				mstatgotcha.motionX -= d2;
				mstatgotcha.motionZ -= d4;
			}
			if (d > 64D) {
				setGotcha(0);
				mstatgotcha = null;
				playSound("random.drr");
			}
			if(rand.nextInt(16) == 0) {
				List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(8D, 8D, 8D));
				for (int k = 0; k < list.size(); k++) {
					Entity entity = (Entity)list.get(k);
					if (!(entity instanceof EntityMob)) {
						continue;
					}
					EntityMob entitymob = (EntityMob)entity;
					if (entitymob.getEntityToAttack() == mstatgotcha) {
						entitymob.setTarget(this);
					}
				}
			}
		}
		
	}


	@Override
	public void onDeath(DamageSource par1DamageSource) {
		super.onDeath(par1DamageSource);
		
		// ﾅｽ竄ｬﾋ・ｶ窶堙ｰ窶｢\ﾅｽﾂｦ
		if (!worldObj.isRemote) {
			// ﾆ筑ﾆ湛ﾆ耽・ｽ[窶敖ｻ窶凖ｨﾅｽﾂｸ窶捏窶堋ｷ窶堙ｩ窶堋ｩ窶堙・ｽH
			if (mod_LMM_littleMaidMob.cfg_DeathMessage && mstatMasterEntity != null) {
				String ls = par1DamageSource.getDamageType();
				Entity lentity = par1DamageSource.getEntity();
				if (lentity != null) {
					if (par1DamageSource.getEntity() instanceof EntityPlayer) {
						ls += ":" + ((EntityPlayer)lentity).username;  
					} else {
						String lt = EntityList.getEntityString(lentity);
						if (lt != null) {
							ls += ":" + lt;
						}
					}
				}
				String lt = getTranslatedEntityName();
				mstatMasterEntity.addChatMessage(String.format("your %s killed by %s", lt, ls));
			}
		}
	}

	// ﾆ竹・ｽ[ﾆ歎ﾆ停｡ﾆ停愴竪ﾆ稚ﾆ巽ﾆ誰ﾆ暖
	@Override
	protected void onNewPotionEffect(PotionEffect par1PotionEffect) {
		super.onNewPotionEffect(par1PotionEffect);
		if (mstatMasterEntity instanceof EntityPlayerMP) {
			((EntityPlayerMP)mstatMasterEntity).playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(this.entityId, par1PotionEffect));
		}
	}

	@Override
	protected void onChangedPotionEffect(PotionEffect par1PotionEffect, boolean par2) {
		super.onChangedPotionEffect(par1PotionEffect, par2);
		// TODO:窶｢K窶牌窶堋ｩ窶堙・堋､窶堋ｩ窶堙姑蛋ﾆ巽ﾆ鍛ﾆ誰
//		if (mstatMasterEntity instanceof EntityPlayerMP) {
//			((EntityPlayerMP)mstatMasterEntity).playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(this.entityId, par1PotionEffect));
//		}
	}

	@Override
	protected void onFinishedPotionEffect(PotionEffect par1PotionEffect) {
		super.onFinishedPotionEffect(par1PotionEffect);
		if (mstatMasterEntity instanceof EntityPlayerMP) {
			((EntityPlayerMP)mstatMasterEntity).playerNetServerHandler.sendPacketToPlayer(new Packet42RemoveEntityEffect(this.entityId, par1PotionEffect));
		}
	}



	/**
	 *  ﾆ辰ﾆ停愴遅ﾆ停愴暖ﾆ椎窶堋ｪ窶｢ﾃ擾ｿｽX窶堋ｳ窶堙ｪ窶堙懌堋ｵ窶堋ｽ・ｽB
	 */
	public void onInventoryChanged() {
		checkClockMaid();
		checkMaskedMaid();
		checkHeadMount();
		getNextEquipItem();
//		setArmorTextureValue();
	}

	/**
	 * ﾆ辰ﾆ停愴遅ﾆ停愴暖ﾆ椎窶堙俄堋窶堙ｩﾅｽﾅｸ窶堙娯倪｢窶敕ｵ窶｢i窶堙ｰ窶露窶佚ｰ
	 */
	public boolean getNextEquipItem() {
		if (worldObj.isRemote) {
			// ﾆ誰ﾆ停ｰﾆ辰ﾆ但ﾆ停愴暖窶伉､窶堙搾ｿｽﾋ・費ｿｽ窶堋ｵ窶堙遺堋｢
			return false;
		}
		
		int li;
		if (isActiveModeClass()) {
			li = getActiveModeClass().getNextEquipItem(maidMode);
		} else {
			li = -1;
		}
		setEquipItem(maidDominantArm, li);
		return li > -1; 
	}

	public void setEquipItem(int pArm, int pIndex) {
		if (pArm == maidDominantArm) {
			maidInventory.currentItem = pIndex;
		}
		int li = mstatSwingStatus[pArm].index;
		if (li != pIndex) {
			if (li > -1) {
				maidInventory.setChanged(li);
			}
			if (pIndex > -1) {
				maidInventory.setChanged(pIndex);
			}
			mstatSwingStatus[pArm].setSlotIndex(pIndex);
		}
	}
	public void setEquipItem(int pIndex) {
		setEquipItem(maidDominantArm, pIndex);
	}


	/**
	 * 窶佚寂ｰﾅｾﾅ耽ﾅｽﾃ暁停壺｢・ｽﾅﾃｭ窶堙姑椎ﾆ抵ｿｽ・ｽ[ﾆ檀窶敖ｻ窶凖ｨ
	 */
	public void getWeaponStatus() {
		// 窶敕ｲ窶堙鯛慊ｹ窶ｹﾃｯ窶廃窶堙娯愿・ｽﾃｪ・ｽﾋ・費ｿｽ
		ItemStack is = maidInventory.getCurrentItem();
		if (is == null) return;
		
		try {
			Method me = is.getItem().getClass().getMethod("isWeaponReload", ItemStack.class, EntityPlayer.class);
			weaponReload = (Boolean)me.invoke(is.getItem(), is, maidAvatar);
		}
		catch (NoSuchMethodException e) {
		}
		catch (Exception e) {
		}
		
		try {
			Method me = is.getItem().getClass().getMethod("isWeaponFullAuto", ItemStack.class);
			weaponFullAuto = (Boolean)me.invoke(is.getItem(), is);
		}
		catch (NoSuchMethodException e) {
		}
		catch (Exception e) {
		}
	}

	// 窶｢ﾃ崘ｽ・ｽﾆ但ﾆ辰ﾆ弾ﾆ停ぎﾅﾃ麺廣

	/**
	 * ﾅ陳ｻ・ｽﾃ昶堙娯倪｢窶敕ｵ窶｢i
	 */
	public ItemStack getCurrentEquippedItem() {
		return maidInventory.getCurrentItem();
	}
	@Override
	public ItemStack getHeldItem() {
		return maidInventory.getCurrentItem();
	}

	@Override
	public ItemStack getCurrentItemOrArmor(int par1) {
		if (par1 == 0) {
			return getHeldItem();
		} else if (par1 < 5) {
			return maidInventory.armorItemInSlot(par1 - 1);
		} else {
			return maidInventory.getStackInSlot(par1 - 5);
		}
	}

	@Override
	public ItemStack func_130225_q(int par1) {
		return maidInventory.armorItemInSlot(par1);
	}

	@Override
	public void setCurrentItemOrArmor(int par1, ItemStack par2ItemStack) {
		par1 &= 0x0000ffff;
		if (par1 == 0) {
			maidInventory.setInventoryCurrentSlotContents(par2ItemStack);
		} else if (par1 > 0 && par1 < 4) {
			maidInventory.armorInventory[par1 - 1] = par2ItemStack;
			setTextureNames();
		} else if (par1 == 4) {
//			maidInventory.mainInventory[mstatMaskSelect] = mstatMaskSelect > -1 ? par2ItemStack : null;
			if (mstatMaskSelect > -1) {
				maidInventory.mainInventory[mstatMaskSelect] = par2ItemStack;
			}
			setTextureNames();
		} else {
			par1 -= 5;
			// ﾅｽ・ｽ窶堋ｿ窶｢ﾂｨ窶堙姑但ﾆ鍛ﾆ致ﾆ断・ｽ[ﾆ暖
			// 窶愿・ｽﾂｩﾅg窶卍｣:窶｢・ｽ窶凖岩堙家湛ﾆ抵ｿｽﾆ鍛ﾆ暖窶敕費ｿｽ窶窶堙娯凖岩堙ｨ・ｽA・ｽﾃ｣ﾋ・岩啗ﾆ池ﾆ鍛ﾆ暖窶堙坂倪｢窶敕ｵﾆ湛ﾆ抵ｿｽﾆ鍛ﾆ暖
			// par1窶堙拘hort窶堙・從窶堋ｳ窶堙ｪ窶堙ｩ窶堙娯堙・堋ｻ窶堙娯堙ｦ窶堋､窶堙会ｿｽB
			int lslotindex = par1 & 0x7f;
			int lequip = (par1 >>> 8) & 0xff;
			maidInventory.setInventorySlotContents(lslotindex, par2ItemStack);
			maidInventory.resetChanged(lslotindex);	// 窶堋ｱ窶堙ｪ窶堙才・凪督｡窶堙遺堋｢窶堋ｯ窶堙・堙茨ｿｽB
			maidInventory.inventoryChanged = true;
//			if (par1 >= maidInventory.mainInventory.length) {
//				LMM_Client.setArmorTextureValue(this);
//			}

			for (LMM_SwingStatus lss: mstatSwingStatus) {
				if (lslotindex == lss.index) {
					lss.index = -1;
				}
			}
			if (lequip != 0xff) {
				setEquipItem(lequip, lslotindex);
//				mstatSwingStatus[lequip].index = lslotindex;
			}
			if (lslotindex >= maidInventory.maxInventorySize) {
				setTextureNames();
			}
			String s = par2ItemStack == null ? null : par2ItemStack.getDisplayName();
			mod_LMM_littleMaidMob.Debug(String.format("ID:%d Slot(%2d:%d):%s", entityId, lslotindex, lequip, s == null ? "NoItem" : s));
		}
	}

	@Override
	public ItemStack[] getLastActiveItems() {
		return maidInventory.armorInventory;
	}

	protected void checkClockMaid() {
		// ﾅｽﾅｾﾅ致窶堙ｰﾅｽ・ｽ窶堙≫堙・堋｢窶堙ｩ窶堋ｩ・ｽH
		mstatClockMaid = maidInventory.getInventorySlotContainItem(Item.pocketSundial.itemID) > -1;
	}
	/**
	 * ﾅｽﾅｾﾅ致窶堙ｰﾅｽ・ｽ窶堙≫堙・堋｢窶堙ｩ窶堋ｩ?
	 */
	public boolean isClockMaid() {
		return mstatClockMaid;
	}

	protected void checkMaskedMaid() {
		// ﾆ辰ﾆ停愴遅ﾆ停愴暖ﾆ椎窶堙家蜘ﾆ停ｹﾆ停ぎ窶堋ｪ窶堋窶堙ｩ窶堋ｩ・ｽH
		for (int i = maidInventory.mainInventory.length - 1; i >= 0; i--) {
			ItemStack is = maidInventory.getStackInSlot(i);
			if (is != null && is.getItem() instanceof ItemArmor && ((ItemArmor)is.getItem()).armorType == 0) {
				// ﾆ蜘ﾆ停ｹﾆ停ぎ窶堙ｰﾅｽ・ｽ窶堙≫堙・堙ｩ
				mstatMaskSelect = i;
				maidInventory.armorInventory[3] = is;
				if (worldObj.isRemote) {
					setTextureNames();
				}
				return;
			}
		}
		
		mstatMaskSelect = -1;
		maidInventory.armorInventory[3] = null;
		return;
	}
	/**
	 * ﾆ抵ｿｽﾆ鍛ﾆ暖窶堙ｰ窶敕ｭ窶堙≫堙・堙ｩ窶堋ｩ 
	 */
	public boolean isMaskedMaid() {
		return mstatMaskSelect > -1;
	}

	protected void checkHeadMount() {
		// 窶凖・ｰﾃ≫堙娯慊ｪ窶｢窶昶倪｢窶敕ｵ窶堙娯敖ｻ窶凖ｨ
		ItemStack lis = maidInventory.getHeadMount();
		mstatPlanter = false;
		mstatCamouflage = false;
		if (lis != null) {
			if (lis.getItem() instanceof ItemBlock) {
				Block lblock = Block.blocksList[lis.getItem().itemID];
				mstatPlanter = (lblock instanceof BlockFlower) && lblock.getRenderType() == 1;
				mstatCamouflage = (lblock instanceof BlockLeaves) || (lblock instanceof BlockPumpkin);
			} else if (lis.getItem() instanceof ItemSkull) {
				mstatCamouflage = true;
			}
		}		
	}
	/**
	 * ﾆ谷ﾆ停堡稚ﾆ停ｰ・ｽ[ﾆ淡ﾆ停ｦ・ｽI 
	 */
	public boolean isCamouflage() {
		return mstatCamouflage;
	}
	/**
	 * 窶敖ｫ・ｽA窶堋ｦ・ｽﾃｳ窶佚・
	 */
	public boolean isPlanter() {
		return mstatPlanter;
	}

	/**
	 * ﾆ竹・ｽ[ﾆ歎ﾆ停｡ﾆ停懌懌┐窶堙俄堙ｦ窶堙ｩﾋ徨・ｽU窶堙ｨﾆ停夲ｿｽ[ﾆ歎ﾆ停｡ﾆ停懌堙娯伉ｬ窶忸窶｢ﾃ｢・ｽﾂｳ
	 */
	public int getSwingSpeedModifier() {
		if (isPotionActive(Potion.digSpeed)) {
			return 6 - (1 + getActivePotionEffect(Potion.digSpeed).getAmplifier()) * 1;
		}
		
		if (isPotionActive(Potion.digSlowdown)) {
			return 6 + (1 + getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2;
		} else {
			return 6;
		}
	}

	/**
	 * ﾅｽﾃｨﾅｽ・ｽ窶堋ｿﾆ但ﾆ辰ﾆ弾ﾆ停ぎ窶堙娯挧窶ｰﾃｳ
	 */
	public void destroyCurrentEquippedItem() {
		maidInventory.setInventoryCurrentSlotContents(null);
	}

	/**
	 * ﾆ抵ｿｽﾆ辰ﾆ檀ﾆ辰ﾆ停愴遅ﾆ停愴暖ﾆ椎窶堙ｰﾅJ窶堋ｭ
	 * @param pEntityPlayer
	 */
	public void displayGUIMaidInventory(EntityPlayer pEntityPlayer) {
		if (!worldObj.isRemote) {
			// server
			Container lcontainer = new LMM_ContainerInventory(pEntityPlayer.inventory, this);
			ModLoader.serverOpenWindow((EntityPlayerMP)pEntityPlayer, lcontainer, mod_LMM_littleMaidMob.containerID, entityId, 0, 0);
		}
	}

	@Override
	public boolean interact(EntityPlayer par1EntityPlayer) {
		float lhealth = getHealth();
		ItemStack itemstack1 = par1EntityPlayer.getCurrentEquippedItem();
		
		// ﾆ致ﾆ停ｰﾆ丹ﾆ辰ﾆ停懌堙・堙鯉ｿｽﾋ・費ｿｽ窶堙ｰ・ｽﾃｦ窶堙会ｿｽs窶堋､
		for (int li = 0; li < maidEntityModeList.size(); li++) {
			if (maidEntityModeList.get(li).preInteract(par1EntityPlayer, itemstack1)) {
				return true;
			}
		}
		// 窶堋ｵ窶堙｡窶堋ｪ窶堙敘ｽﾅｾ窶堙搾ｿｽﾋ・費ｿｽ窶督ｳﾅ津ｸ
		if (par1EntityPlayer.isSneaking()) {
			return false;
		}
		// ﾆ段ﾆ断ﾆ椎窶敖ｻ窶凖ｨ
		if (lhealth > 0F && par1EntityPlayer.riddenByEntity != null && !(par1EntityPlayer.riddenByEntity instanceof LMM_EntityLittleMaid)) {
			// ・ｽﾃ壺堋ｹ窶佚問堋ｦ
			par1EntityPlayer.riddenByEntity.mountEntity(this);
			return true;
		}
		
		
		
		if (mstatgotcha == null && par1EntityPlayer.fishEntity == null) {
			if(itemstack1 != null && itemstack1.itemID == Item.silk.itemID) {
				// 窶｢R窶堙・智窶堋ｮ
				setGotcha(par1EntityPlayer.entityId);
				mstatgotcha = par1EntityPlayer;
				MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
				playSound("random.pop");
				return true;
			} 
			
			if (isContract()) {
				// ﾅ胆窶禿ｱ・ｽﾃｳ窶佚・
				if (lhealth > 0F && isMaidContractOwner(par1EntityPlayer)) {
					if (itemstack1 != null) {
						// 窶凖・ｰﾃ≫｢ﾂｪ窶堙鯉ｿｽﾋ・費ｿｽ
						setPathToEntity(null);
						// ﾆ致ﾆ停ｰﾆ丹ﾆ辰ﾆ停懌堙・堙鯉ｿｽﾋ・費ｿｽ窶堙ｰ・ｽﾃｦ窶堙会ｿｽs窶堋､
						for (int li = 0; li < maidEntityModeList.size(); li++) {
							if (maidEntityModeList.get(li).interact(par1EntityPlayer, itemstack1)) {
								return true;
							}
						}
						if (isRemainsContract()) {
							// 窶凖奇ｿｽﾃｭ
							if (itemstack1.itemID == Item.sugar.itemID) {
								// ﾆ停夲ｿｽ[ﾆ檀・ｽﾃ倪佚・
								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
								eatSugar(false, true);
								worldObj.setEntityState(this, (byte)11);
								
								mod_LMM_littleMaidMob.Debug("give suger." + worldObj.isRemote);
								if (!worldObj.isRemote) {
									setFreedom(isFreedom());
									if (isMaidWait()) {
										// 窶慊ｮ・ｽﾃｬﾆ停夲ｿｽ[ﾆ檀窶堙鯉ｿｽﾃ倪佚・
										boolean lflag = false;
										setActiveModeClass(null);
										for (int li = 0; li < maidEntityModeList.size() && !lflag; li++) {
											lflag = maidEntityModeList.get(li).changeMode(par1EntityPlayer);
											if (lflag) {
												setActiveModeClass(maidEntityModeList.get(li));
											}
										}
										if (!lflag) {
											setMaidMode("Escorter");
											setEquipItem(-1);
//	    									maidInventory.currentItem = -1;
										}
										setMaidWait(false);
										getNextEquipItem();
									} else {
										// 窶佚停ｹ@
										setMaidWait(true);
									}
								}
								return true;
							}
							else if (itemstack1.itemID == Item.dyePowder.itemID) {
								// ﾆ谷ﾆ停ｰ・ｽ[ﾆ抵ｿｽﾆ辰ﾆ檀
								if (!worldObj.isRemote) {
									setColor(15 - itemstack1.getItemDamage());
								}
								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
								return true;
							}
							else if (itemstack1.itemID == Item.feather.itemID) {
								// ﾅｽﾂｩ窶燃・ｽs窶慊ｮ
								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
								setFreedom(!isFreedom());
								worldObj.setEntityState(this, isFreedom() ? (byte)12 : (byte)13);
								return true;
							}
							else if (itemstack1.itemID == Item.saddle.itemID) {
								// ﾅ陳ｨﾅｽﾃ・
								if (!worldObj.isRemote) {
									if (ridingEntity == par1EntityPlayer) {
										this.mountEntity(null);
									} else {
										this.mountEntity(par1EntityPlayer);
									}
									return true;
								}
							}
							else if (itemstack1.itemID == Item.gunpowder.itemID) {
								// test TNT-D
//								playSound(LMM_EnumSound.eatGunpowder, false);
								maidOverDriveTime.setValue(itemstack1.stackSize * 10);
								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, itemstack1.stackSize);
								return true;
							}
							else if (itemstack1.itemID == Item.book.itemID) {
								// IFF窶堙姑棚・ｽ[ﾆ致ﾆ停・
								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
//	    		            	ModLoader.openGUI(par1EntityPlayer, new LMM_GuiIFF(worldObj, this));
								if (worldObj.isRemote) {
									LMM_Client.OpenIFF(this, par1EntityPlayer);
								}
								return true;
							}
							else if ((itemstack1.itemID == Item.glassBottle.itemID) && (experienceValue >= 5)) {
								// Expﾆ畜ﾆ暖ﾆ停ｹ
								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
								if (!worldObj.isRemote) {
									entityDropItem(new ItemStack(Item.expBottle), 0.5F);
									experienceValue -= 5;
									if (maidAvatar != null) {
										maidAvatar.experienceTotal -= 5;
									}
								}
								return true;
							}
							else if (itemstack1.getItem() instanceof ItemPotion) {
								// ﾆ竹・ｽ[ﾆ歎ﾆ停｡ﾆ停・
								if(!worldObj.isRemote) {
									List list = ((ItemPotion)itemstack1.getItem()).getEffects(itemstack1);
									if (list != null) {
										PotionEffect potioneffect;
										for (Iterator iterator = list.iterator(); iterator.hasNext(); addPotionEffect(new PotionEffect(potioneffect))) {
											potioneffect = (PotionEffect)iterator.next();
										}
									}
								}
								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
								return true;
							}
							else if (isFreedom() && itemstack1.itemID == Item.redstone.itemID) {
								// Tracer
								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
								setPathToEntity(null);
								setMaidWait(false);
								setTracer(!isTracer());
								if (isTracer()) {
									worldObj.setEntityState(this, (byte)14);
								} else {
									worldObj.setEntityState(this, (byte)12);
								}
								
								return true;
							}
						} else {
							// ﾆ湛ﾆ暖ﾆ停ｰﾆ辰ﾆ鱈
							if (itemstack1.itemID == Item.sugar.itemID) {
								// ﾅｽﾃｳﾅｽﾃｦ窶ｹ窶倪敕・
								worldObj.setEntityState(this, (byte)10);
								return true;
							} else if (itemstack1.itemID == Item.cake.itemID) {
								// ・ｽﾃ・胆窶禿ｱ
								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
								maidContractLimit = (24000 * 7);
								setFreedom(false);
								setTracer(false);
								setMaidWait(false);
								setMaidMode("Escorter");
								worldObj.setEntityState(this, (byte)11);
								playSound(LMM_EnumSound.Recontract, true);
								return true;
							}
						}
					}
					// ﾆ抵ｿｽﾆ辰ﾆ檀ﾆ辰ﾆ停愴遅ﾆ停愴暖ﾆ椎
					setOwner(par1EntityPlayer.username);
					getNavigator().clearPathEntity();
					isJumping = false;
					displayGUIMaidInventory(par1EntityPlayer);
//    		        	ModLoader.openGUI(par1EntityPlayer, new LMM_GuiInventory(this, par1EntityPlayer.inventory, maidInventory));
//    				serchedChest.clear();
					return true;
				}
			} else {
				// 窶督｢ﾅ胆窶禿ｱ
				if (itemstack1 != null) {
					if (itemstack1.itemID == Item.cake.itemID) {
						// ﾅ胆窶禿ｱ
						MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
						
						deathTime = 0;
						if (!worldObj.isRemote) {
							par1EntityPlayer.triggerAchievement(mod_LMM_littleMaidMob.ac_Contract);
							setContract(true);
							setOwner(par1EntityPlayer.username);
							setHealth(20);
							setMaidMode("Escorter");
							setMaidWait(false);
							setFreedom(false);
							playSound(LMM_EnumSound.getCake, true);
//							playLittleMaidSound(LMM_EnumSound.getCake, true);
//    	                    playTameEffect(true);
							worldObj.setEntityState(this, (byte)7);
							// ﾅ胆窶禿ｱ窶ｹL窶抂窶愿ｺ窶堙・ｿｽA・ｽ窶ｰﾅﾃｺﾅ胆窶禿ｱﾅﾃｺﾅﾃ・
							maidContractLimit = (24000 * 7);
							maidAnniversary = worldObj.getWorldTime();
							// ﾆ弾ﾆ誰ﾆ湛ﾆ蛋ﾆ槌停堙姑但ﾆ鍛ﾆ致ﾆ断・ｽ[ﾆ暖:窶堋｢窶堙ｧ窶堙ｱ・ｽH
//							LMM_Net.sendToAllEClient(this, new byte[] {LMM_Net.LMN_Client_UpdateTexture, 0, 0, 0, 0});
							
						}
						return true;
					} else {
//    	                worldObj.setEntityState(this, (byte)6);
					}
				}
			}
		} else if (lhealth > 0F && mstatgotcha != null) {
			if (!worldObj.isRemote) {
				EntityItem entityitem = new EntityItem(worldObj, mstatgotcha.posX, mstatgotcha.posY, mstatgotcha.posZ, new ItemStack(Item.silk));
				worldObj.spawnEntityInWorld(entityitem);
				setGotcha(0);
				mstatgotcha = null;
			}
			return true;
		} 
		
		return false;
	}

	// ﾆ抵ｿｽﾆ辰ﾆ檀窶堙固胆窶禿ｱ・ｽﾃ昶凖ｨ
	@Override
	public boolean isTamed() {
		return isContract();
	}
	public boolean isContract() {
//		return worldObj.isRemote ? maidContract : super.isTamed();
		return super.isTamed();
	}
	public boolean isContractEX() {
		return isContract() && isRemainsContract();
	}

	@Override
	public void setTamed(boolean par1) {
		setContract(par1);
	}
	@Override
	public void setContract(boolean flag) {
		super.setTamed(flag);
		textureData.setContract(flag);
		if (flag) {
//        	maidMode = mmode_Escorter;
		} else {
		}
	}

	/**
	 * ﾅ胆窶禿ｱﾅﾃｺﾅﾃ披堙固ｽc窶堙ｨ窶堋ｪ窶堋窶堙ｩ窶堋ｩ窶堙ｰﾅm窶戳
	 */
	protected void updateRemainsContract() {
		boolean lflag = false;
		if (maidContractLimit > 0) {
			maidContractLimit--;
			lflag = true;
		}
		if (getMaidFlags(dataWatch_Flags_remainsContract) != lflag) {
			setMaidFlags(lflag, dataWatch_Flags_remainsContract);
		}
	}
	/**
	 * ﾆ湛ﾆ暖ﾆ停ｰﾆ辰ﾆ鱈窶堙俄愿ｼ窶堙≫堙・堋｢窶堙遺堋｢窶堋ｩ窶敖ｻ窶凖ｨ
	 * @return
	 */
	public boolean isRemainsContract() {
		return getMaidFlags(dataWatch_Flags_remainsContract);
	}

	public float getContractLimitDays() {
		return maidContractLimit > 0 ? ((float)maidContractLimit / 24000F) : -1F;
	}

	public boolean updateMaidContract() {
		// 窶慊ｯﾋ・ｪ・ｽﾂｫ窶堙姑蛋ﾆ巽ﾆ鍛ﾆ誰
		boolean lf = isContract();
		if (textureData.isContract() != lf) {
			textureData.setContract(lf);
			return true;
		}
		return false;
	}

	@Override
	public Entity getOwner() {
		return getMaidMasterEntity();
	}
	public String getMaidMaster() {
		return getOwnerName();
	}

	public EntityPlayer getMaidMasterEntity() {
		// ﾅｽﾃ･窶堙ｰﾅl窶慊ｾ
		if (isContract()) {
			EntityPlayer entityplayer = mstatMasterEntity;
			if (mstatMasterEntity == null || mstatMasterEntity.isDead) {
				String lname; 
				// ﾆ探・ｽ[ﾆ弛・ｽ[窶伉､窶堙遺堙ｧ窶堋ｿ窶堙｡窶堙ｱ窶堙・棚・ｽ[ﾆ段窶敖ｻ窶凖ｨ窶堋ｷ窶堙ｩ
				if (!MMM_Helper.isClient
						|| mod_LMM_littleMaidMob.cfg_checkOwnerName 
						|| MMM_Helper.mc.thePlayer == null) {
					lname = getMaidMaster();
				} else {
					lname = MMM_Helper.mc.thePlayer.username;
				}
				entityplayer = worldObj.getPlayerEntityByName(lname);
				// 窶堙・堙ｨ窶堋窶堋ｦ窶堋ｸﾅｽﾃ･窶堙娯督ｼ窶楼窶堙ｰ窶愿ｼ窶堙ｪ窶堙・堙昶堙ｩ
				// TODO:・ｽﾃ・ｿｽﾃ昶凖ｨ窶堙坂｢s窶ｰﾃや堙俄堙遺堙≫堋ｽ窶堙娯堙・弛窶ｰﾃ淒ﾃ焦ｽ@
//				maidAvatar.username = lname;
				
				if (entityplayer != null && maidAvatar != null) {
					maidAvatar.capabilities.isCreativeMode = entityplayer.capabilities.isCreativeMode;
				}
				
			}
			return entityplayer;
		} else {
			return null;
		}
	}

	public boolean isMaidContractOwner(String pname) {
		return pname.equalsIgnoreCase(mstatMasterEntity.username);
	}

	public boolean isMaidContractOwner(EntityPlayer pentity) {
		return pentity == getMaidMasterEntity();
		
//		return pentity == mstatMasterEntity;
	}

	// ﾆ抵ｿｽﾆ辰ﾆ檀窶堙娯佚停ｹ@・ｽﾃ昶凖ｨ
	public boolean isMaidWait() {
		return maidWait;
	}

	public boolean isMaidWaitEx() {
		return isMaidWait() | (mstatWaitCount > 0) | isOpenInventory();
	}

	public void setMaidWait(boolean pflag) {
		// 窶佚停ｹ@・ｽﾃｭ窶佚披堙鯉ｿｽﾃ昶凖ｨ・ｽA isMaidWaitﾅ地窶堙・rue窶堙ｰ窶｢ﾃ披堋ｷ窶堙遺堙ｧAI窶堋ｪ・ｽﾅｸﾅｽﾃｨ窶堙架・壺慊ｮ窶堙ｰ窶凖｢ﾅｽ~窶堋ｳ窶堋ｹ窶堙ｩ・ｽB
		maidWait = pflag;
		setMaidFlags(pflag, dataWatch_Flags_Wait);
		
		aiSit.setSitting(pflag);
		maidWait = pflag;
		isJumping = false;
		setAttackTarget(null);
		setRevengeTarget(null);
		setPathToEntity(null);
		getNavigator().clearPathEntity();
		velocityChanged = true;
	}

	public void setMaidWaitCount(int count) {
		mstatWaitCount = count;
	}

	
	// ﾆ辰ﾆ停愴遅ﾆ停愴暖ﾆ椎窶堙娯｢\ﾅｽﾂｦﾅﾃ滅淡
	// 窶堙懌堋ｳ窶堋ｮ窶堙ｪ窶堙ｩ窶堙娯堙才・ｪ・ｽl窶堋ｾ窶堋ｯ
	public void setOpenInventory(boolean flag) {
		mstatOpenInventory = flag;
	}

	public boolean isOpenInventory() {
		return mstatOpenInventory;
	}

	/**
	 * GUI窶堙ｰﾅJ窶堋｢窶堋ｽﾅｽﾅｾ窶堙家探・ｽ[ﾆ弛・ｽ[窶伉､窶堙・津・堙寂堙ｪ窶堙ｩ・ｽB
	 */
	public void onGuiOpened() {
		setOpenInventory(true);
	}

	/**
	 * GUI窶堙ｰ窶｢ﾃや堙溪堋ｽﾅｽﾅｾ窶堙家探・ｽ[ﾆ弛・ｽ[窶伉､窶堙・津・堙寂堙ｪ窶堙ｩ・ｽB
	 */
	public void onGuiClosed() {
		setOpenInventory(false);
		int li = maidMode & 0x0080;
		setMaidWaitCount((li == 0) ? 50 : 0);
	}

	// ﾋ徨・ｽU窶堙ｨ
	public void setSwing(int attacktime, LMM_EnumSound enumsound) {
		setSwing(attacktime, enumsound, maidDominantArm);
	}
	public void setSwing(int pattacktime, LMM_EnumSound enumsound, int pArm) {
		mstatSwingStatus[pArm].attackTime = pattacktime;
//		maidAttackSound = enumsound;
//        soundInterval = 0;// 窶堋｢窶堙ｩ窶堋ｩ・ｽH
		if (!weaponFullAuto) {
			setSwinging(pArm, enumsound);
		}
		if (!worldObj.isRemote) {
			byte[] lba = new byte[] {
				LMM_Statics.LMN_Client_SwingArm,
				0, 0, 0, 0,
				(byte)pArm,
				0, 0, 0, 0
			};
			MMM_Helper.setInt(lba, 6, enumsound.index);
			LMM_Net.sendToAllEClient(this, lba);
		}
	}

	public void setSwinging(LMM_EnumSound pSound) {
		setSwinging(maidDominantArm, pSound);
	}
	public void setSwinging(int pArm, LMM_EnumSound pSound) {
		if (mstatSwingStatus[pArm].setSwinging()) {
			playLittleMaidSound(pSound, true);
			maidAvatar.swingProgressInt = -1;
			maidAvatar.isSwingInProgress = true;
		}
	}

	public boolean getSwinging() {
		return getSwinging(maidDominantArm);
	}
	public boolean getSwinging(int pArm) {
		return mstatSwingStatus[pArm].isSwingInProgress;
	}

	/**
	 * 窶藩懌堋ｫﾋ徨窶堙姑椎ﾆ抵ｿｽ・ｽ[ﾆ檀ﾆ耽ﾆ辰ﾆ停ぎ
	 */
	public LMM_SwingStatus getSwingStatusDominant() {
		return mstatSwingStatus[maidDominantArm];
	}

	public LMM_SwingStatus getSwingStatus(int pindex) {
		return mstatSwingStatus[pindex];
	}


	// ・ｽﾂ｡・ｽﾂｪ窶堙姑抵ｿｽﾆ辰ﾆ檀窶堙最椎停堙俄ｹQ窶堋ｦ窶堙・堋ｨ窶堙ｩ
	public void setBloodsuck(boolean pFlag) {
		mstatBloodsuck = pFlag;
		setMaidFlags(pFlag, dataWatch_Flags_Bloodsuck);
	}

	public boolean isBloodsuck() {
		return mstatBloodsuck;
	}


	// ・ｽﾂｻ窶愬毒ﾃ麺廣
	public void setLookSuger(boolean pFlag) {
		mstatLookSuger = pFlag;
		setMaidFlags(pFlag, dataWatch_Flags_LooksSugar);
	}

	public boolean isLookSuger() {
		return mstatLookSuger;
	}

	/**
	 * ﾆ馳ﾆ抵ｿｽﾆ鍛・ｽE・ｽE・ｽE窶堋ｱ窶堙ｪ窶堙搾ｿｽE・ｽE・ｽE・ｽﾂｻ窶愬独鍛・ｽI・ｽI
	 * motion : ﾋ徨窶堙ｰ・ｽU窶堙ｩ窶堋ｩ・ｽH
	 * recontract : ﾅ胆窶禿ｱ窶ｰ窶樞卍ｷﾅ津ｸ窶ｰﾃ竿但ﾆ椎・ｽH
	 */
	public void eatSugar(boolean motion, boolean recontract) {
		if (motion) {
			setSwing(2, (getMaxHealth() - getHealth() <= 1F) ?  LMM_EnumSound.eatSugar_MaxPower : LMM_EnumSound.eatSugar);
		}
		int h = hurtResistantTime;
		heal(1);
		hurtResistantTime = h;
		playSound("random.pop");
		mod_LMM_littleMaidMob.Debug(("eat Suger." + worldObj.isRemote));
		
		if (recontract) {
			// ﾅ胆窶禿ｱﾅﾃｺﾅﾃ披堙娯ｰ窶樞卍ｷ
			maidContractLimit += 24000;
			if (maidContractLimit > 168000) {
				maidContractLimit = 168000;	// 24000 * 7
			}
		}
		
		// ﾅｽb窶凖ｨ・ｽﾋ・費ｿｽ
		if (maidAvatar != null) {
			maidAvatar.getFoodStats().addStats(20, 20F);
		}
	}


	// 窶堋ｨﾅｽdﾅｽ窶独蛋ﾆ停ｦ
	/**
	 * ﾅｽdﾅｽ窶凪吮窶堋ｩ窶堙・堋､窶堋ｩ窶堙鯉ｿｽﾃ昶凖ｨ
	 */
	public void setWorking(boolean pFlag) {
		mstatWorkingCount.setEnable(pFlag);
	}
	
	/**
	 * ﾅｽdﾅｽ窶凪吮窶堋ｩ窶堙・堋､窶堋ｩ窶堙ｰ窶｢ﾃ披堋ｷ
	 */
	public boolean isWorking() {
		return mstatWorkingCount.isEnable();
	}

	/**
	 * ﾅｽdﾅｽ窶凪堋ｪ・ｽI窶板ｹ窶堋ｵ窶堙・堙窶脳窶ｰC窶堙ｰﾅﾃ懌堙溪堙・｢ﾃ披堋ｷ
	 */
	public boolean isWorkingDelay() {
		return mstatWorkingCount.isDelay();
	}

	/**
	 * ﾆ暖ﾆ椎抵ｿｽ[ﾆ探・ｽ[ﾆ停夲ｿｽ[ﾆ檀窶堙鯉ｿｽﾃ昶凖ｨ
	 */
	public void setTracer(boolean pFlag) {
		maidTracer = pFlag;
		setMaidFlags(pFlag, dataWatch_Flags_Tracer);
		if (maidTracer) {
			setFreedom(true);
		}
		aiTracer.setEnable(pFlag);
	}

	/**
	 * ﾆ暖ﾆ椎抵ｿｽ[ﾆ探・ｽ[ﾆ停夲ｿｽ[ﾆ檀窶堙・堋窶堙ｩ窶堋ｩ・ｽH
	 */
	public boolean isTracer() {
		return maidTracer;
	}


	// 窶堋ｨ窶之窶堙柁停夲ｿｽ[ﾆ檀
	public void setPlayingRole(int pValue) {
		if (mstatPlayingRole != pValue) {
			mstatPlayingRole = pValue;
			if (pValue == 0) {
				setAttackTarget(null);
				setMaidMode(mstatWorkingInt , true);
			} else {
				setMaidMode(0x00ff, true);
			}
		}
	}

	public int getPlayingRole() {
		return mstatPlayingRole;
	}

	public boolean isPlaying() {
		return mstatPlayingRole != 0;
	}


	// ﾅｽﾂｩ窶燃・ｽs窶慊ｮ
	public void setFreedom(boolean pFlag) {
		// AIﾅﾃ麺廣窶堙姑椎ﾆ短ﾆ鍛ﾆ暖窶堙窶堋ｱ窶堋ｱ窶堙・ｿｽB
		maidFreedom = pFlag;
		aiRestrictRain.setEnable(pFlag);
		aiFreeRain.setEnable(pFlag);
		aiWander.setEnable(pFlag);
//		aiJumpTo.setEnable(!pFlag);
		aiAvoidPlayer.setEnable(!pFlag);
		aiFollow.setEnable(!pFlag);
		aiTracer.setEnable(false);
//		setAIMoveSpeed(pFlag ? moveSpeed_Nomal : moveSpeed_Max);
//		setMoveForward(0.0F);
		
		if (maidFreedom && isContract()) {
			setHomeArea(
					MathHelper.floor_double(posX),
					MathHelper.floor_double(posY),
					MathHelper.floor_double(posZ), 16);
		} else {
			detachHome();
			setPlayingRole(0);
		}
		
		setMaidFlags(maidFreedom, dataWatch_Flags_Freedom);
	}

	public boolean isFreedom() {
		return maidFreedom;
	}


	/**
	 * ﾆ探・ｽ[ﾆ弛・ｽ[窶堙免弾ﾆ誰ﾆ湛ﾆ蛋ﾆ槌槌恥ﾆ鍛ﾆ誰窶堙姑辰ﾆ停愴断ﾆ鍛ﾆ誰ﾆ湛窶堙ｰ窶倪披堙ｩ・ｽB
	 * ﾆ誰ﾆ停ｰﾆ辰ﾆ但ﾆ停愴暖窶伉､窶堙鯉ｿｽﾋ・費ｿｽ
	 */
	protected boolean sendTextureToServer() {
		// 16bit窶堋窶堙ｪ窶堙屡弾ﾆ誰ﾆ湛ﾆ蛋ﾆ槌槌恥ﾆ鍛ﾆ誰窶堙鯉ｿｽ窶昶堙俄堋ｽ窶堙ｨ窶堙ｱ窶堙・
		MMM_TextureManager.instance.postSetTexturePack(this, textureData.getColor(), textureData.getTextureBox());
		return true;
	}


	public boolean updateTexturePack() {
		// ﾆ弾ﾆ誰ﾆ湛ﾆ蛋ﾆ槌槌恥ﾆ鍛ﾆ誰窶堋ｪ・ｽX・ｽV窶堋ｳ窶堙ｪ窶堙・堋｢窶堙遺堋｢窶堋ｩ窶堙ｰﾆ蛋ﾆ巽ﾆ鍛ﾆ誰
		// ﾆ誰ﾆ停ｰﾆ辰ﾆ但ﾆ停愴暖窶伉､窶堙・
		boolean lflag = false;
		MMM_TextureBoxServer lbox;
		
		int ltexture = dataWatcher.getWatchableObjectInt(dataWatch_Texture);
		int larmor = (ltexture >>> 16) & 0xffff;
		ltexture &= 0xffff;
		if (textureData.textureIndex[0] != ltexture) {
			textureData.textureIndex[0] = ltexture;
			lflag = true;
		}
		if (textureData.textureIndex[1] != larmor) {
			textureData.textureIndex[1] = larmor;
			lflag = true;
		}
		if (lflag) {
			MMM_TextureManager.instance.postGetTexturePack(this, textureData.getTextureIndex());
		}
		return lflag;
	}

	@Override
	public int getColor() {
//		return textureData.getColor();
		return dataWatcher.getWatchableObjectByte(dataWatch_Color);
	}

	@Override
	public void setColor(int index) {
		textureData.setColor(index);
		dataWatcher.updateObject(dataWatch_Color, (byte)index);
	}

	public boolean updateMaidColor() {
		// 窶慊ｯﾋ・ｪ・ｽﾂｫ窶堙姑蛋ﾆ巽ﾆ鍛ﾆ誰
		int lc = getColor();
		if (textureData.getColor() != lc) {
			textureData.setColor(lc);
			return true;
		}
		return false;
	}

	/**
	 * 窶｢R窶堙固ｽ・ｽ窶堋ｿﾅｽﾃ･
	 */
	public void updateGotcha() {
		int lid = dataWatcher.getWatchableObjectInt(dataWatch_Gotcha);
		if (lid == 0) {
			mstatgotcha = null;
			return;
		}
		if (mstatgotcha != null && mstatgotcha.entityId == lid) {
			return;
		}
		for (int li = 0; li < worldObj.loadedEntityList.size(); li++) {
			if (((Entity)worldObj.loadedEntityList.get(li)).entityId == lid) {
				mstatgotcha = (Entity)worldObj.loadedEntityList.get(li);
				break;
			}
		}
	}

	public void setGotcha(int pEntityID) {
		dataWatcher.updateObject(dataWatch_Gotcha, Integer.valueOf(pEntityID));
	}
	public void setGotcha(Entity pEntity) {
		setGotcha(pEntity == null ? 0 : pEntity.entityId);
	}


	/**
	 * 窶ｹ|・ｽ\窶堋ｦ窶堙ｰ・ｽX・ｽV
	 */
	public void updateAimebow() {
		boolean lflag = (maidAvatar != null && maidAvatar.isUsingItemLittleMaid()) || mstatAimeBow;
		setMaidFlags(lflag, dataWatch_Flags_Aimebow);
	}

	public boolean isAimebow() {
		return (dataWatcher.getWatchableObjectInt(dataWatch_Flags) & dataWatch_Flags_Aimebow) > 0;
	}


	/**
	 * ﾅeﾅｽﾃｭﾆ稚ﾆ停ｰﾆ丹窶堙姑但ﾆ鍛ﾆ致ﾆ断・ｽ[ﾆ暖
	 */
	public void updateMaidFlagsClient() {
		int li = dataWatcher.getWatchableObjectInt(dataWatch_Flags);
		maidFreedom = (li & dataWatch_Flags_Freedom) > 0;
		maidTracer = (li & dataWatch_Flags_Tracer) > 0;
		maidWait = (li & dataWatch_Flags_Wait) > 0;
		mstatAimeBow = (li & dataWatch_Flags_Aimebow) > 0;
		mstatLookSuger = (li & dataWatch_Flags_LooksSugar) > 0;
		mstatBloodsuck = (li & dataWatch_Flags_Bloodsuck) > 0;
		looksWithInterest = (li & dataWatch_Flags_looksWithInterest) > 0;
		looksWithInterestAXIS = (li & dataWatch_Flags_looksWithInterestAXIS) > 0;
		maidOverDriveTime.updateClient((li & dataWatch_Flags_OverDrive) > 0);
		mstatWorkingCount.updateClient((li & dataWatch_Flags_Working) > 0);
	}

	/**
	 * ﾆ稚ﾆ停ｰﾆ丹ﾅ嘆窶堙俄冤窶堙ｰﾆ短ﾆ鍛ﾆ暖・ｽB
	 * @param pCheck・ｽF 窶佚趣ｿｽﾃ帚冤・ｽB
	 * @param pFlags・ｽF 窶佚趣ｿｽﾃ嵌稚ﾆ停ｰﾆ丹・ｽB
	 */
	public void setMaidFlags(boolean pFlag, int pFlagvalue) {
		int li = dataWatcher.getWatchableObjectInt(dataWatch_Flags);
		li = pFlag ? (li | pFlagvalue) : (li & ~pFlagvalue);
		dataWatcher.updateObject(dataWatch_Flags, Integer.valueOf(li));
	}

	/**
	 * ﾅｽw窶凖ｨ窶堋ｳ窶堙ｪ窶堋ｽﾆ稚ﾆ停ｰﾆ丹窶堙ｰﾅl窶慊ｾ
	 */
	public boolean getMaidFlags(int pFlagvalue) {
		return (dataWatcher.getWatchableObjectInt(dataWatch_Flags) & pFlagvalue) > 0;
	}

	/**
	 *  窶藩懌堋ｫﾋ徨窶堙鯉ｿｽﾃ昶凖ｨ
	 */
	public void setDominantArm(int pindex) {
		if (mstatSwingStatus.length <= pindex) return;
		if (maidDominantArm == pindex) return;
		for (LMM_SwingStatus lss : mstatSwingStatus) {
			lss.index = lss.lastIndex = -1;
		}
		maidDominantArm = pindex;
		dataWatcher.updateObject(dataWatch_DominamtArm, (byte)maidDominantArm);
		mod_LMM_littleMaidMob.Debug("Change Dominant.");
	}

	@Override
	public void setHomeArea(int par1, int par2, int par3, int par4) {
		homeWorld = dimension;
		super.setHomeArea(par1, par2, par3, par4);
	}

	@Override
	public void setTexturePackIndex(int pColor, int[] pIndex) {
		// Server
		textureData.setTexturePackIndex(pColor, pIndex);
		dataWatcher.updateObject(dataWatch_Texture, ((textureData.textureIndex[0] & 0xffff) | (textureData.textureIndex[1] & 0xffff) << 16));
		mod_LMM_littleMaidMob.Debug("changeSize-ID:%d: %f, %f, %b", entityId, width, height, worldObj.isRemote);
		setColor(pColor);
		setTextureNames();
	}

	@Override
	public void setTexturePackName(MMM_TextureBox[] pTextureBox) {
		// Client
		textureData.setTexturePackName(pTextureBox);
		setTextureNames();
		mod_LMM_littleMaidMob.Debug("ID:%d, TextureModel:%s", entityId, textureData.getTextureName(0));
		// ﾆ停堡断ﾆ停ｹ窶堙鯉ｿｽ窶ｰﾅﾃｺ窶ｰﾂｻ
		((MMM_TextureBox)textureData.textureBox[0]).models[0].setCapsValue(MMM_IModelCaps.caps_changeModel, maidCaps);
		// ﾆ湛ﾆ耽ﾆ池窶堙娯｢t窶堋ｯ窶佚問堋ｦ
//		for (Entry<String, MMM_EquippedStabilizer> le : pEntity.maidStabilizer.entrySet()) {
//			if (le.getValue() != null) {
//				le.getValue().updateEquippedPoint(pEntity.textureModel0);
//			}
//		}
		maidSoundRate = LMM_SoundManager.getSoundRate(textureData.getTextureName(0), getColor());

	}

	/**
	 * Client窶廃
	 */
	public void setTextureNames() {
		if (!textureData.setTextureNames()) {
			// TODO:setDefaultTexture
//			if (worldObj.isRemote) {
				setNextTexturePackege(0);
//			}
		}
	}

	public void setNextTexturePackege(int pTargetTexture) {
		textureData.setNextTexturePackege(pTargetTexture);
	}

	public void setPrevTexturePackege(int pTargetTexture) {
		textureData.setPrevTexturePackege(pTargetTexture);
	}


	// textureEntity

	@Override
	public void setTextureBox(MMM_TextureBoxBase[] pTextureBox) {
		textureData.setTextureBox(pTextureBox);
	}

	@Override
	public MMM_TextureBoxBase[] getTextureBox() {
		return textureData.getTextureBox();
	}

	@Override
	public void setTextureIndex(int[] pTextureIndex) {
		textureData.setTextureIndex(pTextureIndex);
	}

	@Override
	public int[] getTextureIndex() {
		return textureData.getTextureIndex();
	}

	@Override
	public void setTextures(int pIndex, ResourceLocation[] pNames) {
		textureData.setTextures(pIndex, pNames);
	}

	@Override
	public ResourceLocation[] getTextures(int pIndex) {
		return textureData.getTextures(pIndex);
	}

	@Override
	public MMM_TextureData getTextureData() {
		return textureData;
	}

	// Tileﾅﾃ滅淡

	/**
	 * ﾅｽg窶堙≫堙・堋｢窶堙ｩTile窶堋ｩ窶堙・堋､窶堋ｩ窶敖ｻ窶凖ｨ窶堋ｵ窶堙・｢ﾃ披堋ｷ・ｽB
	 */
	public boolean isUsingTile(TileEntity pTile) {
		if (isActiveModeClass()) {
			return getActiveModeClass().isUsingTile(pTile);
		}
		for (int li = 0; li < maidTiles.length; li++) {
			if (maidTiles[li] != null &&
					pTile.xCoord == maidTiles[li][0] &&
					pTile.yCoord == maidTiles[li][1] &&
					pTile.zCoord == maidTiles[li][2]) {
				return true;
			}
		}
		return false;
	}

	public boolean isEqualTile() {
		return worldObj.getBlockTileEntity(maidTile[0], maidTile[1], maidTile[2]) == maidTileEntity;
	}

	public boolean isTilePos() {
		return maidTileEntity != null;
	}
	public boolean isTilePos(int pIndex) {
		if (pIndex < maidTiles.length) {
			return maidTiles[pIndex] != null;
		}
		return false;
	}

	/**
	 * ﾆ抵ｿｽ・ｽ[ﾆ谷ﾆ停ｹ窶｢ﾃ擾ｿｽ窶昶堙欝ile窶堙戸・岩冰窶堙ｰ窶愿ｼ窶堙ｪ窶堙ｩ・ｽB
	 */
	public boolean getTilePos(int pIndex) {
		if (pIndex < maidTiles.length && maidTiles[pIndex] != null) {
			maidTile[0] = maidTiles[pIndex][0];
			maidTile[1] = maidTiles[pIndex][1];
			maidTile[2] = maidTiles[pIndex][2];
			return true;
		}
		return false;
	}

	public void setTilePos(int pX, int pY, int pZ) {
		maidTile[0] = pX;
		maidTile[1] = pY;
		maidTile[2] = pZ;
	}
	public void setTilePos(TileEntity pEntity) {
		maidTile[0] = pEntity.xCoord;
		maidTile[1] = pEntity.yCoord;
		maidTile[2] = pEntity.zCoord;
		maidTileEntity = pEntity;
	}
	public void setTilePos(int pIndex) {
		if (pIndex < maidTiles.length) {
			if (maidTiles[pIndex] == null) {
				maidTiles[pIndex] = new int[3];
			}
			maidTiles[pIndex][0] = maidTile[0];
			maidTiles[pIndex][1] = maidTile[1];
			maidTiles[pIndex][2] = maidTile[2];
		}
	}
	public void setTilePos(int pIndex, int pX, int pY, int pZ) {
		if (pIndex < maidTiles.length) {
			if (maidTiles[pIndex] == null) {
				maidTiles[pIndex] = new int[3];
			}
			maidTiles[pIndex][0] = pX;
			maidTiles[pIndex][1] = pY;
			maidTiles[pIndex][2] = pZ;
		}
	}

	public TileEntity getTileEntity() {
		return maidTileEntity = worldObj.getBlockTileEntity(maidTile[0], maidTile[1], maidTile[2]);
	}
	public TileEntity getTileEntity(int pIndex) {
		if (pIndex < maidTiles.length && maidTiles[pIndex] != null) {
			TileEntity ltile = worldObj.getBlockTileEntity(
					maidTiles[pIndex][0], maidTiles[pIndex][1], maidTiles[pIndex][2]);
			if (ltile == null) {
				clearTilePos(pIndex);
			}
			return ltile;
		}
		return null;
	}

	public void clearTilePos() {
		maidTileEntity = null;
	}
	public void clearTilePos(int pIndex) {
		if (pIndex < maidTiles.length) {
			maidTiles[pIndex] = null;
		}
	}
	public void clearTilePosAll() {
		for (int li = 0; li < maidTiles.length; li++) {
			maidTiles[li] = null;
		}
	}

	public double getDistanceTilePos() {
		return getDistance(
				(double)maidTile[0] + 0.5D,
				(double)maidTile[1] + 0.5D,
				(double)maidTile[2] + 0.5D);
	}
	public double getDistanceTilePosSq() {
		return getDistanceSq(
				(double)maidTile[0] + 0.5D,
				(double)maidTile[1] + 0.5D,
				(double)maidTile[2] + 0.5D);
	}

	public double getDistanceTilePos(int pIndex) {
		if (maidTiles.length > pIndex && maidTiles[pIndex] != null) {
			return getDistance(
					(double)maidTiles[pIndex][0] + 0.5D,
					(double)maidTiles[pIndex][1] + 0.5D,
					(double)maidTiles[pIndex][2] + 0.5D);
		}
		return -1D;
	}
	public double getDistanceTilePosSq(int pIndex) {
		if (maidTiles.length > pIndex && maidTiles[pIndex] != null) {
			return getDistanceSq(
					(double)maidTiles[pIndex][0] + 0.5D,
					(double)maidTiles[pIndex][1] + 0.5D,
					(double)maidTiles[pIndex][2] + 0.5D);
		}
		return -1D;
	}
	public double getDistanceTilePos(TileEntity pTile) {
		if (pTile != null) {
			return getDistance(
					(double)pTile.xCoord + 0.5D,
					(double)pTile.yCoord + 0.5D,
					(double)pTile.zCoord + 0.5D);
		}
		return -1D;
	}
	public double getDistanceTilePosSq(TileEntity pTile) {
		if (pTile != null) {
			return getDistanceSq(
					(double)pTile.xCoord + 0.5D,
					(double)pTile.yCoord + 0.5D,
					(double)pTile.zCoord + 0.5D);
		}
		return -1D;
	}

	public void looksTilePos() {
		getLookHelper().setLookPosition(
				maidTile[0] + 0.5D, maidTile[1] + 0.5D, maidTile[2] + 0.5D,
				10F, getVerticalFaceSpeed());
	}
	public void looksTilePos(int pIndex) {
		if (maidTiles.length > pIndex && maidTiles[pIndex] != null) {
			getLookHelper().setLookPosition(
					maidTiles[pIndex][0] + 0.5D,
					maidTiles[pIndex][1] + 0.5D,
					maidTiles[pIndex][2] + 0.5D,
					10F, getVerticalFaceSpeed());
		}
	}

}
