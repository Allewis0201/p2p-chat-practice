import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkCommunication extends Thread {
    private ServerSocket serverSocket;
    private Set<Socket> connectedPeers;
    private PeerHandler peerHandler;

    private BufferedReader bufferedReader;

    public NetworkCommunication(String portNumber) throws IOException {
        serverSocket = new ServerSocket(Integer.valueOf(portNumber));
        connectedPeers = new HashSet<>();
    }

    public void run() {

        try (ServerSocket ss = serverSocket) {
            while (true) {
                if (!ss.isClosed()) {
                    Socket socket = ss.accept();
                    peerHandler = new PeerHandler(socket);
                    connectedPeers.add(socket);
                    // 기존 스레드에서 메시지를 처리하도록 함
                    peerHandler.handleMessages(socket);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectToPeer(String hostname, int portNumber) {
        try {
            // 이미 연결된 소켓인지 확인
            boolean isConnected = false;
            for (Socket socket : connectedPeers) {
                if (socket.getInetAddress().getHostName().equals(hostname) && socket.getPort() == portNumber) {
                    System.out.println("Already connected to peer: " + hostname + ":" + portNumber);
                    isConnected = true;
                    break;
                }
            }

            if (!isConnected) {
                // 새로운 소켓을 생성하여 연결
                Socket socket = new Socket(hostname, portNumber);
                connectedPeers.add(socket);
                new Thread(new PeerHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {

        Set<Socket> uniquePeers = new HashSet<>(connectedPeers); // 중복 제거를 위해 Set 활용

        peerHandler.sendMessage(uniquePeers, message);

    }

    public void close() throws IOException {
        serverSocket.close();
        for (Socket socket : connectedPeers) {
            socket.close();
        }
    }

    // connectedPeers에 대한 getter 메서드
    public Set<Socket> getConnectedPeers() {
        return connectedPeers;
    }
}
