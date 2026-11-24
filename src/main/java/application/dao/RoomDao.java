package application.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import application.entity.Room;
import application.entity.RoomType;

/**
 * 部屋のDAO
 */
public class RoomDao {

	public List<Room> findAll(Map<Integer, RoomType> roomTypesById) {
		List<Room> result = new ArrayList<Room>();
		Connection connection = ConnectionUtil.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(
					"SELECT ROOM_NUMBER, ROOM_TYPE_ID, NICKNAME, OCCUPIED, OCCUPYING_RESERVATION_NUMBER FROM ROOM");
			try {
				ResultSet resultSet = statement.executeQuery();
				try {
					while (resultSet.next()) {
						RoomType roomType = roomTypesById.get(resultSet.getInt("ROOM_TYPE_ID"));
						Room room = new Room(resultSet.getString("ROOM_NUMBER"), roomType,
								resultSet.getString("NICKNAME"));
						if (resultSet.getBoolean("OCCUPIED")) {
							room.occupy(resultSet.getString("OCCUPYING_RESERVATION_NUMBER"));
						}
						result.add(room);
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
			throw new RuntimeException("部屋の読み込みに失敗しました", e);
		}
		return result;
	}

	public void insert(Room room) {
		Connection connection = ConnectionUtil.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(
					"INSERT INTO ROOM (ROOM_NUMBER, ROOM_TYPE_ID, NICKNAME, OCCUPIED, OCCUPYING_RESERVATION_NUMBER) "
							+ "VALUES (?, ?, ?, ?, ?)");
			try {
				statement.setString(1, room.getRoomNumber());
				statement.setInt(2, room.getRoomType().getId());
				statement.setString(3, room.getNickname());
				statement.setBoolean(4, room.isOccupied());
				statement.setString(5, room.getOccupyingReservationNumber());
				statement.executeUpdate();
			}
			finally {
				statement.close();
			}
		}
		catch (SQLException e) {
			throw new RuntimeException("部屋の登録に失敗しました", e);
		}
	}

	public void update(Room room) {
		Connection connection = ConnectionUtil.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(
					"UPDATE ROOM SET ROOM_TYPE_ID = ?, NICKNAME = ? WHERE ROOM_NUMBER = ?");
			try {
				statement.setInt(1, room.getRoomType().getId());
				statement.setString(2, room.getNickname());
				statement.setString(3, room.getRoomNumber());
				statement.executeUpdate();
			}
			finally {
				statement.close();
			}
		}
		catch (SQLException e) {
			throw new RuntimeException("部屋の更新に失敗しました", e);
		}
	}

	public void updateOccupancy(Room room) {
		Connection connection = ConnectionUtil.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(
					"UPDATE ROOM SET OCCUPIED = ?, OCCUPYING_RESERVATION_NUMBER = ? WHERE ROOM_NUMBER = ?");
			try {
				statement.setBoolean(1, room.isOccupied());
				statement.setString(2, room.getOccupyingReservationNumber());
				statement.setString(3, room.getRoomNumber());
				statement.executeUpdate();
			}
			finally {
				statement.close();
			}
		}
		catch (SQLException e) {
			throw new RuntimeException("部屋の更新に失敗しました", e);
		}
	}
}
