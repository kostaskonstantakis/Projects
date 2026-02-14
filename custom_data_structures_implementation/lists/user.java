

public class user {

    public int uid;
    public follower followers;
    public Wall Wall;
    public tweet wall_sentinel;
    public user next;
    public tweet wall_head;
    public followers_list followers_list;

    user(int uid) {
        this.uid = uid;
        this.next = null;
        this.followers = new follower(uid);
        this.Wall = new Wall();
        this.followers_list = new followers_list();
        this.followers = this.followers_list.head;
        this.wall_head = this.Wall.head;
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
     * @return the followers_list
     */
    public follower getFollowers() {
        return followers;
    }

    /**
     * @param followers_list the followers_list to set
     */
    public void setFollowers(followers_list followers_list) {
        this.followers = followers_list.head;
    }

    /**
     * @return the tweets
     */
    public Wall getWall() {
        return Wall;
    }

    /**
     * @param tweets the tweets to set
     */
    public void setTweets(Wall tweets) {
        this.Wall = tweets;
    }

    /**
     * @return the wall_sentinel
     */
    public tweet getWall_sentinel() {
        return wall_sentinel;
    }

    /**
     * @param wall_sentinel the wall_sentinel to set
     */
    public void setWall_sentinel(tweet wall_sentinel) {
        this.wall_sentinel = wall_sentinel;
    }

    /**
     * @return the next
     */
    public user getNext() {
        return next;
    }

    /**
     * @param next the next to set
     */
    public void setNext(user next) {
        this.next = next;
    }

    public tweet getWallHead() {
        return wall_head;
    }

    public void setWallHead(tweet wh) {
        this.wall_head = wh;
    }

    public followers_list getFollowers_list() {
        return followers_list;
    }

    public void setFollowers_list(followers_list fl) {
        this.followers_list = fl;
    }

    boolean isAFollowerOfMine(user u, int uid) {
        while (this.followers != null) {
            if (this.followers.equals(u)) {
                return true;
            } else {
                this.followers = this.followers.next;
            }
        }
        return false;
    }


}
