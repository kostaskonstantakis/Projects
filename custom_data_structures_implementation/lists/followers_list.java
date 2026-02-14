/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kostas
 */
public class followers_list {

    follower head;
    follower node;
    int size;

    public followers_list() {
        head = null;
        size = 0;

        /*
        this.head = new follower();
        this.node = new follower();
        this.head = head;
        this.node = node;
        this.size = size;
         */
    }

    public void add(follower f) {
        follower temp = f;
        follower curr = head;
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

    public void remove(follower f) {
        follower curr = head;
        while (curr.getNext() != null) {
            if (curr.getUid() == f.getUid()) {
                curr.setNext(curr.getNext().getNext());
            }
        }
        this.size--;
    }

    public boolean search(int uid) {
        follower curr = head;
        while (curr.getNext() != null) {
            if (curr.getUid() == uid) {
                return true;
            }
        }
        return false;
    }

    public follower getSearch(int uid) {
        follower curr = head;
        while (curr.getNext() != null) {
            if (curr.getUid() == uid) {
                return curr;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String out = "";
        out += "<" + this.head.getUid() + "> ";
        follower curr = head.getNext();
        while (curr != null) {
            out += "<" + curr.getUid() + "> ";
            curr = curr.getNext();
        }
        return out;
    }

    boolean isEmpty() {
        if (this.get_head() != null && this.size == 0) {
            return true;
        } else {
            return false;
        }
    }

    int get_size() {
        return this.size;
    }

    void set_size(int sz) {
        this.size = sz;
    }

    void set_head(follower n) {
        this.head = n;
    }

    follower get_head() {
        return this.head;
    }

    void set_node(follower n) {
        this.node = n;
    }

    follower get_node() {
        return this.node;
    }

    void increase_size() {
        this.size++;
    }

    void decrease_size() {
        this.size--;
    }

    void traverse()//need to have a ''pointer'' to the list's head
    {
        this.node = get_head();
        while (this.node != null) {
            System.out.print("<" + this.node.uid + ">,");
            this.node = this.node.next;

        }

    }

    /*
    follower search(int uid) {
        follower pointer = get_head(); //point to the start of the list
        while (pointer != null && pointer.uid <= uid) {
            if (pointer.uid==uid) {
                return pointer;
            } else {
                pointer = pointer.next; //go to the next element/soldier of the list
            }
        }
        return get_head();
    }


     void register(user u, follower f, int uid) //might need changes
    {

        if (!this.isEmpty()) {
            get_head().next = this.head;
            set_head(f);
        } else if (this.isEmpty()) {
            set_head(f);
        }
        f.uid = uid;

        this.size++;
    }

    void delete(follower f)//might change it,if I implement a traverse() function
    {

        while (!this.isEmpty() && this.node != null) {
            if (this.node == f) {
                this.size--;
                this.node.next = f.next;
                this.node.uid = f.next.uid;
                this.node.uid = f.next.uid;
                //needs more

            } else {
                this.node = node.next;
            }
        }

    }






     */
}
