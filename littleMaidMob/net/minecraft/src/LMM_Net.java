package net.minecraft.src;

import static net.minecraft.src.LMM_Statics.LMN_Client_SetIFFValue;
import static net.minecraft.src.LMM_Statics.LMN_Server_DecDyePowder;
import static net.minecraft.src.LMM_Statics.LMN_Server_GetIFFValue;
import static net.minecraft.src.LMM_Statics.LMN_Server_SaveIFF;
import static net.minecraft.src.LMM_Statics.LMN_Server_SetIFFValue;
import static net.minecraft.src.LMM_Statics.LMN_Server_UpdateSlots;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class LMM_Net {
	


	
	
	
	
	/**
	 * “n‚³‚ê‚½ƒf�[ƒ^‚Ì�æ“ª‚ÉŽ©•ª‚ÌEntityID‚ð•t—^‚µ‚Ä‘S‚Ä‚ÌƒNƒ‰ƒCƒAƒ“ƒg‚Ö‘—�M
	 */
	public static void sendToAllEClient(LMM_EntityLittleMaid pEntity, byte[] pData) {
		MMM_Helper.setInt(pData, 1, pEntity.entityId);
		((WorldServer)pEntity.worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(pEntity, new Packet250CustomPayload("LMM|Upd", pData));
	}

	/**
	 * “n‚³‚ê‚½ƒf�[ƒ^‚Ì�æ“ª‚ÉŽ©•ª‚ÌEntityID‚ð•t—^‚µ‚Ä“Á’è‚Ì‚ÌƒNƒ‰ƒCƒAƒ“ƒg‚Ö‘—�M
	 */
	public static void sendToEClient(NetServerHandler pHandler, LMM_EntityLittleMaid pEntity, byte[] pData) {
		MMM_Helper.setInt(pData, 1, pEntity.entityId);
		ModLoader.serverSendPacket(pHandler, new Packet250CustomPayload("LMM|Upd", pData));
	}

	public static void sendToClient(NetServerHandler pHandler, byte[] pData) {
		ModLoader.serverSendPacket(pHandler, new Packet250CustomPayload("LMM|Upd", pData));
	}

	/**
	 * “n‚³‚ê‚½ƒf�[ƒ^‚Ì�æ“ª‚ÉEntityID‚ð•t—^‚µ‚ÄƒT�[ƒo�[‚Ö‘—�M�B
	 * 0:Mode, 1-4:EntityID, 5-:Data
	 */
	public static void sendToEServer(LMM_EntityLittleMaid pEntity, byte[] pData) {
		MMM_Helper.setInt(pData, 1, pEntity.entityId);
		ModLoader.clientSendPacket(new Packet250CustomPayload("LMM|Upd", pData));
		mod_LMM_littleMaidMob.Debug(String.format("LMM|Upd:send:%2x:%d", pData[0], pEntity.entityId));
	}

	public static void sendToServer(byte[] pData) {
		ModLoader.clientSendPacket(new Packet250CustomPayload("LMM|Upd", pData));
		mod_LMM_littleMaidMob.Debug(String.format("LMM|Upd:%2x:NOEntity", pData[0]));
	}

	/**
	 * ƒT�[ƒo�[‚ÖIFF‚ÌƒZ�[ƒu‚ðƒŠƒNƒGƒXƒg
	 */
	public static void saveIFF() {
		sendToServer(new byte[] {LMN_Server_SaveIFF});
	}

	/**
	 * littleMaid‚ÌEntity‚ð•Ô‚·�B
	 */
	public static LMM_EntityLittleMaid getLittleMaid(byte[] pData, int pIndex, World pWorld) {
		Entity lentity = MMM_Helper.getEntity(pData, pIndex, pWorld);
		if (lentity instanceof LMM_EntityLittleMaid) {
			return (LMM_EntityLittleMaid)lentity;
		} else {
			return null;
		}
	}

	// Žó�MƒpƒPƒbƒg‚Ì�ˆ—�
	
	public static void serverCustomPayload(NetServerHandler pNetHandler, Packet250CustomPayload pPayload) {
		// ƒT�[ƒo‘¤‚Ì“®�ì
		byte lmode = pPayload.data[0];
		int leid = 0;
		LMM_EntityLittleMaid lemaid = null;
		if ((lmode & 0x80) != 0) {
			leid = MMM_Helper.getInt(pPayload.data, 1);
			lemaid = getLittleMaid(pPayload.data, 1, pNetHandler.playerEntity.worldObj);
			if (lemaid == null) return;
		}
		mod_LMM_littleMaidMob.Debug(String.format("LMM|Upd Srv Call[%2x:%d].", lmode, leid));
		byte[] ldata;
		int lindex;
		int lval;
		String lname;
		
		switch (lmode) {
		case LMN_Server_UpdateSlots : 
			// �‰‰ñ�X�V‚Æ‚©
			// ƒCƒ“ƒxƒ“ƒgƒŠ‚Ì�X�V
			lemaid.maidInventory.clearChanged();
			for (LMM_SwingStatus lswing : lemaid.mstatSwingStatus) {
				lswing.lastIndex = -1;
			}
			break;
			
		case LMN_Server_DecDyePowder:
			// ƒJƒ‰�[”Ô�†‚ðƒNƒ‰ƒCƒAƒ“ƒg‚©‚çŽó‚¯Žæ‚é
			// ƒCƒ“ƒxƒ“ƒgƒŠ‚©‚ç�õ—¿‚ðŒ¸‚ç‚·�B
			int lcolor2 = pPayload.data[1];
			if (!pNetHandler.playerEntity.capabilities.isCreativeMode) {
				for (int li = 0; li < pNetHandler.playerEntity.inventory.mainInventory.length; li++) {
					ItemStack lis = pNetHandler.playerEntity.inventory.mainInventory[li];
					if (lis != null && lis.itemID == Item.dyePowder.itemID) {
						if (lis.getItemDamage() == (15 - lcolor2)) {
							MMM_Helper.decPlayerInventory(pNetHandler.playerEntity, li, 1);
						}
					}
				}
			}
			break;
			
		case LMN_Server_SetIFFValue:
			// IFF‚Ì�Ý’è’l‚ðŽó�M
			lval = pPayload.data[1];
			lindex = MMM_Helper.getInt(pPayload.data, 2);
			lname = MMM_Helper.getStr(pPayload.data, 6);
			mod_LMM_littleMaidMob.Debug("setIFF-SV user:%s %s(%d)=%d", pNetHandler.playerEntity.username, lname, lindex, lval);
			LMM_IFF.setIFFValue(pNetHandler.playerEntity.username, lname, lval);
			sendIFFValue(pNetHandler, lval, lindex);
			break;
		case LMN_Server_GetIFFValue:
			// IFFGUI open
			lindex = MMM_Helper.getInt(pPayload.data, 1);
			lname = MMM_Helper.getStr(pPayload.data, 5);
			lval = LMM_IFF.getIFF(pNetHandler.playerEntity.username, lname);
			mod_LMM_littleMaidMob.Debug("getIFF-SV user:%s %s(%d)=%d", pNetHandler.playerEntity.username, lname, lindex, lval);
			sendIFFValue(pNetHandler, lval, lindex);
			break;
		case LMN_Server_SaveIFF:
			// IFFƒtƒ@ƒCƒ‹‚Ì•Û‘¶
			LMM_IFF.saveIFF(pNetHandler.playerEntity.username);
			if (!MMM_Helper.isClient) {
				LMM_IFF.saveIFF("");
			}
			break;
			
		}
	}

	/**
	 * ƒNƒ‰ƒCƒAƒ“ƒg‚ÖIFF‚Ì�Ý’è’l‚ð’Ê’m‚·‚é�B
	 * @param pNetHandler
	 * @param pValue
	 * @param pIndex
	 */
	protected static void sendIFFValue(NetServerHandler pNetHandler, int pValue, int pIndex) {
		byte ldata[] = new byte[] {
				LMN_Client_SetIFFValue,
				0,
				0, 0, 0, 0
		};
		ldata[1] = (byte)pValue;
		MMM_Helper.setInt(ldata, 2, pIndex);
		sendToClient(pNetHandler, ldata);
	}

}
