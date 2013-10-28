package net.minecraft.src;

import static net.minecraft.src.LMM_Statics.LMN_Client_PlaySound;
import static net.minecraft.src.LMM_Statics.LMN_Client_SetIFFValue;
import static net.minecraft.src.LMM_Statics.LMN_Client_SwingArm;

import java.util.Map;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.client.particle.EntityCrit2FX;
import net.minecraft.client.particle.EntityPickupFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;

/**
 * ƒNƒ‰ƒCƒAƒ“ƒg�ê—p�ˆ—��B
 * ƒ}ƒ‹ƒ`—p‚É•ª—£�B
 * •ª—£‚µ‚Æ‚©‚È‚¢‚ÆNoSuchMethod‚Å—Ž‚¿‚é�B
 */
public class LMM_Client {

	public static void init() {
	}

	public static void addRenderer(Map map) {
		map.put(net.minecraft.src.LMM_EntityLittleMaid.class, new LMM_RenderLittleMaid(0.3F));
	}

	public static GuiContainer getContainerGUI(EntityClientPlayerMP var1, int var2,
			int var3, int var4, int var5) {
		Entity lentity = var1.worldObj.getEntityByID(var3);
		if (lentity instanceof LMM_EntityLittleMaid) {
			LMM_GuiInventory lgui = new LMM_GuiInventory(var1, (LMM_EntityLittleMaid)lentity);
//			var1.openContainer = lgui.inventorySlots;
			return lgui;
		} else {
			return null;
		}
	}

	public static void OpenIFF(LMM_EntityLittleMaid pLittleMaid, EntityPlayer pPlayer) {
		ModLoader.openGUI(pPlayer, new LMM_GuiIFF(pLittleMaid.worldObj, pLittleMaid));
		
	}

// Avatarr
	
	public static void onItemPickup(LMM_EntityLittleMaidAvatar pAvatar, Entity entity, int i) {
		// ƒAƒCƒeƒ€‰ñŽû‚ÌƒGƒtƒFƒNƒg
		// TODO:‚±‚Á‚¿‚ðŽg‚¤‚©�H
//        mc.effectRenderer.addEffect(new EntityPickupFX(mc.theWorld, entity, avatar, -0.5F));
		MMM_Helper.mc.effectRenderer.addEffect(new EntityPickupFX(MMM_Helper.mc.theWorld, entity, pAvatar.avatar, 0.1F));
	}

	public static void onCriticalHit(LMM_EntityLittleMaidAvatar pAvatar, Entity par1Entity) {
		MMM_Helper.mc.effectRenderer.addEffect(new EntityCrit2FX(MMM_Helper.mc.theWorld, par1Entity));
	}

	public static void onEnchantmentCritical(LMM_EntityLittleMaidAvatar pAvatar, Entity par1Entity) {
		EntityCrit2FX entitycrit2fx = new EntityCrit2FX(MMM_Helper.mc.theWorld, par1Entity, "magicCrit");
		MMM_Helper.mc.effectRenderer.addEffect(entitycrit2fx);
	}

	
// Network

	public static void clientCustomPayload(NetClientHandler var1, Packet250CustomPayload var2) {
		// ƒNƒ‰ƒCƒAƒ“ƒg‘¤‚Ì“ÁŽêƒpƒPƒbƒgŽó�M“®�ì
		byte lmode = var2.data[0];
		int leid = 0;
		LMM_EntityLittleMaid lemaid = null;
		if ((lmode & 0x80) != 0) {
			leid = MMM_Helper.getInt(var2.data, 1);
			lemaid =LMM_Net.getLittleMaid(var2.data, 1, MMM_Helper.mc.theWorld);
			if (lemaid == null) return;
		}
		mod_LMM_littleMaidMob.Debug(String.format("LMM|Upd Clt Call[%2x:%d].", lmode, leid));
		
		switch (lmode) {
		case LMN_Client_SwingArm : 
			// ˜r�U‚è
			byte larm = var2.data[5];
			LMM_EnumSound lsound = LMM_EnumSound.getEnumSound(MMM_Helper.getInt(var2.data, 6));
			lemaid.setSwinging(larm, lsound);
//			mod_LMM_littleMaidMob.Debug(String.format("SwingSound:%s", lsound.name()));
			break;
			
		case LMN_Client_SetIFFValue:
			// IFF‚Ì�Ý’è’l‚ðŽó�M
			int lval = var2.data[1];
			int lindex = MMM_Helper.getInt(var2.data, 2);
			String lname = (String)LMM_IFF.DefaultIFF.keySet().toArray()[lindex];
			mod_LMM_littleMaidMob.Debug("setIFF-CL %s(%d)=%d", lname, lindex, lval);
			LMM_IFF.setIFFValue(null, lname, lval);
			break;
			
		case LMN_Client_PlaySound : 
			// ‰¹�º�Ä�¶
			LMM_EnumSound lsound9 = LMM_EnumSound.getEnumSound(MMM_Helper.getInt(var2.data, 5));
			lemaid.playLittleMaidSound(lsound9, true);
			mod_LMM_littleMaidMob.Debug(String.format("playSound:%s", lsound9.name()));
			break;
			
		}
	}

	public static void setAchievement() {
		MMM_Helper.mc.statFileWriter.readStat(mod_LMM_littleMaidMob.ac_Contract, 1);
	}

}
