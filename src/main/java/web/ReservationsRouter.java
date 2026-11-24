package web;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * /api/reservations : GETは予約確認（UC3）、POSTは予約登録（UC2）に振り分ける
 */
public class ReservationsRouter implements HttpHandler {

	private final ReservationHandler reservationHandler = new ReservationHandler();
	private final ReservationConfirmHandler reservationConfirmHandler = new ReservationConfirmHandler();

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
			reservationHandler.handle(exchange);
		}
		else {
			reservationConfirmHandler.handle(exchange);
		}
	}
}
