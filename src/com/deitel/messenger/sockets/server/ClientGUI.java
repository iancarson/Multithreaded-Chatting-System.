package com.deitel.messenger.sockets.server;
import com.deitel.messenger.MessageManager;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import  java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import  javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import  javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

public class ClientGUI extends JFrame {
    private JMenu serverMenu;//for connection purposes
    private JTextArea messageArea;//displays messages
    private JTextArea inputArea;//inputs messages
    private JButton connectButton;//button for connecting
    private JMenuItem connectMenuItem;//menu item for connecting
    private JButton disconnectButton;
    private JMenuItem disconnectMenuItem;//menu item for disconnecting
    private JButton sendButton;//sends messages
    private JLabel statusBar;
    private String userName;
    private MessageManager messageManager;//communicates with server
    private MessageListener messageListener;//receives incoming messages
    //ClientGUI constructor

    public ClientGUI( MessageManager manager)
    {
        super( " Deitel Messenger" );
        messageManager = manager;//set the messageManager
        //create MyMessageListener for receiving messages
        messageListener = new MyMessageListener();
        serverMenu = new JMenu( "SERVER" );//Create Server JMenu
        serverMenu.setMnemonic( 'S');//set mnemonic for server menu
        JMenuBar menuBar =new JMenuBar();//create JMenuBar
        menuBar.add( serverMenu );
        setJMenuBar( menuBar);
        //create ImageIcon for connect buttons
        Icon connectIcon = new ImageIcon(
                getClass().getResource( "/Image/Connect2.png" )
        );
        //create connectButton and connectMenuItem
        connectButton = new JButton( "Connect", connectIcon);
        connectMenuItem = new JMenuItem( "Connect", connectIcon);
        connectMenuItem.setMnemonic( 'C');
        //create ConnectListener for connect buttons
        ActionListener connectListener = new ConnectListener();
        connectButton.addActionListener( connectListener );
        connectMenuItem.addActionListener( connectListener );
        //create ImageIcon for disconnect buttons
        Icon disconnectIcon = new ImageIcon(getClass().getResource( "/Image/Disconnect.gif" ));
        //create disconnectButton and disconnectMenuItem;
        disconnectButton = new JButton( "Disconnect", disconnectIcon);
        disconnectMenuItem = new JMenuItem( "Disconnect", disconnectIcon);
        disconnectMenuItem.setMnemonic( 'D');
        //disable disconnect buttom and menu item
        disconnectButton.setEnabled( false );
        disconnectMenuItem.setEnabled( false );
        //create  DisconnectListener for disconnect buttons
        ActionListener disconnectListener = new DisconnectListener();
        disconnectButton.addActionListener( disconnectListener);
        disconnectMenuItem.addActionListener( disconnectListener );
        //add connect and disconnect JMenuItems fo fileMenu
        serverMenu.add( connectMenuItem);
        serverMenu.add( disconnectMenuItem );
        //add connect and disconnect JButtons to buttonPanel
        JPanel buttonPanel =new JPanel();
        buttonPanel.add( connectButton);
        buttonPanel.add( disconnectButton);
        messageArea =  new JTextArea();//displays messages
        messageArea.setEditable( false);//disable editing
        messageArea.setWrapStyleWord( true);//set wrapstyle to word
        messageArea.setLineWrap( true);//enable line wrapping
        //put messageArea in JScrollPane to enable scrolling
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout(10,10));
        messagePanel.add( new JScrollPane(messageArea),BorderLayout.CENTER);
        inputArea = new JTextArea( 4,20);//for entering new messages
        inputArea.setWrapStyleWord( true);
        inputArea.setLineWrap( true );
        inputArea.setEditable( false );//disable editing
        //create Icon for sendButton
        Icon sendIcon = new ImageIcon(
                getClass().getResource("/Image/Send.gif")
        );
        sendButton = new JButton( "Send", sendIcon);//create send button
        sendButton.setEnabled( false);//disable send button
        sendButton.addActionListener(
                new ActionListener() {
                    @Override
                    //send new message when  user activates sendButton
                    public void actionPerformed(ActionEvent e) {
                        messageManager.sendMessage(userName,
                                inputArea.getText());//send message
                        inputArea.setText( "" );//clear InputArea
                    }
                }
        );
        Box box = new Box( BoxLayout.X_AXIS);//Create new box for layout
        box.add( new JScrollPane( inputArea));//add inputArea to box
        box.add( sendButton);
        messagePanel.add( box, BorderLayout.SOUTH);//add box to panel
        //create JLabel for statusBar with recessed border
        statusBar = new JLabel( " Not connected" );
        statusBar.setBorder( new BevelBorder( BevelBorder.LOWERED));
        add( buttonPanel, BorderLayout.NORTH);//add button to panel
        add( messagePanel, BorderLayout.CENTER);
        add( statusBar, BorderLayout.SOUTH);
        // add WindowListener to disconnect when user quits
        addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        messageManager.disconnect( messageListener );
                        System.exit( 0  );
                    }
                }
        );
    }
    //ConnectListener listens for user requests to connect to server
    private class ConnectListener implements ActionListener
    {
        //connect to server and enable/disable GUI components
        public void actionPerformed(ActionEvent e)
        {
            //connect to server and route message to messageListener
             messageManager.connect( messageListener);
            //prompt for userName
            userName = JOptionPane.showInputDialog(
                    ClientGUI.this, "Enter user name"
            );
            messageArea.setText( "" );//clear messageArea
            //disable connect
            connectButton.setEnabled( false);
            connectMenuItem.setEnabled( false);
            disconnectButton.setEnabled( true);//enable disconnect
            disconnectMenuItem.setEnabled( true);
            sendButton.setEnabled( true);//enable send Button
            inputArea.setEditable( true);//enable editing for input area
            inputArea.requestFocus();//set focus to input area
            statusBar.setText( "Connected:" + userName);//set text

        }
    }
    private class DisconnectListener implements ActionListener
    {
        //disconnect from server and  enable/disable GUI components
        public void actionPerformed(ActionEvent event)
        {
            //disconnect from server and stop routing messages
            messageManager.disconnect( messageListener);
            sendButton.setEnabled( false);
            disconnectButton.setEnabled( false);
            disconnectMenuItem.setEnabled( false);
            inputArea.setEditable( false);
            connectButton.setEnabled( true);
            connectMenuItem.setEnabled( true);
            statusBar.setText( "Not connected" );
        }
    }
    //MymessagListener listens for new messages from MessageManager
    //and displays messages in messageArea using MessageDisplayer.
    private class MyMessageListener implements  MessageListener
    {
        //when received ,display new messages in messageArea
        public void messageReceived( String from, String message)
        {
            //append message using MessageDisplayer
            SwingUtilities.invokeLater(
                    new MessageDisplayer( from, message)
            );
        }
    }
    //Displays new message by appending message to JTextArea.Should
    //be executed only in Event Thread;modifies live swing component
    private class MessageDisplayer implements  Runnable
    {
        private String fromUser;
        private String messageBody;
        //messageDisplayer constructor
        public MessageDisplayer( String from, String body)
        {
            fromUser = from;//store originating user
            messageBody = body;
        }
        //display new message in messageArea
        public void run()
        {
            //append new message
            messageArea.append("\n" + fromUser + ">" + messageBody);
        }
    }
}
