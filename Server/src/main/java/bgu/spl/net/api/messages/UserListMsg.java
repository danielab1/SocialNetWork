package bgu.spl.net.api.messages;

import bgu.spl.net.Message;
import bgu.spl.net.api.BGSProtocol;
import bgu.spl.net.api.ClientMessage;
import bgu.spl.net.api.Database;
import bgu.spl.net.api.User;
import bgu.spl.net.api.bidi.Connections;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class UserListMsg extends ClientMessage {

    public UserListMsg() {
        super((short) 7);
    }

    @Override
    public int getNumOfArguments() {
        return 0;
    }

    /**
     * Process UserList msg : build The AckMsg by the Register users in the Server
     */
    @Override
    public void process(BGSProtocol bgsProtocol, Connections connections, Database db, int conId) {
        ConcurrentHashMap<String, User> registeredUsers = db.getRegisteredUsers();
        Message res = new ErrorMsg(this);

        if (db.getActiveUser(conId) != null) {
            Vector<String> usersList = db.getUsersList();

            String userNameList = "";
            for (int i = 0; i < usersList.size(); i++) {
                userNameList = userNameList + usersList.get(i) + " ";
            }

            res = new AckMsg(this, userNameList);
            short[] registerNum = {(short) registeredUsers.size()};
            ((AckMsg) res).addShorts(registerNum);
        }

        connections.send(conId, res);
    }
}