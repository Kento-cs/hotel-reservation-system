package application.control;

import application.DataStore;
import application.HrsException;
import application.entity.Payment;
import application.entity.PaymentMethod;
import application.entity.Reservation;
import application.entity.ReservationStatus;
import application.entity.Room;
import application.entity.Staff;

/**
 * チェックアウト管理
 */
public class CheckOutManager {

	/**
	 * 2.1 宿泊料を計算する
	 */
	public Reservation calculateFee(String roomNumber) throws HrsException {
		Reservation reservation = findReservationByRoomNumber(roomNumber);
		return reservation;
	}

	/**
	 * 4.1 状態をCHECKED_OUTにする -&gt; 4.2 部屋を空室に戻す -&gt; 4.3 支払い
	 */
	public void checkOut(String roomNumber, PaymentMethod method, Staff staff) throws HrsException {
		Reservation reservation = findReservationByRoomNumber(roomNumber);
		Room room = findRoom(roomNumber);

		reservation.setStatus(ReservationStatus.CHECKED_OUT);
		DataStore.getInstance().saveReservation(reservation);

		room.vacate();
		DataStore.getInstance().saveRoomOccupancy(room);

		Payment payment = new Payment(reservation, reservation.getStayFee());
		payment.pay(method, staff);
		DataStore.getInstance().createPayment(payment);
	}

	private Reservation findReservationByRoomNumber(String roomNumber) throws HrsException {
		Room room = findRoom(roomNumber);
		if (!room.isOccupied()) {
			throw new HrsException("該当する予約がありません");
		}
		Reservation reservation = DataStore.getInstance().getReservationsByNumber()
				.get(room.getOccupyingReservationNumber());
		if (reservation == null) {
			throw new HrsException("該当する予約がありません");
		}
		return reservation;
	}

	public Room findRoom(String roomNumber) throws HrsException {
		for (Room room : DataStore.getInstance().getHotel().getRooms()) {
			if (room.getRoomNumber().equals(roomNumber)) {
				return room;
			}
		}
		throw new HrsException("該当する部屋がありません");
	}
}
