package bgu.spl.net.api.messages;

import bgu.spl.net.Message;
import bgu.spl.net.api.BGSProtocol;
import bgu.spl.net.api.ClientMessage;
import bgu.spl.net.api.Database;
import bgu.spl.net.api.User;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;

public class StatMsg extends ClientMessage {
    private String username;

    public StatMsg(String username){
        super((short) 8);
        this.username = username;
    }

    public StatMsg(){
        super((short) 8);
        this.username = null;
    }

    @Override
    public int getNumOfArguments() {
        return 1;
    }

    @Override
    public void setArguments(String[] s) {
        username = s[0];
    }

    /**
     * Process Stat msg : build The AckMsg by the specific user
     */
    @Override
    public void process(BGSProtocol bgsProtocol, Connections connections, Database db, int conId) {
        Message res = new ErrorMsg(this);
        User user;

        if(db.getActiveUser(conId) != null && (user = db.getRegisteredUser(username)) != null){
            res = new AckMsg(this);
            ((AckMsg)res).addShorts(new short[]{
                    (short) user.getNumOfPosts(),
                    (short) user.getAmountOfFollowers(),
                    (short) user.getAmountOfFollowing()
            });
        }
        connections.send(conId, res);
    }
}
