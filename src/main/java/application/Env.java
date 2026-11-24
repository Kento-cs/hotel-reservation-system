package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * リポジトリ直下の .env ファイル（KEY=VALUE形式）から設定値を読み込むユーティリティ。<br>
 * .env はGit管理対象外（秘密情報用）。キーの一覧は .env.example を参照。
 */
public class Env {

	private static final Map<String, String> VALUES = new HashMap<String, String>();

	static {
		load();
	}

	private Env() {
	}

	private static void load() {
		File file = new File(".env");
		if (!file.exists()) {
			System.out.println(".env が見つかりません。.env.example を参考に .env を作成してください（既定値で動作します）。");
			return;
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			try {
				String line;
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (line.isEmpty() || line.startsWith("#")) {
						continue;
					}
					int index = line.indexOf('=');
					if (index < 0) {
						continue;
					}
					String key = line.substring(0, index).trim();
					String value = line.substring(index + 1).trim();
					VALUES.put(key, value);
				}
			}
			finally {
				reader.close();
			}
		}
		catch (IOException e) {
			throw new RuntimeException(".envの読み込みに失敗しました", e);
		}
	}

	public static String get(String key, String defaultValue) {
		String value = VALUES.get(key);
		return value != null ? value : defaultValue;
	}
}
