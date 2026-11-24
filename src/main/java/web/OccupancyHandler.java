package web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import application.DataStore;
import application.ManagerFactory;
import application.entity.Guest;
import application.entity.Reservation;
import application.entity.Room;

/**
 * GET /api/occupancy : 現在チェックイン中の部屋と宿泊者（名前・連絡先）の一覧を返す（HRS管理者）
 */
public class OccupancyHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		List<Room> occupiedRooms = ManagerFactory.getInstance().getOccupancyManager().getOccupiedRooms();

		List<JsonWriter> items = new ArrayList<JsonWriter>();
		for (Room room : occupiedRooms) {
			Reservation reservation = DataStore.getInstance().getReservationsByNumber()
					.get(room.getOccupyingReservationNumber());

			JsonWriter item = new JsonWriter()
					.put("roomNumber", room.getRoomNumber())
					.put("nickname", room.getNickname())
					.put("typeName", room.getRoomType().getTypeName())
					.put("smokingLabel", room.getRoomType().getSmokingType().getLabel());

			if (reservation != null) {
				Guest guest = reservation.getGuest();
				item.put("reservationNumber", reservation.getReservationNumber())
						.put("guestName", guest.getName())
						.put("guestContact", guest.getContact())
						.put("headCount", reservation.getHeadCount());
			}
			items.add(item);
		}
		HttpUtil.sendJson(exchange, 200, JsonWriter.array(items));
	}
}
