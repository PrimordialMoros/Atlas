/*
 *   Copyright 2020 Moros <https://github.com/PrimordialMoros>
 *
 *    This file is part of Bending.
 *
 *   Bending is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Bending is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with Bending.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.moros.atlas.logging;

import org.slf4j.Logger;

public class Sl4jPluginLogger implements PluginLogger {
	private final Logger logger;

	public Sl4jPluginLogger(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void info(String s) {
		logger.info(s);
	}

	@Override
	public void warn(String s) {
		logger.warn(s);
	}

	@Override
	public void severe(String s) {
		logger.error(s);
	}
}
