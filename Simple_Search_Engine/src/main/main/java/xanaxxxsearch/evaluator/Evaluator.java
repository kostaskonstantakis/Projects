/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xanaxxxsearch.evaluator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 *
 * @author csd3219@csd.uoc.gr
 */
public class Evaluator {
    private final NavigableMap<String, Pair<Long, Long>> vocabulary;
    private static Evaluator instance;
    
    private Evaluator() throws FileNotFoundException, IOException {
        RandomAccessFile vocabularyFile;
        vocabularyFile = new RandomAccessFile(
                "CollectionIndex\\Vocabulary.txt", "r");
        Map<String, Pair<Long,Long>> map = new HashMap<>();
        for (long vocIndex = 0;
                vocIndex<vocabularyFile.length();
                vocIndex = vocabularyFile.getFilePointer()) {
            String sline = new String(vocabularyFile.readUTF()
                    .getBytes("ISO-8859-1"), "UTF-8");
            sline = sline.replaceAll("\n", "");
            String[] line;
            line = sline.split(" ");
            if (line.length == 3) 
                map.put(line[0], new Pair<>(
                        Long.parseLong(line[1]),
                        Long.parseLong(line[2])));
        }
        vocabulary = new TreeMap<>(map);
    }
    public static Evaluator getInstance() {
        if (instance == null) { 
            synchronized (Evaluator.class) {
                if(instance==null) {
                    try {
                        instance = new Evaluator();
                    } catch (Exception ex) {
                        Logger.getLogger(Evaluator.class.getName()).log(Level.SEVERE, null, ex);
                        return null;
                    }
                }
            }
        }
        return instance;
    }

    public int size() {
        return vocabulary.size();
    }

    public boolean isEmpty() {
        return vocabulary.isEmpty();
    }

    public boolean containsKey(Object key) {
        return vocabulary.containsKey(key);
    }

    public Pair<Long, Long> get(Object key) {
        return vocabulary.get(key);
    }

    public Map.Entry<String, Pair<Long, Long>> higherEntry(String key) {
        return vocabulary.higherEntry(key);
    }

    public String higherKey(String key) {
        return vocabulary.higherKey(key);
    }
    
}
