package application.control;

import java.util.Calendar;
import java.util.Date;

import application.DataStore;
import application.HrsException;
import application.entity.Guest;
import application.entity.Reservation;
import application.entity.RoomType;

/**
 * 予約管理
 */
public class ReservationManager {

	/**
	 * 部屋種別を予約する（宿泊日ごとに空席を管理する）
	 *
	 * 6.1 利用者を生成 -&gt; 6.2 空席数を確認 -&gt; 6.3 予約 -&gt; 6.4 予約状態をRESERVEDにする
	 */
	public String reserve(RoomType roomType, String name, String contact, Date date, int headCount)
			throws HrsException {
		if (DataStore.getInstance().countAvailableRooms(roomType, date) <= 0) {
			throw new HrsException("あいにく満室になりました");
		}

		//6.1 利用者を生成
		Guest guest = new Guest(DataStore.getInstance().nextGuestId(), name, contact);
		DataStore.getInstance().createGuest(guest);

		//6.3 予約 / 6.4 予約状態をRESERVEDにする（コンストラクタで初期状態RESERVED）
		String reservationNumber = generateReservationNumber();
		Reservation reservation = new Reservation(reservationNumber, date, headCount, guest, roomType);

		DataStore.getInstance().createReservation(reservation);
		return reservationNumber;
	}

	private synchronized String generateReservationNumber() {
		Calendar calendar = Calendar.getInstance();
		try {
			Thread.sleep(1);
		}
		catch (InterruptedException e) {
		}
		return String.valueOf(calendar.getTimeInMillis());
	}
}
