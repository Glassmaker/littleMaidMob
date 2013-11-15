package net.minecraft.src;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.Map;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.client.stats.StatPlaceholder;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.world.biome.BiomeGenBase;

public class mod_LMM_littleMaidMob extends BaseMod {

	public static String[] cfg_comment = {
		"spawnWeight = Relative spawn weight. The lower the less common. 10=pigs. 0=off",
		"spawnLimit = Maximum spawn count in the World.",
		"minGroupSize = Minimum spawn group count.",
		"maxGroupSize = Maximum spawn group count.",
		"canDespawn = It will despawn, if it lets things go. ",
		"checkOwnerName = At local, make sure the name of the owner. ",
		"antiDoppelganger = Not to survive the doppelganger. ",
		"enableSpawnEgg = Enable LMM SpawnEgg Recipe. ",
		"VoiceDistortion = LittleMaid Voice distortion.",
		"defaultTexture = Default selected Texture Packege. Null is Random",
		"DebugMessage = Print Debug Massages.",
		"DeathMessage = Print Death Massages.",
		"Dominant = Spawn Anywhere.",
		"Aggressive = true: Will be hostile, false: Is a pacifist",
		"AchievementID = used Achievement index.(0 = Disable)",
		"UniqueEntityId = UniqueEntityId(0 is AutoAssigned. max 255)"
	};
	
//	@MLProp(info="Relative spawn weight. The lower the less common. 10=pigs. 0=off")
	public static int cfg_spawnWeight = 5;
//	@MLProp(info="Maximum spawn count in the World.")
	public static int cfg_spawnLimit = 20;
//	@MLProp(info="Minimum spawn group count.")
	public static int cfg_minGroupSize = 1;
//	@MLProp(info="Maximum spawn group count.")
	public static int cfg_maxGroupSize = 3;
//	@MLProp(info="It will despawn, if it lets things go. ")
	public static boolean cfg_canDespawn = false;
//	@MLProp(info="At local, make sure the name of the owner. ")
	public static boolean cfg_checkOwnerName = false;
//	@MLProp(info="Not to survive the doppelganger. ")
	public static boolean cfg_antiDoppelganger = true;
//	@MLProp(info="Enable LMM SpawnEgg Recipe. ")
	public static boolean cfg_enableSpawnEgg = false;
	
	
//	@MLProp(info="LittleMaid Voice distortion.")
	public static boolean cfg_VoiceDistortion = true;
	
//	@MLProp(info="Default selected Texture Packege. Null is Random")
	public static String cfg_defaultTexture = "";
//	@MLProp(info="Print Debug Massages.")
	public static boolean cfg_DebugMessage = true;
//	@MLProp(info="Print Death Massages.")
	public static boolean cfg_DeathMessage = true;
//	@MLProp(info="Spawn Anywhere.")
	public static boolean cfg_Dominant = false;
//	@MLProp(info="true: AlphaBlend(request power), false: AlphaTest(more fast)")
//	public static boolean AlphaBlend = true;
//	@MLProp(info="true: Will be hostile, false: Is a pacifist")
	public static boolean cfg_Aggressive = true;

//	@MLProp(info="used Achievement index.(0 = Disable)")
	public static int cfg_AchievementID = 222000;

//	@MLProp(info="UniqueEntityId(0 is AutoAssigned.)", max=255)
	public static int cfg_UniqueEntityId = 30;

	public static Achievement ac_Contract;
	public static int containerID;


	public static void Debug(String pText, Object... pVals) {
		// ﾆ断ﾆ弛ﾆ鍛ﾆ丹ﾆ抵ｿｽﾆ鍛ﾆ短・ｽ[ﾆ淡
		if (cfg_DebugMessage) {
			System.out.println(String.format("littleMaidMob-" + pText, pVals));
		}
	}

	@Override
	public String getName() {
		return "littleMaidMob";
	}

	@Override
	public String getPriorities() {
		// MMMLib窶堙ｰ窶牌窶ｹ・ｽ
		return "required-after:mod_MMM_MMMLib";
	}

	@Override
	public String getVersion() {
		return "1.6.2-5";
	}

	@Override
	public void load() {
		// MMMLib窶堙軍evisionﾆ蛋ﾆ巽ﾆ鍛ﾆ誰
		MMM_Helper.checkRevision("6");
		MMM_Config.checkConfig(this.getClass());
		
		cfg_defaultTexture = cfg_defaultTexture.trim();
		containerID = 222;
		ModLoader.registerContainerID(this, containerID);
		cfg_UniqueEntityId = MMM_Helper.registerEntity(LMM_EntityLittleMaid.class,
				"LittleMaid", cfg_UniqueEntityId, this, 80, 3, true, 0xefffef, 0x9f5f5f);
		ModLoader.addLocalization("entity.LittleMaid.name", "LittleMaid");
		ModLoader.addLocalization("entity.LittleMaid.name", "ja_JP", "ﾆ椎ﾆ暖ﾆ停ｹﾆ抵ｿｽﾆ辰ﾆ檀");
		if (cfg_enableSpawnEgg) {
			// ・ｽﾂｵﾅﾂｫ窶廃ﾆ椎槌歎ﾆ痴窶堙ｰ窶凖・ｰﾃ・
			ModLoader.addRecipe(new ItemStack(Item.monsterPlacer, 1, cfg_UniqueEntityId), new Object[] {
				"scs",
				"sbs",
				" e ",
				Character.valueOf('s'), Item.sugar,
				Character.valueOf('c'), new ItemStack(Item.dyePowder, 1, 3),
				Character.valueOf('b'), Item.slimeBall,
				Character.valueOf('e'), Item.egg,
			});
		}
		
		if (MMM_Helper.isClient) {
			// ﾆ但ﾆ蛋ﾅｽﾃﾅ陳ｱ窶廃
			if (cfg_AchievementID != 0) {
				while (true) {
					// ﾆ但ﾆ蛋・ｽ[ﾆ置窶堙ｰﾅl窶慊ｾ窶堋ｵ窶堋ｽ・ｽﾃｳ窶佚披堙・督｢窶徙ﾋ弯窶堋ｾ窶堙・ｿｽAUNKNOWN窶堙姑但ﾆ蛋・ｽ[ﾆ置窶堋ｪ窶徙ﾋ弯窶堋ｳ窶堙ｪ窶堙・堋｢窶堙ｩ窶堙娯堙・ｿｽﾃｭ・ｽﾅ凪堋ｷ窶堙ｩ・ｽB
					int laid = 5242880 + cfg_AchievementID;
					StatBase lsb = StatList.getOneShotStat(laid);
					boolean lflag = false;
					if (lsb != null) {
						if (lsb instanceof StatPlaceholder) {
							//StatList.oneShotStats.remove(Integer.valueOf(laid));
							Debug("Replace Achievement: %d(%d)", cfg_AchievementID, laid);
							lflag = true;
						} else {
							Debug("Already Achievement: %d(%d) - %s(%s)", cfg_AchievementID, laid, lsb.statGuid, lsb.getClass().getSimpleName());
							break;
						}
					}
					ac_Contract = new Achievement(cfg_AchievementID, "littleMaid", 1, -4, Item.cake, AchievementList.bakeCake).registerAchievement();
//	                ModLoader.AddAchievementDesc(ac_Contract, "(21)", "Capture the LittleMaid!");
					ModLoader.addAchievementDesc(ac_Contract, "Enlightenment!", "Capture the LittleMaid!");
					ModLoader.addLocalization("achievement.littleMaid", "ja_JP", "ﾅ津･窶堙ｨ・ｽB");
					ModLoader.addLocalization("achievement.littleMaid.desc", "ja_JP", "ﾆ抵ｿｽﾆ辰ﾆ檀窶堋ｳ窶堙ｱ窶堙ｰ窶愿ｼﾅｽﾃｨ窶堋ｵ窶堙懌堋ｵ窶堋ｽ・ｽB");
					if (lflag) {
						LMM_Client.setAchievement();
					}
					break;
				}
			}
			
			// 窶督ｼ・ｽﾃ娯｢ﾃ焦ﾂｷﾆ弾・ｽ[ﾆ置ﾆ停ｹ
			ModLoader.addLocalization("littleMaidMob.text.Health", "Health");
			ModLoader.addLocalization("littleMaidMob.text.Health", "ja_JP", "ﾆ抵ｿｽﾆ辰ﾆ檀窶ｹﾂｭ窶忸");
			ModLoader.addLocalization("littleMaidMob.text.AP", "AP");
			ModLoader.addLocalization("littleMaidMob.text.AP", "ja_JP", "ﾆ抵ｿｽﾆ辰ﾆ檀窶倪｢・ｽb");
			ModLoader.addLocalization("littleMaidMob.text.STATUS", "Status");
			ModLoader.addLocalization("littleMaidMob.text.STATUS", "ja_JP", "ﾆ抵ｿｽﾆ辰ﾆ檀・ｽﾃｳ窶佚・);
			
			// ﾆ断ﾆ稚ﾆ辿ﾆ停ｹﾆ暖ﾆ停堡断ﾆ停ｹ窶堙鯉ｿｽﾃ昶凖ｨ
			LMM_Client.init();
		}
		
		// AIﾆ椎ﾆ湛ﾆ暖窶堙娯凖・ｰﾃ・
		LMM_EntityModeManager.init();
		
		// ﾆ但ﾆ辰ﾆ弾ﾆ停ぎﾆ湛ﾆ抵ｿｽﾆ鍛ﾆ暖・ｽX・ｽV窶廃窶堙姑恥ﾆ単ﾆ鍛ﾆ暖
		ModLoader.registerPacketChannel(this, "LMM|Upd");
		
	}

	@Override
	public void addRenderer(Map map) {
		LMM_Client.addRenderer(map);
	}

	@Override
	public void modsLoaded() {
		// ﾆ断ﾆ稚ﾆ辿ﾆ停ｹﾆ暖ﾆ停堡断ﾆ停ｹ窶堙鯉ｿｽﾃ昶凖ｨ
		MMM_TextureManager.instance.setDefaultTexture(LMM_EntityLittleMaid.class, MMM_TextureManager.instance.getTextureBox("default_Orign"));
		
		if (cfg_UniqueEntityId == -1) return;
		// Dominant
		if(cfg_spawnWeight > 0) {
			if (cfg_Dominant) {
				// 窶堋窶堙ｧ窶堙､窶堙ｩ・ｽﾃｪ・ｽﾅ窶堙家湛ﾆ竹・ｽ[ﾆ停懌堋ｷ窶堙ｩ
				try {
					Field afield[] = (BiomeGenBase.class).getDeclaredFields();
					LinkedList<BiomeGenBase> linkedlist = new LinkedList<BiomeGenBase>();
					for(int j = 0; j < afield.length; j++) {
						Class class1 = afield[j].getType();
						if((afield[j].getModifiers() & 8) != 0 && class1.isAssignableFrom(BiomeGenBase.class)) {
							BiomeGenBase biomegenbase = (BiomeGenBase)afield[j].get(null);
							linkedlist.add(biomegenbase);
						}
					}
					BiomeGenBase[] dominateBiomes = (BiomeGenBase[])linkedlist.toArray(new BiomeGenBase[0]);
					
					ModLoader.addSpawn(net.minecraft.src.LMM_EntityLittleMaid.class, cfg_spawnWeight, cfg_minGroupSize, cfg_maxGroupSize, EnumCreatureType.creature, dominateBiomes);
				} catch (Exception exception) {
					Debug("Dominate Exception.");
				}
			} else {
				// 窶凖奇ｿｽﾃｭﾆ湛ﾆ竹・ｽ[ﾆ停懶ｿｽﾃ昶凖ｨ
				ModLoader.addSpawn(LMM_EntityLittleMaid.class, cfg_spawnWeight, cfg_minGroupSize, cfg_maxGroupSize, EnumCreatureType.creature);
			}
		}
		
		// ﾆ停夲ｿｽ[ﾆ檀ﾆ椎ﾆ湛ﾆ暖窶堙ｰ・ｽ\窶凛
		LMM_EntityModeManager.loadEntityMode();
		LMM_EntityModeManager.showLoadedModes();
		
		if (MMM_Helper.isClient) {
			// 窶ｰﾂｹ・ｽﾂｺ窶堙娯ｰﾃｰ・ｽﾃ・
			LMM_SoundManager.init();
			// ﾆ探ﾆ脱ﾆ停愴檀ﾆ恥ﾆ鍛ﾆ誰
			LMM_SoundManager.loadDefaultSoundPack();
			LMM_SoundManager.loadSoundPack();
		}
		
		// IFF窶堙姑抵ｿｽ・ｽ[ﾆ檀
		LMM_IFF.loadIFFs();
		
	}

	@Override
	public void serverCustomPayload(NetServerHandler var1, Packet250CustomPayload var2) {
		// ﾆ探・ｽ[ﾆ弛窶伉､窶堙娯慊ｮ・ｽﾃｬ
		LMM_Net.serverCustomPayload(var1, var2);
	}

	@Override
	public void clientCustomPayload(NetClientHandler var1, Packet250CustomPayload var2) {
		// ﾆ誰ﾆ停ｰﾆ辰ﾆ但ﾆ停愴暖窶伉､窶堙娯愿・ｽﾃｪﾆ恥ﾆ単ﾆ鍛ﾆ暖ﾅｽﾃｳ・ｽM窶慊ｮ・ｽﾃｬ
		LMM_Client.clientCustomPayload(var1, var2);
	}

	@Override
	public GuiContainer getContainerGUI(EntityClientPlayerMP var1, int var2,
			int var3, int var4, int var5) {
		return LMM_Client.getContainerGUI(var1, var2, var3, var4, var5);
	}

}
