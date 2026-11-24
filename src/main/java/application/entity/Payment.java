package application.entity;

/**
 * 支払い情報
 */
public class Payment {

	private final Reservation reservation;
	private final int amount;
	private PaymentMethod method;
	private PaymentStatus status;
	private Staff handledBy;

	public Payment(Reservation reservation, int amount) {
		this.reservation = reservation;
		this.amount = amount;
		this.status = PaymentStatus.UNPAID;
	}

	public Reservation getReservation() {
		return reservation;
	}

	public int getAmount() {
		return amount;
	}

	public PaymentMethod getMethod() {
		return method;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public Staff getHandledBy() {
		return handledBy;
	}

	public void pay(PaymentMethod method, Staff handledBy) {
		this.method = method;
		this.handledBy = handledBy;
		this.status = PaymentStatus.PAID;
	}
}
