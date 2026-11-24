package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import application.DataStore;
import application.entity.Room;

/**
 * GET /api/rooms/list : 登録済みの部屋一覧を返す（HRS管理者、部屋一覧・編集画面用）
 */
public class RoomListHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		List<Room> rooms = DataStore.getInstance().getHotel().getRooms();

		List<JsonWriter> items = new ArrayList<JsonWriter>();
		for (Room room : rooms) {
			items.add(new JsonWriter()
					.put("roomNumber", room.getRoomNumber())
					.put("typeName", room.getRoomType().getTypeName())
					.put("price", room.getRoomType().getPrice())
					.put("smokingLabel", room.getRoomType().getSmokingType().getLabel())
					.put("nickname", room.getNickname())
					.put("occupied", room.isOccupied()));
		}
		HttpUtil.sendJson(exchange, 200, JsonWriter.array(items));
	}
}
