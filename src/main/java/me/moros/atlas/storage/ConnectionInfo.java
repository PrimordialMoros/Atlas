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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.moros.atlas.Atlas;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.logging.Logger;

public class ConnectionInfo<T extends Storage> {
	private static final Set<String> poolNames = new HashSet<>();

	private final BiFunction<StorageType, HikariConfig, T> constructor;
	private final StorageType engine;
	private Logger logger;
	private Path path = null;
	private String host = "localhost";
	private String database = "";
	private String username = "";
	private String password = "";
	private int port;

	private ConnectionInfo(BiFunction<StorageType, HikariConfig, T> constructor, StorageType engine) {
		this.constructor = constructor;
		this.engine = engine;
		this.logger = Atlas.getLog();
	}

	public @NonNull StorageType getEngine() {
		return engine;
	}

	public @NonNull Logger getLogger() {
		return logger;
	}

	public ConnectionInfo<T> setLogger(@NonNull Logger logger) {
		this.logger = logger;
		return this;
	}

	public @Nullable Path getPath() {
		return path;
	}

	public @NonNull ConnectionInfo<T> setPath(@NonNull Path path) {
		this.path = path;
		return this;
	}

	public @NonNull String getHost() {
		return host;
	}

	public @NonNull ConnectionInfo<T> setHost(@NonNull String host) {
		this.host = host;
		return this;
	}

	public @NonNull String getDatabase() {
		return database;
	}

	public @NonNull ConnectionInfo<T> setDatabase(@NonNull String database) {
		this.database = database;
		return this;
	}

	public @NonNull String getUsername() {
		return username;
	}

	public @NonNull ConnectionInfo<T> setUsername(@NonNull String username) {
		this.username = username;
		return this;
	}

	public @NonNull String getPassword() {
		return password;
	}

	public @NonNull ConnectionInfo<T> setPassword(@NonNull String password) {
		this.password = password;
		return this;
	}

	public int getPort() {
		return port;
	}

	public @NonNull ConnectionInfo<T> setPort(int port) {
		this.port = port;
		return this;
	}

	public @Nullable Storage build(@NonNull String poolName) {
		if (poolNames.contains(poolName)) {
			logger.warning(poolName + " is already registered!");
			return null;
		}
		if (host.isEmpty() || database.isEmpty() || username.isEmpty() || password.isEmpty()) {
			logger.warning("Connection info is invalid! One or more values is empty!");
			return null;
		}
		if ((engine == StorageType.H2 || engine == StorageType.SQLITE) && path == null) {
			logger.warning("Connection path is missing!");
			return null;
		}

		logger.info("Loading storage provider... [" + engine + "]");

		HikariConfig config = new HikariConfig();
		config.setPoolName(poolName);
		config.setMaximumPoolSize(5);
		config.setMinimumIdle(3);
		config.addDataSourceProperty("serverName", host);
		config.addDataSourceProperty("portNumber", port);
		config.addDataSourceProperty("databaseName", database);
		config.addDataSourceProperty("user", username);
		config.addDataSourceProperty("password", password);

		switch (engine) {
			case POSTGRESQL:
				config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
				break;
			case MARIADB:
				config.setDataSourceClassName("org.mariadb.jdbc.MariaDbDataSource");
				break;
			case MYSQL:
				config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
				break;
			case H2:
				config.setDriverClassName("org.h2.Driver");
				config.setJdbcUrl("jdbc:h2:./" + path.toString() + ";MODE=PostgreSQL");
				break;
			case SQLITE:
				config.setDriverClassName("org.sqlite.JDBC");
				config.setJdbcUrl("jdbc:sqlite:" + path.toString() + "?autoReconnect=true");
				break;
		}

		Storage storage = constructor.apply(engine, new HikariDataSource(config));
		if (storage.init()) {
			poolNames.add(poolName);
			return storage;
		}
		return null;
	}

	/**
	 * Constructs a new connection info builder
	 */
	public static @NonNull <T extends Storage> ConnectionInfo<T> create(@NonNull BiFunction<StorageType, HikariConfig, @NonNull T> constructor, @NonNull StorageType engine) {
		return new ConnectionInfo<>(constructor, engine);
	}

	public static @NonNull <T extends Storage> ConnectionInfo<T> mysql(@NonNull BiFunction<StorageType, HikariConfig, @NonNull T> constructor) {
		return new ConnectionInfo<>(constructor, StorageType.MYSQL).setPort(3306);
	}

	public static @NonNull <T extends Storage> ConnectionInfo<T> mariadb(@NonNull BiFunction<StorageType, HikariConfig, @NonNull T> constructor) {
		return new ConnectionInfo<>(constructor, StorageType.MARIADB).setPort(3306);
	}

	public static @NonNull <T extends Storage> ConnectionInfo<T> pgsql(@NonNull BiFunction<StorageType, HikariConfig, @NonNull T> constructor) {
		return new ConnectionInfo<>(constructor, StorageType.POSTGRESQL).setPort(5432);
	}
}
