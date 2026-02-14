/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Kostas
 */
public class follower {

    public int uid;
    public follower next;

    follower(int uid) {
        this.uid = uid;
    }

    /**
     * @return the uid
     */
    public int getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(int uid) {
        this.uid = uid;
    }

    /**
     * @return the next
     */
    public follower getNext() {
        return next;
    }

    /**
     * @param next the next to set
     */
    public void setNext(follower next) {
        this.next = next;
    }

}
