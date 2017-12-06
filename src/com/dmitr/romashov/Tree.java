package com.dmitr.romashov;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tree {
    private Node root;

    public Tree() {
        root = new Node();
    }

    private class Node {
        String letter;
        List<Node> children;
        Set<Integer> chapterNumbers;
        int wordCount;

        public Node() {
            children = new ArrayList<>();
            chapterNumbers = new HashSet<>();
            wordCount = 0;
        }

        public Node(String letter, List<Node> children, Set<Integer> chapterNumbers) {
            this.letter = letter;
            this.children = children;
            this.chapterNumbers = chapterNumbers;
            wordCount = 0;
        }

        public List<Node> getChildren() {
            return children;
        }
    }

    public void insert(String word, int chapterNumber, int wordCount) {
        Node currentNode = root;
        for (int i = 0; i < word.length(); i++) {
            boolean nodeFound = false;
            String letter = word.charAt(i) + "";
            List<Node> nodeChildren = currentNode.getChildren();
            for (Node node : nodeChildren) {
                if (node.letter.equals(letter)) {
                    currentNode = node;
                    nodeFound = true;
                    break;
                }
            }
            if (!nodeFound) {
                Node newNode = new Node(letter, new ArrayList<>(), new HashSet<>());
                currentNode.children.add(newNode);
                currentNode = newNode;
            }
        }
        currentNode.chapterNumbers.add(chapterNumber);
        currentNode.wordCount = wordCount;

    }

    public Set<Integer> findWordChapterNumbers(String word) {
        Node currentNode = root;
        word = word.toLowerCase();
        for (int i = 0; i < word.length(); i++) {
            boolean nodeFound = false;
            String letter = word.charAt(i) + "";
            List<Node> nodeChildren = currentNode.getChildren();
            for (Node node : nodeChildren) {
                if (node.letter.equals(letter)) {
                    currentNode = node;
                    nodeFound = true;
                    break;
                }
            }
            if (!nodeFound) {
                System.out.println("There is no such word");
                return new HashSet<>();
            }
        }
        return currentNode.chapterNumbers;
    }

    public int findWordFreq(String word) {
        Node currentNode = root;
        word = word.toLowerCase();
        for (int i = 0; i < word.length(); i++) {
            boolean nodeFound = false;
            String letter = word.charAt(i) + "";
            List<Node> nodeChildren = currentNode.getChildren();
            for (Node node : nodeChildren) {
                if (node.letter.equals(letter)) {
                    currentNode = node;
                    nodeFound = true;
                    break;
                }
            }
            if (!nodeFound) {
                System.out.println("There is no such word");
                return 0;
            }
        }
        return currentNode.wordCount;
    }

    public ArrayList<String> findLongestWords() {
        ArrayList<String> rootChildrenWords = new ArrayList<>();
        for (int i = 0; i < root.children.size(); i++) {
            Node childNode = root.children.get(i);
            addLongestWord(childNode, rootChildrenWords, childNode.letter);
        }

        // find max length of words
        int longestWordsLength = 0;

        for (String word : rootChildrenWords) {
            if (word.length() > longestWordsLength) {
                longestWordsLength = word.length();
            }
        }

        ArrayList<String> longestWords = new ArrayList<>();

        for (String word : rootChildrenWords) {
            if (word.length() == longestWordsLength) {
                longestWords.add(word);
            }
        }

        return longestWords;
    }

    // add the longest word which begin with currentNode
    private void addLongestWord(Node currentNode, ArrayList<String> words, String subStr) {
        int childrenCount = currentNode.children.size();
        int chapterNumbers = currentNode.chapterNumbers.size();

        // end of word
        if (childrenCount == 0 && chapterNumbers != 0) words.add(subStr);
        // node is letter
        if (childrenCount != 0 && chapterNumbers == 0) {
            List<Node> children = currentNode.children;
            for (Node child : children) {
                addLongestWord(child, words, subStr + child.letter);
            }
        }
        // there is the longer word
        if (childrenCount != 0 && chapterNumbers != 0) {
            List<Node> children = currentNode.children;
            for (Node child : children) {
                addLongestWord(child, words, subStr + child.letter);
            }
        }
    }

    // maxFreq - number of chapters in which the word occurs
    public ArrayList<Pair<String, Integer>> findMostCommonWords() {
        ArrayList<Pair<String, Integer>> words = new ArrayList<>();
        int[] maxFreq = new int[]{0};
        for (int i = 0; i < root.children.size(); i++) {
            Node childNode = root.children.get(i);
            addCommonWord(childNode, words, childNode.letter, maxFreq);
        }

        ArrayList<Pair<String, Integer>> mostCommonWords = new ArrayList<>();

        for (Pair<String, Integer> pair : words) {
            if (pair.getValue() == maxFreq[0]) {
                mostCommonWords.add(pair);
            }
        }

        return mostCommonWords;
    }

    private void addCommonWord(Node currentNode, List<Pair<String, Integer>> words, String subStr, int[] maxFreq) {
        int childrenCount = currentNode.children.size();
        int chapterNumbersCount = currentNode.chapterNumbers.size();

        // end of word
        if (childrenCount == 0 && chapterNumbersCount != 0) {
            if (chapterNumbersCount >= maxFreq[0]) {
                words.add(new Pair<>(subStr, chapterNumbersCount));
                maxFreq[0] = chapterNumbersCount;
            }
        }
        // node is letter
        if (childrenCount != 0 && chapterNumbersCount == 0) {
            List<Node> children = currentNode.children;
            for (Node child : children) {
                addCommonWord(child, words, subStr + child.letter, maxFreq);
            }
        }
        // there is the longest word
        if (childrenCount != 0 && chapterNumbersCount != 0) {
            if (chapterNumbersCount >= maxFreq[0]) {
                words.add(new Pair<>(subStr, chapterNumbersCount));
                maxFreq[0] = chapterNumbersCount;
            }
            List<Node> children = currentNode.children;
            for (Node child : children) {
                addCommonWord(child, words, subStr + child.letter, maxFreq);
            }
        }

    }

    // maxFreq - occurrences in the text
    public ArrayList<Pair<String, Integer>> findMostCommonWords2() {
        ArrayList<Pair<String, Integer>> words = new ArrayList<>();
        int[] maxFreq = new int[]{0};
        for (int i = 0; i < root.children.size(); i++) {
            Node childNode = root.children.get(i);
            addCommonWord2(childNode, words, childNode.letter, maxFreq);
        }

        ArrayList<Pair<String, Integer>> mostCommonWords = new ArrayList<>();

        for (Pair<String, Integer> pair : words) {
            if (pair.getValue() == maxFreq[0]) {
                mostCommonWords.add(pair);
            }
        }

        return mostCommonWords;
    }

    private void addCommonWord2(Node currentNode, List<Pair<String, Integer>> words, String subStr, int[] maxFreq) {
        int childrenCount = currentNode.children.size();
        int wordCount = currentNode.wordCount;

        // end of word
        if (childrenCount == 0 && wordCount != 0) {
            if (wordCount >= maxFreq[0]) {
                words.add(new Pair<>(subStr, wordCount));
                maxFreq[0] = wordCount;
            }
        }
        // node is letter
        if (childrenCount != 0 && wordCount == 0) {
            List<Node> children = currentNode.children;
            for (Node child : children) {
                addCommonWord2(child, words, subStr + child.letter, maxFreq);
            }
        }
        // there is the longest word
        if (childrenCount != 0 && wordCount != 0) {
            if (wordCount >= maxFreq[0]) {
                words.add(new Pair<>(subStr, wordCount));
                maxFreq[0] = wordCount;
            }
            List<Node> children = currentNode.children;
            for (Node child : children) {
                addCommonWord2(child, words, subStr + child.letter, maxFreq);
            }
        }

    }


}
