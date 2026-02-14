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
//that's a sorted(by the timestamp field) leaf oriented binary search tree,emploutismeno fulloprosanatolismeno dentro

    tweet root;
    tweet node;
    int size;

    Wall() {

        this.node = node;
        this.root = root;
        this.size = size;
    }

    void set_size(int sz) {
        this.size = sz;
    }

    int get_size() {
        return this.size;
    }

    void set_root(tweet rt) {
        this.root = rt;
    }

    tweet get_root() {
        return this.root;
    }

    void set_node(tweet n) {
        this.node = n;
    }

    tweet get_node() {
        return this.node;
    }

    tweet search(int tid, int uid, int timestamp) {
        tweet pointer = new tweet();
        pointer = this.get_root();
        while (pointer != null && pointer.timestamp < pointer.rc.timestamp) {
//timestamp might not be needed 
            if (pointer.tid == tid && pointer.lc == null && pointer.rc == null) {
                return pointer;
            } else if (pointer.tid >= tid) {
                pointer = pointer.lc;
            } else {
                pointer = pointer.rc;
            }
        }
        return null;

    }

    int minimum(int a, int b) {
        if (a < b) {
            return a;
        } else if (b < a) {
            return b;
        } else if (b == a) {
            return a;
        }
        return 0;
    }

    int maximum(int a, int b) {
        if (minimum(a, b) == 0) {
            return 0;
        } else if (minimum(a, b) == b) {
            return a;
        } else if (minimum(a, b) == a) {
            return b;
        }
        return 0;
    }

    tweet insert(int tid) {

        //initializations
        tweet tmp = new tweet();
//the node that is gonna be replaced by the trilet of 3 new nodes,,based on the theory
        tmp = this.get_root();
        tweet p_tmp = new tweet(); //previous of tmp
        p_tmp = null;
        while (tmp != null) {
            if (tmp.tid <= tid) {
                tmp = tmp.lc;
            } else {
                tmp = tmp.rc;
            }

            p_tmp = tmp;
            if (tmp.lc.equals(null) || tmp.rc.equals(null)) {
                break;
            }
        }
        tweet np = new tweet();
        //np=null;
        if (tmp == null) {

            np.tid = tid;
            np.lc = np.next = np.rc = null;
            return np;

        } else {
            np.tid = minimum(tid, tmp.tid);
            np.lc.tid = minimum(tid, tmp.tid);
            np.rc.tid = maximum(tid, tmp.tid);
            np.lc.lc = np.lc.rc = null;
            np.rc.lc = np.rc.rc = null;
            np.lc.next = tmp.next;
            np.rc.next = tmp.next;
            tweet list = new tweet();
            list = null;
            if (p_tmp.rc == tmp) {
                list = p_tmp.lc;
                p_tmp.rc = np;
            } else {
                p_tmp.lc = np;
            }
            tweet prev = new tweet();
            while (list != null) {
                prev = list; //prev actually on the cheatsheet
                list = list.rc;

            }
            prev.next = np.lc; //might need to go inside the loop

        }
        return null;

    }

    tweet FindMinimumInSubtreeOf(tweet sub_tree_root) {
        tweet pointer = new tweet();
        pointer = sub_tree_root;
        if (pointer.equals(null)) {
            return null;
        }
        while (pointer.has_lc()) {
            pointer = pointer.lc;
        }
        if (pointer.next != null) {
            return pointer;
        }
        return null;

    }

    tweet FindMaximumInSubtreeOf(tweet sub_tree_root) {
        tweet pointer = new tweet();
        pointer = sub_tree_root;
        if (pointer.equals(null)) {
            return null;
        }
        while (pointer.has_rc()) {
            pointer = pointer.rc;
        }
        if (pointer.next != null) {
            return pointer;
        } else {
            return null;
        }
    }

    tweet delete(int tid) {
        tweet q = new tweet();
        q = this.get_root();
        tweet prev, pq, ppq, list, sibling; //previous of p and previous of the previous of p
//prev is 
        prev = new tweet();
        pq = new tweet();
        ppq = new tweet();
        list = new tweet();
        sibling = new tweet();
        while (q != null && !q.lc.equals(null) && !q.rc.equals(null)) {
            ppq = pq;
            pq = q;
            if (q.lc.equals(null) || !q.rc.equals(null)) {
                break; //I don't think this is actually needed/useful
            }
            if (q.tid > tid) {
                q = q.lc;
            } else {
                list = q;
                q = q.rc;
            }

        }

        if (q == null || q.tid == tid) {
            return this.root;
        }
        if (pq == null) {
            return null;
        }
        if (pq.lc == q) {
            sibling = pq.rc;
        } else {
            sibling = pq.lc;
        }
        if (ppq == null) {
            return sibling;
        }
        if (q == pq.rc) {
            list = pq.lc;
        } else {
            list = list.lc;
        }
        while (list != null) {
            prev = list;
            list = list.rc;

        }
        if (ppq.lc == pq) {
            ppq.lc = sibling;
            prev.next = ppq.lc;
        } else {
            prev.next = ppq.lc;
            ppq.rc = sibling;
        }

        return root;
    }

    void RangeQuery(int a, int b) {
        tweet pointer = new tweet();
        pointer = this.get_root();
        tweet previous_pointer = new tweet();
        previous_pointer = null; //previous of q-pointer
        while (pointer != null) {
            previous_pointer = pointer;
            if (a <= pointer.timestamp) {
                pointer = pointer.lc;
            } else {
                pointer = pointer.rc;
            }
        }
        pointer = previous_pointer;
        while (pointer != null && pointer.timestamp >= a && pointer.timestamp <= b) {

            System.out.print("<" + pointer.tid + ":" + pointer.uid + ":" + pointer.timestamp + ">,");
            pointer = pointer.next;

        }

    }

    boolean InOrder(tweet t) {

        if (t == null) {
            return false;
        } else {

            InOrder(t.lc);
            if (!t.has_lc() && !t.has_rc()) {
                System.out.print("<" + t.tid + ">,");
            }
            InOrder(t.next);
        }
        return true;
    }


}
