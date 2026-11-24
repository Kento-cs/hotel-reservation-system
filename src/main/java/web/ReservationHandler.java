package web;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import application.HrsException;
import application.ManagerFactory;
import application.entity.RoomType;
import util.DateUtil;

/**
 * POST /api/reservations : 部屋を予約する（UC2）
 */
public class ReservationHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
			HttpUtil.sendJson(exchange, 405, JsonWriter.errorObject("POSTのみ受け付けます"));
			return;
		}

		Map<String, String> params = HttpUtil.parseQuery(HttpUtil.readBody(exchange.getRequestBody()));
		Integer roomTypeId = HttpUtil.parseIntOrNull(params, "roomTypeId");
		String name = params.get("name");
		String contact = params.get("contact");
		Date date = DateUtil.convertToDate(params.get("date"));
		int headCount = HttpUtil.parseIntOrDefault(params, "headCount", 1);

		if (roomTypeId == null || name == null || name.isEmpty() || date == null) {
			HttpUtil.sendJson(exchange, 400, JsonWriter.errorObject("入力が不正です"));
			return;
		}
		if (Validation.tooLong(name, 100)) {
			HttpUtil.sendJson(exchange, 400, JsonWriter.errorObject("名前は100文字以内で入力してください"));
			return;
		}
		if (Validation.tooLong(contact, 100)) {
			HttpUtil.sendJson(exchange, 400, JsonWriter.errorObject("連絡先は100文字以内で入力してください"));
			return;
		}

		RoomType roomType = application.DataStore.getInstance().getRoomTypeById(roomTypeId);
		if (roomType == null) {
			HttpUtil.sendJson(exchange, 404, JsonWriter.errorObject("該当する部屋種別がありません"));
			return;
		}
		if (Validation.outOfRange(headCount, 1, roomType.getCapacity())) {
			HttpUtil.sendJson(exchange, 400,
					JsonWriter.errorObject("人数は1人以上" + roomType.getCapacity() + "人以下で入力してください"));
			return;
		}

		try {
			String reservationNumber = ManagerFactory.getInstance().getReservationManager()
					.reserve(roomType, name, contact, date, headCount);
			HttpUtil.sendJson(exchange, 200, new JsonWriter().put("reservationNumber", reservationNumber).toString());
		}
		catch (HrsException e) {
			HttpUtil.sendJson(exchange, 409, JsonWriter.errorObject(e.getMessage()));
		}
	}
}
