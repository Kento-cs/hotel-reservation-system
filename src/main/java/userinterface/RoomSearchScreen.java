package userinterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import application.DataStore;
import application.HrsException;
import application.ManagerFactory;
import application.entity.RoomType;
import application.entity.SmokingType;
import util.DateUtil;

/**
 * 検索画面
 */
public class RoomSearchScreen {

	private final BufferedReader reader;

	public RoomSearchScreen(BufferedReader reader) {
		this.reader = reader;
	}

	/**
	 * 1: 検索条件を入力する() -&gt; 2: 検索要求() -&gt; 3: 空室・部屋種別一覧または警告()
	 * 該当する部屋があれば、続けて予約画面へ遷移するかどうかを尋ねる。
	 */
	public void execute() throws IOException {
		System.out.println("宿泊日を入力してください（yyyy/mm/dd）");
		System.out.print("> ");
		Date stayingDate = DateUtil.convertToDate(reader.readLine());
		if (stayingDate == null) {
			System.out.println("入力が不正です");
			return;
		}

		System.out.println("人数を入力してください");
		System.out.print("> ");
		int headCount = parseIntOrDefault(reader.readLine(), 1);

		System.out.println("値段の下限を入力してください（未指定はEnter）");
		System.out.print("> ");
		Integer minPrice = parseIntOrNull(reader.readLine());

		System.out.println("値段の上限を入力してください（未指定はEnter）");
		System.out.print("> ");
		Integer maxPrice = parseIntOrNull(reader.readLine());

		System.out.println("喫煙区分を入力してください（1: 喫煙, 2: 禁煙, 未指定はEnter）");
		System.out.print("> ");
		SmokingType smokingType = parseSmokingType(reader.readLine());

		try {
			List<RoomType> roomTypes = ManagerFactory.getInstance().getRoomSearchManager()
					.search(stayingDate, headCount, minPrice, maxPrice, smokingType);

			System.out.println("空室・部屋種別一覧");
			for (int i = 0; i < roomTypes.size(); i++) {
				RoomType roomType = roomTypes.get(i);
				int availableCount = DataStore.getInstance().countAvailableRooms(roomType, stayingDate);
				System.out.println((i + 1) + ": " + roomType.getTypeName() + " / " + roomType.getPrice() + "円 / "
						+ roomType.getSmokingType().getLabel() + " / 収容" + roomType.getCapacity() + "人 / 空席"
						+ availableCount);
			}

			System.out.println("この中から予約しますか？（y/n）");
			System.out.print("> ");
			String answer = reader.readLine();
			if ("y".equalsIgnoreCase(answer)) {
				new ReservationScreen(reader).execute(roomTypes, stayingDate, headCount);
			}
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

	private Integer parseIntOrNull(String s) {
		try {
			return Integer.valueOf(s.trim());
		}
		catch (Exception e) {
			return null;
		}
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
}
