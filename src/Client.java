import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Client extends JFrame implements ActionListener, Runnable{

	private JTextField messageField = new JTextField(20);
    private JTextArea  textArea     = new JTextArea(15,18);

    static final int SERVER_PORT = 25000;
    private String name;
    private String serverHost;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    
    Client(String name, String host) {
        super(name);
        this.name = name;
        this.serverHost = host;
        this.setSize(300, 310);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                try {
                    outputStream.close();
                    inputStream.close();
                    socket.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
            @Override
            public void windowClosed(WindowEvent event) {
                windowClosing(event);
            }
        });
        JPanel panel = new JPanel();
        JLabel messageLabel = new JLabel("Napisz:");
        JLabel textAreaLabel = new JLabel("Dialog:");
        
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        panel.add(messageLabel);
        panel.add(messageField);
        
        messageField.addActionListener(this);
        panel.add(textAreaLabel);
        JScrollPane scroll_bars = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(scroll_bars);

        this.setContentPane(panel);
        this.setVisible(true);
        new Thread(this).start();
    }
    
    synchronized public void printReceivedMessage(String message){
        String tmp_text = textArea.getText();
        textArea.setText(tmp_text + ">>> " + message + "\n");
    }

    synchronized public void printSentMessage(String message){
        String text = textArea.getText();
        textArea.setText(text + "<<< " + message + "\n");
    }
	
	@Override
	public void run() {
        if (serverHost.equals("")) {
            serverHost = "localhost";
        }
        try{
            socket = new Socket(serverHost, SERVER_PORT);
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(name);
        } catch(IOException e){
            JOptionPane.showMessageDialog(null, "Polaczenie sieciowe dla klienta nie moze byc utworzone");
            setVisible(false);
            dispose(); 
            return;
        }
        try{
            while(true){
                String message = (String)inputStream.readObject();
                printReceivedMessage(message);
                if(message.equals("exit")){
                    inputStream.close();
                    outputStream.close();
                    socket.close();
                    setVisible(false);
                    dispose();
                    break;
                }
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "Polaczenie sieciowe dla klienta zostalo przerwane");
            setVisible(false);
            dispose();
        }
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String message;
        Object source = event.getSource();
        if (source == messageField)
        {
            try{ message = messageField.getText();
                outputStream.writeObject(message);
                printSentMessage(message);
                if (message.equals("BYE")){
                    inputStream.close();
                    outputStream.close();
                    socket.close();
                    setVisible(false);
                    dispose();
                    return;
                }
                if(message.equals("CLOSE")) {
                	
                }
                if(message.equals("LIST")) {
                	
                }
                
            }catch(IOException e){
            	System.out.println("Wyjatek klienta: "+ e);
            }
        }
        repaint();
		
	}
	
}