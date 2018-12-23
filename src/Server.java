import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;



class Server implements Runnable {

    static final int SERVER_PORT = 25000;

    private PhoneBook phoneBook;
    private boolean serverSocketAccepts = true;

    public void setServerSocketAccepts(boolean serverSocketAccepts) {
        this.serverSocketAccepts = serverSocketAccepts;
    }

    public Server(){
        phoneBook = new PhoneBook();
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
                    //ksiazka przekazywazna do konstruktora = do przemyslenia
                    new ClientThread(this, socket, phoneBook);
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




