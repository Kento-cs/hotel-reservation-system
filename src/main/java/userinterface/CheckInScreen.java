package userinterface;

import java.io.BufferedReader;
import java.io.IOException;

import application.HrsException;
import application.ManagerFactory;
import application.entity.Room;

/**
 * チェックイン画面
 */
public class CheckInScreen {

	private final BufferedReader reader;

	public CheckInScreen(BufferedReader reader) {
		this.reader = reader;
	}

	/**
	 * 1: 予約番号を入力する() -&gt; 2: 照会要求・割り当て -&gt; 3: 部屋番号または該当なしの警告()
	 */
	public void execute() throws IOException {
		System.out.println("予約番号を入力してください");
		System.out.print("> ");
		String reservationNumber = reader.readLine();

		try {
			Room room = ManagerFactory.getInstance().getCheckInManager().checkIn(reservationNumber);
			System.out.println("チェックインが完了しました");
			System.out.println("部屋番号: " + room.getRoomNumber()
					+ (room.getNickname() != null ? "（" + room.getNickname() + "）" : ""));
		}
		catch (HrsException e) {
			System.out.println("警告: " + e.getMessage());
		}
	}
}
