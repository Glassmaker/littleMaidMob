package net.minecraft.src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityOwnable;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.server.MinecraftServer;

/**
 * IFF窶堙ｰﾅﾃ・費ｿｽ窶堋ｷ窶堙ｩ窶堋ｽ窶堙溪堙姑誰ﾆ停ｰﾆ湛・ｽA窶堙吮堙堡筑ﾆ停ｹﾆ蛋窶廃・ｽB
 * username : null=ﾆ抵ｿｽ・ｽ[ﾆ谷ﾆ停ｹﾆ致ﾆ椎槌辰ﾅｽﾅｾ・ｽADefault窶堙ｰﾅｽg窶堋､
 */
public class LMM_IFF {

	public static final int iff_Enemy = 0;
	public static final int iff_Unknown = 1;
	public static final int iff_Friendry = 2;

	/**
	 * ﾆ抵ｿｽ・ｽ[ﾆ谷ﾆ停ｹ窶廃・ｽAﾅｽﾃ｡窶堋ｵ窶堋ｭ窶堙哉筑ﾆ停ｹﾆ蛋窶堙姑断ﾆ稚ﾆ辿ﾆ停ｹﾆ暖・ｽﾃ昶凖ｨ
	 */
	public static Map<String, Integer> DefaultIFF = new TreeMap<String, Integer>();
	/**
	 * ﾆ停・ｽ[ﾆ旦窶突・堙栗FF
	 */
	public static Map<String, Map<String, Integer>> UserIFF = new HashMap<String, Map<String, Integer>>();

	/**
	 * IFF窶堙姑嘆ﾆ鍛ﾆ暖
	 */
	public static Map<String, Integer> getUserIFF(String pUsername) {
		if (pUsername == null) {
			return DefaultIFF;
		}
		if (MMM_Helper.isLocalPlay()) {
			pUsername = "";
		}
		
		if (!UserIFF.containsKey(pUsername)) {
			// IFF窶堋ｪ窶堙遺堋｢窶堙娯堙・ｿｽﾃｬ・ｽﾂｬ
			if (pUsername.isEmpty()) {
				UserIFF.put(pUsername, DefaultIFF);
			} else {
				Map<String, Integer> lmap = new HashMap<String, Integer>();
				lmap.putAll(DefaultIFF);
				UserIFF.put(pUsername, lmap);
			}
		}
		// ﾅﾃｹ窶堙俄堋窶堙ｩ
		return UserIFF.get(pUsername);
	}

	public static void setIFFValue(String pUsername, String pName, int pValue) {
		Map<String, Integer> lmap = getUserIFF(pUsername);
		lmap.put(pName, pValue);
	}

	protected static int checkEntityStatic(String pName, Entity pEntity,
			int pIndex, Map<String, Entity> pMap) {
		int liff = LMM_IFF.iff_Unknown;
		if (pEntity instanceof EntityLivingBase) {
			if (pEntity instanceof LMM_EntityLittleMaid) {
				switch (pIndex) {
				case 0:
					// 窶禿ｬ・ｽﾂｶﾅｽﾃｭ
					liff = LMM_IFF.iff_Unknown;
					break;
				case 1:
					// ﾅｽﾂｩ窶｢ﾂｪ窶堙固胆窶禿ｱﾅｽﾃ・
					pName = (new StringBuilder()).append(pName).append(":Contract").toString();
					((LMM_EntityLittleMaid) pEntity).setContract(true);
					liff = LMM_IFF.iff_Friendry;
					break;
				case 2:
					// 窶伉ｼ・ｽl窶堙固胆窶禿ｱﾅｽﾃ・
					pName = (new StringBuilder()).append(pName).append(":Others").toString();
					((LMM_EntityLittleMaid) pEntity).setContract(true);
					liff = LMM_IFF.iff_Friendry;
					break;
				}
			} else if (pEntity instanceof EntityOwnable) {
				switch (pIndex) {
				case 0:
					// 窶禿ｬ・ｽﾂｶﾅｽﾃｭ
					break;
				case 1:
					// ﾅｽﾂｩ窶｢ﾂｪ窶堙娯ｰﾃ・几
					pName = (new StringBuilder()).append(pName).append(":Taim").toString();
					if (pEntity instanceof EntityTameable) {
						((EntityTameable) pEntity).setTamed(true);
					}
					liff = LMM_IFF.iff_Friendry;
					break;
				case 2:
					// 窶伉ｼ・ｽl窶堙娯ｰﾃ・几
					pName = (new StringBuilder()).append(pName).append(":Others").toString();
					if (pEntity instanceof EntityTameable) {
						((EntityTameable) pEntity).setTamed(true);
					}
					liff = LMM_IFF.iff_Unknown;
					break;
				}
				if (pIndex != 0) {
					if (pEntity instanceof EntityOcelot) {
						((EntityOcelot) pEntity).setTameSkin(1 + (new Random()).nextInt(3));
					}
				}
			}
			if (pMap != null) {
				// 窶｢\ﾅｽﾂｦ窶廃Entity窶堙娯凖・ｰﾃ・
				pMap.put(pName, pEntity);
				mod_LMM_littleMaidMob.Debug(pName + " added.");
			}
			
			// IFF窶堙鯉ｿｽ窶ｰﾅﾃｺ窶冤
			if (!DefaultIFF.containsKey(pName)) {
				if (pEntity instanceof IMob) {
					liff = LMM_IFF.iff_Enemy;
				}
				DefaultIFF.put(pName, liff);
			}
		}
		
		return liff;
	}

	/**
	 * 窶廨窶督｡窶｢ﾃｻﾅｽﾂｯ窶｢ﾃ岩敖ｻ窶凖ｨ
	 */
	public static int getIFF(String pUsername, String entityname) {
		if (entityname == null) {
			return mod_LMM_littleMaidMob.cfg_Aggressive ? iff_Enemy : iff_Friendry;
		}
		int lt = iff_Enemy;
		Map<String, Integer> lmap = getUserIFF(pUsername);
		if (lmap.containsKey(entityname)) {
			lt = lmap.get(entityname);
		} else if (lmap != DefaultIFF && DefaultIFF.containsKey(entityname)) {
			// 窶督｢窶徙ﾋ弯窶堋ｾ窶堋ｯ窶堙⑤efault窶堙俄堙搾ｿｽﾃ昶凖ｨ窶堋ｪ窶堋窶堙ｩﾅｽﾅｾ窶堙坂冤窶堙ｰﾆ坦ﾆ痴・ｽ[
			lt = DefaultIFF.get(entityname);
			lmap.put(entityname, lt);
		} else {
			// 窶督｢窶徙ﾋ弯Entity窶堙鯉ｿｽﾃｪ・ｽ窶｡窶堙坂徙ﾋ弯窶慊ｮ・ｽﾃｬ
			int li = entityname.indexOf(":");
			String ls;
			if (li > -1) {
				ls = entityname.substring(0, li);
			} else {
				ls = entityname;
			}
			Entity lentity = EntityList.createEntityByName(ls, null);
			li = 0;
			if (entityname.indexOf(":Contract") > -1) {
				li = 1;
			} else 
			if (entityname.indexOf(":Taim") > -1) {
				li = 1;
			} else
			if (entityname.indexOf(":Others") > -1) {
				li = 2;
			}
			lt = checkEntityStatic(ls, lentity, li, null);
			lmap.put(entityname, lt);
		}
		return lt;
	}

	/**
	 * 窶廨窶督｡窶｢ﾃｻﾅｽﾂｯ窶｢ﾃ岩敖ｻ窶凖ｨ
	 */
	public static int getIFF(String pUsername, Entity entity) {
		if (entity == null || !(entity instanceof EntityLivingBase)) {
			return mod_LMM_littleMaidMob.cfg_Aggressive ? iff_Enemy : iff_Friendry;
		}
		String lename = EntityList.getEntityString(entity);
		String lcname = lename;
		if (lename == null) {
			// 窶督ｼ・ｽﾃ娯督｢窶凖ｨ窶ｹ`MOB・ｽAﾆ致ﾆ椎抵ｿｽ[ﾆ停橸ｿｽ[窶堙・堋ｩ・ｽH
			return iff_Friendry;
			// return mod_LMM_littleMaidMob.Aggressive ? iff_Unknown :
			// iff_Friendry;
		}
		int li = 0;
		if (entity instanceof LMM_EntityLittleMaid) {
			if (((LMM_EntityLittleMaid) entity).isContract()) {
				if (((LMM_EntityLittleMaid) entity).getMaidMaster().contentEquals(pUsername)) {
					// ﾅｽﾂｩ窶｢ﾂｪ窶堙・
					lcname = (new StringBuilder()).append(lename).append(":Contract").toString();
					li = 1;
				} else {
					// 窶伉ｼ・ｽl窶堙・
					lcname = (new StringBuilder()).append(lename).append(":Others").toString();
					li = 2;
				}
			}
		} else if (entity instanceof EntityOwnable) {
			String loname = ((EntityOwnable)entity).getOwnerName();
			if (!loname.isEmpty()) {
				if (loname.contentEquals(pUsername)) {
					// ﾅｽﾂｩ窶｢ﾂｪ窶堙・
					lcname = (new StringBuilder()).append(lename).append(":Taim").toString();
					li = 1;
				} else {
					// 窶伉ｼ・ｽl窶堙・
					lcname = (new StringBuilder()).append(lename).append(":Others").toString();
					li = 2;
				}
			}
		}
		if (!getUserIFF(pUsername).containsKey(lcname)) {
			checkEntityStatic(lename, entity, li, null);
		}
		return getIFF(pUsername, lcname);
	}

	public static void loadIFFs() {
		// ﾆ探・ｽ[ﾆ弛・ｽ[窶伉､窶堙・
		if (!MMM_Helper.isClient) {
			// ﾆ探・ｽ[ﾆ弛・ｽ[窶伉､・ｽﾋ・費ｿｽ
			loadIFF("");
			File lfile = MinecraftServer.getServer().getFile("");
			for (File lf : lfile.listFiles()) {
				if (lf.getName().endsWith("littleMaidMob.iff")) {
					String ls = lf.getName().substring(17, lf.getName().length() - 20);
					mod_LMM_littleMaidMob.Debug(ls);
					loadIFF(ls);
				}
			}
		} else {
			// ﾆ誰ﾆ停ｰﾆ辰ﾆ但ﾆ停愴暖窶伉､
			loadIFF(null);
		}
	}

	protected static File getFile(String pUsername) {
		File lfile;
		if (pUsername == null) {
			lfile = new File(MMM_Helper.mc.mcDataDir, "config/littleMaidMob.iff");
		} else {
			String lfilename;
			if (pUsername.isEmpty()) {
				lfilename = "config/littleMaidMob.iff";
			} else {
				lfilename = "config/littleMaidMob_".concat(pUsername).concat(".iff");
			}
			lfile = MinecraftServer.getServer().getFile(lfilename);
		}
		mod_LMM_littleMaidMob.Debug(lfile.getAbsolutePath());
		return lfile;
	}

	public static void loadIFF(String pUsername) {
		// IFF ﾆ稚ﾆ叩ﾆ辰ﾆ停ｹ窶堙娯愿・ｿｽﾅｾ窶堙・
		// 窶慊ｮ・ｽﾃｬ窶堙哉探・ｽ[ﾆ弛・ｽ[窶伉､窶堙・・窶凖ｨ
		File lfile = getFile(pUsername);
		if (!(lfile.exists() && lfile.canRead())) {
			return;
		}
		Map<String, Integer> lmap = getUserIFF(pUsername);
		
		try {
			FileReader fr = new FileReader(lfile);
			BufferedReader br = new BufferedReader(fr);
			
			String s;
			while ((s = br.readLine()) != null) {
				String t[] = s.split("=");
				if (t.length > 1) {
					if (t[0].startsWith("triggerWeapon")) {
						LMM_TriggerSelect.appendTriggerItem(pUsername, t[0].substring(13), t[1]);
						continue;
					}
					int i = Integer.valueOf(t[1]);
					if (i > 2) {
						i = iff_Unknown;
					}
					lmap.put(t[0], i);
				}
			}
			
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveIFF(String pUsername) {
		// IFF ﾆ稚ﾆ叩ﾆ辰ﾆ停ｹ窶堙鯉ｿｽ窶假ｿｽﾅｾ窶堙・
		File lfile = getFile(MMM_Helper.isClient ? null : pUsername);
		Map<String, Integer> lmap = getUserIFF(pUsername);
		
		try {
			if ((lfile.exists() || lfile.createNewFile()) && lfile.canWrite()) {
				FileWriter fw = new FileWriter(lfile);
				BufferedWriter bw = new BufferedWriter(fw);
				
				// ﾆ暖ﾆ椎ﾆ狸・ｽ[ﾆ但ﾆ辰ﾆ弾ﾆ停ぎ窶堙姑椎ﾆ湛ﾆ暖
				for (Entry<Integer, List<Integer>> le : LMM_TriggerSelect
						.getUserTrigger(pUsername).entrySet()) {
					StringBuilder sb = new StringBuilder();
					sb.append("triggerWeapon")
							.append(LMM_TriggerSelect.selector.get(le.getKey()))
							.append("=");
					if (!le.getValue().isEmpty()) {
						sb.append(le.getValue().get(0));
						for (int i = 1; i < le.getValue().size(); i++) {
							sb.append(",").append(le.getValue().get(i));
						}
					}
					sb.append("\r\n");
					bw.write(sb.toString());
				}
				
				for (Map.Entry<String, Integer> me : lmap.entrySet()) {
					bw.write(String.format("%s=%d\r\n", me.getKey(),
							me.getValue()));
				}
				
				bw.close();
				fw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
