package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

/**
 * ﾆ停夲ｿｽ[ﾆ檀・ｽﾃ倪堙ｨ窶佚問堋ｦ窶廃ﾆ暖ﾆ椎ﾆ狸・ｽ[ﾆ但ﾆ辰ﾆ弾ﾆ停ぎ窶堙姑坦ﾆ停愴弾ﾆ段・ｽB
 * ﾆ筑ﾆ停ｹﾆ蛋窶佚趣ｿｽﾃｴ窶廃・ｽB
 * ﾆ断・ｽ[ﾆ耽窶堙娯愿・堙晢ｿｽﾅｾ窶堙昶堙巧FF窶堙・ｿｽs窶堙≫堙・堋｢窶堙ｩ・ｽB
 */
public class LMM_TriggerSelect {

	public static List<String> selector = new ArrayList<String>();
	public static Map<String, Map<Integer, List<Integer>>> usersTrigger = new HashMap<String, Map<Integer,List<Integer>>>();
	public static Map<Integer, List<Integer>> defaultTrigger = new HashMap<Integer,List<Integer>>();


	public static Map<Integer, List<Integer>> getUserTrigger(String pUsername) {
		if (pUsername == null) {
			return defaultTrigger;
		}
		if (MMM_Helper.isLocalPlay()) {
			// ﾆ歎ﾆ停愴丹ﾆ停ｹﾅｽﾃ・ｽsﾅｽﾅｾ窶堙坂督ｼ・ｽﾃ姑置ﾆ停ｰﾆ停愴誰窶堙会ｿｽB
			pUsername = "";
		}
		// 窶伉ｶ・ｽﾃ敞蛋ﾆ巽ﾆ鍛ﾆ誰・ｽA窶督ｳ窶堋ｩ窶堙≫堋ｽ窶堙ｧ窶凖・ｰﾃ・
		if (!usersTrigger.containsKey(pUsername)) {
			if (pUsername.isEmpty()) {
				// 窶督ｼ・ｽﾃ娯堋ｪﾆ置ﾆ停ｰﾆ停愴誰窶堙固ｽﾅｾ窶堙哉断ﾆ稚ﾆ辿ﾆ停ｹﾆ暖窶堙娯堙窶堙娯堙免椎ﾆ停愴誰・ｽB
				usersTrigger.put(pUsername, defaultTrigger);
			} else {
				Map<Integer, List<Integer>> lmap = new HashMap<Integer, List<Integer>>();
				lmap.putAll(defaultTrigger);
				usersTrigger.put(pUsername, lmap);
			}
		}
		
		return usersTrigger.get(pUsername);
	}

	public static List<Integer> getuserTriggerList(String pUsername, String pSelector) {
		if (!selector.contains(pSelector)) {
			selector.add(pSelector);
		}
		int lindex = selector.indexOf(pSelector);
		Map<Integer, List<Integer>> lmap = getUserTrigger(pUsername);
		List<Integer> llist;
		if (lmap.containsKey(lindex)) {
			llist = lmap.get(lindex);
		} else {
			llist = new ArrayList<Integer>();
			lmap.put(lindex, llist);
		}
		return llist;
	}


	/**
	 * ﾆ停・ｽ[ﾆ旦・ｽ[窶突・堙家暖ﾆ椎ﾆ狸・ｽ[ﾆ但ﾆ辰ﾆ弾ﾆ停ぎ窶堙ｰ・ｽﾃ昶凖ｨ窶堋ｷ窶堙ｩ・ｽB
	 */
	public static void appendTriggerItem(String pUsername, String pSelector, String pIndexstr) {
		// ﾆ暖ﾆ椎ﾆ狸・ｽ[ﾆ但ﾆ辰ﾆ弾ﾆ停ぎ窶堙娯凖・ｰﾃ・
		appendWeaponsIndex(pIndexstr, getuserTriggerList(pUsername, pSelector));
	}

	/**
	 * ﾆ暖ﾆ椎ﾆ狸・ｽ[ﾆ但ﾆ辰ﾆ弾ﾆ停ぎ窶堙ｰ窶ｰﾃｰ・ｽﾃ坂堋ｵ窶堙・徙ﾋ弯・ｽB
	 */
	private static void appendWeaponsIndex(String indexstr, List<Integer> indexlist) {
		if (indexstr.isEmpty()) return;
		String[] s = indexstr.split(",");
		for (String t : s) {
			indexlist.add(Integer.valueOf(t));
		}
	}

	/**
	 * ﾆ但ﾆ辰ﾆ弾ﾆ停ぎ窶堋ｪﾅｽw窶凖ｨ窶堋ｳ窶堙ｪ窶堋ｽﾆ暖ﾆ椎ﾆ狸・ｽ[窶堙俄徙ﾋ弯窶堋ｳ窶堙ｪ窶堙・堋｢窶堙ｩ窶堋ｩ窶堙ｰ窶敖ｻ窶凖ｨ
	 */
	public static boolean checkWeapon(String pUsername, String pSelector, ItemStack pItemStack) {
		if (!selector.contains(pSelector)) {
			return false;
		}
		if (MMM_Helper.isLocalPlay()) {
			return getuserTriggerList(null, pSelector).contains(pItemStack.itemID);
		}
		if (!usersTrigger.containsKey(pUsername)) {
			return false;
		}
		return getuserTriggerList(pUsername, pSelector).contains(pItemStack.itemID);
	}

}
