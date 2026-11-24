package web;

import java.io.IOException;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import application.HrsException;
import application.ManagerFactory;
import application.entity.Reservation;
import application.entity.Room;

/**
 * GET /api/checkouts/fee?roomNumber=... : チェックアウトする（UC5）宿泊料の計算
 */
public class CheckOutFeeHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		Map<String, String> params = HttpUtil.parseQuery(exchange.getRequestURI().getQuery());
		String roomNumber = params.get("roomNumber");

		try {
			Reservation reservation = ManagerFactory.getInstance().getCheckOutManager().calculateFee(roomNumber);
			Room room = ManagerFactory.getInstance().getCheckOutManager().findRoom(roomNumber);
			String json = new JsonWriter()
					.put("reservationNumber", reservation.getReservationNumber())
					.put("stayFee", reservation.getStayFee())
					.put("nickname", room.getNickname())
					.toString();
			HttpUtil.sendJson(exchange, 200, json);
		}
		catch (HrsException e) {
			HttpUtil.sendJson(exchange, 404, JsonWriter.errorObject(e.getMessage()));
		}
	}
}
