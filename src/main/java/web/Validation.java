package web;

/**
 * 入力値の長さ・範囲チェック（DBスキーマの制約に対応する軽量バリデータ）
 */
public class Validation {

	public static boolean tooLong(String value, int maxLength) {
		return value != null && value.length() > maxLength;
	}

	public static boolean outOfRange(int value, int min, int max) {
		return value < min || value > max;
	}
}
