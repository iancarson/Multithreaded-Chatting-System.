package com.deitel.messenger.sockets.server;

public interface MessageListener {
    //receive new chat message
    public void messageReceived(String from, String message);
}
