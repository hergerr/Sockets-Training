import javax.swing.*;
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

    public void run(){
        String message;
        try(ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream()))
        {
            outputStream = output;
            name = (String)input.readObject();
            while(true){
                message = (String)input.readObject();
                String[] words = message.split("\\s+");
                if (words[0].equals("BYE")) {
                    outputStream.writeObject("OK");
                    break;
                }
                if (message.equals("CLOSE")) {
                    //wyjscie z petli while spowoduje zamkniecie ServerSocket
                    server.setServerSocketAccepts(false);
                    outputStream.writeObject("OK");
                }
                if (words[0].equals("LOAD")) {
                    outputStream.writeObject(phoneBook.load(words[1]));
                }
                if (words[0].equals("SAVE")) {
                    outputStream.writeObject(phoneBook.save(words[1]));
                }
                if (words[0].equals("PUT")) {
                    outputStream.writeObject(phoneBook.put(words[1], words[2]));
                }
                if (words[0].equals("GET")) {
                    outputStream.writeObject(phoneBook.get(words[1]));
                }
                if (words[0].equals("LIST")) {
                    outputStream.writeObject(phoneBook.list());
                }
                if (words[0].equals("REPLACE")) {
                    outputStream.writeObject(phoneBook.replace(words[1], words[2]));
                }
                if (words[0].equals("DELETE")) {
                    outputStream.writeObject(phoneBook.delete(words[1]));
                }
            }
            socket.close();
            socket = null;
        } catch(Exception e) {
//            myServer.removeClient(this);
        }
    }
}
