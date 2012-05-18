package org.spout.vanilla.material;

import java.util.ArrayList;

import org.spout.api.entity.Entity;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.item.MiningTool;

public class Sponge extends Solid implements Mineable {
	public Sponge(String name, int id) {
		super(name, id);
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		if (block.getSource() instanceof Entity) {
			drops.add(new ItemStack(block.getMaterial(), block.getData(), 1));
		}
		return drops;
	}

	@Override
	public short getDurabilityPenalty(MiningTool tool) {
		return 0;
	}
}
