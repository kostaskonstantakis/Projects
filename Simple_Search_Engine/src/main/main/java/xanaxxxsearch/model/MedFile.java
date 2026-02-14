/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xanaxxxsearch.model;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import mitos.stemmer.Stemmer;

/**
 *
 * @author csd3219@csd.uoc.gr
 * MedFile represents an article file
 * 
 */
public class MedFile {    
    private static final String DELIMETER = "\t\n\r\f \\+-*/.,\'\"?;:<>()[]{}";
    private final String pmcid;
    private final String filePath;
    //frequency of each word in each tag of the document
    //key: word
    //val: array with frequency of the word in each tag
    //each position of the array maps to a tag:
    //0: title , 1: abstract , 2: body , 3: journal
    //4: publisher , 5: authors , 6: categories
    private final Map<String, Integer[]> words;
    //max frequency of a word per tag of the document
    private int maxFreqz[];
    //max frequency of a word in all of the tags of a document(df)
    private int maxFreq = 1;
    private void tokenizeAndAdd(String str, int pos, Set<String> stopwords) {
//        str = str.replaceAll("&#x[0-9a-f]{5};", "");
//        str = str.replaceAll("[^\\p{Alpha}\\p{Digit}]"," ");
        str = str.replaceAll("[^a-zA-Z0-9]"," ");
        
//        org.apache.commons.lang3.StringEscapeUtils.escapeJava("Your string to escape");
//        StringTokenizer tokenizer = new StringTokenizer(str, "\t\f\r\n\\s ");
        String[] tokens = str.split("\\s");
        Integer[] val;
//        while(tokenizer.hasMoreTokens() ) {
        for (int i=0;i<tokens.length;++i) {
//            String currentToken = tokenizer.nextToken();
            String currentToken = tokens[i];
            if (currentToken.equals(""))
                continue;
            try {
                currentToken =new String(
                        Stemmer.Stem(currentToken).getBytes("UTF-8"), "UTF-8");
                
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(MedFile.class.getName())
                        .log(Level.SEVERE, null, ex);
                continue;
            }
            if (stopwords.contains(currentToken))
                continue;
            
            if (words.containsKey(currentToken)) {
                val = words.get(currentToken);
                val[pos] = val[pos] + 1;
                int cur = val[0]+val[1]+val[2]+val[3]+val[4]+val[5]+val[6];
                if (cur > maxFreq)
                    maxFreq = cur;                    
                int cur2 = val[pos];
                if (cur2 > maxFreqz[pos]) 
                    maxFreqz[pos] = cur2;
                words.replace(currentToken, val);
            } else {
                val = new Integer[]{0, 0, 0, 0, 0, 0, 0};
                val[pos] = 1;
                words.put(currentToken, val);
            }
                
        }
    }
    
    public MedFile(
            String filePath, 
            String pmcid, 
            Set<String> stopwords,
            String title,
            String abstr,
            String body,
            String journal,
            String publisher,
            List<String> authors,
            Set<String> categories) {
        this.maxFreqz = new int[]{0,0,0,0,0,0,0};
        this.filePath = filePath;
        this.pmcid = pmcid;
        words = new TreeMap<>();
        tokenizeAndAdd(title, 0, stopwords);
        tokenizeAndAdd(abstr, 1, stopwords);
        tokenizeAndAdd(body, 2, stopwords);
        tokenizeAndAdd(journal, 3, stopwords);
        tokenizeAndAdd(publisher, 4, stopwords);
        for (String author:authors)
            tokenizeAndAdd(author, 5, stopwords);
        for (String category:categories)
            tokenizeAndAdd(category, 6, stopwords);
    }

    public int[] getMaxFreqz() {
        return maxFreqz;
    }

    public int getMaxFreq() {
        return maxFreq;
    }
    
    public String getPmcid() {
        return pmcid;
    }

    public String getFilePath() {
        return filePath;
    }

    public Map<String, Integer[]> getWords() {
        return words;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.pmcid != null ? this.pmcid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MedFile other = (MedFile) obj;
        return !((this.pmcid == null) 
                ? (other.pmcid != null) 
                : !this.pmcid.equals(other.pmcid));
    }
    
}
