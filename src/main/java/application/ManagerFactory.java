package application;

import application.control.CheckInManager;
import application.control.CheckOutManager;
import application.control.OccupancyManager;
import application.control.ReservationCancelManager;
import application.control.ReservationConfirmManager;
import application.control.ReservationManager;
import application.control.RoomEditManager;
import application.control.RoomRegistrationManager;
import application.control.RoomSearchManager;

/**
 * 各Managerクラスのインスタンスを提供するファクトリ
 */
public class ManagerFactory {

	private static final ManagerFactory instance = new ManagerFactory();

	private final RoomSearchManager roomSearchManager = new RoomSearchManager();
	private final ReservationManager reservationManager = new ReservationManager();
	private final ReservationConfirmManager reservationConfirmManager = new ReservationConfirmManager();
	private final ReservationCancelManager reservationCancelManager = new ReservationCancelManager();
	private final CheckInManager checkInManager = new CheckInManager();
	private final CheckOutManager checkOutManager = new CheckOutManager();
	private final RoomRegistrationManager roomRegistrationManager = new RoomRegistrationManager();
	private final RoomEditManager roomEditManager = new RoomEditManager();
	private final OccupancyManager occupancyManager = new OccupancyManager();

	private ManagerFactory() {
	}

	public static ManagerFactory getInstance() {
		return instance;
	}

	public RoomSearchManager getRoomSearchManager() {
		return roomSearchManager;
	}

	public ReservationManager getReservationManager() {
		return reservationManager;
	}

	public ReservationConfirmManager getReservationConfirmManager() {
		return reservationConfirmManager;
	}

	public ReservationCancelManager getReservationCancelManager() {
		return reservationCancelManager;
	}

	public CheckInManager getCheckInManager() {
		return checkInManager;
	}

	public CheckOutManager getCheckOutManager() {
		return checkOutManager;
	}

	public RoomRegistrationManager getRoomRegistrationManager() {
		return roomRegistrationManager;
	}

	public RoomEditManager getRoomEditManager() {
		return roomEditManager;
	}

	public OccupancyManager getOccupancyManager() {
		return occupancyManager;
	}
}
