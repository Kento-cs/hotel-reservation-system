package web;

import java.io.IOException;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import application.HrsException;
import application.ManagerFactory;
import application.entity.PaymentMethod;
import application.entity.Staff;

/**
 * POST /api/checkouts : チェックアウトする（UC5）支払い確定
 */
public class CheckOutHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
			HttpUtil.sendJson(exchange, 405, JsonWriter.errorObject("POSTのみ受け付けます"));
			return;
		}

		Map<String, String> params = HttpUtil.parseQuery(HttpUtil.readBody(exchange.getRequestBody()));
		String roomNumber = params.get("roomNumber");
		PaymentMethod method = parsePaymentMethod(params.get("method"));

		if (method == null) {
			HttpUtil.sendJson(exchange, 400, JsonWriter.errorObject("支払い方法が不正です"));
			return;
		}

		try {
			ManagerFactory.getInstance().getCheckOutManager().checkOut(roomNumber, method, new Staff("受付係"));
			HttpUtil.sendJson(exchange, 200, new JsonWriter().put("status", "ok").toString());
		}
		catch (HrsException e) {
			HttpUtil.sendJson(exchange, 409, JsonWriter.errorObject(e.getMessage()));
		}
	}

	private PaymentMethod parsePaymentMethod(String s) {
		if ("1".equals(s)) {
			return PaymentMethod.CASH;
		}
		if ("2".equals(s)) {
			return PaymentMethod.CREDIT;
		}
		return null;
	}
}
