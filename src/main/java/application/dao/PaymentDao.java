package application.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import application.entity.Payment;
import application.entity.PaymentMethod;
import application.entity.Reservation;
import application.entity.Staff;

/**
 * 支払いのDAO
 */
public class PaymentDao {

	public List<Payment> findAll(Map<String, Reservation> reservationsByNumber) {
		List<Payment> result = new ArrayList<Payment>();
		Connection connection = ConnectionUtil.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(
					"SELECT RESERVATION_NUMBER, AMOUNT, METHOD, STATUS, HANDLED_BY FROM PAYMENT");
			try {
				ResultSet resultSet = statement.executeQuery();
				try {
					while (resultSet.next()) {
						Reservation reservation = reservationsByNumber.get(resultSet.getString("RESERVATION_NUMBER"));
						Payment payment = new Payment(reservation, resultSet.getInt("AMOUNT"));
						String method = resultSet.getString("METHOD");
						if (method != null) {
							payment.pay(PaymentMethod.valueOf(method), new Staff(resultSet.getString("HANDLED_BY")));
						}
						result.add(payment);
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
			throw new RuntimeException("支払いの読み込みに失敗しました", e);
		}
		return result;
	}

	public void insert(Payment payment) {
		Connection connection = ConnectionUtil.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(
					"INSERT INTO PAYMENT (RESERVATION_NUMBER, AMOUNT, METHOD, STATUS, HANDLED_BY) "
							+ "VALUES (?, ?, ?, ?, ?)");
			try {
				statement.setString(1, payment.getReservation().getReservationNumber());
				statement.setInt(2, payment.getAmount());
				statement.setString(3, payment.getMethod() != null ? payment.getMethod().name() : null);
				statement.setString(4, payment.getStatus().name());
				statement.setString(5, payment.getHandledBy() != null ? payment.getHandledBy().getName() : null);
				statement.executeUpdate();
			}
			finally {
				statement.close();
			}
		}
		catch (SQLException e) {
			throw new RuntimeException("支払いの登録に失敗しました", e);
		}
	}
}
