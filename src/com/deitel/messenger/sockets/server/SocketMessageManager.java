package com.deitel.messenger.sockets.server;
import com.deitel.messenger.MessageManager;

import java.net.InetAddress;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.deitel.messenger.sockets.server.SocketMessengerConstants.DISCONNECT_STRING;
import static com.deitel.messenger.sockets.server.SocketMessengerConstants.SERVER_PORT;

public class SocketMessageManager implements MessageManager {
    private Socket clientSocket;
    private String serverAddress;
    private PacketReceiver receiver;//receives mutlicast messages
    private boolean connected = false;
    private ExecutorService serverExecutor;//executor for server


    public SocketMessageManager( String address )
    {
        serverAddress = address;//store server address
        serverExecutor = Executors.newCachedThreadPool();
    }
    //connect to server and send messages to given MessageListener
    public void connect( MessageListener listener)
    {
        if ( connected )
        {
            return;//if already connected ,return immediately

        }
        try
        {
            clientSocket = new Socket(
                    InetAddress.getByName( serverAddress ),SERVER_PORT);
            //Create runnable for receiving incoming messages
            receiver = new PacketReceiver( listener );
            serverExecutor.execute( receiver );
            connected = true;

        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    //disconnect from server  and unregister given MessageListener
    public void disconnect( MessageListener listener)
    {
        if( !connected)
            return;
        try//stop listener and disconnect from server
        {
            //notify server that client is disconnecting
            Runnable disconnecter = new MessageSender( clientSocket, "",
                    DISCONNECT_STRING);
            Future disconnecting = serverExecutor.submit( disconnecter);
            disconnecting.get();//wait for disconnect message to be sent
            receiver.stopListening();
            clientSocket.close();
        }catch(ExecutionException e)
        {
            e.printStackTrace();
        }catch(InterruptedException exception)
        {
            exception.printStackTrace();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
        connected = false;
    }
    //send message to server
    public void sendMessage( String from, String message)
    {
        if( !connected )
            return;//if not connected,return immediately
        //create and start new MessageSender to deluver message
        serverExecutor.execute(
                new MessageSender( clientSocket, from, message)
        );
    }
}
