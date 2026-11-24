package application;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.dao.GuestDao;
import application.dao.PaymentDao;
import application.dao.ReservationDao;
import application.dao.RoomDao;
import application.dao.RoomTypeDao;
import application.entity.Guest;
import application.entity.Hotel;
import application.entity.Payment;
import application.entity.Reservation;
import application.entity.Room;
import application.entity.RoomType;
import application.entity.ReservationStatus;
import application.entity.SmokingType;
import application.entity.StandardRoomType;
import application.entity.SuiteRoomType;
import util.DateUtil;

/**
 * 全エンティティを保持する共有データストア。<br>
 * 起動時にHSQLDB（db/hrsdb）から読み込み、以後の変更はDAO経由でDBにも書き込む。
 */
public class DataStore {

	private static final DataStore instance = new DataStore();

	private final RoomTypeDao roomTypeDao = new RoomTypeDao();
	private final RoomDao roomDao = new RoomDao();
	private final GuestDao guestDao = new GuestDao();
	private final ReservationDao reservationDao = new ReservationDao();
	private final PaymentDao paymentDao = new PaymentDao();

	private final Hotel hotel = new Hotel("早稲田ホテル");
	private final List<RoomType> roomTypes = new ArrayList<RoomType>();
	private final Map<String, Reservation> reservationsByNumber = new HashMap<String, Reservation>();
	private final Map<String, Payment> paymentsByReservationNumber = new HashMap<String, Payment>();

	private DataStore() {
		load();
	}

	public static DataStore getInstance() {
		return instance;
	}

	private void load() {
		roomTypes.addAll(roomTypeDao.findAll());
		if (roomTypes.isEmpty()) {
			seed();
			return;
		}

		Map<Integer, RoomType> roomTypesById = new HashMap<Integer, RoomType>();
		for (RoomType roomType : roomTypes) {
			roomTypesById.put(roomType.getId(), roomType);
		}

		for (Room room : roomDao.findAll(roomTypesById)) {
			hotel.addRoom(room);
		}
		for (Reservation reservation : reservationDao.findAll(roomTypesById)) {
			reservationsByNumber.put(reservation.getReservationNumber(), reservation);
		}
		for (Payment payment : paymentDao.findAll(reservationsByNumber)) {
			paymentsByReservationNumber.put(payment.getReservation().getReservationNumber(), payment);
		}
	}

	/**
	 * DBが空（初回起動）の場合の初期データ。DBにも書き込む。<br>
	 * スタンダード・スイートそれぞれに喫煙／禁煙の2種別を用意する（禁煙は喫煙より1,000円高い）。
	 */
	private void seed() {
		RoomType standardSmoking = new StandardRoomType(roomTypeDao.nextId(), 8000, SmokingType.SMOKING);
		roomTypeDao.insert(standardSmoking);
		roomTypes.add(standardSmoking);

		RoomType standardNonSmoking = new StandardRoomType(roomTypeDao.nextId(), 9000, SmokingType.NON_SMOKING);
		roomTypeDao.insert(standardNonSmoking);
		roomTypes.add(standardNonSmoking);

		RoomType suiteSmoking = new SuiteRoomType(roomTypeDao.nextId(), 12000, SmokingType.SMOKING);
		roomTypeDao.insert(suiteSmoking);
		roomTypes.add(suiteSmoking);

		RoomType suiteNonSmoking = new SuiteRoomType(roomTypeDao.nextId(), 13000, SmokingType.NON_SMOKING);
		roomTypeDao.insert(suiteNonSmoking);
		roomTypes.add(suiteNonSmoking);

		String[] nicknames = { "さくら", "つばき", "うめ", "ふじ", "あおい", "もも", "すずらん", "ゆり", "かえで", "きく",
				"つつじ", "ぼたん", "すみれ", "あじさい", "はぎ", "すいせん", "ばら", "ゆきやなぎ", "かすみそう", "りんどう",
				"こすもす", "ひまわり", "あさがお", "もくれん", "すずかけ", "かきつばた", "なでしこ", "きんもくせい", "さざんか", "やまぶき" };
		int n = 0;

		for (int roomNumber = 101; roomNumber <= 110; roomNumber++) {
			createRoom(new Room(String.valueOf(roomNumber), standardSmoking, nicknames[n++]));
		}
		for (int roomNumber = 111; roomNumber <= 120; roomNumber++) {
			createRoom(new Room(String.valueOf(roomNumber), standardNonSmoking, nicknames[n++]));
		}
		for (int roomNumber = 201; roomNumber <= 205; roomNumber++) {
			createRoom(new Room(String.valueOf(roomNumber), suiteSmoking, nicknames[n++]));
		}
		for (int roomNumber = 206; roomNumber <= 210; roomNumber++) {
			createRoom(new Room(String.valueOf(roomNumber), suiteNonSmoking, nicknames[n++]));
		}
	}

	public Hotel getHotel() {
		return hotel;
	}

	public List<RoomType> getRoomTypes() {
		return roomTypes;
	}

	public RoomType getRoomTypeById(int id) {
		for (RoomType roomType : roomTypes) {
			if (roomType.getId() == id) {
				return roomType;
			}
		}
		return null;
	}

	public int nextRoomTypeId() {
		return roomTypeDao.nextId();
	}

	public void createRoomType(RoomType roomType) {
		roomTypeDao.insert(roomType);
		roomTypes.add(roomType);
	}

	/**
	 * 指定した部屋種別・日付の空席数を計算する。<br>
	 * 総部屋数 - その日付でCANCELED・CHECKED_OUT以外の予約数。
	 * （チェックアウト済みの予約は部屋が既に空いているため、空室数の計算から除外する）
	 */
	public int countTotalRooms(RoomType roomType) {
		int total = 0;
		for (Room room : hotel.getRooms()) {
			if (room.getRoomType() == roomType) {
				total++;
			}
		}
		return total;
	}

	public int countAvailableRooms(RoomType roomType, Date date) {
		int total = countTotalRooms(roomType);

		String targetDate = DateUtil.convertToString(date);
		int reserved = 0;
		for (Reservation reservation : reservationsByNumber.values()) {
			if (reservation.getRoomType() != roomType) {
				continue;
			}
			if (reservation.getStatus() == ReservationStatus.CANCELED
					|| reservation.getStatus() == ReservationStatus.CHECKED_OUT) {
				continue;
			}
			if (targetDate.equals(DateUtil.convertToString(reservation.getDate()))) {
				reserved++;
			}
		}
		return total - reserved;
	}

	public void createRoom(Room room) {
		roomDao.insert(room);
		hotel.addRoom(room);
	}

	public Room getRoomByNumber(String roomNumber) {
		for (Room room : hotel.getRooms()) {
			if (room.getRoomNumber().equals(roomNumber)) {
				return room;
			}
		}
		return null;
	}

	public void updateRoom(Room room) {
		roomDao.update(room);
	}

	public void saveRoomOccupancy(Room room) {
		roomDao.updateOccupancy(room);
	}

	public int nextGuestId() {
		return guestDao.nextId();
	}

	public void createGuest(Guest guest) {
		guestDao.insert(guest);
	}

	public Map<String, Reservation> getReservationsByNumber() {
		return reservationsByNumber;
	}

	public void createReservation(Reservation reservation) {
		reservationDao.insert(reservation);
		reservationsByNumber.put(reservation.getReservationNumber(), reservation);
	}

	public void saveReservation(Reservation reservation) {
		reservationDao.update(reservation);
	}

	public Map<String, Payment> getPaymentsByReservationNumber() {
		return paymentsByReservationNumber;
	}

	public void createPayment(Payment payment) {
		paymentDao.insert(payment);
		paymentsByReservationNumber.put(payment.getReservation().getReservationNumber(), payment);
	}
}
