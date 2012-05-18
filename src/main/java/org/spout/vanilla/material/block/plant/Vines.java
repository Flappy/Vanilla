/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.material.block.plant;

import java.util.ArrayList;
import java.util.Random;

import org.spout.api.entity.Entity;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.util.BlockIterator;

import org.spout.vanilla.controller.living.Living;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;

public class Vines extends VanillaBlockMaterial {
	public Vines(String name, int id) {
		super(name, id);
	}

	@Override
	public void initialize() {
		super.initialize();
		this.setHardness(0.2F).setResistance(0.3F);
	}

	private int getMask(BlockFace face) {
		switch (face) {
			case WEST:
				return 0x1;
			case NORTH:
				return 0x2;
			case EAST:
				return 0x4;
			case SOUTH:
				return 0x8;
			default:
				return 0;
		}
	}

	public boolean isAttachedTo(Block block, BlockFace face) {
		int mask = getMask(face);
		return (block.getData() & mask) == mask;
	}

	/**
	 * Sets whether a certain face is attached or not
	 * @param block	of this material
	 * @param face	 to attach to
	 * @param attached whether or not to attach
	 */
	public void setFaceAttached(Block block, BlockFace face, boolean attached) {
		short data = block.getData();
		if (attached) {
			data |= getMask(face);
		} else {
			data &= ~getMask(face);
		}
		block.setData(data);
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public void onUpdate(Block block) {
		//check all directions if it still supports it
		boolean changed = false;
		Block above = block.translate(BlockFace.TOP);
		if (block.getData() != 0) {
			BlockMaterial abovemat = above.getMaterial();
			for (BlockFace face : BlockFaces.NESW) {
				if (this.isAttachedTo(block, face)) {
					if (!this.canAttachTo(block.translate(face), face.getOpposite())) {
						//is there a vine block above to which it can support itself?
						if (!abovemat.equals(VanillaMaterials.VINES) || !this.isAttachedTo(above, face)) {
							this.setFaceAttached(block, face, false);
							changed = true;
						}
					}
				}
			}
		}
		if (block.getData() == 0) {
			//check if there is a block above it can attach to, else destroy
			if (!this.canAttachTo(above, BlockFace.BOTTOM)) {
				this.onDestroy(block);
				return;
			}
		}
		if (changed) {
			block.update();
		}
	}

	public boolean canAttachTo(BlockMaterial material, BlockFace face) {
		if (material instanceof VanillaBlockMaterial) {
			return ((VanillaBlockMaterial) material).canSupport(this, face);
		} else {
			return false;
		}
	}

	public boolean canAttachTo(Block block, BlockFace face) {
		return this.canAttachTo(block.getSubMaterial(), face);
	}

	public BlockFace getTracedFace(Block block) {
		if (block.getMaterial().equals(VanillaMaterials.VINES) && block.getSource() instanceof Entity) {
			//get block by block tracing from the player view
			Entity entity = (Entity) block.getSource();
			if (entity.getController() instanceof Living) {
				BlockIterator iter = ((Living) entity.getController()).getHeadBlockView(7);
				Block next;
				while (iter.hasNext()) {
					next = iter.next();
					if (next.equals(block)) {
						Block target = iter.hasNext() ? iter.next() : null;
						if (target != null) {
							//get what face this target is relative to the main block
							for (BlockFace face : BlockFaces.NESWBT) {
								if (block.translate(face).equals(target)) {
									return face;
								}
							}
						}
						return null;
					}
				}
			}
		}
		return null;
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		return false;
	}

	@Override
	public boolean canPlace(Block block, short data, BlockFace face, boolean isClicked) {
		if (block.getMaterial().equals(VanillaMaterials.VINES)) {
			return true;
		} else if (face == BlockFace.BOTTOM) {
			return false; //TODO: possibly place on top of vines?
		} else if (face == BlockFace.TOP) {
			//place below block
			if (isClicked || !this.canAttachTo(block.translate(BlockFace.TOP), BlockFace.BOTTOM)) {
				return false;
			} else {
				return true;
			}
		} else if (this.canAttachTo(block.translate(face), face.getOpposite()) && !this.isAttachedTo(block, face)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean onPlacement(Block block, short dat, BlockFace face, boolean isClicked) {
		if (block.getMaterial().equals(VanillaMaterials.VINES)) {
			if (isClicked) {
				face = getTracedFace(block);
				if (face == null) {
					return false;
				}
			}
			if (this.canAttachTo(block.translate(face), face.getOpposite()) && !this.isAttachedTo(block, face)) {
				this.setFaceAttached(block, face, true);
				block.update();
				return true;
			} else {
				return false;
			}
		} else if (face == BlockFace.BOTTOM) {
			return false; //TODO: possibly place on top of vines?
		} else if (face == BlockFace.TOP) {
			//place below block
			if (isClicked || !this.canAttachTo(block.translate(BlockFace.TOP), BlockFace.BOTTOM)) {
				return false;
			} else {
				block.setMaterial(VanillaMaterials.VINES).update();
				return true;
			}
		} else if (this.canAttachTo(block.translate(face), face.getOpposite()) && !this.isAttachedTo(block, face)) {
			block.setMaterial(VanillaMaterials.VINES);
			this.setFaceAttached(block, face, true);
			block.update();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isPlacementObstacle() {
		return false;
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		if (block.getSource() instanceof Entity) {
			if (((Entity) block.getSource()).getInventory().getCurrentItem().getMaterial().equals(VanillaMaterials.SHEARS)) {
				drops.add(new ItemStack(VanillaMaterials.VINES, block.getData(), 1));
			}
		}
		return drops;
	}
}
