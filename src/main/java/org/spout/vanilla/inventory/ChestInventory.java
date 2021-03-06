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

import org.spout.vanilla.controller.block.ChestController;
import org.spout.vanilla.controller.living.player.VanillaPlayer;

public class ChestInventory extends WindowInventory {
	private static final long serialVersionUID = 1L;
	private final ChestController owner;
	public static final int LARGE_SIZE = 54, SMALL_SIZE = 27;
	public final static int[] SMALL_CHEST_SLOTS = {54, 55, 56, 57, 58, 59, 60, 61, 62, 45, 46, 47, 48, 49, 50, 51, 52, 53, 36, 37, 38, 39, 40, 41, 42, 43, 44, 27, 28, 29, 30, 31, 32, 33, 34, 35, 18, 19, 20, 21, 22, 23, 24, 25, 26, 9, 10, 11, 12, 13, 14, 15, 16, 17, 0, 1, 2, 3, 4, 5, 6, 7, 8};
	public final static int[] LARGE_CHEST_SLOTS = {81, 82, 83, 84, 85, 86, 87, 88, 89, 72, 73, 74, 75, 76, 77, 78, 79, 80, 63, 64, 65, 66, 67, 68, 69, 70, 71, 54, 55, 56, 57, 58, 59, 60, 61, 62, 45, 46, 47, 48, 49, 50, 51, 52, 53, 36, 37, 38, 39, 40, 41, 42, 43, 44, 27, 28, 29, 30, 31, 32, 33, 34, 35, 18, 19, 20, 21, 22, 23, 24, 25, 26, 9, 10, 11, 12, 13, 14, 15, 16, 17, 0, 1, 2, 3, 4, 5, 6, 7, 8};

	public ChestInventory(ChestController owner, int size, String title) {
		super(Window.CHEST, size, title);
		this.owner = owner;
	}

	public ChestController getOwner() {
		return owner;
	}

	@Override
	public void open(VanillaPlayer player) {
		super.open(player);
		owner.setOpened(true);
	}

	@Override
	public void onClosed(VanillaPlayer player) {
		super.onClosed(player);
		owner.setOpened(false);
	}

	@Override
	public int getNativeSlotIndex(int index) {
		return getSize() == SMALL_CHEST_SLOTS.length ? SMALL_CHEST_SLOTS[index] : LARGE_CHEST_SLOTS[index];
	}

	@Override
	public int getSlotIndex(int nativeIndex) {
		int[] slots = getSize() == SMALL_CHEST_SLOTS.length ? SMALL_CHEST_SLOTS : LARGE_CHEST_SLOTS;
		for (int i = 0; i < slots.length; i++) {
			if (slots[i] == nativeIndex) {
				return i;
			}
		}
		return -1;
	}
}
