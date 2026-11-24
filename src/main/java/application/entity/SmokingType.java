package application.entity;

/**
 * 喫煙区分
 */
public enum SmokingType {
	SMOKING("喫煙"),
	NON_SMOKING("禁煙");

	private final String label;

	SmokingType(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
