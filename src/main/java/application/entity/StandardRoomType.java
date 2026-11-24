package application.entity;

/**
 * スタンダード（部屋種別のサブタイプ、1人部屋）
 */
public class StandardRoomType extends RoomType {

	private static final int CAPACITY = 1;

	public StandardRoomType(int id, int price, SmokingType smokingType) {
		super(id, "スタンダード", price, smokingType, CAPACITY);
	}
}
