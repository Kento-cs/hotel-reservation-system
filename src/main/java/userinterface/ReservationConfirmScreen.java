package userinterface;

import java.io.BufferedReader;
import java.io.IOException;

import application.HrsException;
import application.ManagerFactory;
import application.entity.Reservation;
import util.DateUtil;

/**
 * 予約確認画面
 */
public class ReservationConfirmScreen {

	private final BufferedReader reader;

	public ReservationConfirmScreen(BufferedReader reader) {
		this.reader = reader;
	}

	/**
	 * 1: 予約番号を入力する() -&gt; 2: 照会要求() -&gt; 3: 予約内容または該当なしの警告()
	 */
	public void execute() throws IOException {
		System.out.println("予約番号を入力してください");
		System.out.print("> ");
		String reservationNumber = reader.readLine();

		try {
			Reservation reservation = ManagerFactory.getInstance().getReservationConfirmManager()
					.confirm(reservationNumber);
			System.out.println("部屋種別: " + reservation.getRoomType().getTypeName());
			System.out.println("宿泊日: " + DateUtil.convertToString(reservation.getDate()));
			System.out.println("人数: " + reservation.getHeadCount() + "人");
			System.out.println("状態: " + reservation.getStatus());
			System.out.println("宿泊料: " + reservation.getStayFee() + "円");
		}
		catch (HrsException e) {
			System.out.println("警告: " + e.getMessage());
		}
	}
}
