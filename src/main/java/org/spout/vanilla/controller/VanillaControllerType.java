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
package org.spout.vanilla.controller;

import org.spout.api.entity.Controller;
import org.spout.api.entity.type.EmptyConstructorControllerType;
import org.spout.api.protocol.EntityProtocol;

import org.spout.vanilla.VanillaPlugin;

public class VanillaControllerType extends EmptyConstructorControllerType {
	private final int id;

	public VanillaControllerType(final int id, Class<? extends Controller> controllerClass, String name) {
		this(id, controllerClass, name, null);
	}

	public VanillaControllerType(final int id, Class<? extends Controller> controllerClass, String name, EntityProtocol protocol) {
		super(controllerClass, name);
		this.id = id;
		if (protocol != null) {
			this.setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, protocol);
		}
	}

	public int getID() {
		return this.id;
	}

	@Override
	public String toString() {
		return getClass().getName() + " {id = " + this.getID() + " , name = " + this.getName() + "}";
	}
}
