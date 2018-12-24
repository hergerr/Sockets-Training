import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/*
 *  Książka telefoniczna
 *   - Wątek który obsługuje gniazda Socket serwera
 *
 *  Autor: Tymoteusz Frankiewicz
 *   Data: 24 grudnia 2018 r.
 *
 */


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

    public String toString() {
        return name;
    }

    public void run() {
        String message;
        try (ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {
            outputStream = output;
            name = (String) input.readObject();
            while (true) {
                message = (String) input.readObject();
                String[] words = message.split("\\s+");
                if (words[0].equals("BYE")) {
                    outputStream.writeObject("OK");
                    break;
                }
                if (message.equals("CLOSE")) {
                    server.setServerSocketAccepts(false);
                    outputStream.writeObject("OK");
                }
                if (words[0].equals("LOAD")) {
                    try {
                        outputStream.writeObject(phoneBook.load(words[1]));
                    } catch (IndexOutOfBoundsException e) {
                        outputStream.writeObject(phoneBook.load(""));
                    }
                }
                if (words[0].equals("SAVE")) {
                    try {
                        outputStream.writeObject(phoneBook.save(words[1]));
                    } catch (IndexOutOfBoundsException e) {
                        outputStream.writeObject(phoneBook.save(""));
                    }
                }
                if (words[0].equals("PUT")) {
                    try {
                        outputStream.writeObject(phoneBook.put(words[1], words[2]));
                    } catch (IndexOutOfBoundsException e) {
                        outputStream.writeObject(phoneBook.put("", ""));
                    }
                }
                if (words[0].equals("GET")) {
                    try {
                        outputStream.writeObject(phoneBook.get(words[1]));
                    } catch (IndexOutOfBoundsException e) {
                        outputStream.writeObject(phoneBook.get(""));
                    }
                }
                if (words[0].equals("LIST")) {
                    outputStream.writeObject(phoneBook.list());
                }
                if (words[0].equals("REPLACE")) {
                    try {
                        outputStream.writeObject(phoneBook.replace(words[1], words[2]));
                    } catch (IndexOutOfBoundsException e) {
                        outputStream.writeObject(phoneBook.replace("", ""));

                    }
                }
                if (words[0].equals("DELETE")) {
                    try {
                        outputStream.writeObject(phoneBook.delete(words[1]));
                    } catch (IndexOutOfBoundsException e) {
                        outputStream.writeObject(phoneBook.delete(""));
                    }
                }
            }
            socket.close();
            socket = null;
        } catch (Exception e) {

        }
    }
}
