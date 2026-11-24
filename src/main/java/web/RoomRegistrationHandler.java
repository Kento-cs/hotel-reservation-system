package web;

import java.io.IOException;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import application.Env;
import application.HrsException;
import application.ManagerFactory;
import application.entity.SmokingType;

/**
 * POST /api/rooms : 部屋情報を登録する（UC6, アクタ: HRS管理者）
 */
public class RoomRegistrationHandler implements HttpHandler {

	private static final String ADMIN_PASSWORD = Env.get("ADMIN_PASSWORD", "password");

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
			HttpUtil.sendJson(exchange, 405, JsonWriter.errorObject("POSTのみ受け付けます"));
			return;
		}

		Map<String, String> params = HttpUtil.parseQuery(HttpUtil.readBody(exchange.getRequestBody()));
		String roomNumber = params.get("roomNumber");
		String typeName = params.get("typeName");
		Integer price = HttpUtil.parseIntOrNull(params, "price");
		SmokingType smokingType = parseSmokingType(params.get("smoking"));
		String nickname = params.get("nickname");
		if (nickname != null && nickname.trim().isEmpty()) {
			nickname = null;
		}
		
		String password = params.get("password");
		if (!ADMIN_PASSWORD.equals(password)) {
			HttpUtil.sendJson(exchange, 401, JsonWriter.errorObject("パスワードが間違っています"));
			return;
		}

		if (roomNumber == null || roomNumber.isEmpty() || typeName == null || price == null || smokingType == null) {
			HttpUtil.sendJson(exchange, 400, JsonWriter.errorObject("入力が不正です"));
			return;
		}
		if (Validation.tooLong(roomNumber, 20)) {
			HttpUtil.sendJson(exchange, 400, JsonWriter.errorObject("部屋番号は20文字以内で入力してください"));
			return;
		}
		if (Validation.tooLong(nickname, 50)) {
			HttpUtil.sendJson(exchange, 400, JsonWriter.errorObject("部屋の愛称は50文字以内で入力してください"));
			return;
		}
		if (Validation.outOfRange(price, 0, 100000)) {
			HttpUtil.sendJson(exchange, 400, JsonWriter.errorObject("値段は0円以上100,000円以下で入力してください"));
			return;
		}

		try {
			ManagerFactory.getInstance().getRoomRegistrationManager()
					.registerRoom(roomNumber, typeName, price, smokingType, nickname);
			HttpUtil.sendJson(exchange, 200, new JsonWriter().put("status", "ok").toString());
		}
		catch (HrsException e) {
			HttpUtil.sendJson(exchange, 409, JsonWriter.errorObject(e.getMessage()));
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
