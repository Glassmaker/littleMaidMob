package net.minecraft.src;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

/**
 * ・ｽI・ｽ・ｽ・ｽ・ｽ・ｽﾉサ・ｽ[・ｽo・ｽ[・ｽﾖ撰ｿｽ・ｽ・ｽ・ｽﾌ使・ｽp・ｽ・ｽﾊ知・ｽ・ｽ・ｽ驍ｽ・ｽﾟの擾ｿｽ・ｽ・ｽ・ｽB
 */
public class LMM_GuiTextureSelect extends MMM_GuiTextureSelect {

	public LMM_GuiTextureSelect(GuiScreen pOwner, MMM_ITextureEntity pTarget,
			int pColor, boolean pToServer) {
		super(pOwner, pTarget, pColor, pToServer);
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		super.actionPerformed(par1GuiButton);
		switch (par1GuiButton.id) {
		case 200:
			if (toServer) {
				if (selectColor != selectPanel.color) {
					// ・ｽF・ｽ・ｽ・ｽﾌ設抵ｿｽ
//					theMaid.maidColor = selectPanel.color | 0x010000 | (selectColor << 8);
					// ・ｽT・ｽ[・ｽo・ｽ[・ｽﾖ撰ｿｽ・ｽ・ｽ・ｽﾌ使・ｽp・ｽ・ｽﾊ知
					byte ldata[] = new byte[2];
					ldata[0] = LMM_Statics.LMN_Server_DecDyePowder;
					ldata[1] = (byte)selectColor;
					LMM_Net.sendToServer(ldata);
				}
			}
			break;
		}
	}

}
