package web;

import java.io.IOException;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import application.HrsException;
import application.ManagerFactory;
import application.entity.Room;

/**
 * POST /api/checkins : チェックインする（UC4）
 */
public class CheckInHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
			HttpUtil.sendJson(exchange, 405, JsonWriter.errorObject("POSTのみ受け付けます"));
			return;
		}

		Map<String, String> params = HttpUtil.parseQuery(HttpUtil.readBody(exchange.getRequestBody()));
		String reservationNumber = params.get("reservationNumber");

		try {
			Room room = ManagerFactory.getInstance().getCheckInManager().checkIn(reservationNumber);
			String json = new JsonWriter()
					.put("roomNumber", room.getRoomNumber())
					.put("nickname", room.getNickname())
					.toString();
			HttpUtil.sendJson(exchange, 200, json);
		}
		catch (HrsException e) {
			HttpUtil.sendJson(exchange, 409, JsonWriter.errorObject(e.getMessage()));
		}
	}
}
