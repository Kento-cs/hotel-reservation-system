package application.control;

import application.DataStore;
import application.HrsException;
import application.ManagerFactory;
import application.entity.Room;
import application.entity.RoomType;
import application.entity.SmokingType;

/**
 * 登録済みの部屋情報の編集管理（部屋種別・値段・喫煙区分・ニックネームを変更する。アクタ: HRS管理者）
 */
public class RoomEditManager {

	public void editRoom(String roomNumber, String typeName, int price, SmokingType smokingType, String nickname)
			throws HrsException {
		Room room = DataStore.getInstance().getRoomByNumber(roomNumber);
		if (room == null) {
			throw new HrsException("該当する部屋がありません");
		}

		RoomType roomType = ManagerFactory.getInstance().getRoomRegistrationManager()
				.resolveRoomType(typeName, price, smokingType);
		room.setRoomType(roomType);
		room.setNickname(nickname);
		DataStore.getInstance().updateRoom(room);
	}
}
