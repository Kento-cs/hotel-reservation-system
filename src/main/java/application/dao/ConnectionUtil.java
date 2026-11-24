package application.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * HSQLDB（埋め込みファイルモード）への接続を1つ保持して使い回すユーティリティ。<br>
 * DBファイルはリポジトリ直下の db/hrsdb。
 */
public class ConnectionUtil {

	private static final String URL = "jdbc:hsqldb:file:db/hrsdb;shutdown=true";
	private static final String USER = "SA";
	private static final String PASSWORD = "";

	private static Connection connection;

	static {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			ensureSchema();
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					try {
						Statement statement = connection.createStatement();
						statement.execute("SHUTDOWN");
						statement.close();
					}
					catch (SQLException e) {
						// 終了処理なので握りつぶす
					}
				}
			});
		}
		catch (Exception e) {
			throw new RuntimeException("DBへの接続に失敗しました", e);
		}
	}

	private ConnectionUtil() {
	}

	public static Connection getConnection() {
		return connection;
	}

	private static boolean tableExists(String tableName) throws SQLException {
		ResultSet resultSet = connection.getMetaData().getTables(null, null, tableName, null);
		try {
			return resultSet.next();
		}
		finally {
			resultSet.close();
		}
	}

	private static void createTableIfMissing(String tableName, String ddl) throws SQLException {
		if (tableExists(tableName)) {
			return;
		}
		Statement statement = connection.createStatement();
		try {
			statement.execute(ddl);
		}
		finally {
			statement.close();
		}
	}

	private static void ensureSchema() throws SQLException {
		createTableIfMissing("ROOM_TYPE",
				"CREATE TABLE ROOM_TYPE (" +
						"ID INTEGER NOT NULL PRIMARY KEY," +
						"TYPE_NAME VARCHAR(50) NOT NULL," +
						"PRICE INTEGER NOT NULL," +
						"SMOKING_TYPE VARCHAR(20) NOT NULL," +
						"CAPACITY INTEGER NOT NULL)");

		createTableIfMissing("ROOM",
				"CREATE TABLE ROOM (" +
						"ROOM_NUMBER VARCHAR(20) NOT NULL PRIMARY KEY," +
						"ROOM_TYPE_ID INTEGER NOT NULL," +
						"NICKNAME VARCHAR(50)," +
						"OCCUPIED BOOLEAN NOT NULL," +
						"OCCUPYING_RESERVATION_NUMBER VARCHAR(50))");

		createTableIfMissing("GUEST",
				"CREATE TABLE GUEST (" +
						"ID INTEGER NOT NULL PRIMARY KEY," +
						"NAME VARCHAR(100)," +
						"CONTACT VARCHAR(100))");

		createTableIfMissing("RESERVATION",
				"CREATE TABLE RESERVATION (" +
						"RESERVATION_NUMBER VARCHAR(50) NOT NULL PRIMARY KEY," +
						"STAYING_DATE VARCHAR(20) NOT NULL," +
						"HEAD_COUNT INTEGER NOT NULL," +
						"GUEST_ID INTEGER NOT NULL," +
						"ROOM_TYPE_ID INTEGER NOT NULL," +
						"STATUS VARCHAR(20) NOT NULL," +
						"STAY_FEE INTEGER NOT NULL," +
						"ASSIGNED_ROOM_NUMBER VARCHAR(20))");

		createTableIfMissing("PAYMENT",
				"CREATE TABLE PAYMENT (" +
						"RESERVATION_NUMBER VARCHAR(50) NOT NULL PRIMARY KEY," +
						"AMOUNT INTEGER NOT NULL," +
						"METHOD VARCHAR(20)," +
						"STATUS VARCHAR(20) NOT NULL," +
						"HANDLED_BY VARCHAR(100))");
	}
}
