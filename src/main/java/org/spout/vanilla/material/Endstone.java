package org.spout.vanilla.material;

import java.util.ArrayList;

import org.spout.api.entity.Entity;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.item.MiningTool;
import org.spout.vanilla.material.item.tool.Pickaxe;

public class Endstone extends Solid implements Mineable {
	public Endstone(String name, int id) {
		super(name, id);
	}

	@Override
	public short getDurabilityPenalty(MiningTool tool) {
		//TODO Is this right bergerkiller?
		if (tool instanceof Pickaxe) {
			return 1;
		} else {
			return 2;
		}
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		if (block.getSource() instanceof Entity) {
			if (((Entity) block.getSource()).getInventory().getCurrentItem().getMaterial() instanceof Pickaxe) {
				drops.add(new ItemStack(block.getMaterial(), block.getData(), 1));
			}
		}
		return drops;
	}
}
