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

	// ’è�”‚ÍStatics‚ÖˆÚ“®
//	protected static final UUID maidUUID = UUID.nameUUIDFromBytes("net.minecraft.src.littleMaidMob".getBytes());
	protected static final UUID maidUUID = UUID.fromString("e2361272-644a-3028-8416-8536667f0efb");
	protected static AttributeModifier attCombatSpeed = (new AttributeModifier(maidUUID, "Combat speed boost", 0.07D, 0)).setSaved(false);
	protected static AttributeModifier attAxeAmp = (new AttributeModifier(maidUUID, "Axe Attack boost", 0.5D, 1)).setSaved(false);


	// •Ï�”Œ¸‚ç‚µ‚½‚¢‚È‚Ÿ
//    protected long maidContractLimit;		// Œ_–ñŽ¸Œø“ú
	protected int maidContractLimit;		// Œ_–ñŠúŠÔ
	protected long maidAnniversary;			// Œ_–ñ“úUID‚Æ‚µ‚ÄŽg—p
	protected int maidDominantArm;			// —˜‚«˜r�A1Byte
	/** ƒeƒNƒXƒ`ƒƒŠÖ˜A‚Ìƒf�[ƒ^‚ðŠÇ—� **/
	public MMM_TextureData textureData;
	public Map<String, MMM_EquippedStabilizer> maidStabilizer = new HashMap<String, MMM_EquippedStabilizer>();
	
	
	public LMM_InventoryLittleMaid maidInventory;
	public LMM_EntityLittleMaidAvatar maidAvatar;
	public LMM_EntityCaps maidCaps;	// Client‘¤‚Ì‚Ý
	
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
	
	// “®“I‚È�ó‘Ô
	protected EntityPlayer mstatMasterEntity;	// Žå
	protected double mstatMasterDistanceSq;		// Žå‚Æ‚Ì‹——£�AŒvŽZŒy—Ê‰»—p
	protected Entity mstatgotcha;				// ƒ�ƒCƒ„�[ƒh—p
	protected boolean mstatBloodsuck;
	protected boolean mstatClockMaid;
	// ƒ}ƒXƒN”»’è
	protected int mstatMaskSelect;
	// ’Ç‰Á‚Ì“ª•”‘•”õ
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
	// ˜r�U‚è
	public LMM_SwingStatus mstatSwingStatus[]; 
	public boolean mstatAimeBow;
	// ŽñŽü‚è
	private boolean looksWithInterest;
	private boolean looksWithInterestAXIS;
	private float rotateAngleHead;			// Angle
	private float prevRotateAngleHead;		// prevAngle

	/**
	 * ŒÂ‘Ì‚²‚Æ‚É’l‚ðƒoƒ‰‚Â‚©‚¹‚é‚Ì‚ÉŽg‚¤�B
	 */
	public float entityIdFactor;
	
	public boolean weaponFullAuto;	// ‘•”õ‚ªƒtƒ‹ƒI�[ƒg•�Ší‚©‚Ç‚¤‚©
	public boolean weaponReload;	// ‘•”õ‚ªƒŠƒ��[ƒh‚ð—~‚µ‚Ä‚¢‚é‚©‚Ç‚¤‚©
	public boolean maidCamouflage;
	
	
	// ‰¹�º
//	protected LMM_EnumSound maidAttackSound;
	protected LMM_EnumSound maidDamegeSound;
	protected int maidSoundInterval;
	protected float maidSoundRate;
	
	// ŽÀŒ±—p
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
		// �‰Šú�Ý’è
		maidInventory = new LMM_InventoryLittleMaid(this);
		if (par1World != null ) {
			maidAvatar = new LMM_EntityLittleMaidAvatar(par1World, this);
		}
		mstatOpenInventory = false;
//		isMaidChaseWait = false;
		mstatTime = 6000;
		maidOverDriveTime = new MMM_Counter(5, 300, -100);
		mstatWorkingCount = new MMM_Counter(11, 10, -10);
		
		// ƒ‚ƒfƒ‹ƒŒƒ“ƒ_ƒŠƒ“ƒO—p‚Ìƒtƒ‰ƒOŠl“¾—pƒwƒ‹ƒp�[ŠÖ�”
		maidCaps = new LMM_EntityCaps(this);
		
		// Œ`‘ÔŒ`�¬�ê
		textureData = new MMM_TextureData(this, maidCaps);
		textureData.setColor(12);
		MMM_TextureBox ltb[] = new MMM_TextureBox[2];
		ltb[0] = ltb[1] = MMM_TextureManager.instance.getDefaultTexture(this);
		setTexturePackName(ltb);
		
		entityIdFactor = (float)(entityId * 70);
		// ˜r�U‚è
		mstatSwingStatus = new LMM_SwingStatus[] { new LMM_SwingStatus(), new LMM_SwingStatus()};
		setDominantArm(rand.nextInt(mstatSwingStatus.length));
		
		// �Ä�¶‰¹�º
//		maidAttackSound = LMM_EnumSound.attack;
		maidDamegeSound = LMM_EnumSound.hurt;
		maidSoundInterval = 0;
		
		// –ì�¶Ží—p�‰Šú’l�Ý’è
		setHealth(15F);
		
		// ˆÚ“®—pƒtƒBƒWƒJƒ‹�Ý’è
		getNavigator().setAvoidsWater(true);
		getNavigator().setBreakDoors(true);
		
		
		// TODO:‚±‚ê‚ÍƒeƒXƒg
//		maidStabilizer.put("HeadTop", MMM_StabilizerManager.getStabilizer("WitchHat", "HeadTop"));
		
		
		
		// EntityMode‚Ì’Ç‰Á
		maidEntityModeList = LMM_EntityModeManager.getModeList(this);
		// ƒ‚�[ƒhƒŠƒXƒg
		maidActiveModeClass = null;
		maidModeList = new HashMap<Integer, EntityAITasks[]>();
		maidModeIndexList = new HashMap<String, Integer>();
		initModeList();
		mstatModeName = "";
		maidMode = 65535;
		// �‰Šú‰»ŽžŽÀ�sƒR�[ƒh
		for (LMM_EntityModeBase lem : maidEntityModeList) {
			lem.initEntity();
		}
	}

	@Override
	public EntityLivingData onSpawnWithEgg(EntityLivingData par1EntityLivingData) {
		// ƒeƒNƒXƒ`ƒƒ�[‚ðƒ‰ƒ“ƒ_ƒ€‚Å‘I‘ð
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
		// �‰Šúƒpƒ‰ƒ��[ƒ^�[
		super.applyEntityAttributes();
		// ‘Î�ÛˆÚ“®‰Â”\”ÍˆÍ
		getEntityAttribute(SharedMonsterAttributes.followRange).setAttribute(20.0D);
		// Šî–{ˆÚ“®‘¬“x
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.23000000417232513D);
		// •W�€�UŒ‚—Í‚P
		getAttributeMap().func_111150_b(SharedMonsterAttributes.attackDamage).setAttribute(1.0D);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		/*
		 * DataWatcher‚ÍƒNƒ‰ƒCƒAƒ“ƒg‚©‚çƒT�[ƒo�[‚Ö‚Í’l‚ð“n‚³‚È‚¢�A“n‚¹‚È‚¢�B
		 */
		
		// Žg—p’†ƒŠƒXƒg
		// 0:Flags
		// 1:Air
		// 2, 3, 4, 5,
		// 6: HP
		// 7, 8:PotionMap
		// 9: ArrowCount
		// 10: ŒÅ—L–¼�Ì
		// 11: –¼•t”»’è
		// 12: GrowingAge
		// 16: Tame(4), Sit(1) 
		// 17: ownerName
		
		// maidAvater—pEntityPlayerŒÝŠ·•Ï�”
		// 17 -> 18
		// 18 : AbsoptionŒø‰Ê‚ðƒNƒ‰ƒCƒAƒ“ƒg‘¤‚Ö“]‘—‚·‚é‚Ì‚ÉŽg‚¤
		dataWatcher.addObject(dataWatch_Absoption, Float.valueOf(0.0F));
		
		// “ÆŽ©•ª
		// 19:maidColor
		dataWatcher.addObject(dataWatch_Color, Byte.valueOf((byte)0));
		// 20:‘I‘ðƒeƒNƒXƒ`ƒƒƒCƒ“ƒfƒbƒNƒX
		dataWatcher.addObject(dataWatch_Texture, Integer.valueOf(0));
		// 21:ƒ‚ƒfƒ‹ƒp�[ƒc‚Ì•\Ž¦ƒtƒ‰ƒO
		dataWatcher.addObject(dataWatch_Parts, Integer.valueOf(0));
		// 22:�ó‘Ô‘JˆÚƒtƒ‰ƒOŒQ(32Bit)�A�Ú�×‚ÍStaticsŽQ�Æ
		dataWatcher.addObject(dataWatch_Flags, Integer.valueOf(0));
		// 23:GotchaID
		dataWatcher.addObject(dataWatch_Gotcha, Integer.valueOf(0));
		// 24:ƒ�ƒCƒhƒ‚�[ƒh
		dataWatcher.addObject(dataWatch_Mode, Short.valueOf((short)0));
		// 25:—˜‚«˜r
		dataWatcher.addObject(dataWatch_DominamtArm, Byte.valueOf((byte)0));
		// 26:ƒAƒCƒeƒ€‚ÌŽg—p”»’è
		dataWatcher.addObject(dataWatch_ItemUse, Integer.valueOf(0));
		// 27:•ÛŽ�ŒoŒ±’l
		dataWatcher.addObject(dataWatch_ExpValue, Integer.valueOf(0));
		
		// TODO:test
		// 31:Ž©—R•Ï�”�AEntityMode“™‚ÅŽg—p‰Â”\‚È•Ï�”�B
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
		
		// TODO:‚±‚ê‚¢‚ç‚È‚­‚Ë�H
		aiProfiler = worldObj != null && worldObj.theProfiler != null ? worldObj.theProfiler : null;

		// “®�ìƒ‚�[ƒh—p‚ÌTasksList‚ð�‰Šú‰»
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
		// ˆÚ“®—pAI
		ltasks[0].addTask(30, aiTracer);
		ltasks[0].addTask(31, aiFollow);
		ltasks[0].addTask(32, aiWander);
		ltasks[0].addTask(33, new EntityAILeapAtTarget(this, 0.3F));
		// Mutex‚Ì‰e‹¿‚µ‚È‚¢“ÁŽê�s“®
		ltasks[0].addTask(40, aiCloseDoor);
		ltasks[0].addTask(41, aiOpenDoor);
		ltasks[0].addTask(42, aiRestrictRain);
		// Žñ‚Ì“®‚«’P“Æ
		ltasks[0].addTask(51, new EntityAIWatchClosest(this, EntityLivingBase.class, 10F));
		ltasks[0].addTask(52, new EntityAILookIdle(this));
		
		// ’Ç‰Á•ª
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
		// ƒ‚�[ƒh–¼�Ì‚ÌŠl“¾
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
		// ƒ‚�[ƒh‚É‰ž‚¶‚ÄAI‚ð�Ø‚è‘Ö‚¦‚é
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
		
		// AI‚ð�ª’ê‚©‚ç�‘‚«Š·‚¦‚é
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

		// ƒ‚�[ƒh�Ø‘Ö‚É‰ž‚¶‚½�ˆ—�Œn‚ðŠm•Û
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
		// Šù‘¶‚ÌAI‚ð�í�œ‚µ‚Ä’u‚«Š·‚¦‚é�B
		// “®�ì‚ðƒNƒŠƒA
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
				// TODO: –¢ŽÀ‘•‚Ì‹@”\�Aƒ‚�[ƒhƒ`ƒFƒ“ƒWŽž‚Ì�‰Šú‰»‚ð�s‚¤�B
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
	 * “K—p‚³‚ê‚Ä‚¢‚éƒ‚�[ƒhƒNƒ‰ƒX
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

	// Œø‰Ê‰¹‚Ì�Ý’è
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
		// •�’i‚Ì�º
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
	 * ŠÈˆÕ‰¹�º�Ä�¶�A•W�€‚Ì‰¹�º‚Ì‚ÝŽg—p‚·‚é‚±‚Æ�B
	 */
	public void playSound(String pname) {
		playSound(pname, 0.5F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
	}

	/**
	 * ƒlƒbƒgƒ��[ƒN‘Î‰ž‰¹�º�Ä�¶
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
	 * ‰¹�º�Ä�¶—p�B
	 * ’Ê�í‚Ì�Ä�¶‚Å‚Íƒlƒbƒgƒ��[ƒN‰z‚µ‚É‚È‚é‚Ì‚Å‚»‚Ì‘Î�ô�B
	 */
	public void playLittleMaidSound(LMM_EnumSound enumsound, boolean force) {
		// ‰¹�º‚Ì�Ä�¶
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
		// ƒfƒXƒ|�[ƒ“”»’è
		return mod_LMM_littleMaidMob.cfg_canDespawn || super.canDespawn();
	}

	@Override
	public boolean getCanSpawnHere() {
		// ƒXƒ|�[ƒ“‰Â”\‚©�H
		if (mod_LMM_littleMaidMob.cfg_spawnLimit <= getMaidCount()) {
			mod_LMM_littleMaidMob.Debug("Spawn Limit.");
			return false;
		}
		int lx = MathHelper.floor_double(this.posX);
		int ly = MathHelper.floor_double(this.boundingBox.minY);
		int lz = MathHelper.floor_double(this.posZ);
		/*
		// TODO:ƒT�[ƒo�[‘¤‚Å”»’è‚Å‚«‚È‚¢‚Ì‚ÅˆÓ–¡‚È‚µ?
		MMM_TextureBox lbox = MMM_TextureManager.instance.getTextureBox(textureBox[0]);
		if (worldObj == null || textureModel == null  
				|| !textureBox[0].mo.getCanSpawnHere(worldObj, lx, ly, lz, this)) {
			mod_LMM_littleMaidMob.Debug(String.format("%s is can't spawn hear.", textureName));
			return false;
		}
		*/
		if (mod_LMM_littleMaidMob.cfg_Dominant) {
			// ƒhƒ~ƒiƒ“ƒg
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
			// Žñ•R‚ðƒhƒ�ƒbƒv
			EntityItem entityitem = new EntityItem(worldObj, mstatgotcha.posX, mstatgotcha.posY, mstatgotcha.posZ, new ItemStack(Item.silk));
			worldObj.spawnEntityInWorld(entityitem);
			mstatgotcha = null;
		}
		super.setDead();
	}

	/**
	 * “Ç‚Ý�ž‚Ý—Ìˆæ“à‚Ìƒ�ƒCƒh‚³‚ñ‚Ì�”
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
		// ‚¨Žq‚³‚ñ‚Ì�Ý’è
		return null;
	}

	// ƒGƒtƒFƒNƒg•\Ž¦
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
		// worldObj.setEntityState(this, (byte))‚ÅŽw’è‚³‚ê‚½ƒAƒNƒVƒ‡ƒ“‚ðŽÀ�s
		switch (par1) {
		case 10:
			// •s‹@Œ™
			showParticleFX("smoke", 0.02D, 0.02D, 0.02D);
			break;
		case 11:
			// ƒSƒLƒQƒ“
			double a = getContractLimitDays() / 7D;
			double d6 = a * 0.3D;
			double d7 = a;
			double d8 = a * 0.3D;
			worldObj.spawnParticle("note", posX, posY + height + 0.1D, posZ, d6, d7, d8);
			break;
		case 12:
			// Ž©—R�s“®
			showParticleFX("reddust", 0.5D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
			break;
		case 13:
			// •sŽ©—R�s“®
			showParticleFX("smoke", 0.02D, 0.02D, 0.02D);
			break;
		case 14:
			// ƒgƒŒ�[ƒT�[
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
		// ”­Œõ�ˆ—�—p
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


	// AIŠÖ˜A
	@Override
	protected boolean isAIEnabled() {
		// �VAI‘Î‰ž
		return true;
	}
	
	/**
	 * “G–¡•ûŽ¯•Ê
	 */
	public boolean getIFF(Entity pEntity) {
		// “G–¡•ûŽ¯•Ê(“G=false)
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
				// ŒŒ‚É‰ì‚¦‚Ä‚¢‚éŽž‚Í“G
				return false;
			}
			if (pEntity instanceof LMM_EntityLittleMaid) {
				// ‚¨—V‚Ñƒ‚�[ƒh‚Ìƒ�ƒCƒh‚É‚Í“G‘Î‚µ‚È‚¢
				if (((LMM_EntityLittleMaid)pEntity).mstatPlayingRole > LMM_EntityMode_Playing.mpr_NULL) {
					return true;
				}
			}
			if (pEntity instanceof EntityCreature) {
				// ‘ŠŽè‚ª‰½‚ðƒ^�[ƒQƒbƒg‚É‚µ‚Ä‚¢‚é‚©‚ÅŒˆ‚Ü‚é
				Entity et = ((EntityCreature)pEntity).getEntityToAttack();
				if (et != null && et == mstatMasterEntity) {
					return false;
				}
				if (et == this) {
					return false;
				}
				if (et instanceof LMM_EntityLittleMaid) {
					// “¯‚¶ƒ}ƒXƒ^�[‚Ìƒ�ƒCƒh‚ð�UŒ‚‘Î�Û‚Æ‚µ‚Ä‚¢‚é
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
		// IFF‚Ì�Ý’è�AƒNƒ‰ƒX–ˆ‚Ì”»’è‚µ‚©‚Å‚«‚È‚¢‚Ì‚ÅŽg‚í‚È‚¢�B
		return true;
	}

	@Override
	public boolean attackEntityAsMob(Entity par1Entity) {
		
		// �³�íŽž‚Í‰ñ•œ—D�æ�ˆ—�
		if (getHealth() < 10 && !isBloodsuck() && maidInventory.hasItem(Item.sugar.itemID)) {
			return true;
		}
		
		// “ÁŽê‚È�UŒ‚�ˆ—�
		if (isActiveModeClass() && getActiveModeClass().attackEntityAsMob(maidMode, par1Entity)) {
			return true;
		}
		
		// •W�€�ˆ—�
		setSwing(20, isBloodsuck() ? LMM_EnumSound.attack_bloodsuck : LMM_EnumSound.attack);
		maidAvatar.attackTargetEntityWithCurrentItem(par1Entity);
		return true;
	}

	@Override
	public boolean isBreedingItem(ItemStack par1ItemStack) {
		// ‚¨�D‚Ý‚Í‰½�H
		if (isContractEX()) {
			return par1ItemStack.itemID == Item.sugar.itemID;
		} else {
			return par1ItemStack.itemID == Item.cake.itemID;
		}
	}

	
	@Override
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
		// ƒf�[ƒ^ƒZ�[ƒu
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
		// ’Ç‰Á•ª
		for (int li = 0; li < maidEntityModeList.size(); li++) {
			maidEntityModeList.get(li).writeEntityToNBT(par1nbtTagCompound);
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
		// ƒf�[ƒ^ƒ��[ƒh
		super.readEntityFromNBT(par1nbtTagCompound);
		
		if (par1nbtTagCompound.hasKey("ModeColor")) {
			// ‹Œ”Å‚©‚ç‚ÌŒp�³
	        String s = par1nbtTagCompound.getString("Master");
	        if(s.length() > 0) {
	        	setOwner(s);
	            setContract(true);
	        }
	        NBTTagList nbttaglist = par1nbtTagCompound.getTagList("Inventory");
	        maidInventory.readFromNBT(nbttaglist);
	        // ƒA�[ƒ}�[ƒXƒ�ƒbƒg•Ï�X‚É‘Î‰ž‚·‚é‚½‚ß‚ÌƒR�[ƒh
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
				// ƒ_ƒ~�[‚Ì�”’l‚ð“ü‚ê‚é
				maidAnniversary = worldObj.getWorldTime() - entityId;
			}
			
		} else {
			// �VŒ^
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
				// ’l‚ª‚¨‚©‚µ‚¢Žž‚Í‚P“ú•ª
//	        	maidContractLimit = worldObj.getWorldTime() + 24000L;
				maidContractLimit = 24000;
			}
			maidAnniversary = par1nbtTagCompound.getLong("Anniversary");
			if (maidAnniversary == 0L && isContract()) {
				// ƒ_ƒ~�[‚Ì�”’l‚ð“ü‚ê‚é
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
			
			// ’Ç‰Á•ª
			for (int li = 0; li < maidEntityModeList.size(); li++) {
				maidEntityModeList.get(li).readEntityFromNBT(par1nbtTagCompound);
			}
		}
		onInventoryChanged();
		
		// ƒhƒbƒyƒ‹‘Î�ô
		if (mod_LMM_littleMaidMob.cfg_antiDoppelganger && maidAnniversary > 0L) {
			for (int i = 0; i < worldObj.loadedEntityList.size(); i++) {
				Entity entity1 = (Entity)worldObj.loadedEntityList.get(i);
				if (!entity1.isDead && entity1 instanceof LMM_EntityLittleMaid) {
					LMM_EntityLittleMaid elm = (LMM_EntityLittleMaid)entity1;
					if (elm != this && elm.isContract() && elm.maidAnniversary == maidAnniversary
							&& elm.getMaidMaster().equalsIgnoreCase(getMaidMaster())) {
						// �V‚µ‚¢•û‚ðŽc‚·
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
		// ƒAƒCƒeƒ€‚Ì•\Ž¦
		if (maidAvatar != null) {
			return maidAvatar.getItemIcon(par1ItemStack, par2);
		}
		
		if (par1ItemStack.getItem().requiresMultipleRenderPasses()) {
			return par1ItemStack.getItem().getIconFromDamageForRenderPass(par1ItemStack.getItemDamage(), par2);
		} else {
			return super.getItemIcon(par1ItemStack, par2);
		}
	}


	// ‚¨‚ñ‚Ô‚¨‚Î‚¯‚Í–³“G
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
		// TODO:‚±‚±‚Í—v’²�®
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
			// Žp�¨�§Œä
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
		// TODO:ƒAƒbƒvƒf�[ƒgŽž‚Éƒ`ƒFƒbƒN
		++ticksExisted;
		//
		
		if(ridingEntity instanceof EntityPlayer) {
			EntityPlayer lep = (EntityPlayer)ridingEntity;
			
			// ƒwƒbƒhƒnƒK�[
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

	// ŽñŽü‚è
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


	// ƒ_ƒ��[ƒWƒRƒ“ƒgƒ��[ƒ‹
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
		// ƒ_ƒ��[ƒWƒ\�[ƒX‚É‰ž‚¶‚Ä‰¹�º•Ï�X
		if (par1DamageSource == DamageSource.fall) {
			maidDamegeSound = LMM_EnumSound.hurt_fall;
		}
		if(!par1DamageSource.isUnblockable() && isBlocking()) {
			// ƒuƒ�ƒbƒLƒ“ƒO
//			par2 = (1.0F + par2) * 0.5F;
			mod_LMM_littleMaidMob.Debug(String.format("Blocking success ID:%d, %f -> %f" , this.entityId, par2, (par2 = (1.0F + par2) * 0.5F)));
			maidDamegeSound = LMM_EnumSound.hurt_guard;
		}
		
		// ”íƒ_ƒ�
		float llasthealth = getHealth();
		if (par2 > 0 && getActiveModeClass() != null && !getActiveModeClass().damageEntity(maidMode, par1DamageSource, par2)) {
			//XXX: experimenting
			//maidAvatar.damageEntity(par1DamageSource, par2);
			maidAvatar.attackEntityFrom(par1DamageSource, par2);
			
			// ƒ_ƒ��[ƒW‚ðŽó‚¯‚é‚Æ‘Ò‹@‚ð‰ð�œ
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
		
		// ƒ_ƒ��[ƒWƒ\�[ƒX‚ð“Á’è‚µ‚Ä‰¹�º‚Ì�Ý’è
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
			// —V‚Ñ‚Í�I‚í‚è‚¾�I
			setPlayingRole(0);
			getNextEquipItem();
		}
		// ƒQ�[ƒ€“ïˆÕ“x‚É‚æ‚éƒ_ƒ��[ƒW‚Ì•â�³
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
			// ƒm�[ƒ_ƒ��[ƒW
			if (maidDamegeSound == LMM_EnumSound.hurt) {
				maidDamegeSound = LMM_EnumSound.hurt_nodamege;
			}
			playSound(maidDamegeSound, true);
			return false;
		}
		
		if(super.attackEntityFrom(par1DamageSource, par2)) {
			//Œ_–ñŽÒ‚Ì–¼‘Oƒ`ƒFƒbƒN‚Íƒ}ƒ‹ƒ`—p
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
    			// ƒq�[ƒ‰�[‚Í–ò�Ü‚Å�UŒ‚
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
	 * ‘Î�Û‚Éƒ|�[ƒVƒ‡ƒ“‚ðŽg‚¤�B
	 */
	public void usePotionTotarget(EntityLivingBase entityliving) {
		ItemStack itemstack = maidInventory.getCurrentItem();
		if (itemstack != null && itemstack.getItem() instanceof ItemPotion) {
			// ƒ|�[ƒVƒ‡ƒ“Œø‰Ê‚Ì”­“®
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
		// ƒ�ƒCƒh‚³‚ñ‚Í‚¨�»“œ‚ÆƒRƒRƒA‚Æ•s’èŒ`‚Ì‰½‚©‚Å‚Å‚«‚Ä‚é‚Ì�I
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
		
		// ƒCƒ“ƒxƒ“ƒgƒŠ‚ðƒuƒ`ƒ}ƒPƒ��I
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
		// •Â�Š�Ú�G‰ñ”ð—p
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
//		// AI‘Î‰žŒ^‚Í‚±‚Á‚¿‚ªŒÄ‚Î‚ê‚é
//		dataWatcher.updateObject(dataWatch_Health, Integer.valueOf(getHealth()));
		
		// ’Ç‰Á•ª
		for (LMM_EntityModeBase ieml : maidEntityModeList) {
			ieml.updateAITick(getMaidModeInt());
		}
	}

	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
	}

	/**
	 * –„‘’‘Î�ôƒRƒs�[
	 */
	private boolean isBlockTranslucent(int par1, int par2, int par3) {
		return this.worldObj.isBlockNormalCube(par1, par2, par3);
	}

	/**
	 * –„‘’‘Î�ôƒRƒs�[
	 */
	@Override
	protected boolean pushOutOfBlocks(double par1, double par3, double par5) {
		// EntityPlayerSP‚Ì‚ðˆø‚Á’£‚Á‚Ä‚«‚½
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
		// ‰ñ•œ”»’è
		float lhealth = getHealth();
		if (lhealth > 0) {
			if (!worldObj.isRemote) {
				if (getSwingStatusDominant().canAttack()) {
					if (!isBloodsuck()) {
						// ’Ê�íŽž‚Í‰ñ•œ—D�æ
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
		// –„‘’‘Î�ô
		boolean grave = true;
		grave &= pushOutOfBlocks(posX - (double)width * 0.34999999999999998D, boundingBox.minY, posZ + (double)width * 0.34999999999999998D);
		grave &= pushOutOfBlocks(posX - (double)width * 0.34999999999999998D, boundingBox.minY, posZ - (double)width * 0.34999999999999998D);
		grave &= pushOutOfBlocks(posX + (double)width * 0.34999999999999998D, boundingBox.minY, posZ - (double)width * 0.34999999999999998D);
		grave &= pushOutOfBlocks(posX + (double)width * 0.34999999999999998D, boundingBox.minY, posZ + (double)width * 0.34999999999999998D);
		if (grave && onGround) {
			jump();
		}
		if(lhealth > 0) {
			// ‹ß�ÚŠÄŽ‹‚Ì’Ç‰Á‚Í‚±‚±
			// ƒAƒCƒeƒ€‚Ì‰ñŽû
			if (!worldObj.isRemote) {
				List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(1.0D, 0.0D, 1.0D));
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						Entity entity = (Entity)list.get(i);
						if (!entity.isDead) {
							if (entity instanceof EntityArrow) {
								// “ÁŽê‰ñŽû
								((EntityArrow)entity).canBePickedUp = 1;
							}
							entity.onCollideWithPlayer(maidAvatar);
						}
					}
					// ƒAƒCƒeƒ€‚ªˆê”t‚É‚È‚Á‚Ä‚¢‚ÄƒAƒCƒeƒ€‚Éƒ^ƒQ‚ð‚Æ‚Á‚Ä‚¢‚é�ê�‡‚Íƒ^ƒQ‚ðƒNƒŠƒA
					if (entityToAttack instanceof EntityItem && maidInventory.getFirstEmptyStack() == -1) {
						setTarget(null);
					}
				}
			}
			// ŽžŒv‚ðŽ�‚Á‚Ä‚¢‚é
			// TODO:‘½•ª‚±‚Ì•Ó‚è‚Ì�ˆ—�‚Í‚¨‚©‚µ‚¢
			if (isContractEX() && mstatClockMaid) {
				// ƒQ�[ƒ€“àŽžŠÔ‚É�‡‚í‚¹‚½‰¹�º‚Ì�Ä�¶
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
					// ‰ñ•œ
					if (getHealth() < getMaxHealth()) {
						if (maidInventory.consumeInventoryItem(Item.sugar.itemID)) {
							eatSugar(true, false);
						}
					}
					// ‚Â‚Ü‚Ý�H‚¢
					if (rand.nextInt(50000) == 0 && maidInventory.consumeInventoryItem(Item.sugar.itemID)) {
						eatSugar(true, false);
					}
					// Œ_–ñ�X�V
					if (isContractEX()) {
						float f = getContractLimitDays();
						if (f <= 6 && maidInventory.consumeInventoryItem(Item.sugar.itemID)) {
							// Œ_–ñ�X�V
							eatSugar(true, true);
						}
					}
				}
			}
		}
	}

	@Override
	public void onUpdate() {
		// Entity�‰‰ñ�¶�¬Žž‚ÌƒCƒ“ƒxƒ“ƒgƒŠ�X�V—p
		// ƒT�[ƒo�[‚Ì•û‚ª�æ‚É‹N“®‚·‚é‚Ì‚ÅƒNƒ‰ƒCƒAƒ“ƒg‘¤‚ª�X�V‚ðŽó‚¯Žæ‚ê‚È‚¢
		if (firstload > 0) {
			// �‰‰ñ�X�V—p
			// ƒT�[ƒo�[‚Ì•û‚ª�æ‚É‹N“®‚µ‚Ä‚¢‚é‚Ì‚Å‹­�§“Ç‚Ý�ž‚Ý‚ÌŽè�‡‚ª•K—v
			if (--firstload == 0) {
				if (worldObj.isRemote) {
					LMM_Net.sendToEServer(this, new byte[] {LMM_Statics.LMN_Server_UpdateSlots, 0, 0, 0, 0});
				} else {
				}
			}
		}
		
		// ”ò‚Ñ“¹‹ï—p
		weaponFullAuto = false;
		weaponReload = false;
		
		// Žå‚ÌŠm”F‚È‚Ç
		mstatMasterEntity = getMaidMasterEntity();
		if (mstatMasterEntity != null) {
			mstatMasterDistanceSq = getDistanceSqToEntity(mstatMasterEntity);
		}
		// ƒ‚ƒfƒ‹ƒTƒCƒY‚ÌƒŠƒAƒ‹ƒ^ƒCƒ€•Ï�X—L‚è�H
		textureData.onUpdate();
		// ƒŠƒAƒ‹ƒ^ƒCƒ€•Ï“®’l‚ðƒAƒbƒvƒf�[ƒg
		if (worldObj.isRemote) {
			// ƒNƒ‰ƒCƒAƒ“ƒg‘¤
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
			// ƒT�[ƒo�[‘¤
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
			// �X‚Ë‚é
			if (!isContractEX() && !isFreedom()) {
				setFreedom(true);
				setMaidWait(false);
			}
			// ˆÚ“®‘¬“x‚Ì•Ï�X
			AttributeInstance latt = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
			// ‘®�«‚ð‰ð�œ
			latt.removeModifier(attCombatSpeed);
			if (isContract()) {
				if (!isFreedom() || (entityToAttack != null || getAttackTarget() != null)) {
					// ‘®�«‚ð�Ý’è
					latt.applyModifier(attCombatSpeed);
				}
			}
		}
		
		// “ÆŽ©�ˆ—�—p–ˆŽž�ˆ—�
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
		
		// Aveter‚Ì–ˆŽž�ˆ—�
		if (maidAvatar != null) {
			maidAvatar.getValue();
			maidAvatar.onUpdate();
//			maidAvatar.setValue();
		}
		
		// ƒJƒEƒ“ƒ^Œn
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
		
		// ‚­‚Ñ‚©‚µ‚°	
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
		
		// Ž�‚¿•¨‚ÌŠm”F
		if (maidInventory.inventoryChanged) {
			onInventoryChanged();
			maidInventory.inventoryChanged = false;
		}
		if (!worldObj.isRemote) {
			// ƒT�[ƒo�[‘¤�ˆ—�
			// ƒCƒ“ƒxƒ“ƒgƒŠ‚Ì�X�V
//			if (!mstatOpenInventory) {
				for (int li = 0 ;li < maidInventory.getSizeInventory(); li++) {
					boolean lchange = false;
					int lselect = 0xff;
					// ‘I‘ð‘•”õ‚ª•Ï‚í‚Á‚½
					for (int lj = 0; lj < mstatSwingStatus.length; lj++) {
						lchange = mstatSwingStatus[lj].checkChanged();
						if (mstatSwingStatus[lj].index == li) {
							lselect = lj;
						}
					}
					// ƒCƒ“ƒxƒ“ƒgƒŠ‚Ì’†�g‚ª•Ï‚í‚Á‚½
					if (lchange || maidInventory.isChanged(li)) {
						((WorldServer)worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(this, new Packet5PlayerInventory(this.entityId, (li | lselect << 8) + 5, maidInventory.getStackInSlot(li)));
						maidInventory.resetChanged(li);
						mod_LMM_littleMaidMob.Debug(String.format("ID:%d-%s - Slot(%x:%d-%d,%d) Update.", entityId, worldObj.isRemote ? "Client" : "Server", lselect, li, mstatSwingStatus[0].index, mstatSwingStatus[1].index));
					}
//				}
			}
			
			// ‹|�\‚¦
			mstatAimeBow &= !getSwingStatusDominant().canAttack();
			// �\‚¦‚Ì�X�V
			updateAimebow();
			
			// TODO:test
			if (dataWatcher.getWatchableObjectInt(dataWatch_ExpValue) != experienceValue) {
				dataWatcher.updateObject(dataWatch_ExpValue, Integer.valueOf(experienceValue));
			}
			
			// Ž©•ª‚æ‚è‘å‚«‚È‚à‚Ì‚Í�æ‚Á‚¯‚È‚¢�iƒCƒJ�œ‚­�j
			if (riddenByEntity != null && !(riddenByEntity instanceof EntitySquid)) {
				if (height * width < riddenByEntity.height * riddenByEntity.width) {
					if (riddenByEntity instanceof EntityLivingBase) {
						attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)riddenByEntity), 0);
					}
					riddenByEntity.mountEntity(null);
					return;
				}
			}
			
			// •€‘•”õŽž‚Í�UŒ‚—Í‚ª�ã‚ª‚é
			AttributeInstance latt = this.getEntityAttribute(SharedMonsterAttributes.attackDamage);
			// ‘®�«‚ð‰ð�œ
			latt.removeModifier(attAxeAmp);
			ItemStack lis = getCurrentEquippedItem();
			if (lis != null && lis.getItem() instanceof ItemAxe) {
				// ‘®�«‚ð�Ý’è
				latt.applyModifier(attAxeAmp);
			}
		} else {
			// Client
			// TODO:test
			experienceValue = dataWatcher.getWatchableObjectInt(dataWatch_ExpValue);
		}
		
		// •R‚Å�f’v
		if(mstatgotcha != null) {
			double d = mstatgotcha.getDistanceSqToEntity(this);
			if (entityToAttack == null) {
				// ƒCƒ“ƒRƒ€‚²‚Á‚±—p
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
		
		// Ž€ˆö‚ð•\Ž¦
		if (!worldObj.isRemote) {
			// ƒ}ƒXƒ^�[”»’èŽ¸”s‚·‚é‚©‚à�H
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

	// ƒ|�[ƒVƒ‡ƒ“ƒGƒtƒFƒNƒg
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
		// TODO:•K—v‚©‚Ç‚¤‚©‚Ìƒ`ƒFƒbƒN
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
	 *  ƒCƒ“ƒxƒ“ƒgƒŠ‚ª•Ï�X‚³‚ê‚Ü‚µ‚½�B
	 */
	public void onInventoryChanged() {
		checkClockMaid();
		checkMaskedMaid();
		checkHeadMount();
		getNextEquipItem();
//		setArmorTextureValue();
	}

	/**
	 * ƒCƒ“ƒxƒ“ƒgƒŠ‚É‚ ‚éŽŸ‚Ì‘•”õ•i‚ð‘I‘ð
	 */
	public boolean getNextEquipItem() {
		if (worldObj.isRemote) {
			// ƒNƒ‰ƒCƒAƒ“ƒg‘¤‚Í�ˆ—�‚µ‚È‚¢
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
	 * ‘Î‰žŒ^ŽËŒ‚•�Ší‚ÌƒŠƒ��[ƒh”»’è
	 */
	public void getWeaponStatus() {
		// ”ò‚Ñ“¹‹ï—p‚Ì“ÁŽê�ˆ—�
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

	// •ÛŽ�ƒAƒCƒeƒ€ŠÖ˜A

	/**
	 * Œ»�Ý‚Ì‘•”õ•i
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
			// Ž�‚¿•¨‚ÌƒAƒbƒvƒf�[ƒg
			// “ÆŽ©Šg’£:•�’Ê‚ÉƒXƒ�ƒbƒg”Ô�†‚Ì’Ê‚è�A�ãˆÊ‚Wƒrƒbƒg‚Í‘•”õƒXƒ�ƒbƒg
			// par1‚ÍShort‚Å“n‚³‚ê‚é‚Ì‚Å‚»‚Ì‚æ‚¤‚É�B
			int lslotindex = par1 & 0x7f;
			int lequip = (par1 >>> 8) & 0xff;
			maidInventory.setInventorySlotContents(lslotindex, par2ItemStack);
			maidInventory.resetChanged(lslotindex);	// ‚±‚ê‚ÍˆÓ–¡‚È‚¢‚¯‚Ç‚È�B
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
		// ŽžŒv‚ðŽ�‚Á‚Ä‚¢‚é‚©�H
		mstatClockMaid = maidInventory.getInventorySlotContainItem(Item.pocketSundial.itemID) > -1;
	}
	/**
	 * ŽžŒv‚ðŽ�‚Á‚Ä‚¢‚é‚©?
	 */
	public boolean isClockMaid() {
		return mstatClockMaid;
	}

	protected void checkMaskedMaid() {
		// ƒCƒ“ƒxƒ“ƒgƒŠ‚Éƒwƒ‹ƒ€‚ª‚ ‚é‚©�H
		for (int i = maidInventory.mainInventory.length - 1; i >= 0; i--) {
			ItemStack is = maidInventory.getStackInSlot(i);
			if (is != null && is.getItem() instanceof ItemArmor && ((ItemArmor)is.getItem()).armorType == 0) {
				// ƒwƒ‹ƒ€‚ðŽ�‚Á‚Ä‚é
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
	 * ƒ�ƒbƒg‚ð”í‚Á‚Ä‚é‚© 
	 */
	public boolean isMaskedMaid() {
		return mstatMaskSelect > -1;
	}

	protected void checkHeadMount() {
		// ’Ç‰Á‚Ì“ª•”‘•”õ‚Ì”»’è
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
	 * ƒJƒ‚ƒtƒ‰�[ƒWƒ…�I 
	 */
	public boolean isCamouflage() {
		return mstatCamouflage;
	}
	/**
	 * ”«�A‚¦�ó‘Ô 
	 */
	public boolean isPlanter() {
		return mstatPlanter;
	}

	/**
	 * ƒ|�[ƒVƒ‡ƒ““™‚É‚æ‚é˜r�U‚èƒ‚�[ƒVƒ‡ƒ“‚Ì‘¬“x•â�³
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
	 * ŽèŽ�‚¿ƒAƒCƒeƒ€‚Ì”j‰ó
	 */
	public void destroyCurrentEquippedItem() {
		maidInventory.setInventoryCurrentSlotContents(null);
	}

	/**
	 * ƒ�ƒCƒhƒCƒ“ƒxƒ“ƒgƒŠ‚ðŠJ‚­
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
		
		// ƒvƒ‰ƒOƒCƒ“‚Å‚Ì�ˆ—�‚ð�æ‚É�s‚¤
		for (int li = 0; li < maidEntityModeList.size(); li++) {
			if (maidEntityModeList.get(li).preInteract(par1EntityPlayer, itemstack1)) {
				return true;
			}
		}
		// ‚µ‚á‚ª‚ÝŽž‚Í�ˆ—�–³Œø
		if (par1EntityPlayer.isSneaking()) {
			return false;
		}
		// ƒiƒfƒŠ”»’è
		if (lhealth > 0F && par1EntityPlayer.riddenByEntity != null && !(par1EntityPlayer.riddenByEntity instanceof LMM_EntityLittleMaid)) {
			// �Ú‚¹‘Ö‚¦
			par1EntityPlayer.riddenByEntity.mountEntity(this);
			return true;
		}
		
		
		
		if (mstatgotcha == null && par1EntityPlayer.fishEntity == null) {
			if(itemstack1 != null && itemstack1.itemID == Item.silk.itemID) {
				// •R‚ÅŒq‚®
				setGotcha(par1EntityPlayer.entityId);
				mstatgotcha = par1EntityPlayer;
				MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
				playSound("random.pop");
				return true;
			} 
			
			if (isContract()) {
				// Œ_–ñ�ó‘Ô
				if (lhealth > 0F && isMaidContractOwner(par1EntityPlayer)) {
					if (itemstack1 != null) {
						// ’Ç‰Á•ª‚Ì�ˆ—�
						setPathToEntity(null);
						// ƒvƒ‰ƒOƒCƒ“‚Å‚Ì�ˆ—�‚ð�æ‚É�s‚¤
						for (int li = 0; li < maidEntityModeList.size(); li++) {
							if (maidEntityModeList.get(li).interact(par1EntityPlayer, itemstack1)) {
								return true;
							}
						}
						if (isRemainsContract()) {
							// ’Ê�í
							if (itemstack1.itemID == Item.sugar.itemID) {
								// ƒ‚�[ƒh�Ø‘Ö
								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
								eatSugar(false, true);
								worldObj.setEntityState(this, (byte)11);
								
								mod_LMM_littleMaidMob.Debug("give suger." + worldObj.isRemote);
								if (!worldObj.isRemote) {
									setFreedom(isFreedom());
									if (isMaidWait()) {
										// “®�ìƒ‚�[ƒh‚Ì�Ø‘Ö
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
										// ‘Ò‹@
										setMaidWait(true);
									}
								}
								return true;
							}
							else if (itemstack1.itemID == Item.dyePowder.itemID) {
								// ƒJƒ‰�[ƒ�ƒCƒh
								if (!worldObj.isRemote) {
									setColor(15 - itemstack1.getItemDamage());
								}
								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
								return true;
							}
							else if (itemstack1.itemID == Item.feather.itemID) {
								// Ž©—R�s“®
								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
								setFreedom(!isFreedom());
								worldObj.setEntityState(this, isFreedom() ? (byte)12 : (byte)13);
								return true;
							}
							else if (itemstack1.itemID == Item.saddle.itemID) {
								// Œ¨ŽÔ
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
								// IFF‚ÌƒI�[ƒvƒ“
								MMM_Helper.decPlayerInventory(par1EntityPlayer, -1, 1);
//	    		            	ModLoader.openGUI(par1EntityPlayer, new LMM_GuiIFF(worldObj, this));
								if (worldObj.isRemote) {
									LMM_Client.OpenIFF(this, par1EntityPlayer);
								}
								return true;
							}
							else if ((itemstack1.itemID == Item.glassBottle.itemID) && (experienceValue >= 5)) {
								// Expƒ{ƒgƒ‹
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
								// ƒ|�[ƒVƒ‡ƒ“
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
							// ƒXƒgƒ‰ƒCƒL
							if (itemstack1.itemID == Item.sugar.itemID) {
								// ŽóŽæ‹‘”Û
								worldObj.setEntityState(this, (byte)10);
								return true;
							} else if (itemstack1.itemID == Item.cake.itemID) {
								// �ÄŒ_–ñ
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
					// ƒ�ƒCƒhƒCƒ“ƒxƒ“ƒgƒŠ
					setOwner(par1EntityPlayer.username);
					getNavigator().clearPathEntity();
					isJumping = false;
					displayGUIMaidInventory(par1EntityPlayer);
//    		        	ModLoader.openGUI(par1EntityPlayer, new LMM_GuiInventory(this, par1EntityPlayer.inventory, maidInventory));
//    				serchedChest.clear();
					return true;
				}
			} else {
				// –¢Œ_–ñ
				if (itemstack1 != null) {
					if (itemstack1.itemID == Item.cake.itemID) {
						// Œ_–ñ
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
							// Œ_–ñ‹L”O“ú‚Æ�A�‰ŠúŒ_–ñŠúŠÔ
							maidContractLimit = (24000 * 7);
							maidAnniversary = worldObj.getWorldTime();
							// ƒeƒNƒXƒ`ƒƒ‚ÌƒAƒbƒvƒf�[ƒg:‚¢‚ç‚ñ�H
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

	// ƒ�ƒCƒh‚ÌŒ_–ñ�Ý’è
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
	 * Œ_–ñŠúŠÔ‚ÌŽc‚è‚ª‚ ‚é‚©‚ðŠm”F
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
	 * ƒXƒgƒ‰ƒCƒL‚É“ü‚Á‚Ä‚¢‚È‚¢‚©”»’è
	 * @return
	 */
	public boolean isRemainsContract() {
		return getMaidFlags(dataWatch_Flags_remainsContract);
	}

	public float getContractLimitDays() {
		return maidContractLimit > 0 ? ((float)maidContractLimit / 24000F) : -1F;
	}

	public boolean updateMaidContract() {
		// “¯ˆê�«‚Ìƒ`ƒFƒbƒN
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
		// Žå‚ðŠl“¾
		if (isContract()) {
			EntityPlayer entityplayer = mstatMasterEntity;
			if (mstatMasterEntity == null || mstatMasterEntity.isDead) {
				String lname; 
				// ƒT�[ƒo�[‘¤‚È‚ç‚¿‚á‚ñ‚ÆƒI�[ƒi”»’è‚·‚é
				if (!MMM_Helper.isClient
						|| mod_LMM_littleMaidMob.cfg_checkOwnerName 
						|| MMM_Helper.mc.thePlayer == null) {
					lname = getMaidMaster();
				} else {
					lname = MMM_Helper.mc.thePlayer.username;
				}
				entityplayer = worldObj.getPlayerEntityByName(lname);
				// ‚Æ‚è‚ ‚¦‚¸Žå‚Ì–¼‘O‚ð“ü‚ê‚Ä‚Ý‚é
				// TODO:�Ä�Ý’è‚Í•s‰Â‚É‚È‚Á‚½‚Ì‚ÅŒo‰ßŠÏŽ@
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

	// ƒ�ƒCƒh‚Ì‘Ò‹@�Ý’è
	public boolean isMaidWait() {
		return maidWait;
	}

	public boolean isMaidWaitEx() {
		return isMaidWait() | (mstatWaitCount > 0) | isOpenInventory();
	}

	public void setMaidWait(boolean pflag) {
		// ‘Ò‹@�í‘Ô‚Ì�Ý’è�A isMaidWaitŒn‚Åtrue‚ð•Ô‚·‚È‚çAI‚ª�ŸŽè‚ÉˆÚ“®‚ð’âŽ~‚³‚¹‚é�B
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

	
	// ƒCƒ“ƒxƒ“ƒgƒŠ‚Ì•\Ž¦ŠÖŒW
	// ‚Ü‚³‚®‚ê‚é‚Ì‚Íˆê�l‚¾‚¯
	public void setOpenInventory(boolean flag) {
		mstatOpenInventory = flag;
	}

	public boolean isOpenInventory() {
		return mstatOpenInventory;
	}

	/**
	 * GUI‚ðŠJ‚¢‚½Žž‚ÉƒT�[ƒo�[‘¤‚ÅŒÄ‚Î‚ê‚é�B
	 */
	public void onGuiOpened() {
		setOpenInventory(true);
	}

	/**
	 * GUI‚ð•Â‚ß‚½Žž‚ÉƒT�[ƒo�[‘¤‚ÅŒÄ‚Î‚ê‚é�B
	 */
	public void onGuiClosed() {
		setOpenInventory(false);
		int li = maidMode & 0x0080;
		setMaidWaitCount((li == 0) ? 50 : 0);
	}

	// ˜r�U‚è
	public void setSwing(int attacktime, LMM_EnumSound enumsound) {
		setSwing(attacktime, enumsound, maidDominantArm);
	}
	public void setSwing(int pattacktime, LMM_EnumSound enumsound, int pArm) {
		mstatSwingStatus[pArm].attackTime = pattacktime;
//		maidAttackSound = enumsound;
//        soundInterval = 0;// ‚¢‚é‚©�H
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
	 * —˜‚«˜r‚ÌƒŠƒ��[ƒhƒ^ƒCƒ€
	 */
	public LMM_SwingStatus getSwingStatusDominant() {
		return mstatSwingStatus[maidDominantArm];
	}

	public LMM_SwingStatus getSwingStatus(int pindex) {
		return mstatSwingStatus[pindex];
	}


	// �¡�ª‚Ìƒ�ƒCƒh‚ÍŒŒ‚É‹Q‚¦‚Ä‚¨‚é
	public void setBloodsuck(boolean pFlag) {
		mstatBloodsuck = pFlag;
		setMaidFlags(pFlag, dataWatch_Flags_Bloodsuck);
	}

	public boolean isBloodsuck() {
		return mstatBloodsuck;
	}


	// �»“œŠÖ˜A
	public void setLookSuger(boolean pFlag) {
		mstatLookSuger = pFlag;
		setMaidFlags(pFlag, dataWatch_Flags_LooksSugar);
	}

	public boolean isLookSuger() {
		return mstatLookSuger;
	}

	/**
	 * ƒyƒ�ƒb�E�E�E‚±‚ê‚Í�E�E�E�»“œƒb�I�I
	 * motion : ˜r‚ð�U‚é‚©�H
	 * recontract : Œ_–ñ‰„’·Œø‰ÊƒAƒŠ�H
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
			// Œ_–ñŠúŠÔ‚Ì‰„’·
			maidContractLimit += 24000;
			if (maidContractLimit > 168000) {
				maidContractLimit = 168000;	// 24000 * 7
			}
		}
		
		// Žb’è�ˆ—�
		if (maidAvatar != null) {
			maidAvatar.getFoodStats().addStats(20, 20F);
		}
	}


	// ‚¨ŽdŽ–ƒ`ƒ…
	/**
	 * ŽdŽ–’†‚©‚Ç‚¤‚©‚Ì�Ý’è
	 */
	public void setWorking(boolean pFlag) {
		mstatWorkingCount.setEnable(pFlag);
	}
	
	/**
	 * ŽdŽ–’†‚©‚Ç‚¤‚©‚ð•Ô‚·
	 */
	public boolean isWorking() {
		return mstatWorkingCount.isEnable();
	}

	/**
	 * ŽdŽ–‚ª�I—¹‚µ‚Ä‚à—]‰C‚ðŠÜ‚ß‚Ä•Ô‚·
	 */
	public boolean isWorkingDelay() {
		return mstatWorkingCount.isDelay();
	}

	/**
	 * ƒgƒŒ�[ƒT�[ƒ‚�[ƒh‚Ì�Ý’è
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
	 * ƒgƒŒ�[ƒT�[ƒ‚�[ƒh‚Å‚ ‚é‚©�H
	 */
	public boolean isTracer() {
		return maidTracer;
	}


	// ‚¨—V‚Ñƒ‚�[ƒh
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


	// Ž©—R�s“®
	public void setFreedom(boolean pFlag) {
		// AIŠÖ˜A‚ÌƒŠƒZƒbƒg‚à‚±‚±‚Å�B
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
	 * ƒT�[ƒo�[‚ÖƒeƒNƒXƒ`ƒƒƒpƒbƒN‚ÌƒCƒ“ƒfƒbƒNƒX‚ð‘—‚é�B
	 * ƒNƒ‰ƒCƒAƒ“ƒg‘¤‚Ì�ˆ—�
	 */
	protected boolean sendTextureToServer() {
		// 16bit‚ ‚ê‚ÎƒeƒNƒXƒ`ƒƒƒpƒbƒN‚Ì�”‚É‚½‚è‚ñ‚×
		MMM_TextureManager.instance.postSetTexturePack(this, textureData.getColor(), textureData.getTextureBox());
		return true;
	}


	public boolean updateTexturePack() {
		// ƒeƒNƒXƒ`ƒƒƒpƒbƒN‚ª�X�V‚³‚ê‚Ä‚¢‚È‚¢‚©‚ðƒ`ƒFƒbƒN
		// ƒNƒ‰ƒCƒAƒ“ƒg‘¤‚Ì
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
		// “¯ˆê�«‚Ìƒ`ƒFƒbƒN
		int lc = getColor();
		if (textureData.getColor() != lc) {
			textureData.setColor(lc);
			return true;
		}
		return false;
	}

	/**
	 * •R‚ÌŽ�‚¿Žå
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
	 * ‹|�\‚¦‚ð�X�V
	 */
	public void updateAimebow() {
		boolean lflag = (maidAvatar != null && maidAvatar.isUsingItemLittleMaid()) || mstatAimeBow;
		setMaidFlags(lflag, dataWatch_Flags_Aimebow);
	}

	public boolean isAimebow() {
		return (dataWatcher.getWatchableObjectInt(dataWatch_Flags) & dataWatch_Flags_Aimebow) > 0;
	}


	/**
	 * ŠeŽíƒtƒ‰ƒO‚ÌƒAƒbƒvƒf�[ƒg
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
	 * ƒtƒ‰ƒOŒQ‚É’l‚ðƒZƒbƒg�B
	 * @param pCheck�F ‘Î�Û’l�B
	 * @param pFlags�F ‘Î�Ûƒtƒ‰ƒO�B
	 */
	public void setMaidFlags(boolean pFlag, int pFlagvalue) {
		int li = dataWatcher.getWatchableObjectInt(dataWatch_Flags);
		li = pFlag ? (li | pFlagvalue) : (li & ~pFlagvalue);
		dataWatcher.updateObject(dataWatch_Flags, Integer.valueOf(li));
	}

	/**
	 * Žw’è‚³‚ê‚½ƒtƒ‰ƒO‚ðŠl“¾
	 */
	public boolean getMaidFlags(int pFlagvalue) {
		return (dataWatcher.getWatchableObjectInt(dataWatch_Flags) & pFlagvalue) > 0;
	}

	/**
	 *  —˜‚«˜r‚Ì�Ý’è
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
		// ƒ‚ƒfƒ‹‚Ì�‰Šú‰»
		((MMM_TextureBox)textureData.textureBox[0]).models[0].setCapsValue(MMM_IModelCaps.caps_changeModel, maidCaps);
		// ƒXƒ^ƒr‚Ì•t‚¯‘Ö‚¦
//		for (Entry<String, MMM_EquippedStabilizer> le : pEntity.maidStabilizer.entrySet()) {
//			if (le.getValue() != null) {
//				le.getValue().updateEquippedPoint(pEntity.textureModel0);
//			}
//		}
		maidSoundRate = LMM_SoundManager.getSoundRate(textureData.getTextureName(0), getColor());

	}

	/**
	 * Client—p
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

	// TileŠÖŒW

	/**
	 * Žg‚Á‚Ä‚¢‚éTile‚©‚Ç‚¤‚©”»’è‚µ‚Ä•Ô‚·�B
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
	 * ƒ��[ƒJƒ‹•Ï�”‚ÉTile‚ÌˆÊ’u‚ð“ü‚ê‚é�B
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
