package com.deitel.messenger.sockets.server;

import com.deitel.messenger.MessageManager;

public class DeitelMessenger {
    public static void main(String[] args)
    {
        MessageManager messageManager;
        String address = "localhost";
        if( args.length == 0)
            //conect to localHost

            messageManager = new SocketMessageManager(address );
        else
            //connect using commandLine arg
        messageManager =  new SocketMessageManager(args[ 0 ]);
        //create GUI for SocketMessageManager
        ClientGUI clientGUI = new ClientGUI( messageManager);
        clientGUI.setSize(500, 400);
        clientGUI.setResizable( true);
        clientGUI.setVisible( true);
    }
}
