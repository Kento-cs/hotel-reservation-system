package application.entity;

/**
 * 支払い方法
 */
public enum PaymentMethod {
	CASH("現金"),
	CREDIT("クレジット");

	private final String label;

	PaymentMethod(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
