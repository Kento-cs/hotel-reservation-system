package userinterface;

import java.io.BufferedReader;
import java.io.IOException;

import application.HrsException;
import application.ManagerFactory;
import application.entity.PaymentMethod;
import application.entity.Reservation;
import application.entity.Staff;

/**
 * チェックアウト画面
 */
public class CheckOutScreen {

	private final BufferedReader reader;

	public CheckOutScreen(BufferedReader reader) {
		this.reader = reader;
	}

	/**
	 * 1: 部屋番号を入力する() -&gt; 2: 清算要求() -&gt; 3: 宿泊料を計算して表示するまたは該当なしの警告() -&gt;
	 * 4: 支払い要求() -&gt; 6: 支払い方法を選択する() -&gt; 5: チェックアウト完了を表示する()
	 */
	public void execute() throws IOException {
		System.out.println("部屋番号を入力してください");
		System.out.print("> ");
		String roomNumber = reader.readLine();

		Reservation reservation;
		try {
			reservation = ManagerFactory.getInstance().getCheckOutManager().calculateFee(roomNumber);
		}
		catch (HrsException e) {
			System.out.println("警告: " + e.getMessage());
			return;
		}

		System.out.println("宿泊料: " + reservation.getStayFee() + "円");
		System.out.println("支払い方法を選択してください（1: 現金, 2: クレジット）");
		System.out.print("> ");
		PaymentMethod method = parsePaymentMethod(reader.readLine());
		if (method == null) {
			System.out.println("入力が不正です");
			return;
		}

		try {
			ManagerFactory.getInstance().getCheckOutManager().checkOut(roomNumber, method, new Staff("受付係"));
			System.out.println("チェックアウトが完了しました");
		}
		catch (HrsException e) {
			System.out.println("警告: " + e.getMessage());
		}
	}

	private PaymentMethod parsePaymentMethod(String s) {
		if (s == null) {
			return null;
		}
		String trimmed = s.trim();
		if ("1".equals(trimmed)) {
			return PaymentMethod.CASH;
		}
		if ("2".equals(trimmed)) {
			return PaymentMethod.CREDIT;
		}
		return null;
	}
}
