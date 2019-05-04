package com.deitel.messenger.sockets.server;

public interface SocketMessengerConstants {
    //address for mutlicast datagrams
    public static final String MULTICAST_ADDRESS = "239.0.0.1";
    //port for listening for mutlicast datagrams
    public static final int MULTICAST_LISTENING_PORT =5555;
    //port for sending multicast datagrams
    public static final int MULTICAST_SENDING_PORT = 5554;
    //port for socket connections to DeitelMessengerServer
    public  static final int SERVER_PORT = 12345;
    //String that indicates disconnect
    public static final String DISCONNECT_STRING ="DISCONNECT";
    //String that separates the user name from the message body
    public static final String MESSAGE_SEPARATOR = ">>>";
    //Message size(in bytes)
    public static final int MESSAGE_SIZE = 512;
}
