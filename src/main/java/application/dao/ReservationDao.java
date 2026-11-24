package application.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import application.entity.Guest;
import application.entity.Reservation;
import application.entity.ReservationStatus;
import application.entity.RoomType;
import util.DateUtil;

/**
 * 予約のDAO
 */
public class ReservationDao {

	private final GuestDao guestDao = new GuestDao();

	public List<Reservation> findAll(Map<Integer, RoomType> roomTypesById) {
		List<Reservation> result = new ArrayList<Reservation>();
		Connection connection = ConnectionUtil.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(
					"SELECT RESERVATION_NUMBER, STAYING_DATE, HEAD_COUNT, GUEST_ID, ROOM_TYPE_ID, STATUS, "
							+ "STAY_FEE, ASSIGNED_ROOM_NUMBER FROM RESERVATION");
			try {
				ResultSet resultSet = statement.executeQuery();
				try {
					while (resultSet.next()) {
						result.add(toReservation(resultSet, roomTypesById));
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
			throw new RuntimeException("予約の読み込みに失敗しました", e);
		}
		return result;
	}

	private Reservation toReservation(ResultSet resultSet, Map<Integer, RoomType> roomTypesById) throws SQLException {
		Guest guest = guestDao.findById(resultSet.getInt("GUEST_ID"));
		RoomType roomType = roomTypesById.get(resultSet.getInt("ROOM_TYPE_ID"));

		Reservation reservation = new Reservation(
				resultSet.getString("RESERVATION_NUMBER"),
				DateUtil.convertToDate(resultSet.getString("STAYING_DATE")),
				resultSet.getInt("HEAD_COUNT"),
				guest,
				roomType);
		reservation.setStatus(ReservationStatus.valueOf(resultSet.getString("STATUS")));
		reservation.setStayFee(resultSet.getInt("STAY_FEE"));
		reservation.setAssignedRoomNumber(resultSet.getString("ASSIGNED_ROOM_NUMBER"));
		return reservation;
	}

	public void insert(Reservation reservation) {
		Connection connection = ConnectionUtil.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(
					"INSERT INTO RESERVATION (RESERVATION_NUMBER, STAYING_DATE, HEAD_COUNT, GUEST_ID, "
							+ "ROOM_TYPE_ID, STATUS, STAY_FEE, ASSIGNED_ROOM_NUMBER) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			try {
				statement.setString(1, reservation.getReservationNumber());
				statement.setString(2, DateUtil.convertToString(reservation.getDate()));
				statement.setInt(3, reservation.getHeadCount());
				statement.setInt(4, reservation.getGuest().getId());
				statement.setInt(5, reservation.getRoomType().getId());
				statement.setString(6, reservation.getStatus().name());
				statement.setInt(7, reservation.getStayFee());
				statement.setString(8, reservation.getAssignedRoomNumber());
				statement.executeUpdate();
			}
			finally {
				statement.close();
			}
		}
		catch (SQLException e) {
			throw new RuntimeException("予約の登録に失敗しました", e);
		}
	}

	public void update(Reservation reservation) {
		Connection connection = ConnectionUtil.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(
					"UPDATE RESERVATION SET STATUS = ?, ASSIGNED_ROOM_NUMBER = ? WHERE RESERVATION_NUMBER = ?");
			try {
				statement.setString(1, reservation.getStatus().name());
				statement.setString(2, reservation.getAssignedRoomNumber());
				statement.setString(3, reservation.getReservationNumber());
				statement.executeUpdate();
			}
			finally {
				statement.close();
			}
		}
		catch (SQLException e) {
			throw new RuntimeException("予約の更新に失敗しました", e);
		}
	}
}
