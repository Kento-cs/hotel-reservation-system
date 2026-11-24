package web;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

/**
 * ホテル予約システムのWebサーバー（フロントエンドはstatic/index.html）
 */
public class HrsHttpServer {

	private static final int PORT = 8080;

	public static void main(String[] args) throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

		server.createContext("/", new StaticFileHandler("index.html"));
		server.createContext("/admin", new StaticFileHandler("admin.html"));
		server.createContext("/api/hotel", new HotelInfoHandler());
		server.createContext("/api/room-types/search", new RoomSearchHandler());
		server.createContext("/api/reservations/cancel", new ReservationCancelHandler());
		server.createContext("/api/reservations", new ReservationsRouter());
		server.createContext("/api/checkins", new CheckInHandler());
		server.createContext("/api/checkouts/fee", new CheckOutFeeHandler());
		server.createContext("/api/checkouts", new CheckOutHandler());
		server.createContext("/api/rooms/list", new RoomListHandler());
		server.createContext("/api/rooms/edit", new RoomEditHandler());
		server.createContext("/api/rooms", new RoomRegistrationHandler());
		server.createContext("/api/occupancy", new OccupancyHandler());
		server.createContext("/api/availability", new AvailabilityCalendarHandler());

		server.setExecutor(null);
		server.start();

		System.out.println("HRS Webサーバーを起動しました: http://localhost:" + PORT + "/");
	}
}
