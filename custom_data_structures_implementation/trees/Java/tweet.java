public class tweet {
    int tid;                                    /* Tweet identifier                  */   
    int uid;                                    /* User identifier                   */
    int timestamp;                              /* Publication date (form: YYYYMMDD) */
    tweet next;                         /* Pointer to the next node's leaf   */
    tweet lc;                           /* Pointer to the node's left child  */
    tweet rc;                            /* Pointer to the node's right child  */
    tweet()
    {
        this.lc=lc;
        this.next=next;
        this.rc=rc;
        this.tid=tid;
        this.timestamp=timestamp;
        this.uid=uid;
    }

   int get_uid(){return this.uid;}
   void set_uid(int u){this.uid=u;}
   int get_tid(){return this.tid;}
   void set_tid(int t){this.tid=t;}
   void set_timestamp(int t){this.timestamp=t;}
   int get_timestamp(){return this.timestamp;}
   void set_lc(tweet n){this.lc=n;}
   tweet get_lc(){ return this.lc;}
   void set_rc(tweet n){this.rc=n;}
   tweet get_rc(){ return this.rc;}
   void set_next(tweet n){this.next=n;}
   tweet get_next(){ return this.next;}
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


	}