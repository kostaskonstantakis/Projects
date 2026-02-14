public class user
{
    int uid;                                    /* User identifier                   */
    follower followers;                 /* Pointer to the node's followers   */
    followers_tree followers_tree;
    tweet wall_head;                    /* Pointer to the node's tweet wall  */
    Wall wall;
    user par;                           /* Pointer to the node's parent      */
    user lc;                            /* Pointer to the node's left child  */
    user rc;
	user(int uid)
	{
	  this.uid=uid;
	  this.followers=followers;
	  this.wall_head=wall_head;
	  this.par=par;
	  this.lc=lc;
            this.rc = rc;
            this.followers_tree = new followers_tree();
            this.wall = new Wall();
            this.followers = this.followers_tree.root;
            this.wall_head = this.wall.root;
	}

   int get_uid(){return this.uid;}
   void set_uid(int u){this.uid=u;}
   void set_followers(follower f){this.followers=f;}
   follower get_followers(){return this.followers;}
   void set_wall_head(tweet t){this.wall_head=t;}
   tweet get_wall_head(){return this.wall_head;}
   void set_lc(user n){this.lc=n;}
   user get_lc(){ return this.lc;}
   void set_rc(user n){this.rc=n;}
   user get_rc(){ return this.rc;}
   void set_par(user n){this.par=n;}
   user get_par(){ return this.par;}
   boolean has_lc()
        {
		if(this.get_lc()!=null)
			return true;
		else
                    return false;

	}

	boolean has_rc()
        {
		if(this.get_rc()!=null)
                    return true;
		else
                    return false;
	}

        //might be useless actually,without taking into consideration the sentinel node
        boolean is_leaf() {
        if (this.get_lc() == null && this.get_rc() == null) {
            return true;
        } else {
            return false;
        }
    }

         boolean is_root() {
             if (this.get_par().equals(null)) {
            return true;
        } else {
            return false;
        }
    }

    boolean is_node() {
        if (!this.is_leaf() && !this.is_root()) {
            return true;
        } else {
            return false;
        }
    }

    boolean isSpecificNode(user s) {
        if (this.equals(s)) {
            return true;
        } else {
            return false;
        }

    }
     boolean isAFollowerOfMine(int uid) {

            if (this.followers_tree.search(uid)) {
                return true;
            } else {
                return false;
            }
        }
        
}