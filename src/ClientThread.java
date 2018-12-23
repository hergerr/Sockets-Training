import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class ClientThread implements Runnable {
    private Socket socket;
    private String name;
    private Server server;
    private PhoneBook phoneBook;

    private ObjectOutputStream outputStream = null;


    ClientThread(Server server, Socket socket, PhoneBook phoneBook) {
        this.phoneBook = phoneBook;
        this.server = server;
        this.socket = socket;
        new Thread(this).start();
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public void sendMessage(String message) {
        try {
            outputStream.writeObject(message);
            if (message.equals("bye")) {
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void run() {
        String message;
        try (ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream());) {
            outputStream = output;
            name = (String) input.readObject();

            while (true) {
                message = (String) input.readObject();

                if (message.equals("BYE")) {
                    break;
                }
                if (message.equals("CLOSE")) {
                    //docelowo zamkniecie ServerSocket??
                    break;
                }
                if (message.equals("LOAD")) {
                    outputStream.writeObject(phoneBook.load("mapa"));
                }
                if (message.equals("SAVE")) {
                    outputStream.writeObject(phoneBook.save("mapa"));
                }
                if (message.equals("PUT")) {
                    outputStream.writeObject(phoneBook.put("Wladimir Putin", "231"));
                }
                if (message.equals("LIST")) {
                    outputStream.writeObject(phoneBook.list());
                }
                if (message.equals("REPLACE")) {
                    outputStream.writeObject(phoneBook.replace("Wladirmir P", "312"));
                }
                if (message.equals("DELETE")) {
                    outputStream.writeObject(phoneBook.delete("Wladimir Putin"));
                }

            }
            socket.close();
            socket = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}