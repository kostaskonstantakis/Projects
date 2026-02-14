/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kostas
 */
public class followers_tree {
//that's a sorted (by uid) binary search tree,WITHOUT A SENTINEL NODE (as it seems)

    follower root;
    follower node;
    int size;

    followers_tree() {
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

    void set_root(follower rt) {
        this.root = rt;
    }

    follower get_root() {
        return this.root;
    }

    void set_node(follower n) {
        this.node = n;
    }

    follower get_node() {
        return this.node;
    }

    //code from the fall semester
    boolean isEmpty() {
        if (this.root.equals(null)) {
            return true;
        } else {
            return false;
        }
    }

    boolean InOrder(follower f) {

        if (f == null) {
            return false;
        } else {

            InOrder(f.lc);
            System.out.print("<" + f.uid + ">,");
            InOrder(f.rc);
        }
        return true;
    }

    boolean PostOrder(follower f) {

        if (f == null) {
            return false;
        } else {

            PostOrder(f.lc);
            PostOrder(f.rc);
            System.out.print(f.uid);

        }
        return true;
    }

    boolean PreOrder(follower f) {

        if (f == null) {
            return false;
        } else {

            System.out.print(f.uid);
            PreOrder(f.lc);
            PreOrder(f.rc);

        }
        return true;
    }

    //might actually be useless or  may need to be modified...
    boolean is_leaf() {
        if (this.node.get_lc() == null && this.node.get_rc() == null) {
            return true;
        } else {
            return false;
        }

    }

    follower searchFollower(follower f, int uid) {
        if (f == null) {
            return null;
        } else if (uid == f.uid) {
            return f;
        } else if (uid < f.uid) {
            return searchFollower(f.lc, uid);
        } else {
            return searchFollower(f.rc, uid);
        }
    }

    boolean search(int uid) {
        follower f = new follower();
        f = this.get_root();
        while (!f.equals(null)) {
            if (uid == f.uid) {
                return true;
            } else if (f.uid > uid) {
                f = f.lc;
            } else if (f.uid < uid) {
                f = f.rc;
            }

        }
        return false;

    }

    void register(int uid) {
        if (search(uid))
            System.err.println("This user is already in the tree!");
        else {

            follower pointer = new follower();
            follower previous = new follower();
            pointer = this.get_root();
            while (pointer != null) {
                previous = pointer;
                if (uid < pointer.uid) {
                    pointer = pointer.lc;
                } else {
                    pointer = pointer.rc;
                }


                pointer.lc = pointer.rc = null;

                if (uid < previous.uid) {
                    previous.lc = pointer;
            } else {
                    previous.rc = pointer;
                }

            }

        }

    }

    void delete(follower f, int uid) {

        if (!search(uid)) {
            System.err.println("This user isn't in the tree!It might never got inserted or deleted beforehand");
        } else {
            if (!f.has_rc() && !f.has_lc()) {
                f.uid = 0;
                f = null;
            } else if (!f.has_rc() && f.has_lc()) {
                f.lc = f;
                f.lc.lc = null;
                f.lc.rc = null;
                f.lc.uid = f.uid;

            } else if (f.has_rc() && !f.has_lc()) {
                f.rc = f;
                f.rc.rc = null;
                f.rc.lc = null;
                f.rc.uid = f.uid;

            } else if (f.has_rc() && f.has_lc()) { // 3rd case

                follower replacement = new follower();
                replacement = this.InOrderSuccessorOf(f);
                replacement.lc = f.lc;
                replacement.rc = f.rc;
                replacement.uid = f.uid;
                f.lc = f.rc = null;
                f.uid = 0;
                f = null;


            }


        }

    }

    follower FindMinimumInSubtreeOf(follower sub_tree_root) {
        follower pointer = new follower();
        pointer = sub_tree_root;
        if (pointer.equals(null)) {
            return null;
        }
        while (pointer.has_lc()) {
            pointer = pointer.lc;
        }
        return pointer;

    }

    follower FindMaximumInSubtreeOf(follower sub_tree_root) {
        follower pointer = new follower();
        pointer = sub_tree_root;
        if (pointer.equals(null)) {
            return null;
        }
        while (pointer.has_rc()) {
            pointer = pointer.rc;
        }
        return pointer;

    }


    follower InOrderSuccessorOf(follower f) {

        if (f.equals(null)) {
            return null;//if null or sentinel there's no next node
        } else if (f.equals(this.root.lc)) {
            return this.root;
        } else if (f.equals(this.root.rc)) {
            return this.FindMinimumInSubtreeOf(f.rc);

            //my successor is the minimum uid greater than me,which is located in the subtree of my rc
        } else if (!f.has_lc() && !f.has_rc() && f.is_leaf()) {//f is a leaf

            if (f.equals(this.InOrderSuccessorOf(f).lc))
                return this.InOrderSuccessorOf(f); //if I'm an lc of my father,he's the next one InOrder traversal
            else
                return this.FindMinimumInSubtreeOf(this.root.lc.rc);
                /*f is rc of its father,which in turn is the previous one in InOrder
or the next one in PostOrder.My successor in this case is the minimum node/uid in the subtree of my grandpa*/

            //if it's a lc then the father is next,otherwise it's the next minimum greater uid in a leaf nearby

        }
        return null;
    }



}
