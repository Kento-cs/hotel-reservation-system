package web;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * 指定した静的HTMLファイル（static配下）を配信する
 */
public class StaticFileHandler implements HttpHandler {

	private final Path indexFile;

	public StaticFileHandler(String fileName) {
		this.indexFile = Paths.get("static", fileName);
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if (!Files.exists(indexFile)) {
			String message = indexFile + " が見つかりません。リポジトリのルートディレクトリで起動してください。";
			byte[] bytes = message.getBytes("UTF-8");
			exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
			exchange.sendResponseHeaders(404, bytes.length);
			OutputStream out = exchange.getResponseBody();
			out.write(bytes);
			out.close();
			return;
		}

		byte[] bytes = Files.readAllBytes(indexFile);
		exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
		exchange.sendResponseHeaders(200, bytes.length);
		OutputStream out = exchange.getResponseBody();
		out.write(bytes);
		out.close();
	}
}
