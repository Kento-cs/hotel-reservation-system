package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import application.DataStore;
import application.HrsException;
import application.ManagerFactory;
import application.entity.RoomType;
import application.entity.SmokingType;
import util.DateUtil;

/**
 * GET /api/room-types/search : 部屋を検索する（UC1）
 */
public class RoomSearchHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		Map<String, String> params = HttpUtil.parseQuery(exchange.getRequestURI().getQuery());
		Date date = DateUtil.convertToDate(params.get("date"));
		int headCount = HttpUtil.parseIntOrDefault(params, "headCount", 1);
		Integer minPrice = HttpUtil.parseIntOrNull(params, "minPrice");
		Integer maxPrice = HttpUtil.parseIntOrNull(params, "maxPrice");
		SmokingType smokingType = parseSmokingType(params.get("smoking"));

		if (date == null) {
			HttpUtil.sendJson(exchange, 400, JsonWriter.errorObject("宿泊日を入力してください"));
			return;
		}

		try {
			List<RoomType> roomTypes = ManagerFactory.getInstance().getRoomSearchManager()
					.search(date, headCount, minPrice, maxPrice, smokingType);

			List<JsonWriter> items = new ArrayList<JsonWriter>();
			for (RoomType roomType : roomTypes) {
				items.add(new JsonWriter()
						.put("id", roomType.getId())
						.put("typeName", roomType.getTypeName())
						.put("price", roomType.getPrice())
						.put("smokingLabel", roomType.getSmokingType().getLabel())
						.put("capacity", roomType.getCapacity())
						.put("availableCount", DataStore.getInstance().countAvailableRooms(roomType, date)));
			}
			HttpUtil.sendJson(exchange, 200, JsonWriter.array(items));
		}
		catch (HrsException e) {
			HttpUtil.sendJson(exchange, 404, JsonWriter.errorObject(e.getMessage()));
		}
	}

	private SmokingType parseSmokingType(String s) {
		if ("1".equals(s)) {
			return SmokingType.SMOKING;
		}
		if ("2".equals(s)) {
			return SmokingType.NON_SMOKING;
		}
		return null;
	}
}
