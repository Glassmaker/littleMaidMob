package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

/**
 * ƒ‚�[ƒh�Ø‚è‘Ö‚¦—pƒgƒŠƒK�[ƒAƒCƒeƒ€‚ÌƒRƒ“ƒeƒi�B
 * ƒ}ƒ‹ƒ`‘Î�ô—p�B
 * ƒf�[ƒ^‚Ì“Ç‚Ý�ž‚Ý‚ÍIFF‚Å�s‚Á‚Ä‚¢‚é�B
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
			// ƒVƒ“ƒOƒ‹ŽÀ�sŽž‚Í–¼�Ìƒuƒ‰ƒ“ƒN‚É�B
			pUsername = "";
		}
		// ‘¶�Ýƒ`ƒFƒbƒN�A–³‚©‚Á‚½‚ç’Ç‰Á
		if (!usersTrigger.containsKey(pUsername)) {
			if (pUsername.isEmpty()) {
				// –¼�Ì‚ªƒuƒ‰ƒ“ƒN‚ÌŽž‚ÍƒfƒtƒHƒ‹ƒg‚Ì‚à‚Ì‚ÖƒŠƒ“ƒN�B
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
	 * ƒ†�[ƒU�[–ˆ‚ÉƒgƒŠƒK�[ƒAƒCƒeƒ€‚ð�Ý’è‚·‚é�B
	 */
	public static void appendTriggerItem(String pUsername, String pSelector, String pIndexstr) {
		// ƒgƒŠƒK�[ƒAƒCƒeƒ€‚Ì’Ç‰Á
		appendWeaponsIndex(pIndexstr, getuserTriggerList(pUsername, pSelector));
	}

	/**
	 * ƒgƒŠƒK�[ƒAƒCƒeƒ€‚ð‰ð�Í‚µ‚Ä“o˜^�B
	 */
	private static void appendWeaponsIndex(String indexstr, List<Integer> indexlist) {
		if (indexstr.isEmpty()) return;
		String[] s = indexstr.split(",");
		for (String t : s) {
			indexlist.add(Integer.valueOf(t));
		}
	}

	/**
	 * ƒAƒCƒeƒ€‚ªŽw’è‚³‚ê‚½ƒgƒŠƒK�[‚É“o˜^‚³‚ê‚Ä‚¢‚é‚©‚ð”»’è
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
