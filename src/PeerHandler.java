import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class PeerHandler implements Runnable {
    private Socket socket;
    private Set<String> processedMessages; // 처리된 메시지를 저장하는 Set


    public PeerHandler(Socket socket) throws IOException {
        this.socket = socket;
    }

    public void run() {
        try (BufferedReader peerReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            while (!socket.isClosed()) {
                // 처리할 내용
            }

            System.out.println("Connection closed by peer.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleMessages(Socket socket) {

        try
        {
            BufferedReader peerReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String message = peerReader.readLine();
                if (message == null) break;
                System.out.println(message);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally {

        }
    }

    public void sendMessage(Set<Socket> uniquePeers, String message)
    {
        for (Socket socket : uniquePeers) {
            try {
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                printWriter.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
