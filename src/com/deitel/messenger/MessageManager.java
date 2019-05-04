package com.deitel.messenger;

import com.deitel.messenger.sockets.server.MessageListener;

public interface MessageManager {
    //connect to message server and route incoming messages
    //to given MessageListener
    public void connect(MessageListener listener);
        //disconnect from message server and stop routing
        //incoming messages to given MessageListener
        public void disconnect(MessageListener listener);
        //send messages to mesage server
    public void sendMessage(String from,String message );

}
