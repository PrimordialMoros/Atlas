/*
 *   Copyright 2020 Moros <https://github.com/PrimordialMoros>
 *
 *    This file is part of Atlas.
 *
 *   Atlas is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Atlas is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with Atlas.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.moros.atlas.storage;

import me.moros.atlas.logging.PluginLogger;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Storage {
	@NonNull StorageType getType();

	boolean init(@NonNull PluginLogger logger);

	void close();
}
