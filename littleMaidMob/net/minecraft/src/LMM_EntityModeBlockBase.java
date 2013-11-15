package net.minecraft.src;

import net.minecraft.tileentity.TileEntity;

public abstract class LMM_EntityModeBlockBase extends LMM_EntityModeBase {

//	protected TileEntity fTile;
	protected double fDistance;


	public LMM_EntityModeBlockBase(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
	}

	@Override
	public void updateBlock() {
		// ・ｽ譓・ｽﾆなゑｿｽTile・ｽ・ｽ・ｽZ・ｽb・ｽg
		owner.setTilePos(0);
	}

	/**
	 * ・ｽ・ｽ・ｽﾅに使・ｽp・ｽ・ｽ・ｽ・ｽTile・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ鼾・ｿｽ・ｽshouldBlock・ｽﾖ費ｿｽﾔよう・ｽﾉゑｿｽ・ｽ・ｽB
	 */
	@Override
	public boolean isSearchBlock() {
		boolean lflag = false;
		for (int li = 0; li < owner.maidTiles.length; li++) {
			if (owner.maidTiles[li] != null) {
				TileEntity ltile = owner.getTileEntity(li);
				if (ltile != null && !checkWorldMaid(ltile)) {
					if (!lflag) {
						owner.setTilePos(ltile);
					}
					lflag = true;
				}
			}
		}
		return !lflag;
	}

	@Override
	public boolean overlooksBlock(int pMode) {
		if (owner.isTilePos()) {
			owner.setTilePos(0);
		}
		return true;
	}


	/**
	 * ・ｽ・ｽ・ｽﾌ・ｿｽ・ｽC・ｽh・ｽ・ｽ・ｽg・ｽp・ｽ・ｽ・ｽﾄゑｿｽ・ｽ驍ｩ・ｽ・ｽ・ｽ`・ｽF・ｽb・ｽN・ｽB
	 * @return
	 */
	protected boolean checkWorldMaid(TileEntity pTile) {
		// ・ｽ・ｽ・ｽE・ｽﾌ・ｿｽ・ｽC・ｽh・ｽ・ｽ・ｽ・ｽ
		for (Object lo : owner.worldObj.loadedEntityList) {
			if (lo == owner) continue;
			if (lo instanceof LMM_EntityLittleMaid) {
				LMM_EntityLittleMaid lem = (LMM_EntityLittleMaid)lo;
				if (lem.isUsingTile(pTile)) {
					// ・ｽN・ｽ・ｽ・ｽ・ｽ・ｽg・ｽp・ｽ・ｽ
					return true;
				}
			}
		}
		return false;
	}

}
