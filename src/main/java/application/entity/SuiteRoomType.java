package application.entity;

/**
 * スイート（部屋種別のサブタイプ、2人部屋）
 */
public class SuiteRoomType extends RoomType {

	private static final int CAPACITY = 2;

	public SuiteRoomType(int id, int price, SmokingType smokingType) {
		super(id, "スイート", price, smokingType, CAPACITY);
	}
}
