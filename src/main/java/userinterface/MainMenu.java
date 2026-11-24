package userinterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * ホテル予約システムのメインメニュー（CUI）
 */
public class MainMenu {

	private final BufferedReader reader;

	MainMenu() {
		reader = new BufferedReader(new InputStreamReader(System.in));
	}

	private void execute() throws IOException {
		while (true) {
			System.out.println("");
			System.out.println("Menu");
			System.out.println("1: 部屋を検索する");
			System.out.println("2: 予約を確認する");
			System.out.println("3: チェックインする");
			System.out.println("4: チェックアウトする");
			System.out.println("5: 部屋情報を登録する（HRS管理者）");
			System.out.println("6: 予約をキャンセルする");
			System.out.println("9: 終了");
			System.out.print("> ");

			String menu = reader.readLine();
			if (menu == null || "9".equals(menu.trim())) {
				break;
			}

			switch (menu.trim()) {
				case "1":
					new RoomSearchScreen(reader).execute();
					break;
				case "2":
					new ReservationConfirmScreen(reader).execute();
					break;
				case "3":
					new CheckInScreen(reader).execute();
					break;
				case "4":
					new CheckOutScreen(reader).execute();
					break;
				case "5":
					new RoomRegistrationScreen(reader).execute();
					break;
				case "6":
					new ReservationCancelScreen(reader).execute();
					break;
				default:
					System.out.println("入力が不正です");
			}
		}
		System.out.println("終了しました");
		reader.close();
	}

	public static void main(String[] args) throws Exception {
		new MainMenu().execute();
	}
}
