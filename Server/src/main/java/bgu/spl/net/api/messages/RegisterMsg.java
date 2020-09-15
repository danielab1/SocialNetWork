package bgu.spl.net.api.messages;

import bgu.spl.net.api.BGSProtocol;
import bgu.spl.net.api.ClientMessage;
import bgu.spl.net.api.Database;
import bgu.spl.net.api.User;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;

import java.util.Vector;

public class RegisterMsg extends ClientMessage {

    private String username;
    private String password;

    public RegisterMsg(String username, String password){
        super((short) 1);
        this.username = username;
        this.password = password;
    }

    public RegisterMsg(){
        super((short) 1);
        username = null;
        password = null;
    }

    @Override
    public int getNumOfArguments() {
        return 2;
    }

    @Override
    public void setArguments(String[] args) {
        username = args[0];
        password = args[1];
    }
    /**
     * Process Register msg:creat new user and if it is possible add him to the register list
     */
    @Override
    public void process(BGSProtocol bgsProtocol, Connections connections, Database db, int conId) {
        Vector<String> usersList = db.getUsersList();

        User newUser = new User(username, password);

        synchronized (usersList){
            if(db.isRegistered(username))
                connections.send(conId, new ErrorMsg(this));
            else {
                db.registerUser(newUser);
                connections.send(conId, new AckMsg(this));
            }
        }
    }
}
