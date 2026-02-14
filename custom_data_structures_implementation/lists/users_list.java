

public class users_list {

    public user head;
    public user node;
    public int size;

    public users_list() {
        head = null;
        size = 0;
    }

    public void add(user u) {
        user temp = u;
        user curr = head;
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

    public void remove(user f) {
        user curr = head;
        while (curr.getNext() != null) {
            if (curr.getUid() == f.getUid()) {
                curr.setNext(curr.getNext().getNext());
            }
        }
        this.size--;
    }

    public boolean search(int uid) {
        user curr = head;
        while (curr.getNext() != null) {
            if (curr.getUid() == uid) {
                return true;
            }
            curr = curr.getNext();
        }
        return false;
    }

    public user getSearch(int uid) {
        user curr = head;
        while (curr.getNext() != null) {
            if (curr.getUid() == uid) {
                return curr;
            }
            curr = curr.getNext();
        }
        return null;
    }

    @Override
    public String toString() {
        String out = "";
        out += "<" + this.head.getUid() + "> ";
        user curr = head.getNext();
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

    void set_head(user n) {
        this.head = n;
    }

    user get_head() {
        return this.head;
    }

    void set_node(user n) {
        this.node = n;
    }

    user get_node() {
        return this.node;
    }

    void increase_size() {
        this.size++;
    }

    void decrease_size() {
        this.size--;
    }

    void X() {
        this.node = this.get_head();
        int counter = 1;
        while (this.node != null) {
            System.out.println("\tUser " + counter + " =" + this.node.uid);
            System.out.print("Followers: ");
            this.node.followers_list.traverse(); //followers_list maybe?-NOT DONE YET
            System.out.println("Tweets: ");
            this.node.Wall.traverse();//tweets_list or wall -NOT DONE YET
            counter++;
            this.node = this.node.next;
        }

    }

    int traverse()//need to have a ''pointer'' to the list's head
    {
        this.node = get_head();
        while (this.node != null) {
            System.out.print("<" + this.node.uid + ">,");
            this.node = this.node.next;

        }
        return 0;

    }

    /*previous implementation code-might not work!

    void register(user u,int uid) //might need changes
{
    Wall w = new Wall();
    if(!this.isEmpty())
    {
        get_head().next = this.head;
	set_head(u);
    }
    else if(this.isEmpty())
                set_head(u);
                u.uid=uid;
    u.wall_head = w.head;
//dunno if I should do this here,or in Main or in Wall(probably not the last one)
    u.wall_sentinel = w.sentinel;
    /*it might be the head actually when you first create the link between the pointer to head
                & the wall list itself */
 /*		this.size++;
}


void delete(user u)//might change it,if I implement a traverse() function
{

	while(!this.isEmpty()&&this.node!=null)
            {
                if(this.node==u)
                {
                    this.size--;
                    this.node.next = u.next;
                   this.node.uid=u.next.uid;
                    this.node.uid=u.next.uid;
                   /* u=null;//may need to be changed-I don't know
		    u.uid = 0;
                    u.next=null;  */
 /*    u.followers=null;
                    u.wall_head=null;
                    u.wall_sentinel=null;

                }
                else
                this.node=node.next;
            }


}

    




     */
    user search2(int uid) {
        user pointer = get_head(); //point to the start of the list
        while (pointer != null && pointer.uid <= uid) {
            if (pointer.uid == uid) {
                return pointer;
            } else {
                pointer = pointer.next; //go to the next element/soldier of the list
            }
        }
        return get_head();
    }

}
