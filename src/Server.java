import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


class Server extends JFrame implements Runnable {

    private static final long serialVersionUID = 1L;

    static final int SERVER_PORT = 25000;

    public static void main(String [] args){
        new Server();
    }



    public Server(){
        new Thread(this).start();
    }


    public void run() {
        boolean socket_created = false;

        try (ServerSocket serwer = new ServerSocket(SERVER_PORT)) {
            String host = InetAddress.getLocalHost().getHostName();
            System.out.println("Serwer został uruchomiony na hoscie " + host);
            socket_created = true;
            

            while (true) { 
                Socket socket = serwer.accept();
                if (socket != null) {
                    new ClientThread(this, socket);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
            if (!socket_created) {
                JOptionPane.showMessageDialog(null, "Gniazdko dla serwera nie może być utworzone");
                System.exit(0);
            } else {
                JOptionPane.showMessageDialog(null, "BLAD SERWERA: Nie mozna polaczyc sie z klientem ");
            }
        }
    }



} // koniec klasy MyServer



class ClientThread implements Runnable {
    private Socket socket;
    private String name;
    private Server server;

    private ObjectOutputStream outputStream = null;

    
    ClientThread(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        new Thread(this).start();  // Utworzenie dodatkowego watka
        // do obslugi komunikacji sieciowej
    }

    public String getName(){ return name; }

    public String toString(){ return name; }


    public void run(){
        String message;
        try( ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream()); )
        {
            outputStream = output;
            name = (String)input.readObject();
            while(true){
                message = (String)input.readObject();
                if (message.equals("bye")){
                    break;
                }
            }
            socket.close();
            socket = null;
        } catch(Exception e) {
           e.printStackTrace();
        }
    }

} // koniec klasy ClientThread


