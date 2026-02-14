/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xanaxxxsearch.indexer;

import xanaxxxsearch.model.TermEntry;
import xanaxxxsearch.model.MedFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import javafx.util.Pair;
import mitos.stemmer.Stemmer;

/**
 *
 * @author csd3219@csd.uoc.gr
 */
public class test {

    public static boolean checkmem() {
        return (Runtime.getRuntime().totalMemory()*100)/Runtime.getRuntime().maxMemory() >= 70;
    }
    
    public static void writePartialFiles(String voc, String post, Set<TermEntry> termEntries) throws FileNotFoundException, IOException {
        RandomAccessFile vocabulary, posting;
        vocabulary = new RandomAccessFile("CollectionIndex\\" + voc, "rw");
        posting = new RandomAccessFile("CollectionIndex\\" + post, "rw");
        long postingFileOffset = 0;
        
        for (TermEntry iter:termEntries) {
            String vocLine = iter.getTerm()+" "+iter.getDf()+" "+postingFileOffset + "\n";
            vocabulary.writeUTF(vocLine);
            for (TermEntry.Term$PostingFileEntry pfe:iter.getPostingFile()) {
                String postLine = pfe.getPcmid()+" "+pfe.getTf();
                for (Long p:pfe.getPositions()) 
                    postLine = postLine+" "+p;
                postLine = postLine + " " + pfe.getDocumentsFileOffset() + "\n";
                posting.writeBytes(postLine);
                postingFileOffset = posting.getFilePointer();
            }
        }
        vocabulary.close();
        posting.close();
    }
    
    public static void mergeFiles(String voc1, String voc2, String post1, String post2) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        RandomAccessFile vocabulary1, 
                vocabulary2, 
                mergedVocabulary, 
                postfile1, 
                postfile2, 
                mergedPosting;
        Map<String, Pair<Long, List<Long>>> map = new HashMap<>();
        vocabulary1 = new RandomAccessFile("CollectionIndex\\" + voc1, "r"); //initially, it was just /...
        vocabulary2 = new RandomAccessFile("CollectionIndex\\" + voc2, "r");
        mergedVocabulary = new RandomAccessFile("CollectionIndex\\" + voc1 + voc2, "rw");
        postfile1 = new RandomAccessFile("CollectionIndex\\" + post1, "r");
        postfile2 = new RandomAccessFile("CollectionIndex\\" + post2, "r");
        mergedPosting = new RandomAccessFile("CollectionIndex\\" + post1 + post2, "rw");
        boolean read1 = false, read2 = true;
        String[] line1 = null, line2 = null;
        long v1=0,v2=0, postOffset = 0;

        while (v1<vocabulary1.length() && v2<vocabulary2.length()) {
            String sline;
            if (v1<vocabulary1.length() && read1) {
                sline = new String(vocabulary1.readUTF()
                        .getBytes("ISO-8859-1"), "UTF-8");
                sline = sline.replaceAll("\n", "");
                line1 = sline.split(" ");
                v1 = vocabulary1.getFilePointer();
            }
                
            if (v2<vocabulary2.length() && read2) {
                sline = new String(vocabulary2.readUTF()
                        .getBytes("ISO-8859-1"), "UTF-8");
                sline = sline.replaceAll("\n", "");
                line2 = sline.split(" ");
                v2 = vocabulary2.getFilePointer();
            }
            if (line1 == null) {
                mergedVocabulary.writeUTF(line2[0]+" "+line2[1]+" "+postOffset);
                long tmp2 = vocabulary2.getFilePointer();
                if (tmp2 != vocabulary2.length()) {
                    sline = new String(vocabulary2.readUTF()
                            .getBytes("ISO-8859-1"), "UTF-8");
                    vocabulary2.seek(tmp2);
                    long pos = Long.parseLong(line2[2]);
                    line2 = sline.split(" ");
                    postfile2.seek(pos);
                    while (postfile2.getFilePointer() != Long.parseLong(line2[2])) {
                        mergedPosting.writeBytes(postfile2.readLine());
                    }
                }
                else {
                    postfile2.seek(Long.parseLong(line2[2]));
                    mergedPosting.writeBytes(postfile2.readLine());
                }
            }
            else if (line2 == null) {
                mergedVocabulary.writeUTF(line1[0]+" "+line1[1]+" "+postOffset);
                long tmp1 = vocabulary1.getFilePointer();
                if (tmp1 != vocabulary1.length()) {
                    sline = new String(vocabulary1.readUTF()
                            .getBytes("ISO-8859-1"), "UTF-8");
                    vocabulary2.seek(tmp1);
                    long pos = Long.parseLong(line1[2]);
                    line1 = sline.split(" ");
                    postfile1.seek(pos);
                    while (postfile1.getFilePointer() != Long.parseLong(line1[2])) {
                        mergedPosting.writeBytes(postfile1.readLine());
                    }
                }
                else {
                    postfile1.seek(Long.parseLong(line1[2]));
                    mergedPosting.writeBytes(postfile1.readLine());
                }
            }
            else {
                if (line1[0].equals(line2[0])) {
                    //TODO:implement this
                }
                else if (line1[0].compareTo(line2[0]) < 0) {
                    postfile1.seek(Long.parseLong(line1[2]));
                    mergedPosting.writeBytes(postfile1.readLine());
                    mergedVocabulary.writeUTF(line1[0]+" "+line1[1]+" "+postOffset);
                    postOffset = postfile1.getFilePointer();
                    read1 = true;
                    read2 = false;
                }
                else {
                    postfile2.seek(Long.parseLong(line2[2]));
                    mergedPosting.writeBytes(postfile2.readLine());
                    mergedVocabulary.writeUTF(line2[0]+" "+line2[1]+" "+postOffset);
                    postOffset = postfile2.getFilePointer();
                    read1 = false;
                    read2 = true;
                }
            }
            mergedVocabulary.close();
            mergedPosting.close();
            vocabulary1.close();
            vocabulary2.close();
            postfile1.close();
            postfile2.close();
            (new File("CollectionIndex\\" + voc1)).delete();
            (new File("CollectionIndex\\" + voc2)).delete();
            (new File("CollectionIndex\\" + post1)).delete();
            (new File("CollectionIndex\\" + post2)).delete();
        }
        
        
        
    }
    public static void main(String[] args) throws IOException {
        
        File directory = new File("C:\\Users\\gussl\\MedicalCollection\\"); //might need the // in the end after 00
        String grPath = "C:\\Users\\gussl\\Desktop\\CSD\\hy463\\2019\\Project2019\\5_Resources_Stoplists\\stopwordsGr.txt";
        String enPath = "C:\\Users\\gussl\\Desktop\\CSD\\hy463\\2019\\Project2019\\5_Resources_Stoplists\\stopwordsEn.txt";
        if (new File("CollectionIndex").mkdirs()) {
            System.err.println("Failed to create CollectionIndex directory");
            return;
        }
        RandomAccessFile vocabulary = null;
        RandomAccessFile posting = null;
        RandomAccessFile documents = null;
        vocabulary = new RandomAccessFile("CollectionIndex\\Vocabulary.txt", "rw");
        posting = new RandomAccessFile("CollectionIndex\\Posting.txt", "rw");
        documents = new RandomAccessFile("CollectionIndex\\Documents.txt", "rw");
        System.out.println("STARTING INDEXING.");
        long start = System.nanoTime();
        List<String> filePaths = listFilesForDirectory(directory);
        List<Pair<String,String>> partialFiles = new ArrayList<>();
        /*
         * Set containing all term entries for the vocabulary file
         * sorted by each term String
         */
        Set<TermEntry> termEntries = new HashSet<>();
        long postingFileOffset = 0;
        long documentsFileOffset = 0;
        boolean found;
        Stemmer.Initialize();
        Indexer indexer = new Indexer(enPath, grPath);
        long i=0, vocCnt=0, postCnt=0;
        for (String p:filePaths) {
            indexer.setFile(new File(p));
            MedFile md = indexer.read(); 
            //create vocabulary and posting file data in memory
            for (Entry<String,Integer[]> e:md.getWords().entrySet()) {
                Integer[] value = e.getValue();
                double tf = (value[0]+value[1]+value[2]+value[3]
                        +value[4]+value[5]+value[6])/md.getMaxFreq();
                TermEntry currentTemp = new TermEntry();
                currentTemp.setTerm(e.getKey());
                found = false;
                for (TermEntry iter:termEntries) {
                    if (iter.equals(currentTemp)) {
                        //term has already been read, need to update postingFile
                        found = iter.updatePostingFileEntry(
                                md.getPmcid(),postingFileOffset);//TODO: calc this in medfile creation 
                        if (!found)
                            iter.setDf(iter.getDf()+1);
                        break;
                    }
                }
                //term has not been read for current document, new entry for
                //the posting file and maybe new entry for the vocabulary file
                if (!found) {
                    currentTemp.addNewPostingFileEntry(
                            tf, 
                            postingFileOffset, //TODO: calc this in medfile creation 
                            md.getPmcid(), 
                            md.getFilePath(), 
                            documentsFileOffset);
                }
            }
            //check memory once every two files
            if ((i++)/2==0 && checkmem()) {
                writePartialFiles("v"+vocCnt, "p"+postCnt, termEntries);
                partialFiles.add(new Pair("v"+vocCnt, "p"+postCnt));
                termEntries.clear();
                ++vocCnt;
                ++postCnt;
            }
            while (partialFiles.size() > 1) {
                List<Pair<String, String>> temp = new ArrayList<>();
                for (int j=0;j<partialFiles.size()-1;++j) {
                    int k = j+1;
                    mergeFiles("v"+j, "v"+k, "p"+j,"p"+k);
                    temp.add(new Pair("v"+j+"v"+k, "p"+j+"p"+k));
                }
                partialFiles.clear();
                partialFiles.addAll(temp);
            }
            //write to documentsFile an entry for each file
            documents.writeBytes(md.getPmcid()+" "+md.getFilePath()+" "+md.getMaxFreq() + "\n");
            documentsFileOffset = documents.getFilePointer();    
        }
        //commented out for testing
        documents.close();
        termEntries = new TreeSet<>(termEntries);
        System.out.println("DocumentsFile Complete");
        for (TermEntry iter : termEntries) {
            String vocLine = iter.getTerm() + " " + iter.getDf() + " " + postingFileOffset + "\n";
            vocabulary.writeUTF(vocLine);
            for (TermEntry.Term$PostingFileEntry pfe : iter.getPostingFile()) {
                String postLine = pfe.getPcmid() + " " + pfe.getTf();
                for (Long p : pfe.getPositions()) {
                    postLine = postLine + " " + p;
                }
                postLine = postLine + " " + pfe.getDocumentsFileOffset() + "\n";
                posting.writeBytes(postLine);
                postingFileOffset = posting.getFilePointer();
            }
        }
        //commented out for testing
        posting.close();
        vocabulary.close();
        long end = System.nanoTime();
        long total_time = end - start;
        System.out.println("FINISHED INDEXING IN " + total_time + " ns");
        
    }
    
    public static List<String> listFilesForDirectory(File directory) throws IOException {
        List<String> l = new ArrayList<>();
        for (File file:directory.listFiles()) 
            if (file.isDirectory()) 
                l.addAll(listFilesForDirectory(file));
            else 
                l.add(file.getPath());
        return l;
    }

}

