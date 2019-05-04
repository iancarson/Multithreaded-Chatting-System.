import com.deitel.messenger.sockets.server.MessageListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import  java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.StringTokenizer;

import static com.deitel.messenger.sockets.server.SocketMessengerConstants.DISCONNECT_STRING;
import static com.deitel.messenger.sockets.server.SocketMessengerConstants.MESSAGE_SEPARATOR;


public class MessageReceiver implements Runnable{
    private BufferedReader input;
    private MessageListener messageListener;
    private boolean keepListening = true;
    //MessageReceiver constructor
    public MessageReceiver(MessageListener listener,Socket clientSocket)
    {
        //set Listener to which new messages should be sent
        messageListener = listener;
            //set Timeout for reading from client
            try {
                clientSocket.setSoTimeout( 5000 );
                //create BufferedReader for reading incoming messages
                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
    public void run()
    {
        String message;//String for incoming messages
        //listen for messages until stopped
        while( keepListening )
        {
            try
            {
                message  = input.readLine();//read message from client
            }
            catch(SocketTimeoutException e)
            {
                continue;
            }catch(IOException e)
            {
                e.printStackTrace();
                break;
            }
            //ensure non-null message
            if( message != null)
            {
                //tokenize message to retrieve user name and messageBody
                StringTokenizer tokenizer = new StringTokenizer(message,MESSAGE_SEPARATOR);
                //Ignore messages that do not contain a user
                //name and message body
                if( tokenizer.countTokens() == 2)
                {
                    //send message to messageListener
                    messageListener.messageReceived(tokenizer.nextToken(),tokenizer.nextToken());//message body
                }
                else
                {
                    //if disconnect message received ,stop listening
                    if( message.equalsIgnoreCase(MESSAGE_SEPARATOR + DISCONNECT_STRING))
                        stopListening();
                }
            }
        }
        try
        {
            input.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public void stopListening()
    {
        keepListening = false;
    }
}
