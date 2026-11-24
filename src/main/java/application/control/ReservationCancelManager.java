package application.control;

import application.DataStore;
import application.HrsException;
import application.entity.Reservation;
import application.entity.ReservationStatus;

/**
 * 予約キャンセル管理（保守: 演習6で追加）
 */
public class ReservationCancelManager {

	/**
	 * 7.1 予約を照会 -&gt; 7.2 キャンセル可能か確認 -&gt; 7.3 予約状態をCANCELEDにする
	 * （空席数はCANCELED以外の予約数から動的に計算するため、明示的な増加処理は不要）
	 */
	public void cancel(String reservationNumber) throws HrsException {
		Reservation reservation = DataStore.getInstance().getReservationsByNumber().get(reservationNumber);
		if (reservation == null) {
			throw new HrsException("該当する予約がありません");
		}
		if (reservation.getStatus() != ReservationStatus.RESERVED) {
			throw new HrsException("この予約はキャンセルできない状態です");
		}

		reservation.setStatus(ReservationStatus.CANCELED);
		DataStore.getInstance().saveReservation(reservation);
	}
}
