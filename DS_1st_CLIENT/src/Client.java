/*Papadakis Fragkiskos
321/2017147
 */
import java.awt.Component;
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Client { //klasi sundesis tou client me to server

    static ObjectOutputStream clientOutputStream;
    static ObjectInputStream clientInputStream;
    private static Socket socket;

    public static void main(String[] args) {

        try {
            String strInClient;
            //Syndesi sto eksipiretiti sto localhost me port 5555
            socket = new Socket("127.0.0.1", 5555);

            clientOutputStream = new ObjectOutputStream(socket.getOutputStream());
            clientInputStream = new ObjectInputStream(socket.getInputStream());

            //diamorfwsi twn streams gia anagnwsi kai egrgradi apo to stream tou server
            //emfanisi apomakrismenis dieuthinsis kai portas syndesis
            //emfanisi topoikhs dieuthinsis kai portas sindesis
            System.out.println("Sending Messages to the Server...");

            System.out.println("Connecting to " + socket.getInetAddress() + " Port: " + socket.getLocalPort());

            System.out.println("Local Address: " + socket.getLocalAddress() + " Local Port: " + socket.getLocalPort() + "\n");

            clientOutputStream.writeUTF("BEGIN"); //ksekinima protokolou
            clientOutputStream.flush();

            strInClient = clientInputStream.readUTF();
            System.out.println("Server -> " + strInClient);
            ClientPanel cp = new ClientPanel();
            if (strInClient.equals("LISTENING")) { //efoson to prwtokollo synexistei apo to server
                cp.show(); //emfanizetai to frame - grafika
            }

        } catch (ConnectException ex) {
            Component parentComponent = null;
            JOptionPane.showMessageDialog(parentComponent, "Connection is Closed...");
        } catch (IOException | NumberFormatException ex) {
        }

    }

    public static void closeConnection() {
        try {
            clientInputStream.close();
            clientOutputStream.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
