/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Kostas
 */
public class tagged_tweet {

    int tid;
    int uid;
    int timestamp;
    int time_relevance;
    tagged_tweet next;
    tagged_tweet prev;

    tagged_tweet(int tid, int timestamp, int uid) {
        this.tid = tid;
        this.timestamp = timestamp;
        this.uid = uid;
    }

    tagged_tweet() {

    }

    int get_uid() {
        return this.uid;
    }

    void set_uid(int u) {
        this.uid = u;
    }

    void set_next(tagged_tweet t) {
        this.next = t;
    }

    tagged_tweet get_next() {
        return this.next;
    }

    int get_tid() {
        return this.tid;
    }

    void set_tid(int t) {
        this.tid = t;
    }

    void set_prev(tagged_tweet t) {
        this.prev = t;
    }

    tagged_tweet get_prev() {
        return this.prev;
    }

    int get_timestamp() {
        return this.timestamp;
    }

    void set_timestamp(int time) {
        this.timestamp = time;
    }

    int get_time_relevance() {
        return this.time_relevance;
    }

    void set_time_relevance(int tr) {
        this.time_relevance = tr;
    }

}
