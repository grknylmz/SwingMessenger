import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by gurkanyilmaz on 19/08/15.
 */
public class ChatServer {
    static Vector ClientSockets;
    static Vector LoginNames;

    ChatServer() throws IOException {
        ServerSocket server = new ServerSocket(5217);
        ClientSockets = new Vector();
        LoginNames = new Vector();

        while(true){
            Socket client = server.accept();
            AcceptClient acceptClient = new AcceptClient(client);
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.print("Chat Server is running...");
        ChatServer chatServer = new ChatServer();

    }


    class AcceptClient extends Thread {
        Socket ClientSocket;
        DataInputStream din;
        DataOutputStream dout;
        AcceptClient(Socket client) throws IOException {
            ClientSocket = client;
            din = new DataInputStream(ClientSocket.getInputStream());
            dout = new DataOutputStream(ClientSocket.getOutputStream());

            String loginName = din.readUTF();
            LoginNames.add(loginName);
            ClientSockets.add(ClientSocket);


            start();
        }
        public void run(){
            while (true){
                String msgFromClient = null;
                StringTokenizer st = new StringTokenizer(msgFromClient);
                String LoginName = st.nextToken();
                String msgType = st.nextToken();
                int Io = -1;
                String msg = "";

                while (st.hasMoreTokens()){
                    msg = msg + " " + st.nextToken();
                }

                try {
                    if(msgType.equals("LOGIN")){
                        for(int i = 0 ; i < LoginNames.size() ; i++){
                            Socket pSocket = (Socket) ClientSockets.elementAt(i);
                            DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
                            pOut.writeUTF(LoginName + " has logged in.");
                        }
                    }


                    else if(msgType.equals("LOGOUT")){

                        for(int i = 0 ; i < LoginNames.size() ; i++){
                            if(LoginName == LoginNames.elementAt(i))
                                Io = i;

                            Socket pSocket = (Socket) ClientSockets.elementAt(i);
                            DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
                            pOut.writeUTF(LoginName + " has logged out.");

                        }
                        if(Io >= 0 ) {
                            LoginNames.removeElementAt(Io);
                            ClientSockets.removeElementAt(Io);
                        }
                    }
                    else {

                        for(int i = 0 ; i < LoginNames.size() ; i++){
                            Socket pSocket = (Socket) ClientSockets.elementAt(i);
                            DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
                            pOut.writeUTF(LoginName + msg);
                        }
                    }

                    msgFromClient = din.readUTF();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
