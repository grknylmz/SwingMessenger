import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by gurkanyilmaz on 19/08/15.
 */
public class ChatClient extends JFrame implements Runnable{
    Socket socket;
    JTextArea ta;
    JButton send, logout;
    JTextField tf;


    Thread thread;

    DataInputStream din ;
    DataOutputStream dout;

    String loginName;


    public ChatClient( String login) throws IOException {
        super(login);
        this.loginName = login;

        ta = new JTextArea(18, 50);
        tf = new JTextField(50);


        send = new JButton("Send");
        logout = new JButton("Logout");
        socket = new Socket("localhost" , 5217);



        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dout.writeUTF(loginName + " " + "DATA" + tf.getText().toString());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });


        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    dout.writeUTF(loginName + " " + "LOGOUT");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });


        din = new DataInputStream(socket.getInputStream());
        dout = new DataOutputStream(socket.getOutputStream());

        dout.writeUTF(loginName);
        dout.writeUTF(loginName + " Logged in.");
        thread = new Thread(this);
        setup();
    }

    private void setup() {
        setSize(600, 400);
        JPanel panel = new JPanel();

        panel.add(new JScrollPane(ta));
        panel.add(tf);
        panel.add(send);
        panel.add(logout);
        add(panel);

        setVisible(true);
        setResizable(false);
    }

    @Override
    public void run(){
        while(true){
            try {
                ta.append("\n" + din.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) throws IOException {
        ChatClient client = new ChatClient("User1");
    }



}
