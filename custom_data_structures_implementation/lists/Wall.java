/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Kostas
 */
public class Wall {

    public tweet head;
    public tweet node;
    public int size;
    public tweet sentinel;

    public Wall() {
        head = null;
        size = 0;
        /*this.sentinel = sentinel;
        this.sentinel.next = null;
        this.sentinel.tid = 0;
        this.sentinel.uid = 0; */
    }

    public void add(tweet u) {
        tweet temp = u;
        tweet curr = head;
        if (head == null) {
            head = temp;
        } else {
            while (curr.getNext() != null) {
                curr = curr.getNext();
            }
            curr.setNext(temp);
            this.size++;
        }
    }

    public void remove(tweet t) {
        tweet curr = head;
        while (curr.getNext() != null) {
            if (curr.getTid() == t.getTid()) {
                curr.setNext(curr.getNext().getNext());
            }
        }
        this.size--;
    }

    public boolean search(int tid) {
        tweet curr = head;
        while (curr.getNext() != null) {
            if (curr.getTid() == tid) {
                return true;
            }
            curr = curr.getNext();
        }
        return false;
    }

    public tweet getSearch(int tid) {
        tweet curr = head;
        while (curr.getNext() != null) {
            if (curr.getTid() == tid) {
                return curr;
            }
            curr = curr.getNext();
        }
        return null;
    }

    @Override
    public String toString() {
        String out = "";
        out += "<" + this.head.getUid() + ":" + this.head.getTid() + "> ";
        tweet curr = head.getNext();
        while (curr != null) {
            out += "[" + curr.getUid() + ":" + curr.getTid() + "] ";
            curr = curr.getNext();
        }
        return out;
    }

    void set_head(tweet n) {
        this.head = n;
    }

    tweet get_head() {
        return this.head;
    }

    void set_node(tweet n) {
        this.node = n;
    }

    tweet get_node() {
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

    tweet search2(int tid) {
        tweet pointer = get_head(); //point to the start of the list
        while (pointer != this.sentinel) {
//if it's sorted,then I'll add && pointer.tid<=tid on the condition
            if (pointer.tid == tid) {
                return pointer;
            } else {
                pointer = pointer.next; //go to the next element/soldier of the list
            }
        }
        return get_head();
    }

    void traverse() { //better not int,amirite? 
        this.node = this.get_head();
        while (!this.node.equals(this.sentinel)) {
            System.out.print("<" + this.node.tid + ">,");
            this.node = this.node.next;

        }


    }
}
