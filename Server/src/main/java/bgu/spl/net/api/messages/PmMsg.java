package bgu.spl.net.api.messages;

import bgu.spl.net.Message;
import bgu.spl.net.api.BGSProtocol;
import bgu.spl.net.api.ClientMessage;
import bgu.spl.net.api.Database;
import bgu.spl.net.api.User;
import bgu.spl.net.api.bidi.Connections;

public class PmMsg extends ClientMessage {
    private String username;
    private String content;

    public PmMsg(String username,String content) {
        super((short) 6);
        this.username = username;
        this.content = content;
    }

    public PmMsg() {
        super((short) 6);
        this.username = null;
        this.content = null;
    }

    @Override
    public int getNumOfArguments() {
        return 2;
    }

    @Override
    public void setArguments(String[] s) {
        username = s[0];
        content = s[1];
    }

    /** Process The PM Msg:
     * check all the condition if the PM msg can be send. Send ack if the pm can be send
     */
    @Override
    public void process(BGSProtocol bgsProtocol, Connections connections, Database db, int conId) {
        User sender, recipient;


        if (((sender = db.getActiveUser(conId)) != null) && (recipient = db.getRegisteredUser(username)) != null){
            connections.send(conId, new AckMsg(this));
            sender.addPm(this);

            Message resUser = new NotificationMsg(0, sender.getUsername(), content);
            synchronized(recipient) {
                int conUserId = recipient.getConnectionId();
                if(conUserId != -1)
                    connections.send(conUserId, resUser);
                else recipient.addPostToQueue(resUser);
            }
        }
        else connections.send(conId, new ErrorMsg(this));
    }
}
