package com.deitel.messenger.sockets.server;
import java.io.IOException;
import java.util.Formatter;
import java.net.Socket;

import static com.deitel.messenger.sockets.server.SocketMessengerConstants.MESSAGE_SEPARATOR;


public class MessageSender implements  Runnable{
    private Socket clientSocket;//socket over which to send message
    private String messageToSend;
    public MessageSender( Socket socket, String userName, String message)
    {
        clientSocket = socket;//store socket for client
        //build message to be sent\
        messageToSend = userName + MESSAGE_SEPARATOR + message;
    }
    public void run() {
        try {
            Formatter output =
                    new Formatter(clientSocket.getOutputStream());
            output.format("%s\n", messageToSend);
            output.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
