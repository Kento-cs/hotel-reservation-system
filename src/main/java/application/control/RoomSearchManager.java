package application.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import application.DataStore;
import application.HrsException;
import application.entity.RoomType;
import application.entity.SmokingType;

/**
 * 部屋検索管理
 */
public class RoomSearchManager {

	/**
	 * 条件に合う空室（部屋種別）を検索する
	 *
	 * @param date 宿泊日
	 * @param headCount 人数
	 * @param minPrice 値段範囲（下限）。指定しない場合はnull
	 * @param maxPrice 値段範囲（上限）。指定しない場合はnull
	 * @param smokingType 喫煙区分。指定しない場合はnull
	 */
	public List<RoomType> search(Date date, int headCount, Integer minPrice, Integer maxPrice,
			SmokingType smokingType) throws HrsException {
		List<RoomType> result = new ArrayList<RoomType>();
		for (RoomType roomType : DataStore.getInstance().getRoomTypes()) {
			if (DataStore.getInstance().countAvailableRooms(roomType, date) <= 0) {
				continue;
			}
			if (roomType.getCapacity() < headCount) {
				continue;
			}
			if (minPrice != null && roomType.getPrice() < minPrice) {
				continue;
			}
			if (maxPrice != null && roomType.getPrice() > maxPrice) {
				continue;
			}
			if (smokingType != null && roomType.getSmokingType() != smokingType) {
				continue;
			}
			result.add(roomType);
		}

		if (result.isEmpty()) {
			throw new HrsException("該当する部屋がありません");
		}
		return result;
	}
}
