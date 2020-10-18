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

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class SqlStreamReader {
	public static @NonNull List<@NonNull String> parseQueries(@NonNull InputStream is) {
		List<String> queries = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("--")) continue;
				sb.append(line);
				if (line.endsWith(";")) {
					sb.deleteCharAt(sb.length() - 1);
					String result = sb.toString().trim();
					if (!result.isEmpty()) {
						queries.add(result);
					}
					sb = new StringBuilder();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return queries;
	}
}
