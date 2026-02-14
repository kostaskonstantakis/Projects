public class follower {
    int uid;                                    /* User identifier                   */
    follower lc;                        /* Pointer to the node's left child  */
    follower rc;
    follower()
    {
        this.lc=lc;
        this.rc=rc;
        this.uid=uid;
    }
    int get_uid(){return this.uid;}
   void set_uid(int u){this.uid=u;}
   void set_lc(follower n){this.lc=n;}
   follower get_lc(){return this.lc;}
   void set_rc(follower n){this.rc=n;}
   follower get_rc(){return this.rc;}
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

        

         boolean is_leaf() {
        if (this.get_lc() == null && this.get_rc() == null) {
            return true;
        } else {
            return false;
        }
    }

         boolean is_root() {
             if (this.lc.equals(null) && this.rc.equals(null)) {
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

    boolean isSpecificNode(follower f) {
        if (this.equals(f)) {
            return true;
        } else {
            return false;
        }

    }
     /* boolean isAFollowerOfMine(user u)
      {
          if(this.)

       }  */

	}