package application;

/**
 * ホテル予約システムの業務例外（該当なし、状態不整合など）
 */
public class HrsException extends Exception {

	public HrsException(String message) {
		super(message);
	}
}
