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
package org.spout.vanilla.inventory;

import org.spout.api.inventory.ItemStack;

/**
 * Represents a enchantment table inventory belonging to an enchantment table
 * controller.
 */
public class EnchantmentTableInventory extends WindowInventory {
	private static final long serialVersionUID = 1L;
	private static final int[] SLOTS = {28, 29, 30, 31, 32, 33, 34, 35, 36, 19, 20, 21, 22, 23, 24, 25, 26, 27, 10, 11, 12, 13, 14, 15, 16, 17, 18, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

	public EnchantmentTableInventory() {
		super(Window.ENCHANTMENT_TABLE, 1, "Enchantment table");
	}

	@Override
	public int getNativeSlotIndex(int index) {
		return SLOTS[index];
	}

	@Override
	public int getSlotIndex(int nativeIndex) {
		for (int i = 0; i < SLOTS.length; i++) {
			if (SLOTS[i] == nativeIndex) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Whether the inventory contains an item to enchant
	 * @return true if an item is present
	 */
	public boolean hasItem() {
		return this.getItem(0) != null;
	}

	/**
	 * Returns the {@link ItemStack} in the enchantment slot (slot 36); can
	 * return null.
	 * @return ingredient item stack
	 */
	public ItemStack getItem() {
		return getItem(0);
	}
}
