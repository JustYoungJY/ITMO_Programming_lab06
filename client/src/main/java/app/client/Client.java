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

/**
 * The client uses non-blocking mode to send a request and receive a response.
 */
public class Client {
    private final String host;
    private final int port;
    private final Gson gson;
    private SocketChannel socketChannel;

    private static final int MAX_RECONNECT_ATTEMPTS = 5;
    private static final int RECONNECT_DELAY_MILLIS = 5000;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        this.gson = new GsonBuilder().create();

        // connecting to the server or throwing an error
        try {
            connect();
        } catch (IOException e) {
            throw new RuntimeException("Failed to connect to server initially", e);
        }
    }

    private void connect() throws IOException {
        // If the old channel is not closed, then we will close it.
        if (socketChannel != null && socketChannel.isOpen()) {
            socketChannel.close();
        }
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(host, port));

        while (!socketChannel.finishConnect()) {
            //...
        }
        System.out.println("Connected to server at " + host + ":" + port);
    }

    public Response sendRequest(Request request) throws IOException {
        // Serialize request to JSON bytes
        byte[] dataBytes = NetworkUtil.toJson(request).getBytes(StandardCharsets.UTF_8);

        // Prepare length prefix
        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        lengthBuffer.putInt(dataBytes.length);
        lengthBuffer.flip();

        // Data buffer for JSON payload
        ByteBuffer dataBuffer = ByteBuffer.wrap(dataBytes);

        int attempt = 0; // reconnect attempt counter
        while (true) {
            try {
                // Send length prefix
                writeFully(lengthBuffer);
                // Send data payload
                writeFully(dataBuffer);

                return receiveResponse();
            } catch (IOException e) {
                attempt++;
                if (attempt > MAX_RECONNECT_ATTEMPTS) {
                    throw new IOException("Failed to reconnect after " + MAX_RECONNECT_ATTEMPTS + " attempts", e);
                }
                System.err.println("Connection lost or server unavailable: " + e.getMessage());
                System.out.println("Attempting to reconnect (" + attempt + "/" + MAX_RECONNECT_ATTEMPTS + ")...");
                try {
                    Thread.sleep(RECONNECT_DELAY_MILLIS);
                    connect();
                    System.out.println("Reconnected. Resending the request...");

                    // After successful reconnect, reset buffers and retry
                    lengthBuffer.rewind();
                    dataBuffer.rewind();
                } catch (IOException re) {
                    System.err.println("Reconnect attempt failed: " + re.getMessage());
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IOException("Reconnect interrupted", ie);
                }
            }
        }
    }

    // Write all bytes in buffer to channel
    private void writeFully(ByteBuffer buffer) throws IOException {
        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }

    // Receive a length-prefixed response
    private Response receiveResponse() throws IOException {
        ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
        readFromChannel(lengthBuffer);
        lengthBuffer.flip();

        int messageLength = lengthBuffer.getInt();
        ByteBuffer dataBuffer = ByteBuffer.allocate(messageLength);

        readFromChannel(dataBuffer);
        dataBuffer.flip();

        // Decode JSON and deserialize
        String jsonResponse = StandardCharsets.UTF_8.decode(dataBuffer).toString();
        return NetworkUtil.fromJson(jsonResponse, Response.class);
    }

    // Helper to read the full buffer from channel
    private void readFromChannel(ByteBuffer buffer) throws IOException {
        while (buffer.hasRemaining()) {
            int bytesRead = socketChannel.read(buffer);
            if (bytesRead == -1) {
                throw new IOException("Server has closed the connection");
            }
        }
    }

    public void close() {
        try {
            socketChannel.close();
        } catch (IOException e) {
            System.err.println("Error closing client: " + e.getMessage());
        }
    }
}