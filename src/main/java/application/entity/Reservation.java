package application.entity;

import java.util.Date;

/**
 * 予約
 */
public class Reservation {

	private final String reservationNumber;
	private final Date date;
	private final int headCount;
	private final Guest guest;
	private final RoomType roomType;
	private ReservationStatus status;
	private int stayFee;
	private String assignedRoomNumber;

	public Reservation(String reservationNumber, Date date, int headCount, Guest guest, RoomType roomType) {
		this.reservationNumber = reservationNumber;
		this.date = date;
		this.headCount = headCount;
		this.guest = guest;
		this.roomType = roomType;
		this.status = ReservationStatus.RESERVED;
		this.stayFee = roomType.getPrice();
	}

	public String getReservationNumber() {
		return reservationNumber;
	}

	public Date getDate() {
		return date;
	}

	public int getHeadCount() {
		return headCount;
	}

	public Guest getGuest() {
		return guest;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public ReservationStatus getStatus() {
		return status;
	}

	public void setStatus(ReservationStatus status) {
		this.status = status;
	}

	public int getStayFee() {
		return stayFee;
	}

	public void setStayFee(int stayFee) {
		this.stayFee = stayFee;
	}

	public String getAssignedRoomNumber() {
		return assignedRoomNumber;
	}

	public void setAssignedRoomNumber(String assignedRoomNumber) {
		this.assignedRoomNumber = assignedRoomNumber;
	}
}
