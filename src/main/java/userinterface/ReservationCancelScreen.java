package userinterface;

import java.io.BufferedReader;
import java.io.IOException;

import application.HrsException;
import application.ManagerFactory;

/**
 * 予約キャンセル画面（保守: 演習6で追加）
 */
public class ReservationCancelScreen {

	private final BufferedReader reader;

	public ReservationCancelScreen(BufferedReader reader) {
		this.reader = reader;
	}

	/**
	 * 1: 予約番号を入力する() -&gt; 2: キャンセル要求() -&gt; 3: キャンセル完了または警告()
	 */
	public void execute() throws IOException {
		System.out.println("キャンセルする予約番号を入力してください");
		System.out.print("> ");
		String reservationNumber = reader.readLine();

		try {
			ManagerFactory.getInstance().getReservationCancelManager().cancel(reservationNumber);
			System.out.println("予約をキャンセルしました");
		}
		catch (HrsException e) {
			System.out.println("警告: " + e.getMessage());
		}
	}
}
