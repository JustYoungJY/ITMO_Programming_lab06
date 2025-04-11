package app.client;

import app.network.NetworkUtil;
import app.transfer.Request;
import app.transfer.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;


public class Client {
    private final String host;
    private final int port;
    private SocketChannel socketChannel;
    private final Gson gson;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        // Создаем Gson с адаптером для ZonedDateTime уже зарегистрированным в NetworkUtil
        this.gson = new GsonBuilder().create();
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(host, port));
            while (!socketChannel.finishConnect()) {
                // Можно добавить небольшую задержку, если нужно
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to connect to server", e);
        }
    }

    public Response sendRequest(Request request) throws IOException {
        String jsonRequest = NetworkUtil.toJson(request) + "\n";
        ByteBuffer writeBuffer = ByteBuffer.wrap(jsonRequest.getBytes(StandardCharsets.UTF_8));
        while (writeBuffer.hasRemaining()) {
            socketChannel.write(writeBuffer);
        }
        ByteBuffer readBuffer = ByteBuffer.allocate(4096);
        int bytesRead = socketChannel.read(readBuffer);
        // Простая блокирующая задержка: ждем, пока сервер ответит
        while (bytesRead == 0) {
            bytesRead = socketChannel.read(readBuffer);
        }
        readBuffer.flip();
        String jsonResponse = StandardCharsets.UTF_8.decode(readBuffer).toString().trim();
        return NetworkUtil.fromJson(jsonResponse, Response.class);
    }

    public void close() {
        try {
            socketChannel.close();
        } catch (IOException e) {
            System.err.println("Error closing client: " + e.getMessage());
        }
    }
}