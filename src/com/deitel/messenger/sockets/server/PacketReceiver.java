package com.deitel.messenger.sockets.server;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;
import java.util.StringTokenizer;

import static com.deitel.messenger.sockets.server.SocketMessengerConstants.*;


public class PacketReceiver implements Runnable {
    private MessageListener messageListener;//receives messages
    private MulticastSocket multicastSocket;//receive broadcast messages
    private InetAddress multicastGroup;
    private boolean keepListening = true;
    public PacketReceiver( MessageListener listener)
    {
        messageListener = listener;//set MessageListener
        try
        {
           //create new MulticastSocket
           multicastSocket = new MulticastSocket(
                   MULTICAST_LISTENING_PORT
           ) ;
           //Use InetAddress to get multicast group
            multicastGroup = InetAddress.getByName( MULTICAST_ADDRESS);
            //Join multicast group to receive messges
            multicastSocket.joinGroup( multicastGroup);
            //set 5 seconds when waiting for mew Packets
            multicastSocket.setSoTimeout(( 5000 ));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public void run()
    {
        //listen for messages until stopped
        while( keepListening )
        {
            //create buffer for incoming message
            byte[] buffer = new byte[ MESSAGE_SIZE];
            //Create DatagramPacket for incoming message
            DatagramPacket packet = new DatagramPacket( buffer, MESSAGE_SIZE);
            try
            {
                multicastSocket.isConnected();
                if(!multicastSocket.isClosed())
                multicastSocket.receive( packet);

            }catch(SocketTimeoutException ex)
            {
                continue;
            }
            catch(IOException e)
            {
                e.printStackTrace();
                break;
            }

            //put message data in a string
            String message = new String( packet.getData() );

            message =message.trim();
            //tokenize message to retrieve  user name and message body
            StringTokenizer tokenizer = new StringTokenizer(
                    message,MESSAGE_SEPARATOR
            );
            //ignore messages that do not contain a user
            //name and message body
            if( tokenizer.countTokens() == 2 )
            {
                //send message to messageListener
                messageListener.messageReceived(
                        tokenizer.nextToken(),//user name
                        tokenizer.nextToken()
                );
            }
            try
            {
                multicastSocket.leaveGroup( multicastGroup );//leave group
                multicastSocket.close();
            }
            catch( IOException e)
            {
                e.printStackTrace();
            }
        }

    }
    //stop listening for new Messages
    public void stopListening()
    {
        keepListening = false;
    }
}
