package userinterface;

import java.io.BufferedReader;
import java.io.IOException;

import application.HrsException;
import application.ManagerFactory;
import application.control.RoomRegistrationManager;
import application.entity.SmokingType;

/**
 * 部屋情報登録画面（アクタ: HRS管理者）
 */
public class RoomRegistrationScreen {

	private final BufferedReader reader;

	public RoomRegistrationScreen(BufferedReader reader) {
		this.reader = reader;
	}

	/**
	 * 部屋番号や種別の入力() -&gt; 部屋登録() -&gt; 登録完了画面の表示()
	 */
	public void execute() throws IOException {
		System.out.println("部屋番号を入力してください");
		System.out.print("> ");
		String roomNumber = reader.readLine();

		System.out.println("部屋種別を選択してください（1: " + RoomRegistrationManager.TYPE_NAME_STANDARD + ", 2: "
				+ RoomRegistrationManager.TYPE_NAME_SUITE + "）");
		System.out.print("> ");
		String typeName = parseTypeName(reader.readLine());
		if (typeName == null) {
			System.out.println("入力が不正です");
			return;
		}

		System.out.println("値段を入力してください");
		System.out.print("> ");
		int price = parseIntOrDefault(reader.readLine(), -1);
		if (price < 0) {
			System.out.println("入力が不正です");
			return;
		}

		System.out.println("喫煙区分を選択してください（1: 喫煙, 2: 禁煙）");
		System.out.print("> ");
		SmokingType smokingType = parseSmokingType(reader.readLine());
		if (smokingType == null) {
			System.out.println("入力が不正です");
			return;
		}

		System.out.println("部屋の愛称を入力してください（任意、未入力可）");
		System.out.print("> ");
		String nickname = reader.readLine();
		if (nickname != null && nickname.trim().isEmpty()) {
			nickname = null;
		}

		try {
			ManagerFactory.getInstance().getRoomRegistrationManager()
					.registerRoom(roomNumber, typeName, price, smokingType, nickname);
			System.out.println("部屋情報の登録が完了しました");
		}
		catch (HrsException e) {
			System.out.println("警告: " + e.getMessage());
		}
	}

	private String parseTypeName(String s) {
		if (s == null) {
			return null;
		}
		String trimmed = s.trim();
		if ("1".equals(trimmed)) {
			return RoomRegistrationManager.TYPE_NAME_STANDARD;
		}
		if ("2".equals(trimmed)) {
			return RoomRegistrationManager.TYPE_NAME_SUITE;
		}
		return null;
	}

	private SmokingType parseSmokingType(String s) {
		if (s == null) {
			return null;
		}
		String trimmed = s.trim();
		if ("1".equals(trimmed)) {
			return SmokingType.SMOKING;
		}
		if ("2".equals(trimmed)) {
			return SmokingType.NON_SMOKING;
		}
		return null;
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
