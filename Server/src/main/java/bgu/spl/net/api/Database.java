package bgu.spl.net.api;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class Database {

    /**
     *This Class represent the Data Structure that saves all the info we need in the Server
     */
    private ConcurrentHashMap<String, User> registeredUsers;
    private ConcurrentHashMap<Integer,String> usersSessionId;
    private Vector<String> usersList;

    public Database(){
        registeredUsers = new ConcurrentHashMap<>();
        usersSessionId = new ConcurrentHashMap<>();
        usersList = new Vector<>();
    }

    public Vector<String> getUsersList(){
        return usersList;
    }

    public boolean isRegistered(String username){
        return registeredUsers.containsKey(username);
    }

    public void registerUser(User user){
        String username = user.getUsername();
        registeredUsers.putIfAbsent(username, user);
        usersList.add(username);
    }

    public User getActiveUser(int connectionId){
        String username = usersSessionId.get(connectionId);
        if(username != null)
            return getRegisteredUser(username);
        return null;
    }

    public boolean isActive(int connectionId){
            return usersSessionId.containsKey(connectionId);
    }

    public User getRegisteredUser(String username){
        return registeredUsers.get(username);
    }

    public String getConnectedUser(int id){ return usersSessionId.get(id);}

    public void logoutUser(int connectionId){
        //remove entry from usersSession
        String username = usersSessionId.get(connectionId);

        User user = registeredUsers.get(username);
        //set loggedIn field in User to false
        user.logout();
        usersSessionId.remove(connectionId);
        //set User's connection id to be -1
        user.setConnectionId(-1);
    }

    public void loginUser(int connectionId, String username){
        //add entry to usersSession
            usersSessionId.putIfAbsent(connectionId, username);

            User user = registeredUsers.get(username);
            //set loggedIn field in User to true
            user.login();
            //set User's connection id to be new connection id
            user.setConnectionId(connectionId);
        }

    public ConcurrentHashMap<String, User> getRegisteredUsers() {
        return registeredUsers;
    }
}
