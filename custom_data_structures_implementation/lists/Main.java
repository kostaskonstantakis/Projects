/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Kostas
 */
public class Main {

    public static users_list usersList;

    public enum hashtag {
        sports, politics, economics, music, movies, nature, art, environment, technology, weather;
    }

    public static tagged_tweet Hashtags[];
    public static Hashtag_list hl0, hl1, hl2, hl3, hl4, hl5, hl6, hl7, hl8, hl9;
//declaring 10 hashtag lists,each one for each array element/category of tweets

    public int map_hashtag_to_integer(hashtag h) {
        switch (h) {
            case sports:
                return 0;
            case politics:
                return 1;
            case economics:
                return 2;
            case music:
                return 3;
            case movies:
                return 4;
            case nature:
                return 5;
            case art:
                return 6;
            case environment:
                return 7;
            case technology:
                return 8;
            case weather:
                return 9;
        }
        return -1;
    }

    public hashtag map_integer_to_hashtag(int i, hashtag h) {

        switch (i) {
            case 0:
                return h.sports;
            case 1:
                return h.politics;
            case 2:
                return h.economics;
            case 3:
                return h.music;
            case 4:
                return h.movies;
            case 5:
                return h.nature;
            case 6:
                return h.nature;
            case 7:
                return h.environment;
            case 8:
                return h.technology;
            case 9:
                return h.weather;
        }
        return h;
    }

    /*
	 * 1) You need to create the classes that correspond to the C structs
	 * 2) You need to create the global variables like, in the C header
     */
    /**
     * @brief Optional function to initialize data structures that need
     * initialization
     *
     * @return true on success false on failure
     */
    public static boolean initialize() {
        Hashtags = new tagged_tweet[10];
        usersList = new users_list();
        hl0 = new Hashtag_list();
        hl1 = new Hashtag_list();
        hl2 = new Hashtag_list();
        hl3 = new Hashtag_list();
        hl4 = new Hashtag_list();
        hl5 = new Hashtag_list();
        hl6 = new Hashtag_list();
        hl7 = new Hashtag_list();
        hl8 = new Hashtag_list();
        hl9 = new Hashtag_list();

        /* Hashtags[0].next = hl0.head;
        Hashtags[1].next = hl1.head;
        Hashtags[2].next = hl2.head;
        Hashtags[3].next = hl3.head;
        Hashtags[4].next = hl4.head;
        Hashtags[5].next = hl5.head;
        Hashtags[6].next = hl6.head;
        Hashtags[7].next = hl7.head;
        Hashtags[8].next = hl8.head;
        Hashtags[9].next = hl9.head; */
        return true;
    }

    /**
     * @brief Register user
     *
     * @param uid The user's id
     *
     * @return true on success false on failure
     */
    public static boolean register_user(int uid) {
        user u = new user(uid);
        System.out.println("R " + uid);
        usersList.add(u);
        System.out.println("\tUsers= " + usersList.toString());
        System.out.println("DONE");
        return true;
    }

    /**
     * @brief User uid1 is now following user uid2
     *
     * @param uid1 The 1st user's id
     * @param uid2 The 2nd user's id
     *
     * @return true on success false on failure
     */
    public static boolean subscribe(int uid1, int uid2) {
        user u1 = usersList.getSearch(uid1);
        followers_list f = new followers_list();
        System.out.println("S " + uid1 + " " + uid2);

        if (u1 != null) {
            u1.getFollowers_list().add(new follower(uid2));
        }
        System.out.println("\t Followers= " + u1.getFollowers_list().toString());
        System.out.println("DONE");
        return true;
    }

    /**
     * @brief New tweet event
     *
     * @param uid The user who posted the tweet
     * @param tid The tweet's id
     * @param tag The tweet's hashtag
     * @param timestamp The time the tweet was posted
     * @param time_relevance How long the tweet was relevant/trending
     *
     * @return true on success false on failure
     */
    public static boolean tweet(int uid, int tid, int tag, int timestamp, int time_relevance) {
        System.out.println("\tT " + uid + " " + tid + " " + tag + " " + timestamp + " " + time_relevance);
        user u1 = usersList.getSearch(uid);
        if (u1 != null) {
            u1.getWall().add(new tweet(uid, tid, tag, timestamp, time_relevance));
        }
        user curr = usersList.head;
        System.out.println("User " + curr.getUid() + " = " + curr.getWall().toString());
        curr = curr.getNext();
        while (curr != null) {
            System.out.println("User " + curr.getUid() + " = " + curr.getWall().toString());
            curr = curr.getNext();
        }
        return true;
    }

    /**
     * @brief User uid1 stopped following user uid2
     *
     * @param uid1 The 1st user's id
     * @param uid2 The 2nd user's id
     *
     * @return true on success false on failure
     */
    public static boolean unsubscribe(int uid1, int uid2) {
        return true;
    }

    /**
     * @brief Delete user account
     *
     * @param uid The id of the user account to delete
     *
     * @return true on success false on failure
     */
    public static boolean delete_user(int uid) {
        return true;
    }

    /**
     * @brief Lookup tweet in user's wall
     *
     * @param uid The user's id
     * @param tid The tweet's id
     *
     * @return true on success false on failure
     */
    public static boolean lookup(int uid, int tid) {
        user u = new user(uid);
        Wall w = new Wall();
        System.out.println("L " + uid + " " + tid);
        u = usersList.getSearch(uid);
        w.head = u.wall_head;
        w.sentinel = u.wall_sentinel; //probably right
        tweet t = new tweet(tid, uid, 0, 0, 0);
        t = w.getSearch(tid);
        System.out.println("\t\t\t Tweet= <" + t.tid + ":" + t.uid + ">");
        System.out.println("DONE");
        return true;

    }

    /**
     * @brief Combine tweets that have following tags
     *
     * @param tag1 The first tag
     * @param tag2 The second tag
     *
     * @return true on success false on failure
     */
    public static boolean merge(int tag1, int tag2) {
        return true;
    }

    /**
     * @brief Find tweets of the same tag that were trending at the same time as
     * the current tweet
     *
     * @param tid The tweet's id
     * @param tag The tweet's hashtag
     *
     * @return true on success false on failure
     */
    public static boolean time_relevant(int tid, int tag) {

        System.out.println("F " + tid + " " + tag);
        System.out.print("\t\t Relevant Tweets = ");
        if (Hashtags[tag].next.equals(hl0.head)) {
            tagged_tweet t = new tagged_tweet();
            tagged_tweet t1 = new tagged_tweet();
            t1 = hl0.get_head();
            t = hl0.search(tid);
            int upper_threshold, lower_threshold;
            upper_threshold = t.timestamp + t.time_relevance;
            lower_threshold = t.timestamp - t.time_relevance;
            hl0.ZigZag(t, lower_threshold, upper_threshold);

        } else if (Hashtags[tag].next.equals(hl1.head)) {
            tagged_tweet t = new tagged_tweet();
            tagged_tweet t1 = new tagged_tweet();
            t1 = hl1.get_head();
            t = hl1.search(tid);
            int upper_threshold, lower_threshold;
            upper_threshold = t.timestamp + t.time_relevance;
            lower_threshold = t.timestamp - t.time_relevance;
            hl1.ZigZag(t, lower_threshold, upper_threshold);

        } else if (Hashtags[tag].next.equals(hl2.head)) {
            tagged_tweet t = new tagged_tweet();
            tagged_tweet t1 = new tagged_tweet();
            t1 = hl2.get_head();
            t = hl2.search(tid);
            int upper_threshold, lower_threshold;
            upper_threshold = t.timestamp + t.time_relevance;
            lower_threshold = t.timestamp - t.time_relevance;
            hl2.ZigZag(t, lower_threshold, upper_threshold);

        } else if (Hashtags[tag].next.equals(hl3.head)) {
            tagged_tweet t = new tagged_tweet();
            tagged_tweet t1 = new tagged_tweet();
            t1 = hl3.get_head();
            t = hl3.search(tid);
            int upper_threshold, lower_threshold;
            upper_threshold = t.timestamp + t.time_relevance;
            lower_threshold = t.timestamp - t.time_relevance;
            hl3.ZigZag(t, lower_threshold, upper_threshold);

        } else if (Hashtags[tag].next.equals(hl4.head)) {
            tagged_tweet t = new tagged_tweet();
            tagged_tweet t1 = new tagged_tweet();
            t1 = hl4.get_head();
            t = hl4.search(tid);
            int upper_threshold, lower_threshold;
            upper_threshold = t.timestamp + t.time_relevance;
            lower_threshold = t.timestamp - t.time_relevance;
            hl4.ZigZag(t, lower_threshold, upper_threshold);

        } else if (Hashtags[tag].next.equals(hl5.head)) {
            tagged_tweet t = new tagged_tweet();
            tagged_tweet t1 = new tagged_tweet();
            t1 = hl5.get_head();
            t = hl5.search(tid);
            int upper_threshold, lower_threshold;
            upper_threshold = t.timestamp + t.time_relevance;
            lower_threshold = t.timestamp - t.time_relevance;
            hl5.ZigZag(t, lower_threshold, upper_threshold);

        } else if (Hashtags[tag].next.equals(hl6.head)) {
            tagged_tweet t = new tagged_tweet();
            tagged_tweet t1 = new tagged_tweet();
            t1 = hl6.get_head();
            t = hl6.search(tid);
            int upper_threshold, lower_threshold;
            upper_threshold = t.timestamp + t.time_relevance;
            lower_threshold = t.timestamp - t.time_relevance;
            hl6.ZigZag(t, lower_threshold, upper_threshold);

        } else if (Hashtags[tag].next.equals(hl7.head)) {
            tagged_tweet t = new tagged_tweet();
            tagged_tweet t1 = new tagged_tweet();
            t1 = hl7.get_head();
            t = hl7.search(tid);
            int upper_threshold, lower_threshold;
            upper_threshold = t.timestamp + t.time_relevance;
            lower_threshold = t.timestamp - t.time_relevance;
            hl7.ZigZag(t, lower_threshold, upper_threshold);

        } else if (Hashtags[tag].next.equals(hl8.head)) {
            tagged_tweet t = new tagged_tweet();
            tagged_tweet t1 = new tagged_tweet();
            t1 = hl8.get_head();
            t = hl8.search(tid);
            int upper_threshold, lower_threshold;
            upper_threshold = t.timestamp + t.time_relevance;
            lower_threshold = t.timestamp - t.time_relevance;
            hl8.ZigZag(t, lower_threshold, upper_threshold);

        } else if (Hashtags[tag].next.equals(hl9.head)) {
            tagged_tweet t = new tagged_tweet();
            tagged_tweet t1 = new tagged_tweet();
            t1 = hl9.get_head();
            t = hl9.search(tid);
            int upper_threshold, lower_threshold;
            upper_threshold = t.timestamp + t.time_relevance;
            lower_threshold = t.timestamp - t.time_relevance;
            hl9.ZigZag(t, lower_threshold, upper_threshold);

        }

        System.out.println("DONE");
        return true;
    }

    /**
     * @brief Print all active users
     *
     * @return true on success false on failure
     */
    public static boolean print_users() {
        System.out.println("X");
        usersList.X();
        System.out.println("DONE");
        return true;
    }

    /**
     * @brief Print all tagged tweets
     *
     * @return true on success false on failure
     */
    public static boolean print_tweets() {
        System.out.println("Y");
        hl0.traverse();
        System.out.print("\n");
        hl1.traverse();
        System.out.print("\n");
        hl2.traverse();
        System.out.print("\n");
        hl3.traverse();
        System.out.print("\n");
        hl4.traverse();
        System.out.print("\n");
        hl5.traverse();
        System.out.print("\n");
        hl6.traverse();
        System.out.print("\n");
        hl7.traverse();
        System.out.print("\n");
        hl8.traverse();
        System.out.print("\n");
        hl9.traverse();
        System.out.print("\n");
        System.out.println("DONE");
        return true;
    }

    /**
     * @brief The main function
     *
     * @param argc Number of arguments
     * @param argv Argument vector
     *
     * @return 0 on success 1 on failure
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        BufferedReader inputFile;
        String line;
        String[] params;

        initialize();
        /* Check command buff arguments */
        if (args.length != 1) {
            System.err.println("Usage: <executable-name> <input_file>");
            System.exit(0);
        }

        /* Open input file */
        inputFile = new BufferedReader(new FileReader(args[0]));

        /* Read input file and handle the events */
        while ((line = inputFile.readLine()) != null) {

            if (line.length() == 0) {
                continue;
            }

            System.out.println(">>> Event: " + line);
            params = line.split(" ");
            char eventID = line.charAt(0);

            switch (eventID) {

                /* Comment */
                case '#':
                    break;

                /* Register user
				 * R <uid> */
                case 'R': {
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
                case 'S': {
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
				 * T <uid> <tid> <hashtag> <timestamp> <time_relevance> */
                case 'T': {
                    int uid = Integer.parseInt(params[1]);
                    int tid = Integer.parseInt(params[2]);
                    int tag = Integer.parseInt(params[3]);
                    int timestamp = Integer.parseInt(params[4]);
                    int time_relevance = Integer.parseInt(params[5]);

                    if (tweet(uid, tid, tag, timestamp, time_relevance)) {
                        System.out.println("T " + uid + " " + tid + " " + tag + " " + timestamp + " " + time_relevance + " " + " succeeded");
                    } else {
                        System.err.println("T " + uid + " " + tid + " " + tag + " " + timestamp + " " + time_relevance + " " + " failed");
                    }

                    break;
                }

                /* User uid1 stopped following user uid2
				 * U <uid1> <uid2> */
                case 'U': {
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
                case 'D': {
                    int uid = Integer.parseInt(params[1]);

                    if (delete_user(uid)) {
                        System.out.println("P " + uid + " " + " succeeded");
                    } else {
                        System.err.println("P " + uid + " " + " failed");
                    }

                    break;
                }

                /* Lookup tweet in user's Wall
				 * L <uid> <tid> */
                case 'L': {
                    int uid = Integer.parseInt(params[1]);
                    int tid = Integer.parseInt(params[2]);

                    if (lookup(uid, tid)) {
                        System.out.println("B " + uid + " " + tid + " succeeded");
                    } else {
                        System.err.println("B " + uid + " " + tid + " failed");
                    }

                    break;
                }

                /* Combine tweets that have following tags
				 * M <hashtag1> <hashtag2> */
                case 'M': {
                    int tag1 = Integer.parseInt(params[1]);
                    int tag2 = Integer.parseInt(params[2]);

                    if (merge(tag1, tag2)) {
                        System.out.println("U " + " succeeded");
                    } else {
                        System.err.println("U " + " failed");
                    }

                    break;
                }

                /* Find tweets of the same tag that were trending
				 * at the same time as the current tweet
				 * F <tid> <hashtag> */
                case 'F': {
                    int tid = Integer.parseInt(params[1]);
                    int tag = Integer.parseInt(params[2]);

                    if (time_relevant(tid, tag)) {
                        System.out.println("W " + " " + tid + " " + tag + " " + "succeeded");
                    } else {
                        System.err.println("W " + " " + tid + " " + tag + " " + "failed");
                    }

                    break;
                }

                /* Print all active users
				 * X */
                case 'X': {
                    if (print_users()) {
                        System.out.println("X succeeded");
                    } else {
                        System.err.println("X failed");
                    }

                    break;
                }

                /* Print all tagged tweets
				 * Y */
                case 'Y': {
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
