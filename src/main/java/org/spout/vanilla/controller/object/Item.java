/*
 * This file is part of vanilla (http://www.spout.org/).
 *
 * vanilla is licensed under the SpoutDev License Version 1.
 *
 * vanilla is free software: you can redistribute it and/or modify
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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.controller.object;

import java.util.Set;

import org.spout.api.geo.World;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.controller.ControllerType;
import org.spout.vanilla.protocol.msg.CollectItemMessage;

/**
 * Controller that serves as the base for all items that are not in an inventory (dispersed in the world).
 */
public class Item extends Substance {
	private ItemStack is;
	private int roll, unpickable;
	private Vector3 initial;


	public Item(ItemStack is, Vector3 initial) {
		this.is = is;
		this.roll = 1;
		unpickable = 10;
		this.initial = initial;
		setMoveable(false); //TODO Items can move so need to fix the NPE that occurs when the item dropped.
	}

	@Override
	public void onAttached() {
		getParent().setData(ControllerType.KEY, ControllerType.DROPPEDITEM.id);
	}

	@Override
	public void onTick(float dt) {
		if (dt <= 1) {
			setVelocity(getVelocity().add(initial));
		}

		if (unpickable > 0) {
			unpickable--;
			super.onTick(dt);
			return;
		}

		float x = (getRandom().nextBoolean() ? 1 : -1) * getRandom().nextFloat();
		float y = getRandom().nextFloat();
		float z = (getRandom().nextBoolean() ? 1 : -1) * getRandom().nextFloat();
		setVelocity(getVelocity().add(x, y, z));
		super.onTick(dt);
		World world = getParent().getWorld();
		//TODO replace with getClosestPlayer when my Spout PR gets pulled!
		Set<Player> players = world.getPlayers();
		double minDistance = -1;
		Player closestPlayer = null;
		for (Player plr : players) {
			double distance = plr.getEntity().getPosition().getSquaredDistance(getParent().getPosition());
			if (distance < minDistance || minDistance == -1) {
				closestPlayer = plr;
				minDistance = distance;
			}
		}

		if (closestPlayer == null) {
			return;
		}
		double maxDistance = VanillaConfiguration.ITEM_PICKUP_RANGE.getDouble();
		maxDistance = maxDistance * maxDistance;
		if (minDistance > maxDistance) {
			return;
		}

		int collected = getParent().getId();
		int collector = closestPlayer.getEntity().getId();
		CollectItemMessage message = new CollectItemMessage(collected, collector);
		for (Player plr : players) {
			plr.getSession().send(message);
		}

		closestPlayer.getEntity().getInventory().addItem(is);
		getParent().kill();
	}

	public Material getMaterial() {
		return is.getMaterial();
	}

	public int getAmount() {
		return is.getAmount();
	}

	public short getData() {
		return is.getData();
	}

	public int getRoll() {
		return roll;
	}
}
