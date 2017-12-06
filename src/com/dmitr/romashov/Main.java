package com.dmitr.romashov;

import javafx.util.Pair;

import java.io.*;
import java.util.*;

public class Main {

    static HashMap<String, Pair<Integer, Set<Integer>>> readFile(String path){

        String splitReg = "[^\\p{L}'-]+";
        String matchReg = "(\\p{L}+|(\\p{L}+-?){1,3}|\\p{L}+'\\p{L}+)";
        HashMap<String, Pair<Integer, Set<Integer>>> wordsMap = new HashMap<>();

        try {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "Cp1252"))){
                String line;
                int chapterNumber = 0;

                while ((line = bufferedReader.readLine()) != null){
                    line = line.trim();
                    if (!line.equals("")){
                        if (line.contains("CHAPTER")){
                            ++chapterNumber;
                            continue;
                        }
                        String[] strWords = line.split(splitReg);
                        for (String word:strWords) {
                            word = word.toLowerCase();
                            if (word.matches(matchReg)){
                                if (!wordsMap.containsKey(word)){
                                    wordsMap.put(word, new Pair(0, new HashSet<Integer>()));
                                }
                                Pair<Integer, Set<Integer>> integerSetPair = wordsMap.get(word);
                                int currWordCount = integerSetPair.getKey();
                                Set<Integer> currChapterNumbers = integerSetPair.getValue();
                                currChapterNumbers.add(chapterNumber);
                                Pair<Integer, Set<Integer>> newPair = new Pair<>(currWordCount + 1, currChapterNumbers);
                                wordsMap.put(word, newPair);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error with file reading");
            e.printStackTrace();
        }

        return wordsMap;
    }

    public static void main(String[] args) {
        Tree tree = new Tree();
        HashMap<String, Pair<Integer, Set<Integer>>> wordsMap = readFile("Jerome - three_men_in_a_boat.txt");

        // insert words
        for (String word:wordsMap.keySet()) {
            Pair<Integer, Set<Integer>> integerSetPair = wordsMap.get(word);
            int wordCount = integerSetPair.getKey();
            for (int chapterNumber:integerSetPair.getValue()) {
                tree.insert(word, chapterNumber, wordCount);
            }
        }

        // find word chapter numbers
        String searchWord = "mast-high";
        Set<Integer> chapterNumbers = tree.findWordChapterNumbers(searchWord);
        if (chapterNumbers.size() != 0){
            System.out.println("Chapter numbers of the word \"" + searchWord + "\":");
            for (int chapterNumber:chapterNumbers) {
                System.out.print(chapterNumber + " ");
            }
            System.out.println();
        }

        // find word freq
        searchWord = "aaa";
        tree.insert(searchWord, 1, 2);
        int wordFreq = tree.findWordFreq(searchWord);
        if (wordFreq != 0){
            System.out.println("Frequency of the word \"" + searchWord + "\":");
            System.out.println(wordFreq);
            System.out.println();
        }

        tree.insert("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb", 21, 1);

        // find longest words
        ArrayList<String> longestWords = tree.findLongestWords();
        if (longestWords.size() != 0){
            System.out.println("Longest words:");
            for (String word:longestWords) {
                System.out.print(word + " ");
            }
            System.out.println();
        } else {
            System.out.println("Words didn't founded");
        }

        ArrayList<Pair<String, Integer>> mostCommonWords = tree.findMostCommonWords();
        if (mostCommonWords.size() != 0){
            System.out.println("Most common words(chapter numbers):");
            for (Pair<String, Integer> pair:mostCommonWords) {
                System.out.println(pair.getKey() + " " + pair.getValue());
            }
        } else {
            System.out.println("Words didn't founded");
        }

        ArrayList<Pair<String, Integer>> mostCommonWords2 = tree.findMostCommonWords2();
        if (mostCommonWords2.size() != 0){
            System.out.println("Most common words(frequency of the words in text):");
            for (Pair<String, Integer> pair:mostCommonWords2) {
                System.out.println(pair.getKey() + " " + pair.getValue());
            }
        } else {
            System.out.println("Words didn't founded");
        }
    }
}
