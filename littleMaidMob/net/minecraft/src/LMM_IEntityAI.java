package net.minecraft.src;

public interface LMM_IEntityAI {
	
	//実行可能判定, Executable judgment
	public void setEnable(boolean pFlag);
	public boolean getEnable();
	/**
	 * モードチェンジ実行時に設定される動作状態。
	 * Operating condition being set in the mode change at runtime.
	 */
//	public void setDefaultEnable();

}
