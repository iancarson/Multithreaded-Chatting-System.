package com.deitel.messenger.sockets.server;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static com.deitel.messenger.sockets.server.SocketMessengerConstants.MULTICAST_ADDRESS;
import static com.deitel.messenger.sockets.server.SocketMessengerConstants.MULTICAST_LISTENING_PORT;
import static com.deitel.messenger.sockets.server.SocketMessengerConstants.MULTICAST_SENDING_PORT;

public class MulticastSender implements     Runnable {
    private byte[] messageBytes;//message data
    public MulticastSender(byte[] bytes)
    {
        messageBytes = bytes;
    }
    public void run()
    {
        try
        {
            DatagramSocket socket = new DatagramSocket( MULTICAST_SENDING_PORT );
            //use InetAddress reserved for multicast group
            InetAddress group = InetAddress.getByName( MULTICAST_ADDRESS );
            //Create DatagramPacket containing message
            DatagramPacket packet = new DatagramPacket( messageBytes,messageBytes.length,group,MULTICAST_LISTENING_PORT );
            socket.send(packet);
            socket.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

}
