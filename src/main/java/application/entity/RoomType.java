package application.entity;

/**
 * 部屋種別
 */
public abstract class RoomType {

	private final int id;
	private final String typeName;
	private int price;
	private SmokingType smokingType;
	private final int capacity;

	protected RoomType(int id, String typeName, int price, SmokingType smokingType, int capacity) {
		this.id = id;
		this.typeName = typeName;
		this.price = price;
		this.smokingType = smokingType;
		this.capacity = capacity;
	}

	public int getId() {
		return id;
	}

	public String getTypeName() {
		return typeName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public SmokingType getSmokingType() {
		return smokingType;
	}

	public void setSmokingType(SmokingType smokingType) {
		this.smokingType = smokingType;
	}

	public int getCapacity() {
		return capacity;
	}
}
