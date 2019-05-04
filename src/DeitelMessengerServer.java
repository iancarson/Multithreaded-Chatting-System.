import com.deitel.messenger.sockets.server.MessageListener;
import com.deitel.messenger.sockets.server.MulticastSender;
import com.deitel.messenger.sockets.server.SocketMessengerConstants;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class DeitelMessengerServer implements SocketMessengerConstants, MessageListener {
    private ExecutorService serviceExecutor;//executor for server
   //start chat server
    public void startServer()
    {
        //create executor for server runnables
        serviceExecutor = Executors.newCachedThreadPool();
        try//create server and manage new clients
        {
            //create ServerSocket for incoming connections
            ServerSocket serverSocket = new ServerSocket( SERVER_PORT, 100);
            System.out.printf("%s%d%s","Server listening on port",SERVER_PORT, "...." );
            //listen for clients constantly
            while(true)
            {
                //accept new client connection
                Socket clientSocket = serverSocket.accept();
                //create MessageReceiver for receiving messages from client
                serviceExecutor.execute(
                        new MessageReceiver( this, clientSocket));
                //print connection information
                System.out.println("Connection received from:" +
                clientSocket.getInetAddress() );
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public void messageReceived(String from, String message)
    {
        //create String containing entire message
        String completeMessage = from + MESSAGE_SEPARATOR + message;
        //create and start MulticastSender to broadcast messages
        serviceExecutor.execute(
                new MulticastSender( completeMessage.getBytes() ) );
    }
}
