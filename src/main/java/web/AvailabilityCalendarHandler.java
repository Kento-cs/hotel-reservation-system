package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import application.DataStore;
import application.entity.RoomType;
import util.DateUtil;

/**
 * GET /api/availability : 部屋種別・月を指定して、日ごとの空室数を返す（UC1の補助: 空室カレンダー）
 */
public class AvailabilityCalendarHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		Map<String, String> params = HttpUtil.parseQuery(exchange.getRequestURI().getQuery());
		Integer roomTypeId = HttpUtil.parseIntOrNull(params, "roomTypeId");
		String month = params.get("month");

		if (roomTypeId == null || month == null || month.isEmpty()) {
			HttpUtil.sendJson(exchange, 400, JsonWriter.errorObject("roomTypeIdとmonth(yyyy/MM)を指定してください"));
			return;
		}

		String[] parts = month.split("/");
		int year;
		int monthIndex;
		try {
			if (parts.length != 2) {
				throw new NumberFormatException();
			}
			year = Integer.parseInt(parts[0]);
			monthIndex = Integer.parseInt(parts[1]) - 1;
		}
		catch (NumberFormatException e) {
			HttpUtil.sendJson(exchange, 400, JsonWriter.errorObject("monthはyyyy/MM形式で指定してください"));
			return;
		}

		RoomType roomType = DataStore.getInstance().getRoomTypeById(roomTypeId);
		if (roomType == null) {
			HttpUtil.sendJson(exchange, 404, JsonWriter.errorObject("該当する部屋種別がありません"));
			return;
		}

		int totalCount = DataStore.getInstance().countTotalRooms(roomType);

		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(year, monthIndex, 1);
		int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		List<JsonWriter> items = new ArrayList<JsonWriter>();
		for (int day = 1; day <= daysInMonth; day++) {
			calendar.set(Calendar.DAY_OF_MONTH, day);
			Date date = calendar.getTime();
			int availableCount = DataStore.getInstance().countAvailableRooms(roomType, date);
			items.add(new JsonWriter()
					.put("date", DateUtil.convertToString(date))
					.put("availableCount", availableCount)
					.put("totalCount", totalCount));
		}
		HttpUtil.sendJson(exchange, 200, JsonWriter.array(items));
	}
}
