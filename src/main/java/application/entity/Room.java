package application.entity;

/**
 * 部屋
 */
public class Room {

	private final String roomNumber;
	private RoomType roomType;
	private String nickname;
	private boolean occupied;
	private String occupyingReservationNumber;

	public Room(String roomNumber, RoomType roomType) {
		this(roomNumber, roomType, null);
	}

	public Room(String roomNumber, RoomType roomType, String nickname) {
		this.roomNumber = roomNumber;
		this.roomType = roomType;
		this.nickname = nickname;
		this.occupied = false;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	public boolean isOccupied() {
		return occupied;
	}

	public void occupy(String reservationNumber) {
		this.occupied = true;
		this.occupyingReservationNumber = reservationNumber;
	}

	public void vacate() {
		this.occupied = false;
		this.occupyingReservationNumber = null;
	}

	public String getOccupyingReservationNumber() {
		return occupyingReservationNumber;
	}
}
