package bgu.spl.net.api.messages;

import bgu.spl.net.Message;
import bgu.spl.net.api.BGSProtocol;
import bgu.spl.net.api.ClientMessage;
import bgu.spl.net.api.Database;
import bgu.spl.net.api.User;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.ConnectionsImpl;

import java.util.Vector;

public class LoginMsg extends ClientMessage {
    private String username;
    private String password;

    public LoginMsg(String username, String password) {
        super((short) 2);
        this.username = username;
        this.password = password;
    }

    public LoginMsg() {
        super((short) 2);
        this.username = null;
        this.password = null;
    }

    @Override
    public int getNumOfArguments() {
        return 2;
    }

    @Override
    public void setArguments(String[] s) {
        username = s[0];
        password = s[1];
    }

    /** Process The Login Msg:
     * check all the condition if the login action is possible otherwise send a error msg
     */

    @Override
    public void process(BGSProtocol bgsProtocol, Connections connections, Database db, int conId) {
        Message res;
        Message waitingMsg;
        User user = db.getRegisteredUser(username);

        if (user != null) {
            synchronized (user) {
                if (db.isRegistered(username) && (user.getPassword().equals(password)) && !user.isLoggedIn() && !db.isActive(conId)) {
                    db.loginUser(conId, username);
                    res = new AckMsg(this);
                    connections.send(conId, res);

                    while ((waitingMsg = user.getNextMsg()) != null) {
                        connections.send(conId, waitingMsg);
                    }
                }
                else
                    connections.send(conId, new ErrorMsg(this));
            }
        } else
            connections.send(conId, new ErrorMsg(this));

    }
}


