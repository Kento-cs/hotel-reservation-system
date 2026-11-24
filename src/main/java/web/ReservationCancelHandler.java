package web;

import java.io.IOException;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import application.HrsException;
import application.ManagerFactory;

/**
 * POST /api/reservations/cancel : 予約をキャンセルする（保守: 演習6で追加）
 */
public class ReservationCancelHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
			HttpUtil.sendJson(exchange, 405, JsonWriter.errorObject("POSTのみ受け付けます"));
			return;
		}

		Map<String, String> params = HttpUtil.parseQuery(HttpUtil.readBody(exchange.getRequestBody()));
		String reservationNumber = params.get("reservationNumber");

		try {
			ManagerFactory.getInstance().getReservationCancelManager().cancel(reservationNumber);
			HttpUtil.sendJson(exchange, 200, new JsonWriter().put("status", "ok").toString());
		}
		catch (HrsException e) {
			HttpUtil.sendJson(exchange, 409, JsonWriter.errorObject(e.getMessage()));
		}
	}
}
