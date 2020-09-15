package bgu.spl.net.api.messages;

import bgu.spl.net.Message;
import bgu.spl.net.api.BGSProtocol;
import bgu.spl.net.api.ClientMessage;
import bgu.spl.net.api.Database;
import bgu.spl.net.api.User;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PostMsg extends ClientMessage {
    private String content;

    public PostMsg(String content){
        super((short) 5);
        this.content = content;
    }

    public PostMsg(){
        super((short) 5);
        this.content = null;
    }

    @Override
    public int getNumOfArguments() {
        return 1;
    }

    @Override
    public void setArguments(String[] s) {
        content = s[0];
    }

    /** Process The Post Msg:
     * check all the condition if the Post action is possible otherwise send a error msg
     * puts all the Tagged users and Followers in one list and remove Duplicate names and Send NOTIFICATION Msg to relevant users
     */
    @Override
    public void process(BGSProtocol bgsProtocol, Connections connections, Database db, int conId) {
        User sender;

        if ((sender = db.getActiveUser(conId)) != null){
            connections.send(conId, new AckMsg(this)); //ACK for being able to post
            String sendersName = sender.getUsername();
            sender.addPost(this);
            //Add all the taggedFriends and the followers into one list and then remove duplicate name
            ConcurrentHashMap<String, User> followers = sender.getFollowers();
            List<String> recipientsUsername = getUsernames(content);
            followers.forEachKey(1, username -> recipientsUsername.add(username));

            List<String> names = removeDuplicates(recipientsUsername);
            names.forEach(username -> {
                User user;
                if((user = db.getRegisteredUser(username)) != null){
                    sendPostAck(connections, user, sendersName);
                }
            });

        } else connections.send(conId, new ErrorMsg(this));
    }
    /**
     * @return List of the names that written after '@' in Message
     */
    private static List<String> getUsernames(String line){ //take the tagged users from the post
        List<String> names = new LinkedList<>();
        int charInd = line.indexOf("@");
        while(charInd != -1 && charInd < line.length()-1){
            String name;
            int nextSpace = line.indexOf(" ", charInd);
            if(nextSpace != -1){
                name = line.substring(charInd+1, nextSpace);
                charInd = line.indexOf("@", nextSpace);
            }

            else {
                name = line.substring(charInd + 1);
                charInd = -1;
            }

            if(name.length() > 0)
                names.add(name);
        }
        return names;
    }
    /**
     * @return List of the names without duplicate names
     */
    private static List<String> removeDuplicates(List<String> names){
        List<String> newList = new LinkedList<>();
        names.forEach(name ->{
            if(!newList.contains(name))
                newList.add(name);
        });
        return newList;
    }
    /**
     * Creat An Ack Msg for each user that the Post msg is relevant to him
     */
    private void sendPostAck(Connections connections, User user, String sendersName){
        synchronized (user) {
            Message resUser = new NotificationMsg(1, sendersName, content);
            int userConId = user.getConnectionId(); //Is user logged in right now?
            if (userConId != -1)
                connections.send(userConId, resUser);
            else user.addPostToQueue(resUser);
        }
    }


}
