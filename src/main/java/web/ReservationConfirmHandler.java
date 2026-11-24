package web;

import java.io.IOException;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import application.HrsException;
import application.ManagerFactory;
import application.entity.Reservation;
import util.DateUtil;

/**
 * GET /api/reservations?number=... : 予約を確認する（UC3）
 */
public class ReservationConfirmHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		Map<String, String> params = HttpUtil.parseQuery(exchange.getRequestURI().getQuery());
		String reservationNumber = params.get("number");

		try {
			Reservation reservation = ManagerFactory.getInstance().getReservationConfirmManager()
					.confirm(reservationNumber);
			String json = new JsonWriter()
					.put("reservationNumber", reservation.getReservationNumber())
					.put("typeName", reservation.getRoomType().getTypeName())
					.put("date", DateUtil.convertToString(reservation.getDate()))
					.put("headCount", reservation.getHeadCount())
					.put("status", reservation.getStatus().toString())
					.put("stayFee", reservation.getStayFee())
					.toString();
			HttpUtil.sendJson(exchange, 200, json);
		}
		catch (HrsException e) {
			HttpUtil.sendJson(exchange, 404, JsonWriter.errorObject(e.getMessage()));
		}
	}
}
