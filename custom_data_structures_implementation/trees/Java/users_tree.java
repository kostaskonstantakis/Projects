/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kostas
 */
public class users_tree {
//that's a sorted(by uid) doubly linked binary search tree,with a sentinel node
    user root;
    user node;
    user sentinel;
    int size;

    users_tree() {
        this.node = node;
        this.root = root;
        this.sentinel = sentinel;
        this.size = size;
        this.sentinel.followers = null;
        this.sentinel.lc = null;
        this.sentinel.par = root; //or it could be some node or null at first-NOT YET DONE!
        this.sentinel.rc = null;
        this.sentinel.uid = 0;
        this.sentinel.wall_head = null; //probably right
        this.root.par = null;
    }

    void set_size(int sz) {
        this.size = sz;
    }

    int get_size() {
        return this.size;
    }

    void set_root(user rt) {
        this.root = rt;
    }

    user get_root() {
        return this.root;
    }

    void set_node(user n) {
        this.node = n;
    }

    user get_node() {
        return this.node;
    }

    void set_sentinel(user s) { //dunno if this is actually useful,at all....:(
        this.sentinel = s;
    }

    user get_sentinel() {
        return this.sentinel;
    }

    void increase_size() {
        this.size++;
    }

    void decrease_size() {
        this.size--;
    }

    //code from the fall semester-MIGHT NEED FIXING FOR THE SENTINEL IMPLEMENTATION HERE
    boolean isEmpty() {
        if (this.root.equals(null) || this.root.equals(this.sentinel)) {
            return true;
        } else {
            return false;
        }
    }

    boolean InOrder(user s) {
       
        if (s == null) {
            return false;
        } else {
            
            InOrder(s.lc);
            System.out.print("<" + s.uid + ">,");

            InOrder(s.rc);
        }
        return true;
    }

    boolean PostOrder(user s) {
        
        if (s == null) {
            return false;
        } else {
            
            PostOrder(s.lc);
            PostOrder(s.rc);
            System.out.print(s.uid);

        }
        return true;
    }

    boolean PreOrder(user s) {
        
        if (s == null) {
            return false;
        } else {
            
            System.out.print(s.uid);
            PreOrder(s.lc);
            PreOrder(s.rc);

        }
        return true;
    }

    boolean InOrderX(user s) {

        if (s == null) {
            return false;
        } else {

            InOrderX(s.lc);
            System.out.println("\t\t\t User =" + s.uid);
            System.out.println("\t Followers:" + s.followers_tree.InOrder(s.followers_tree.root));
            System.out.println("\t Tweets:" + s.wall.InOrder(s.wall.root));
            InOrderX(s.rc);
        }
        return true;
    }

    int searchUid(int uid) {
        user u = new user(uid);
        u = this.get_root();
        this.sentinel.uid = uid;
       while ( u.uid!=uid )
       {
          if (uid < u.uid) u=u.lc;
          else u=u.rc;
       }
       if (!u.equals(this.sentinel)) return u.uid;
        else
            return -1;  //failure

        /* while (u != this.sentinel) {
            if (u.uid == uid) {
                return uid;
            } else if (u.uid < uid) {
                u = u.lc;
            } else {
                u = u.rc;
            }
        }
        return -1;  */
    }

    user searchUser(int uid) {
        user u = new user(uid);
        u = this.get_root();
        this.sentinel.uid = uid;
        while (u.uid != uid) {
            if (uid < u.uid) {
                u = u.lc;
            } else {
                u = u.rc;
            }
        }
        if (!u.equals(this.sentinel)) {
            return u;
        } else {
            return null;  //failure
        }
    }

    void register(int uid) {

        if (searchUid(uid) == uid) {
            System.err.println("This user is already in the tree!");
        } else {

            user pointer, new_node, previous;
            pointer = new user(uid);
            new_node = new user(0);
            previous = new user(0);
            pointer = this.get_root();
            while (pointer != this.sentinel) {
            previous = pointer;
            if (uid < pointer.uid) {
                pointer = pointer.lc;
            } else {
                pointer = pointer.rc;
                }
            new_node.uid = uid;
            new_node.lc = new_node.rc = this.sentinel;

            if (previous == null) {
                break;
                //return new_node;
            } else if (uid < previous.uid) {
                previous.lc = new_node;
            } else {
                previous.rc = new_node;
            }

            }

        this.R();

        }
    }

    boolean is_leaf() {
        if (this.node.get_lc() == this.get_sentinel() && this.node.get_rc() == this.get_sentinel() && !this.node.equals(this.sentinel)) {
            return true;
        } else {
            return false;
        }

    }

    void R() {

        System.out.println("\t\t\t Users=  " + this.InOrder(this.root));

    }

    void delete(user u, int uid) {
        if (this.searchUid(uid) != u.uid) //I didn't find that key in the tree-WHOOPS
        {
            System.err.println("This user doesn't exist or has been already deleted beforehand!");
        } else {

            if (!u.has_rc() && !u.has_lc()) //u is a leaf,delete him
            {
                u.uid = 0;
                u.followers = null;
                u.wall_head = null;
            } else if (!u.has_rc() && u.has_lc())//only a left child
            {
                u.lc = u;
                u.lc.lc = null;
                u.lc.rc = null;
                u.lc.uid = u.uid;

            } else if (u.has_rc() && !u.has_lc()) //u only has a rc {
            {
                u.rc = u;
            u.rc.rc = null;
            u.rc.lc = null;
            u.rc.uid = u.uid;

            } else if (u.has_rc() && u.has_lc()) //it's an internal node
              {

                  user replacement = new user(0);
                  replacement = this.InOrderSuccessorOf(u);
                  replacement.lc = u.lc;
                  replacement.rc = u.rc;
                  replacement.uid = u.uid;
                  replacement.followers = u.followers;
                  replacement.par = u.par;
                  replacement.wall_head = u.wall_head;
                  u.lc = u.rc = null;
                  u.uid = 0;
                  u.followers = null;
                  u.wall_head = null;
                  u = null;

            }

            /*   user to_be_deleted = new user();
            user replacement = new user();
            user replacement_child = new user();
            replacement_child = replacement.rc;
            if (to_be_deleted.lc == null || to_be_deleted.rc == null) {
                replacement = to_be_deleted;
            } else {
                replacement = this.InOrderSuccessor(to_be_deleted);
            }
            if (replacement.lc != null) {
                replacement_child = replacement.lc;
            } else {
                replacement_child = replacement.rc;
            }
            if (replacement_child != null) {
                replacement_child.par = replacement.par;
            }
            /*if (replacement.par == null) {
                break; //deletion of root
            } else*//* if (replacement == replacement.par.lc) {
                replacement.par.lc = replacement_child;
            } else {
                replacement.par.rc = replacement_child;
            }
            if (replacement != to_be_deleted) {
                to_be_deleted.uid = replacement.uid;
            }  */

    }
    }

    user InOrderSuccessorOf(user u) {

        if (u.equals(null) || u.equals(this.sentinel)) {
            return null;//if null or sentinel there's no next node
        } else if (!u.rc.equals(this.sentinel) && !u.par.equals(this.root)) {
            return u.rc; //if u is internal node,then
        } else if (!u.rc.equals(this.sentinel) && u.par.equals(this.root) && u.rc.has_rc()) {
            return u.rc.rc;//the next one is 2 levels below
        } else if (u.lc.equals(this.sentinel) && u.rc.equals(this.sentinel) && u.equals(u.par.lc)) {
            return u.par;
        } else if (u.lc.equals(this.sentinel) && u.rc.equals(this.sentinel) && u.equals(u.par.rc)) {
            return u.par.par;
        }
        return null;

    }

    void X() {
        this.InOrderX(this.root);

    }
}
