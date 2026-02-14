public class tweet_hashTable {
    int tid;                                    /* Tweet identifier                  */
    int uid;                                    /* User identifier                   */
    int timestamp;                              /* Publication date (form: YYYYMMDD) */
    char remove;                               /* Delete tweet flag */

    tweet_hashTable()
    {
        this.remove=remove;
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
   void set_remove(char r){this.remove=r;}
   char get_remove(){ return this.remove;}

   public boolean remove_in_proper_range()
   {
       if (this.remove == 'T' || this.remove == 'F')
           return true;
       else return false;

   }

}	                