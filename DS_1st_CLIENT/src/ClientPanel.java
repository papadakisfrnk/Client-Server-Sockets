/*Papadakis Fragkiskos
321/2017147
 */
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class ClientPanel {

    //frames gia grafika
    private JFrame selectFrame, searchFrame, insertFrame;
    private JPanel panel, panelButton;
    private JLabel label;
    private JTextField textClient;
    private JTextArea textServer;
    private JButton search, insert, back, end;

    public ClientPanel() {
        initialize();
    }

    public void initialize() {
        //prwto frame gia tin epilogi search i insert
        selectFrame = new JFrame();
        selectFrame.setTitle("Select");
        selectFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        selectFrame.setSize(400, 200);
        selectFrame.setLocationRelativeTo(null);
        panel = new JPanel();
        label = new JLabel("For search movie press SEARCH For insert movie press INSERT");

        //koumpia prwtou frame
        search = new JButton("Search");
        insert = new JButton("Insert");

        //prosthetoume ta koumpia sto frame
        panel.add(search);
        panel.add(insert);
        panel.add(label);
        selectFrame.add(panel, BorderLayout.CENTER);

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { //otan o xristis patisei to search
                //pavei na emfanizei to trexon frame kai emfanizei to parathiro gia search
                selectFrame.setVisible(false);
                searchSelection();//kalleitai h methodos (me emfanisi parathirou)
            }
        });

        insert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {//otan o xristis patisei to insert
                //pavei na emfanizei to trexon frame kai emfanizei to parathiro gia insert
                selectFrame.setVisible(false);
                insertSelection(); //kalleitai h methodos (me emfanisisi parathirou)
            }
        });
    }

    public void searchSelection() {
        searchFrame = new JFrame();
        searchFrame.setTitle("Search from the Server");
        searchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        searchFrame.setSize(500, 400);
        searchFrame.setLocationRelativeTo(null); //to parathiro na emfanizetai sto kentro 
        //prosthetoume ta koumpia sto frame
        search = new JButton("Search");
        end = new JButton("Terminate");
        back = new JButton("Back");

        textClient = new JTextField(20); //to field pou grafei o client gia anazitisi
        textServer = new JTextArea(); //to field pou emfanizei o server ta antikeimena
        textServer.setEditable(false);
        panel = new JPanel();
        label = new JLabel("Client");

        textServer.setPreferredSize(new Dimension(300, 300));
        JPanel panel2 = new JPanel();
        JLabel label2 = new JLabel("Server");

        panel.add(textClient);
        panel.add(label);
        panel2.add(textServer);
        panel2.add(label2);

        panelButton = new JPanel();
        panelButton.add(search);
        panelButton.add(back);
        panelButton.add(end);

        searchFrame.add(panel, BorderLayout.NORTH);
        searchFrame.add(panel2, BorderLayout.CENTER);
        searchFrame.add(panelButton, BorderLayout.PAGE_END);

        searchFrame.setVisible(true);

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //ean o xristis patisei to back emfanizetai to prwto parathiro
                searchFrame.setVisible(false);
                show();
            }
        });
        end.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String strOutClient = "END";
                try {
                    //ean o xristis patisei to terminate
                    //kleinei to socket tou client kai i sundesi me ton server
                    //stelnei to end ston server gia na termatisei to prwtokollo
                    Client.clientOutputStream.writeUTF(strOutClient);
                    Client.clientOutputStream.flush();
                    Client.closeConnection(); //kleinei tin syndesi
                    searchFrame.setVisible(false);
                } catch (IOException ex) {
                    Logger.getLogger(ClientPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    String strOutClient;
                    Client.clientOutputStream.writeUTF("SEARCH");
                    Client.clientOutputStream.flush();
                    strOutClient = textClient.getText(); //painei to text pou exei grapsei o client
                    //kai to stelnei san antikeimeno string ston server
                    Client.clientOutputStream.writeUTF(strOutClient);
                    Client.clientOutputStream.flush();

                    //otan labei o client apo to server to antikeimeno i tin lista tin emfanizei sto textServer text field
                    textServer.setText(Client.clientInputStream.readObject().toString());

                    String strInClient;
                    strInClient = Client.clientInputStream.readUTF();
                    System.out.println("Server -> " + strInClient);
                    Client.closeConnection(); //kleinei tin syndesi
                    //ean stalthei apo to server to NORECORD tote kleinei to parathiro kai tou emfanizei se joptionpane oti den yparxei 
                    //katoxiromeni sto server tainia-tainies skinotheti
                    //to NORECORD apotelei meros tou protokollou kai gia auto to lambanei o client apo to servr
                    if (strInClient.equals("NORECORD")) {
                        searchFrame.setVisible(false);
                        Component parentComponent = null;
                        JOptionPane.showMessageDialog(parentComponent, "No Record In Server");
                        Client.closeConnection(); //kleinei tin syndesi

                    }
                    //afou exoun oloklirwthei oles oi energies sto telos sumfwna me to prwtokollo stelnetai to END apo to client sto server
                    Client.clientOutputStream.writeUTF("END");

                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(ClientPanel.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

    }

    public void insertSelection() { //methodos me grafika gia to insert
        insertFrame = new JFrame();
        insertFrame.setTitle("Insert to the Server");
        insertFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //ama patisei to x tou parathirou termatizei to project tou client
        insertFrame.setSize(500, 400);
        insertFrame.setLocationRelativeTo(null);
        //koumpia tou insert
        insert = new JButton("Insert");
        back = new JButton("Back");
        panel = new JPanel();
        end = new JButton("Terminate");

        JTextField nameText = new JTextField(20);
        JTextField directorText = new JTextField(20);
        JTextField genreText = new JTextField(20);
        JTextField timeText = new JTextField(20);
        JTextField descText = new JTextField(50);

        JLabel nameLabel = new JLabel("Name");
        JLabel directorLabel = new JLabel("Director");
        JLabel genreLabel = new JLabel("Genre");
        JLabel timeLabel = new JLabel("Movie Time");
        JLabel descLabel = new JLabel("Description");

        JPanel panelButton = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        //prosthetoume ta text kai ta koumpia sto panel
        //to panel exei boxlayout me katheti emfanisi 
        panel.add(nameLabel);
        panel.add(nameText);
        panel.add(directorLabel);
        panel.add(directorText);
        panel.add(genreLabel);
        panel.add(genreText);
        panel.add(timeLabel);
        panel.add(timeText);
        panel.add(descLabel);
        panel.add(descText);
        panelButton.add(insert);
        panelButton.add(back);
        panelButton.add(end);

        insertFrame.add(panel, BorderLayout.CENTER);
        insertFrame.add(panelButton, BorderLayout.PAGE_END);

        insertFrame.setVisible(true); //to insert frame einai orato

        insert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try { //otan patithei to  koumpi insert stelnetai to antikeimeno string sto server
                    //sumfwna me to prwtokollo
                    Client.clientOutputStream.writeUTF("INSERT");
                    Client.clientOutputStream.flush();

                    //pairnei apo ta text field auta pou exei grapsei sto frame o xristis
                    //
                    Movies mv = new Movies(nameText.getText(), directorText.getText(), genreText.getText(), Integer.parseInt(timeText.getText()), descText.getText());
                    Client.clientOutputStream.writeUTF(nameText.getText());
                    Client.clientOutputStream.flush();
                    Client.clientOutputStream.writeObject(mv);
                    Client.clientOutputStream.flush();
                    insertFrame.setVisible(false);
                    Component parentComponent = null;
                    JOptionPane.showMessageDialog(parentComponent, "The registration has been completed");
                    //sumfwna me to protokollo o client otan ginetai i kataxyrwsh dexetai antikeimeno string OK apo to server
                    String strInClient = Client.clientInputStream.readUTF();
                    System.out.println("Server -> " + strInClient);
                    Client.closeConnection(); //kleinei tin syndesi

                } catch (IOException ex) {
                    Logger.getLogger(ClientPanel.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertFrame.setVisible(false);
                show();
            }
        });

        end.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String strOutClient = "END";
                try {
                    Client.clientOutputStream.writeUTF(strOutClient); //o client stelnei sto server antikeimeno string
                    Client.clientOutputStream.flush();

                    Client.closeConnection(); //kleinei tin syndesi

                    insertFrame.setVisible(false);

                } catch (IOException ex) {
                    Logger.getLogger(ClientPanel.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public void show() {
        initialize();
        selectFrame.setVisible(true); //orato frame otan kalite i methodos show()
    }
}
