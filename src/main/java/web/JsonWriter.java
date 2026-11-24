package web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * APIレスポンス用の最小限のJSON組み立てヘルパー
 */
public class JsonWriter {

	private final Map<String, String> fields = new LinkedHashMap<String, String>();

	public JsonWriter put(String key, String value) {
		fields.put(key, value == null ? "null" : "\"" + escape(value) + "\"");
		return this;
	}

	public JsonWriter put(String key, int value) {
		fields.put(key, String.valueOf(value));
		return this;
	}

	public JsonWriter put(String key, boolean value) {
		fields.put(key, String.valueOf(value));
		return this;
	}

	public JsonWriter putRaw(String key, String rawJson) {
		fields.put(key, rawJson);
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{");
		boolean first = true;
		for (Map.Entry<String, String> entry : fields.entrySet()) {
			if (!first) {
				sb.append(",");
			}
			first = false;
			sb.append("\"").append(escape(entry.getKey())).append("\":").append(entry.getValue());
		}
		sb.append("}");
		return sb.toString();
	}

	public static String array(List<JsonWriter> items) {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < items.size(); i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(items.get(i).toString());
		}
		sb.append("]");
		return sb.toString();
	}

	public static String errorObject(String message) {
		return new JsonWriter().put("error", message).toString();
	}

	public static String escape(String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
				case '"':
					sb.append("\\\"");
					break;
				case '\\':
					sb.append("\\\\");
					break;
				case '\n':
					sb.append("\\n");
					break;
				case '\r':
					sb.append("\\r");
					break;
				case '\t':
					sb.append("\\t");
					break;
				default:
					if (c < 0x20) {
						sb.append(String.format("\\u%04x", (int) c));
					}
					else {
						sb.append(c);
					}
			}
		}
		return sb.toString();
	}
}
