package application.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.entity.RoomType;
import application.entity.SmokingType;
import application.entity.StandardRoomType;
import application.entity.SuiteRoomType;

/**
 * 部屋種別のDAO
 */
public class RoomTypeDao {

	public List<RoomType> findAll() {
		List<RoomType> result = new ArrayList<RoomType>();
		Connection connection = ConnectionUtil.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(
					"SELECT ID, TYPE_NAME, PRICE, SMOKING_TYPE, CAPACITY FROM ROOM_TYPE");
			try {
				ResultSet resultSet = statement.executeQuery();
				try {
					while (resultSet.next()) {
						result.add(toRoomType(resultSet));
					}
				}
				finally {
					resultSet.close();
				}
			}
			finally {
				statement.close();
			}
		}
		catch (SQLException e) {
			throw new RuntimeException("部屋種別の読み込みに失敗しました", e);
		}
		return result;
	}

	private RoomType toRoomType(ResultSet resultSet) throws SQLException {
		int id = resultSet.getInt("ID");
		String typeName = resultSet.getString("TYPE_NAME");
		int price = resultSet.getInt("PRICE");
		SmokingType smokingType = SmokingType.valueOf(resultSet.getString("SMOKING_TYPE"));

		if ("スタンダード".equals(typeName)) {
			return new StandardRoomType(id, price, smokingType);
		}
		return new SuiteRoomType(id, price, smokingType);
	}

	public int nextId() {
		Connection connection = ConnectionUtil.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT MAX(ID) FROM ROOM_TYPE");
			try {
				ResultSet resultSet = statement.executeQuery();
				try {
					resultSet.next();
					return resultSet.getInt(1) + 1;
				}
				finally {
					resultSet.close();
				}
			}
			finally {
				statement.close();
			}
		}
		catch (SQLException e) {
			throw new RuntimeException("部屋種別IDの採番に失敗しました", e);
		}
	}

	public void insert(RoomType roomType) {
		Connection connection = ConnectionUtil.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(
					"INSERT INTO ROOM_TYPE (ID, TYPE_NAME, PRICE, SMOKING_TYPE, CAPACITY) "
							+ "VALUES (?, ?, ?, ?, ?)");
			try {
				statement.setInt(1, roomType.getId());
				statement.setString(2, roomType.getTypeName());
				statement.setInt(3, roomType.getPrice());
				statement.setString(4, roomType.getSmokingType().name());
				statement.setInt(5, roomType.getCapacity());
				statement.executeUpdate();
			}
			finally {
				statement.close();
			}
		}
		catch (SQLException e) {
			throw new RuntimeException("部屋種別の登録に失敗しました", e);
		}
	}
}
