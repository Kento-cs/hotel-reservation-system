package application.entity;

/**
 * 利用者
 */
public class Guest {

	private final int id;
	private String name;
	private String contact;

	public Guest(int id, String name, String contact) {
		this.id = id;
		this.name = name;
		this.contact = contact;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getContact() {
		return contact;
	}
}
