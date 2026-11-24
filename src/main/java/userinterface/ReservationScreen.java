package userinterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import application.HrsException;
import application.ManagerFactory;
import application.entity.RoomType;

/**
 * 予約画面
 */
public class ReservationScreen {

	private final BufferedReader reader;

	public ReservationScreen(BufferedReader reader) {
		this.reader = reader;
	}

	/**
	 * 1: 一覧から部屋種別を選択する() -&gt; 2: 名前・連絡先を入力する() -&gt; 3: 確認要求() -&gt;
	 * 4: 予約内容確認画面() -&gt; 5: 確定する() -&gt; 6: 予約要求() -&gt; 7: 予約完了・予約番号を表示()
	 */
	public void execute(List<RoomType> roomTypes, Date stayingDate, int headCount) throws IOException {
		System.out.println("予約する部屋種別の番号を選択してください");
		System.out.print("> ");
		int index = parseIntOrDefault(reader.readLine(), -1) - 1;
		if (index < 0 || index >= roomTypes.size()) {
			System.out.println("入力が不正です");
			return;
		}
		RoomType roomType = roomTypes.get(index);

		System.out.println("名前を入力してください");
		System.out.print("> ");
		String name = reader.readLine();

		System.out.println("連絡先を入力してください");
		System.out.print("> ");
		String contact = reader.readLine();

		System.out.println("予約内容確認");
		System.out.println("部屋種別: " + roomType.getTypeName());
		System.out.println("料金: " + roomType.getPrice() + "円");
		System.out.println("人数: " + headCount + "人");
		System.out.println("この内容で確定しますか？（y/n）");
		System.out.print("> ");
		String answer = reader.readLine();
		if (!"y".equalsIgnoreCase(answer)) {
			System.out.println("予約を中止しました");
			return;
		}

		try {
			String reservationNumber = ManagerFactory.getInstance().getReservationManager()
					.reserve(roomType, name, contact, stayingDate, headCount);
			System.out.println("予約が完了しました");
			System.out.println("予約番号: " + reservationNumber);
		}
		catch (HrsException e) {
			System.out.println("警告: " + e.getMessage());
		}
	}

	private int parseIntOrDefault(String s, int defaultValue) {
		try {
			return Integer.parseInt(s.trim());
		}
		catch (Exception e) {
			return defaultValue;
		}
	}
}
