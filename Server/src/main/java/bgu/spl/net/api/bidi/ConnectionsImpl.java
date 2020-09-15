package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.ConnectionHandler;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements Connections<T> {

    private ConcurrentHashMap<Integer, ConnectionHandler<T>> connections;

    public ConnectionsImpl() {
        connections = new ConcurrentHashMap<>();
    }

    public void addConnection(int id, ConnectionHandler handler){
        connections.putIfAbsent(id, handler);
    }

    @Override
    public boolean send(int connectionId, T msg) {
        ConnectionHandler handler = connections.get(connectionId);
        handler.send(msg);

        return false;
    }
    @Override
    public void broadcast(T msg) {
        connections.forEachValue(1, handler->{
            handler.send(msg);
        });
    }

    @Override
    public void disconnect(int connectionId) {
        connections.remove(connectionId);
    }
}

