/*Papadakis Fragkiskos
321/2017147
 */
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    public static void main(String[] args) {

        Movies m;
        String strInServer;
        String key;
        String namekey;
        ObjectOutputStream out;
        ObjectInputStream in;

        ServerSocket server = null;
        try {
            server = new ServerSocket(5555, 50); //dimiorgia server socket me port 5555 kai mexri 50 client
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Accepting Connection...");
        System.out.println("Local Address :" + server.getInetAddress() + " Port: " + server.getLocalPort());
        while (true) { //gia na min kleinei o server 
            try {

                Socket socket = server.accept();  //apodoxi

//diamorfwsi paketou gia apostoli antikeimenwn sto client 
//diamorfwsi paketo gia lipsi antikeimenwn apo ton client
                ObjectOutputStream serverOutputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream serverInputStream = new ObjectInputStream(socket.getInputStream());

                //out = new ObjectOutputStream(new FileOutputStream("object.dat"));
                in = new ObjectInputStream(new FileInputStream("object.dat")); //gia na diabasei ta antikeimena apo to arxeio

                HashMap<String, Movies> nameMap = new HashMap<>(); //hashmap gia tis tainies
                ArrayList<Movies> directorList = new ArrayList<>(); //lista gia to skinotheti

                strInServer = serverInputStream.readUTF(); //o server lambanei antikeimeno stirng gia na ksekinisei to prwtokolo
                while (!strInServer.equals("END")) { //oso o client den steilei END gia na kleisei i syndesi me to client symfwna me to protokollo

                    System.out.println("Client -> " + strInServer);

                    if (strInServer.equals("BEGIN")) { //ekeinisi prwtokollou apot o server
                        serverOutputStream.writeUTF("LISTENING"); //apostoli antikeimenou string gia na akolouthisei to prwtokollo o client
                        serverOutputStream.flush();

                        strInServer = serverInputStream.readUTF();
                        System.out.println("Client -> " + strInServer);

                        if (strInServer.equals("INSERT")) {
                            System.out.println("Server -> " + strInServer);
                            namekey = serverInputStream.readUTF();
                            //otan stelnei antikeimeno movies o client, o server to castarei kai to metatrepei antikeimeno m
                            m = (Movies) serverInputStream.readObject();

                            nameMap = (HashMap<String, Movies>) in.readObject(); //pairnei ta antikeimena apo to arxeio gia tis tainies (cast se hashmap gia na ta prosthesei)
                            directorList = (ArrayList<Movies>) in.readObject(); //pairnei ta antikeimena apo to arxeia gia tous skinothetes (cast se lista gia na ta prosthesei)
                            nameMap.put(namekey, m); //prosthetei sto hashmap to onoma tis tanias kai tin tainia (key kai value)
                            directorList.add(m); //prosthetei to antikeimeno tainia stin lista

                            try {
                                out = new ObjectOutputStream(new FileOutputStream("object.dat")); //gia na prosthesei sto arxeio
                                out.writeObject(nameMap); //prosthetei sto arxeio to hashmap 
                                out.flush();
                                out.writeObject(directorList); //prosthetei sto arxeio tin lista
                                out.close();
                                serverOutputStream.writeUTF("OK");
                                serverOutputStream.flush();
                            } catch (IOException ioex) {
                                System.out.println("IO ERROR");
                            }
                        } else if (strInServer.equals("SEARCH")) { //ean dektei search

                            key = serverInputStream.readUTF(); //diabazei antikeimeno string gia na emfanisei eite tainia eite lista eite norecord

                            nameMap = (HashMap<String, Movies>) in.readObject(); //diabazei ta antikeimena hashmap apo to arxeio
                            directorList = (ArrayList<Movies>) in.readObject();//diabazei ta antikeimena lista apo to arxeio
                            if (nameMap.containsKey(key)) { //ean to antikeimeno string pou dextike o server yparxei san key
                                serverOutputStream.writeObject(nameMap.get(key)); //stelnei sto client to value kai emfanizei opws i toString
                                serverOutputStream.flush();
                                serverOutputStream.writeUTF("OK"); //akolouthei to prwtokollo
                                serverOutputStream.flush();
                                strInServer = serverInputStream.readUTF(); //dexetai to END symfwna me to protwkollo
                                System.out.println("Client -> " + strInServer);
                                serverOutputStream.close();
                                serverInputStream.close();
                                socket.close(); //kleinei to socket
                            } else if (!nameMap.containsKey(key)) { //ean den yparxei to key sto hashmap
                                ArrayList<Movies> list = new ArrayList<>();
                                boolean found = false;
                                for (Movies md : directorList) { //gia kathe antikeimeno movies pou yparxei sti lista
                                    if (md.getDirector().equals(key)) { //ean o director einai equals me to antikeimeno string pou esteile o client
                                        list.add(md); //prosthetei to antikeimeno stin lista
                                        found = true; //mesa sto block ginetai true
                                    }

                                }
                                if (found) { //ean einai true 
                                    serverOutputStream.writeObject(list); //o server stelnei sto client tin lista
                                    serverOutputStream.flush();
                                    serverOutputStream.writeUTF("OK"); //akolouthei to protokollo
                                    serverOutputStream.flush();
                                    strInServer = serverInputStream.readUTF(); //dexetai to END symfwna me to protokollo
                                    System.out.println("Client -> " + strInServer);
                                    serverOutputStream.close();
                                    serverInputStream.close();
                                    socket.close(); //kleinei to socket
                                } else { //ean den yparxei oute tainia oute onoma director 
                                    serverOutputStream.writeObject("");
                                    serverOutputStream.flush();
                                    serverOutputStream.writeUTF("NORECORD"); //akolouthei to protollo kai stelnei NORECORD sto client
                                    serverOutputStream.flush();
                                    strInServer = serverInputStream.readUTF(); 
                                    System.out.println("Client -> " + strInServer);
                                    socket.close();
                                }
                            }
                        }
                    } else {
                        serverOutputStream.writeUTF("Out of Protocol...");
                        serverOutputStream.flush();
                        socket.close();
                    }
                }
                //ean o server paralabei kati allo kleinei tin sundesi
                serverOutputStream.close();
                serverInputStream.close();
                socket.close();
            } catch (SocketException ex) {
                System.out.println("Client Disconnected...");
            } catch (EOFException ex) {
                System.out.println("End of file reached.");
            } catch (FileNotFoundException ex) {
                System.out.println("File dont found");

            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Error during I/O");
                ex.getMessage();

            }
        }

    }
}
