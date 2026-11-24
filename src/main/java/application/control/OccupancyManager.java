package application.control;

import java.util.ArrayList;
import java.util.List;

import application.DataStore;
import application.entity.Room;

/**
 * 宿泊者管理（現在チェックイン中の部屋の一覧を扱う。アクタ: HRS管理者）
 */
public class OccupancyManager {

	public List<Room> getOccupiedRooms() {
		List<Room> result = new ArrayList<Room>();
		for (Room room : DataStore.getInstance().getHotel().getRooms()) {
			if (room.isOccupied()) {
				result.add(room);
			}
		}
		return result;
	}
}
