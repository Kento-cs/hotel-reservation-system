package application.control;

import application.DataStore;
import application.HrsException;
import application.entity.Reservation;

/**
 * 予約確認管理
 */
public class ReservationConfirmManager {

	/**
	 * 予約番号を検索する
	 */
	public Reservation confirm(String reservationNumber) throws HrsException {
		Reservation reservation = DataStore.getInstance().getReservationsByNumber().get(reservationNumber);
		if (reservation == null) {
			throw new HrsException("該当する予約がありません");
		}
		return reservation;
	}
}
