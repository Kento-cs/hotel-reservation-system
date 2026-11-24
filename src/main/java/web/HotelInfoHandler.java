package web;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import application.DataStore;

/**
 * GET /api/hotel : ホテル名などの基本情報を取得する
 */
public class HotelInfoHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String json = new JsonWriter()
				.put("name", DataStore.getInstance().getHotel().getName())
				.toString();
		HttpUtil.sendJson(exchange, 200, json);
	}
}
