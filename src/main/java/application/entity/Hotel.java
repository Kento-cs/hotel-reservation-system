package application.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * ホテル（部屋を合成集約で保持する）
 */
public class Hotel {

	private final String name;
	private final List<Room> rooms = new ArrayList<Room>();

	public Hotel(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void addRoom(Room room) {
		rooms.add(room);
	}

	public List<Room> getRooms() {
		return rooms;
	}
}
