package bgu.spl.net.api.messages;

import bgu.spl.net.api.BGSProtocol;
import bgu.spl.net.api.ClientMessage;
import bgu.spl.net.api.Database;
import bgu.spl.net.api.User;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
public class LogOutMsg extends ClientMessage {

    public LogOutMsg(){
        super((short) 3);
    }

    @Override
    public int getNumOfArguments() {
        return 0;
    }

    /** Process The Login Msg:
     * check all the condition if the login out action is possible otherwise send a error msg
     */
    @Override
    public void process(BGSProtocol bgsProtocol, Connections connections, Database db, int conId) {
        User user;
        if((user = db.getActiveUser(conId)) == null)
            connections.send(conId, new ErrorMsg(this));
        else {
            synchronized (user){
                db.logoutUser(conId);
                connections.send(conId, new AckMsg(this));
                connections.disconnect(conId);
                bgsProtocol.shouldTerminate();
            }
        }
    }
}
