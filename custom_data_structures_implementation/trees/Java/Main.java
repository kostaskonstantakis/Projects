
/******************************************************
 *                                                    *
 * file: Main.java                                    *
 *                                                    *
 * @Author:   Iacovos G. Kolokasis                    *
 * @Version:  30-03-2018                              *
 * @email:    kolokasis@csd.uoc.gr                    *
 *                                                    *
 * Source file for the needs of cs-240b project 2018  *
 *                                                    *
 ******************************************************
 */
import java.io.*;

public class Main {


/* Global size of tweets hashTable */
    public static int max_tweets_g = 953;//as closer to the max as possible,that way not so many sollicions happen,even though the array is gigantic :(

    public static tweet_hashTable HashTable[];



    public static int max_tweets = 1000;

 


    public static users_tree users_tree;
    /* Global variable, array of primes */ //this might not be needed
//THE HASHTABLE'S SIZE MUST BE SELECTED FROM THESE PRIME NUMBERS!!!
/*public static int primes_g[

    160] ={  5
    

    ,   7,  11,  13,  17,  19,  23,  29,  31,  37,41,  43,  47,  53,  59,  61,  67,  71,  73,  79,83,  89,  97, 101, 103, 107, 109, 113, 127, 131137, 139, 149, 151, 157, 163, 167, 173, 179, 181,191, 193, 197, 199, 211, 223, 227, 229, 233, 239,241, 251, 257, 263, 269, 271, 277, 281, 283, 293,307, 311, 313, 317, 331, 337, 347, 349, 353, 359,367, 373, 379, 383, 389, 397, 401, 409, 419, 421,431, 433, 439, 443, 449, 457, 461, 463, 467, 479,487, 491, 499, 503, 509, 521, 523, 541, 547, 557,563, 569, 571, 577, 587, 593, 599, 601, 607, 613,617, 619, 631, 641, 643, 647, 653, 659, 661, 673,677, 683, 691, 701, 709, 719, 727, 733, 739, 743,751, 757, 761, 769, 773, 787, 797, 809, 811, 821,823, 827, 829, 839, 853, 857, 859, 863, 877, 881,883, 887, 907, 911, 919, 929, 937, 941, 947, 953};
*/

    static int hashFunction1(int x) {
        return x % max_tweets_g;
    }

    static int hashFunction2(int x) {
        return (x * x) % max_tweets_g;
    }


    public static int H1(int K, int o) { //H(K,0)=h1(x);
        return hashFunction1(K);
    }

    public static int H2(int K, int i) { //H(K,i+1)=(H(K,i) +h2(x))mod m
        return ((H1(K, i - 1) + hashFunction2(K)) % max_tweets_g);
    }

    public void HashTableInsert(tweet_hashTable t) {
        int position1, position2;
        position1 = this.H1(t.tid, 0);
        position2 = this.H2(t.tid, 1);

        if (HashTable[position1].tid != t.tid && HashTable[position1].remove == 'F') {
            if (HashTable[position2].tid != t.tid && HashTable[position2].remove == 'F') {
                for (int i = 0; i < max_tweets_g; i++) {
                    if (i != position1 || i != position2) {
                    if (HashTable[i].remove == 'T' && HashTable[i].tid == 0) {
                        HashTable[i].remove = 'F';
                        HashTable[i].tid = t.tid;
                        HashTable[i].timestamp = t.timestamp;
                        HashTable[i].uid = t.uid;
                        HashTable[i] = t;
                        }
                    }
                }

            } else if (HashTable[position2].tid != t.tid && HashTable[position2].remove == 'T') {
                HashTable[position2].remove = 'F';
                HashTable[position2].tid = t.tid;
                HashTable[position2].timestamp = t.timestamp;
                HashTable[position2].uid = t.uid;
                HashTable[position2] = t;

            }

        } else if (HashTable[position1].tid != t.tid && HashTable[position1].remove == 'T') {
            HashTable[position1].remove = 'F';
            HashTable[position1].tid = t.tid;
            HashTable[position1].timestamp = t.timestamp;
            HashTable[position1].uid = t.uid;
            HashTable[position1] = t;

        }

    }

    public static int HashTableSearch(tweet_hashTable t) {
        int position1, position2;
        position1 = H1(t.tid, 0);
        position2 = H2(t.tid, 1);
        if (HashTable[H1(t.tid, 0)].equals(t)) {
            return H1(t.tid, 0);
        } else if (HashTable[position2].equals(t)) {
            return position2;
        } else {
            for (int i = 0; i < max_tweets_g; i++) {
                if (i == position1 || i == position2) {
                    i++;
                }
                if (HashTable[i].equals(t)) {
                    return i;
                }
            }
        }
        return -1;
    }

    void HashTableDelete(tweet_hashTable t) {
        int position = 0;
        position = this.HashTableSearch(t);
        if (HashTable[position].equals(t) && HashTable[position].remove == 'F') {
            HashTable[position].remove = 'T';
        }
        //else if (HashTable[position].equals(t)&&HashTable[position].remove=='T') break;
    }

	 
	/*
	 * 1) You need to create the classes that correspond to the C structs
	 * 2) You need to create the global variables like, in the C header
     */
	
    /**
	 * @brief Optional function to initialize data structures that 
	 *        need initialization
	 *
	 * @return true on success
	 *         false on failure
	 */
	public static boolean initialize()
	{
            HashTable = new tweet_hashTable[max_tweets_g];
            users_tree = new users_tree();
		return true;
	}

	/**
	 * @brief Register user
	 *
	 * @param uid The user's id
	 *
	 * @return true on success
	 *         false on failure
	 */
	public static boolean register_user(int uid)
    {
        System.out.println("R " + uid);
        users_tree.register(uid);
        System.out.println("DONE");
		return true;
	}

	/**
	 * @brief User uid1 is now following user uid2
	 *
	 * @param uid1 The 1st user's id
	 * @param uid2 The 2nd user's id
	 *
	 * @return true on success
	 *         false on failure
	 */
	public static boolean subscribe(int uid1, int uid2)
    {

        System.out.println("S " + uid1 + " " + uid2);
        user u = users_tree.searchUser(uid1);
        if (!u.equals(null)) {
            if (!u.isAFollowerOfMine(uid2)) {
                u.followers_tree.register(uid2); //might throw a null pointer
            System.out.print("\t\t\t Followers = ");
            u.followers_tree.InOrder(u.followers_tree.root);

            } else
                System.err.println("User " + uid2 + "already is a follower of user " + uid1 + " !");
        } else {
            System.err.println("User " + uid1 + "doesn't exist in the users' tree!");
        }
        System.out.println("DONE");
		return true;
	}

	/**
	 * @brief New tweet event
	 * 
	 * @param uid            The user who posted the tweet
	 * @param tid            The tweet's id
	 * @param timestamp      The time the tweet was posted
	 * 
	 * @return true on success
	 *         false on failure
	 */
	public static boolean tweet(int uid, int tid, int timestamp)
	{
		return true;
	}

	/**
	 * @brief User uid1 stopped following user uid2
	 *
	 * @param uid1 The 1st user's id
	 * @param uid2 The 2nd user's id
	 *
	 * @return true on success
	 *         false on failure
	 */
	public static boolean unsubscribe(int uid1, int uid2)
    {
        System.out.println("U " + uid1 + " " + uid2);
        user u = users_tree.searchUser(uid1);
        if (!u.equals(null)) {
            if (u.isAFollowerOfMine(uid2)) {

                u.followers_tree.delete(null, uid2);
                user u2 = users_tree.searchUser(uid2);
                if (u2 != null) {
                    //search for tweets that belong to uid1
                }
            }

        //bla bla -more code goes here
        System.out.println("DONE");

        }
        return true;
    }

	/**
	 * @brief Delete user account
	 * 
	 * @param uid The id of the user account to delete
	 * 
	 * @return true on success
	 *         false on failure
	 */
	public static boolean delete_user(int uid)
    {

		return true;
    }

    /**
     * @brief Get the history of the user tweets
     *
     * @param uid   The id of the user account
     * @param date1 Start date
     * @param date2 End date
     *
     * @return  1 on success
     *          0 on failure
     */
    public static boolean history(int uid, int date1, int date2)
    {
        System.out.println("H " + uid + " " + date1 + " " + date2);
        user u = users_tree.searchUser(uid);
        if (!u.equals(null)) {

            int min = 0;
            min = u.wall.minimum(date1, date2);
            tweet minimum = new tweet();
            minimum = u.wall.FindMinimumInSubtreeOf(u.wall.root);//might not be correct
            if (minimum.timestamp >= date1 && minimum.timestamp < date2 && !minimum.next.equals(null)) {
                System.out.print("\t\t\t User = ");
            }
            u.wall.RangeQuery(date1, date2);
        } else
            System.err.println("User " + uid + " doesn't exist or didn't write any tweets during that period");

        //bla bla more code here
        System.out.println("DONE");
        return true;
    }

	/**
	 * @brief Lookup tweet in the tweets hash table
	 * 
	 * @param tid The tweet's id
	 *
	 * @return true on success
	 *         false on failure
	 */
	public static boolean lookup(int tid)
    {
        System.out.println("L " + tid);
        tweet_hashTable t = new tweet_hashTable();
        t.tid = tid;
        int position = 0;
        position = HashTableSearch(t);
        System.out.println("\t\t\t Tweet = <" + HashTable[position].tid + ":" + HashTable[position].uid + ":" + HashTable[position].timestamp + ">");
        System.out.println("DONE");
		return true;
	}

	/**
	 * @brief Print all active users
	 *
	 * @return true on success
	 *         false on failure
	 */
	public static boolean print_users()
    {
        System.out.println("X");
        users_tree.X();
        System.out.println("DONE");
		return true;
	}

	/**
	 * @brief Print all tagged tweets
	 *
	 * @return true on success
	 *         false on failure
	 */
	public static boolean print_tweets()
    {
        System.out.println("Y");
        System.out.println(" Tweets:");
        for (int i = 0; i < max_tweets_g; i++) {
            System.out.println("\t\t\tTweet " + i + 1 + " = <" + HashTable[i].tid + ":" + HashTable[i].uid + ":" + HashTable[i].timestamp + ">");
        }

        System.out.println("DONE");
		return true;
	}

	/**
	 * @brief The main function
	 *
	 * @param argc Number of arguments
	 * @param argv Argument vector
	 *
	 * @return 0 on success
	 *         1 on failure
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException
	{
		BufferedReader inputFile;       /* Input file                          */
		String line;                    /* Data for eachline of the input file */
		String [] params;               /* Event parameters arguments          */
        int t_hashtable_size;           /* Hash table size                     */

		/* Check command buff arguments */       
		if (args.length != 2) {
			System.err.println("Usage: <executable-name> <hashtable_size> <input_file>");
			System.exit(0);
		}

        /* Read the size of the hash table */
        t_hashtable_size = Integer.parseInt(args[0]);

		/* Open input file */        
		inputFile = new BufferedReader(new FileReader(args[1]));

		/* Read input file and handle the events */
		while ((line = inputFile.readLine()) != null) {

			if (line.length() == 0) continue;

			System.out.println(">>> Event: " + line);
			params = line.split(" ");
			char eventID = line.charAt(0);

			switch(eventID) {

				/* Comment */
				case '#':
					break;

				/* Parse Hashtable size */
				case 'Z':
                    max_tweets_g = Integer.parseInt(params[1]);
					break;
				
                    /* Register user
				 * R <uid> */
				case 'R':
					{
						int uid = Integer.parseInt(params[1]);

						if (register_user(uid)) {
							System.out.println("R " + uid + " succeeded");
						} else {
							System.err.println("R " + uid + " failed");
						}

						break;
					}

				/* User uid1 is now following user uid2
				 * S <uid1> <uid2> */
				case 'S':
					{
						int uid1 = Integer.parseInt(params[1]);
						int uid2 = Integer.parseInt(params[2]);

						if (subscribe(uid1, uid2)) {
							System.out.println("S " + uid1 + " " + uid2 + " succeeded");
						} else {
							System.err.println("S " + uid1 + " " + uid2 + " failed");
						}

						break;
					}

				/* New tweet event
				 * T <uid> <tid> <timestamp> */
				case 'T':
					{   
						int uid            = Integer.parseInt(params[1]);
						int tid            = Integer.parseInt(params[2]);
						int timestamp      = Integer.parseInt(params[3]);

                                            if (tweet(uid, tid, timestamp)) {
                                                System.out.println("T " + uid + " " + tid + " " + timestamp + " succeeded");
                                            } else {
                                                System.err.println("T " + uid + " " + tid + " " + timestamp + " failed");
                                            }
						break;
					}

				/* User uid1 stopped following user uid2
				 * U <uid1> <uid2> */
				case 'U':
					{
						int uid1 = Integer.parseInt(params[1]);
						int uid2 = Integer.parseInt(params[2]);

						if (unsubscribe(uid1, uid2)) {
							System.out.println("U " + uid1 + " " + uid2 + " succeeded");
						} else {
							System.err.println("U " + uid1 + " " + uid2 + " failed");
						}

						break;
					}

				/* Delete user account
				 * D <uid> */
                case 'D':
                    {
                        int uid = Integer.parseInt(params[1]);

                        if (delete_user(uid)) {
                            System.out.println("D " + uid + " " + " succeeded");
                        } else {
                            System.err.println("D " + uid + " " + " failed");
                        }

                        break;
                    }

                    /* History of a user's tweets
                     * H <uid> <date1> <date2>
                     */
                case 'H':
                    {
                        int uid = Integer.parseInt(params[1]);
                        int timestamp1 = Integer.parseInt(params[2]);
                        int timestamp2 = Integer.parseInt(params[3]);

                        if (history(uid, timestamp1, timestamp2)) {
                            System.out.println("H " + uid + " " + timestamp1 + " " + timestamp2 + " succeeded");
                        } else {
                            System.out.println("H " + uid + " " + timestamp1 + " " + timestamp2 + " failed");
                        }

                        break;
                    }

				/* Lookup tweet in user's wall
				 * L <uid> <tid> */
				case 'L':
					{
						int tid = Integer.parseInt(params[1]);

						if (lookup(tid)) {
							System.out.println("L " + tid + " succeeded");
						} else {
							System.err.println("L " + tid + " failed");
						}

						break;
					}

				/* Print all active users
				 * X */
				case 'X':
					{
						if (print_users()) {
							System.out.println("X succeeded");
						} else {
							System.err.println("X failed");
						}

						break;
					}

				/* Print all tagged tweets
				 * Y */
				case 'Y':
					{
						if (print_tweets()) {
							System.out.println("Y succeeded");
						} else {
							System.err.println("Y failed");
						}

						break;
					}

				/* Empty line */
				case '\n':
					break;

				/* Ignore everything else */
				default:
					System.out.println("Ignoring " + line);
					break;
			}
		}
	}
}
