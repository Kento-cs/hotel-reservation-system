package application.control;

import application.DataStore;
import application.HrsException;
import application.entity.Reservation;
import application.entity.ReservationStatus;
import application.entity.Room;
import application.entity.RoomType;

/**
 * チェックイン管理
 */
public class CheckInManager {

	/**
	 * 2.2 状態をCHECKED_INにする -&gt; 2.3 空いている部屋を割り当てる -&gt; 2.4 部屋の空室状況を使用中にする
	 */
	public Room checkIn(String reservationNumber) throws HrsException {
		Reservation reservation = DataStore.getInstance().getReservationsByNumber().get(reservationNumber);
		if (reservation == null) {
			throw new HrsException("該当する予約がありません");
		}
		if (reservation.getStatus() != ReservationStatus.RESERVED) {
			throw new HrsException("この予約はチェックイン可能な状態ではありません");
		}

		// ▼ 本日の予約かどうかを確認する処理を追加
		String todayStr = util.DateUtil.convertToString(new java.util.Date());
		String resDateStr = util.DateUtil.convertToString(reservation.getDate());
		if (!todayStr.equals(resDateStr)) {
			throw new HrsException("本日の予約ではありません（宿泊予定日: " + resDateStr + "）");
		}

		RoomType roomType = reservation.getRoomType();
		Room availableRoom = null;
		for (Room room : DataStore.getInstance().getHotel().getRooms()) {
			if (room.getRoomType() == roomType && !room.isOccupied()) {
				availableRoom = room;
				break;
			}
		}
		if (availableRoom == null) {
			throw new HrsException("空いている部屋がありません");
		}

		reservation.setStatus(ReservationStatus.CHECKED_IN);
		availableRoom.occupy(reservationNumber);
		reservation.setAssignedRoomNumber(availableRoom.getRoomNumber());
		DataStore.getInstance().saveReservation(reservation);
		DataStore.getInstance().saveRoomOccupancy(availableRoom);

		return availableRoom;
	}
}
