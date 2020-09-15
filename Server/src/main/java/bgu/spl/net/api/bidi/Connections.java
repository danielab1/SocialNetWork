package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.ConnectionHandler;


public interface Connections<T> {

    boolean send(int connectionId, T msg);

    void broadcast(T msg);

    void disconnect(int connectionId);

    void addConnection(int id, ConnectionHandler handler);

}
