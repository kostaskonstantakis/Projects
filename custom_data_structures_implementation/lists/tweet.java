

public class tweet {

    public int tid;
    public int uid;
    public int tag;
    public int timestamp;
    public int time_relevance;
    public tweet next;

    tweet(int uid, int tid, int tag, int timestamp, int time_relevance) {
        this.tid = tid;
        this.uid = uid;
        this.tag = tag;
        this.timestamp = timestamp;
        this.time_relevance = time_relevance;
    }

    /**
     * @return the tid
     */
    public int getTid() {
        return tid;
    }

    /**
     * @param tid the tid to set
     */
    public void setTid(int tid) {
        this.tid = tid;
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
     * @return the tag
     */
    public int getTag() {
        return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(int tag) {
        this.tag = tag;
    }

    /**
     * @return the timestamp
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the time_relevance
     */
    public int getTime_relevance() {
        return time_relevance;
    }

    /**
     * @param time_relevance the time_relevance to set
     */
    public void setTime_relevance(int time_relevance) {
        this.time_relevance = time_relevance;
    }

    /**
     * @return the next
     */
    public tweet getNext() {
        return next;
    }

    /**
     * @param next the next to set
     */
    public void setNext(tweet next) {
        this.next = next;
    }

}
