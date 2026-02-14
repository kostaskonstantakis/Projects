/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Kostas
 */
public class Hashtag_list {

    tagged_tweet head;
    tagged_tweet node;
    int size;

    Hashtag_list() {
        this.head = head;
        this.node = node;
        this.size = size;
    }

    void set_head(tagged_tweet n) {
        this.head = n;
    }

    tagged_tweet get_head() {
        return this.head;
    }

    void set_node(tagged_tweet n) {
        this.node = n;
    }

    tagged_tweet get_node() {
        return this.node;
    }

    void increase_size() {
        this.size++;
    }

    void decrease_size() {
        this.size--;
    }

    int get_size() {
        return this.size;
    }

    void set_size(int sz) {
        this.size = sz;
    }

    int traverse()//need to have a ''pointer'' to the list's head
    {

        this.node = this.get_head();
        while (this.node != null && this.node.timestamp < this.node.next.timestamp) //or could be this.node.prev.timestamp<this.node.timestamp
        {
            System.out.print("<" + this.node.tid + ":" + this.node.uid + ":" + this.node.timestamp + ":" + this.node.time_relevance + ">,");
            this.node = this.node.next;

        }
        return 0;

    }

    tagged_tweet search(int tid) {
        tagged_tweet pointer = get_head(); //point to the start of the list
        while (pointer != null && pointer.timestamp <= pointer.next.timestamp) {

            if (pointer.tid == tid) {
                return pointer;
            } else {
                pointer = pointer.next; //go to the next element/soldier of the list
            }
        }
        return get_head();
    }

    void ZigZag(tagged_tweet t, int lower, int upper) {
        tagged_tweet pointer1 = new tagged_tweet();
        tagged_tweet pointer2 = new tagged_tweet();
        pointer1 = t;
        pointer2 = t;
        while (pointer1 != null && pointer1.timestamp >= lower) {
            System.out.print("<" + pointer1.tid + ":" + pointer1.uid + ":" + pointer1.timestamp + ":" + pointer1.time_relevance + ">, ");
            pointer1 = pointer1.prev;
        }

        while (pointer2 != null && pointer2.timestamp <= upper) {
            System.out.print("<" + pointer2.tid + ":" + pointer2.uid + ":" + pointer2.timestamp + ":" + pointer2.time_relevance + ">, ");
            pointer2 = pointer2.next;
        }

    }
}
