package bgu.spl.net.api;

import bgu.spl.net.Message;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class User {
    /**
     *This Class returns a new instance of User that represent Client that connect to the server
     */
    private String username;
    private String password;
    private int connectionId;
    private boolean isLoggedIn;
    private ConcurrentLinkedQueue<Message> waitingPosts;
    private ConcurrentHashMap<String, User> followers;
    private ConcurrentHashMap<String, User> following;
    private List<Message> posts;
    private List<Message> pms;
    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.connectionId = -1;
        this.isLoggedIn = false;
        this.waitingPosts = new ConcurrentLinkedQueue<>();
        this.followers = new ConcurrentHashMap<>();
        this.following = new ConcurrentHashMap<>();
        this.posts = new LinkedList<>();
        this.pms = new LinkedList<>();
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public String getUsername() {
        return username;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public boolean isLoggedIn(){
        return isLoggedIn;
    }

    public ConcurrentHashMap<String, User> getFollowers(){ return followers; }

    public void logout(){
        isLoggedIn = false;
    }

    public void login(){
        isLoggedIn = true;
    }

    public int getAmountOfFollowers(){
        return followers.size();
    }

    public int getAmountOfFollowing(){
        return following.size();
    }

    public int getNumOfPosts(){
        return posts.size();
    }

    public void addPost(Message msg){
        posts.add(msg);
    }

    public void addPm(Message msg){
        pms.add(msg);
    }

    public boolean followOrUnfollow(int follow, User user){
        if(follow == 0)
            return follow(user);
        else
            return unfollow(user);
    }

    public boolean follow(User user){
        String username = user.getUsername();
        if(following.putIfAbsent(username, user) == null){
            user.addFollower(this);
            return true;
        }
        return false;
    }

    public boolean addFollower(User user){
        String username = user.getUsername();
        if(followers.putIfAbsent(username, user) == null){
            return true;
        }
        return false;
    }

    public boolean unfollow(User user){
        String username = user.getUsername();
        if(following.remove(username) != null){
            user.removeFollower(this);
            return true;
        }

        return false;
    }

    public boolean removeFollower(User user){
        String username = user.getUsername();
        if(followers.remove(username) != null){
            return true;
        }

        return false;
    }

    public void addPostToQueue(Message msg){
        waitingPosts.add(msg);
    }

    public Message getNextMsg(){
        if(!waitingPosts.isEmpty())
            return waitingPosts.poll();

        return null;
    }

    public String getPassword() {
        return password;
    }

}
