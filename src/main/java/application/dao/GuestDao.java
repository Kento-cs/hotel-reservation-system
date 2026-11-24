package application.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.entity.Guest;

/**
 * 利用者のDAO
 */
public class GuestDao {

	public Guest findById(int id) {
		Connection connection = ConnectionUtil.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT ID, NAME, CONTACT FROM GUEST WHERE ID = ?");
			try {
				statement.setInt(1, id);
				ResultSet resultSet = statement.executeQuery();
				try {
					if (!resultSet.next()) {
						return null;
					}
					return new Guest(resultSet.getInt("ID"), resultSet.getString("NAME"), resultSet.getString("CONTACT"));
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
			throw new RuntimeException("利用者の読み込みに失敗しました", e);
		}
	}

	public int nextId() {
		Connection connection = ConnectionUtil.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT MAX(ID) FROM GUEST");
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
			throw new RuntimeException("利用者IDの採番に失敗しました", e);
		}
	}

	public void insert(Guest guest) {
		Connection connection = ConnectionUtil.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement("INSERT INTO GUEST (ID, NAME, CONTACT) VALUES (?, ?, ?)");
			try {
				statement.setInt(1, guest.getId());
				statement.setString(2, guest.getName());
				statement.setString(3, guest.getContact());
				statement.executeUpdate();
			}
			finally {
				statement.close();
			}
		}
		catch (SQLException e) {
			throw new RuntimeException("利用者の登録に失敗しました", e);
		}
	}
}
