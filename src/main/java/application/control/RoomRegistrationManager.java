package application.control;

import application.DataStore;
import application.HrsException;
import application.entity.Room;
import application.entity.RoomType;
import application.entity.SmokingType;
import application.entity.StandardRoomType;
import application.entity.SuiteRoomType;

/**
 * 部屋情報登録管理
 */
public class RoomRegistrationManager {

	public static final String TYPE_NAME_STANDARD = "スタンダード";
	public static final String TYPE_NAME_SUITE = "スイート";

	/**
	 * 部屋を登録する。同じ部屋種別（種別名・値段・喫煙区分が一致するもの）が既にあればそれを再利用し、
	 * なければ新しい部屋種別を作成する。
	 */
	public void registerRoom(String roomNumber, String typeName, int price, SmokingType smokingType, String nickname)
			throws HrsException {
		for (Room room : DataStore.getInstance().getHotel().getRooms()) {
			if (room.getRoomNumber().equals(roomNumber)) {
				throw new HrsException("その部屋番号は既に登録されています");
			}
		}

		RoomType roomType = resolveRoomType(typeName, price, smokingType);
		DataStore.getInstance().createRoom(new Room(roomNumber, roomType, nickname));
	}

	/**
	 * 種別名・値段・喫煙区分が一致する部屋種別を探し、なければ新しく作成する（部屋の登録・編集の両方で使用）
	 */
	public RoomType resolveRoomType(String typeName, int price, SmokingType smokingType) throws HrsException {
		RoomType roomType = findMatchingRoomType(typeName, price, smokingType);
		if (roomType == null) {
			roomType = createRoomType(typeName, price, smokingType);
			DataStore.getInstance().createRoomType(roomType);
		}
		return roomType;
	}

	private RoomType findMatchingRoomType(String typeName, int price, SmokingType smokingType) {
		for (RoomType roomType : DataStore.getInstance().getRoomTypes()) {
			if (roomType.getTypeName().equals(typeName) && roomType.getPrice() == price
					&& roomType.getSmokingType() == smokingType) {
				return roomType;
			}
		}
		return null;
	}

	private RoomType createRoomType(String typeName, int price, SmokingType smokingType) throws HrsException {
		int id = DataStore.getInstance().nextRoomTypeId();
		if (TYPE_NAME_STANDARD.equals(typeName)) {
			return new StandardRoomType(id, price, smokingType);
		}
		if (TYPE_NAME_SUITE.equals(typeName)) {
			return new SuiteRoomType(id, price, smokingType);
		}
		throw new HrsException("不明な部屋種別です: " + typeName);
	}
}
