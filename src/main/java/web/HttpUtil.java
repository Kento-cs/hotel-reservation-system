package web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

/**
 * クエリ文字列・フォームボディの解析ヘルパー
 */
public class HttpUtil {

	public static void sendJson(HttpExchange exchange, int statusCode, String json) throws IOException {
		byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
		exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
		exchange.sendResponseHeaders(statusCode, bytes.length);
		OutputStream out = exchange.getResponseBody();
		out.write(bytes);
		out.close();
	}

	public static Map<String, String> parseQuery(String query) {
		Map<String, String> result = new HashMap<String, String>();
		if (query == null || query.isEmpty()) {
			return result;
		}
		for (String pair : query.split("&")) {
			int idx = pair.indexOf('=');
			if (idx < 0) {
				continue;
			}
			String key = decode(pair.substring(0, idx));
			String value = decode(pair.substring(idx + 1));
			result.put(key, value);
		}
		return result;
	}

	public static String readBody(InputStream in) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}

	public static Integer parseIntOrNull(Map<String, String> params, String key) {
		String value = params.get(key);
		if (value == null || value.trim().isEmpty()) {
			return null;
		}
		try {
			return Integer.valueOf(value.trim());
		}
		catch (NumberFormatException e) {
			return null;
		}
	}

	public static int parseIntOrDefault(Map<String, String> params, String key, int defaultValue) {
		Integer value = parseIntOrNull(params, key);
		return value == null ? defaultValue : value;
	}

	private static String decode(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		}
		catch (Exception e) {
			return s;
		}
	}
}
