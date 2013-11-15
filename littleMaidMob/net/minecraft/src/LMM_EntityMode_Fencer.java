package net.minecraft.src;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

/**
 * ・ｽﾆ趣ｿｽ・ｽ譓・ｽﾆゑｿｽ・ｽﾄ・ｿｽ・ｽ[・ｽh・ｽ關費ｿｽ・ｽ0x0080・ｽﾍ包ｿｽ・ｽ・ｽA0x00c0・ｽﾍ鯉ｿｽ・ｽﾜみれモ・ｽ[・ｽh・ｽﾆ具ｿｽﾊ。
 */
public class LMM_EntityMode_Fencer extends LMM_EntityModeBase {

	public static final int mmode_Fencer		= 0x0080;
	public static final int mmode_Bloodsucker	= 0x00c0;


	public LMM_EntityMode_Fencer(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
	}

	@Override
	public int priority() {
		return 3000;
	}

	@Override
	public void init() {
		// ・ｽo・ｽ^・ｽ・ｽ・ｽ[・ｽh・ｽﾌ厄ｿｽ・ｽﾌ追会ｿｽ
		ModLoader.addLocalization("littleMaidMob.mode.Fencer", "Fencer");
		ModLoader.addLocalization("littleMaidMob.mode.Fencer", "ja_JP", "・ｽ・ｽq・ｽ・ｽ・ｽm");
		ModLoader.addLocalization("littleMaidMob.mode.F-Fencer", "F-Fencer");
		ModLoader.addLocalization("littleMaidMob.mode.F-Fencer", "ja_JP", "・ｽ・ｽ・ｽR・ｽ・ｽ・ｽm");
		ModLoader.addLocalization("littleMaidMob.mode.T-Fencer", "T-Fencer");
		ModLoader.addLocalization("littleMaidMob.mode.D-Fencer", "D-Fencer");
		ModLoader.addLocalization("littleMaidMob.mode.Bloodsucker", "Bloodsucker");
		ModLoader.addLocalization("littleMaidMob.mode.Bloodsucker", "ja_JP", "・ｽ・ｽ・ｽﾉ飢・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽy");
		ModLoader.addLocalization("littleMaidMob.mode.F-Bloodsucker", "F-Bloodsucker");
		ModLoader.addLocalization("littleMaidMob.mode.F-Bloodsucker", "ja_JP", "・ｽﾊ厄ｿｽ・ｽ・ｽ・ｽy");
		ModLoader.addLocalization("littleMaidMob.mode.T-Bloodsucker", "T-Bloodsucker");
		ModLoader.addLocalization("littleMaidMob.mode.D-Bloodsucker", "D-Bloodsucker");
		LMM_TriggerSelect.appendTriggerItem(null, "Sword", "");
		LMM_TriggerSelect.appendTriggerItem(null, "Axe", "");
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		// Fencer:0x0080
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = new EntityAITasks(owner.aiProfiler);
		
//		ltasks[1].addTask(1, new EntityAIOwnerHurtByTarget(owner));
//		ltasks[1].addTask(2, new EntityAIOwnerHurtTarget(owner));
		ltasks[1].addTask(3, new LMM_EntityAIHurtByTarget(owner, true));
		ltasks[1].addTask(4, new LMM_EntityAINearestAttackableTarget(owner, EntityLivingBase.class, 0, true));
		
		owner.addMaidMode(ltasks, "Fencer", mmode_Fencer);
		
		
		// Bloodsucker:0x00c0
		EntityAITasks[] ltasks2 = new EntityAITasks[2];
		ltasks2[0] = pDefaultMove;
		ltasks2[1] = new EntityAITasks(owner.aiProfiler);
		
		ltasks2[1].addTask(1, new LMM_EntityAIHurtByTarget(owner, true));
		ltasks2[1].addTask(2, new LMM_EntityAINearestAttackableTarget(owner, EntityLivingBase.class, 0, true));
		
		owner.addMaidMode(ltasks2, "Bloodsucker", mmode_Bloodsucker);
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		ItemStack litemstack = owner.maidInventory.getStackInSlot(0);
		if (litemstack != null) {
			if (litemstack.getItem() instanceof ItemSword || LMM_TriggerSelect.checkWeapon(owner.getMaidMaster(), "Sword", litemstack)) {
				owner.setMaidMode("Fencer");
				return true;
			} else  if (litemstack.getItem() instanceof ItemAxe || LMM_TriggerSelect.checkWeapon(owner.getMaidMaster(), "Axe", litemstack)) {
				owner.setMaidMode("Bloodsucker");
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean setMode(int pMode) {
		switch (pMode) {
		case mmode_Fencer :
//			pentitylittlemaid.maidInventory.currentItem = getNextEquipItem(pentitylittlemaid, pMode);
			owner.setBloodsuck(false);
			owner.aiAttack.isGuard = true;
			return true;
		case mmode_Bloodsucker :
//			pentitylittlemaid.maidInventory.currentItem = getNextEquipItem(pentitylittlemaid, pMode);
			owner.setBloodsuck(true);
			return true;
		}
		
		return false;
	}

	@Override
	public int getNextEquipItem(int pMode) {
		int li;
		int ll = -1;
		double ld = 0;
		double lld;
		ItemStack litemstack;
		
		// ・ｽ・ｽ・ｽ[・ｽh・ｽﾉ会ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽﾊ費ｿｽ・ｽ・ｽA・ｽ・ｽ・ｽx・ｽD・ｽ・ｽ
		switch (pMode) {
		case mmode_Fencer : 
			for (li = 0; li < owner.maidInventory.maxInventorySize; li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack == null) continue;
				
				// ・ｽ・ｽ
				if (litemstack.getItem() instanceof ItemSword || LMM_TriggerSelect.checkWeapon(owner.getMaidMaster(), "Sword", litemstack)) {
					return li;
				}
				
				// ・ｽU・ｽ・ｽ・ｽﾍな搾ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽﾌゑｿｽ・ｽL・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ
				lld = 1;
				try {
					lld = MMM_Helper.getAttackVSEntity(litemstack);
				}
				catch (Exception e) {
				}
				if (lld > ld) {
					ll = li;
					ld = lld;
				}
			}
			break;
		case mmode_Bloodsucker :
			for (li = 0; li < owner.maidInventory.maxInventorySize; li++) {
				litemstack = owner.maidInventory.getStackInSlot(li);
				if (litemstack == null) continue;
				
				// ・ｽ・ｽ
				if (litemstack.getItem() instanceof ItemAxe || LMM_TriggerSelect.checkWeapon(owner.getMaidMaster(), "Axe", litemstack)) {
					return li;
				}
				
				// ・ｽU・ｽ・ｽ・ｽﾍな搾ｿｽ・ｽ・ｽ・ｽ・ｽ・ｽﾌゑｿｽ・ｽL・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ
				lld = 1;
				try {
					lld = MMM_Helper.getAttackVSEntity(litemstack);
				}
				catch (Exception e) {
				}
				if (lld > ld) {
					ll = li;
					ld = lld;
				}
			}
			break;
		}
		
		return ll;
	}

	@Override
	public boolean checkItemStack(ItemStack pItemStack) {
		// ・ｽ・ｽ・ｽ・ｽ・ｽA・ｽC・ｽe・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ・ｽ
		return pItemStack.getItem() instanceof ItemSword || pItemStack.getItem() instanceof ItemAxe;
	}

}
