/*
 * (C) Copyright IBM Copr. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.wordle;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class Wordle {

	private static final String NOT = "~";
	

	
	public static List<String> getOrderedFiveWords(String[] args) throws IOException {
		int length = 5;
		Set<Character> contains = new HashSet<>();
		Set<Character> doesNotContain = new HashSet<>();
		
		Map<Integer, Character> positionMap = new HashMap<>();
		Map<Integer, Set<Character>> notPositionMap = new HashMap<>();
		
		if(args.length > 0) {
			length = Integer.valueOf(args[0]);
		}
		if(args.length > 1) {
			String mashDoesNotContain = args[1];
			
			for(int i = 0; i < mashDoesNotContain.length(); i++) {
				doesNotContain.add(mashDoesNotContain.charAt(i));
			}
		}
		if(args.length > 2) {
			for(int i = 2; i < args.length; i++) {
				String code = args[i];
				// Expected format:  "x1" to indicate x in position 1 or "v~2" to indicate v is included but not at index 2
				char character = code.charAt(0);
				
				if(code.contains(NOT)) {
					int pos = Integer.parseInt(String.valueOf(code.charAt(2)));
					
					Set<Character> existingNotChars = notPositionMap.get(pos);
					
					if(existingNotChars == null) {
						existingNotChars = new HashSet<>();
					}
					
					existingNotChars.add(character);
					notPositionMap.put(pos, existingNotChars);
					
					contains.add(character);
				}
				else {
					int pos = Integer.parseInt(String.valueOf(code.charAt(1)));
					
					positionMap.put(pos, character);
					contains.add(character);
				}
				
				doesNotContain.remove(character);
			}
		}
		
		FileInputStream fis = new FileInputStream("/usr/share/dict/words");
	    
		Set<String> fiveLetterWords = new HashSet<>();
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
			String dictWord;
			while ((dictWord = br.readLine()) != null) {
				
				Set<String> words = new HashSet<>();
				
				if(dictWord.length() == (length - 1) && !dictWord.endsWith("s")) {
					words.add(dictWord + "s");
				}
				
				if(dictWord.length() == (length - 1) && dictWord.endsWith("e")) {
					words.add(dictWord + "d");
				}
				
				if(dictWord.length() == (length - 3)) {
					words.add(dictWord + "ing");
				}
				
				if(dictWord.length() == length) {
					words.add(dictWord);
				}
				
				for(String word : words) {
					word = word.toLowerCase();
					
					boolean matches = validateWord(word, contains, doesNotContain, positionMap, notPositionMap);
					
					if(matches) {
						updateCharacterDistribution(word);
						
						fiveLetterWords.add(word);
					}
				}
			}
		}
		
		if(fiveLetterWords.isEmpty()) {
			System.out.println("Zero matching dictonary words, adding made up words");
			fiveLetterWords = makeStuffUp(contains, doesNotContain, positionMap, notPositionMap);
		}
		
		return orderWords(fiveLetterWords);
	}
	
	private static boolean validateWord(String word, Set<Character> contains, Set<Character> doesNotContain, Map<Integer, Character> positionMap, Map<Integer, Set<Character>> notPositionMap) {
		boolean matches = true;
		
		if(!doesNotContain.isEmpty()) {
			for(Character character : doesNotContain) {
				if(word.contains(String.valueOf(character))) {
					matches = false;
				}
			}
		}
		
		if(!contains.isEmpty()) {
			for(Character character : contains) {
				if(!word.contains(String.valueOf(character))) {
					matches = false;
				}
			}
		}
		
		if(!notPositionMap.isEmpty()) {
			for(Entry<Integer, Set<Character>> entry : notPositionMap.entrySet()) {
				char wordCharAtPos = word.charAt(entry.getKey());
				for(Character ch : entry.getValue()) {
					if(ch.charValue() == wordCharAtPos) {
						matches = false;
					}
				}
			}
		}
		
		if(!positionMap.isEmpty()) {
			for(Entry<Integer, Character> entry : positionMap.entrySet()) {
				char wordCharAtPos = word.charAt(entry.getKey());
				if(!entry.getValue().equals(wordCharAtPos)) {
					matches = false;
				}
			}
		}
		
		return matches;
	}
	
	private static List<Character> getAllLetters() {
		List<Character> letters = new ArrayList<>();
		letters.add('a');
		letters.add('b');
		letters.add('c');
		letters.add('d');
		letters.add('e');
		letters.add('f');
		letters.add('g');
		letters.add('h');
		letters.add('i');
		letters.add('j');
		letters.add('k');
		letters.add('l');
		letters.add('m');
		letters.add('n');
		letters.add('o');
		letters.add('p');
		letters.add('q');
		letters.add('r');
		letters.add('s');
		letters.add('t');
		letters.add('u');
		letters.add('v');
		letters.add('w');
		letters.add('x');
		letters.add('y');
		letters.add('z');
		
		return letters;
	}
	
	public static Set<String> makeStuffUp(Set<Character> contains, Set<Character> doesNotContain, Map<Integer, Character> positionMap, Map<Integer, Set<Character>> notPositionMap) {
		
		HashSet<String> madeUpWords = new HashSet<>();
		
		List<Character> firstLetterOptions = positionMap.containsKey(0) ? convertToList(positionMap.get(0)) : getAllLetters();
		List<Character> secondLetterOptions = positionMap.containsKey(1) ? convertToList(positionMap.get(1)) : getAllLetters();
		List<Character> thirdLetterOptions = positionMap.containsKey(2) ? convertToList(positionMap.get(2)) : getAllLetters();
		List<Character> fourthLetterOptions = positionMap.containsKey(3) ? convertToList(positionMap.get(3)) : getAllLetters();
		List<Character> fifthLetterOptions = positionMap.containsKey(4) ? convertToList(positionMap.get(4)) : getAllLetters();
		
		for(Character firstCh : firstLetterOptions) {
			for(Character secondCh : secondLetterOptions) {
				for(Character thirdCh : thirdLetterOptions) {
					for(Character fourthCh : fourthLetterOptions) {
						for(Character fifthCh : fifthLetterOptions) {
							String word = firstCh.toString() + secondCh.toString() + thirdCh.toString() + fourthCh.toString() + fifthCh.toString();
							if(validateWord(word, contains, doesNotContain, positionMap, notPositionMap)) {
								madeUpWords.add(word);
								updateCharacterDistribution(word);
								
							}
						}
					}
				}
			}
		}
		
		return madeUpWords;
	}
	
	private static List<Character> convertToList(Character ch) {
		List<Character> chList = new ArrayList<>();
		chList.add(ch);
		
		return chList;
	}
	
	public static void main(String[] args) throws Exception {
		
		
		List<String> fiveLetterWords = getOrderedFiveWords(args);
		
		System.out.println(fiveLetterWords.size() + " five letter words");
		
		if(fiveLetterWords.size() > 10) {
			for(int i = 0; i < 10; i++) {
				System.out.println(fiveLetterWords.get(i));
			}
		}
		else {
			for(String word : fiveLetterWords) {
				System.out.println("\t" + word);
			}
		}
	
	}
	
	private static Map<Character, Integer> firstLetter = new HashMap<>();
	private static Map<Character, Integer> secondLetter = new HashMap<>();
	private static Map<Character, Integer> thirdLetter = new HashMap<>();
	private static Map<Character, Integer> fourthLetter = new HashMap<>();
	private static Map<Character, Integer> fifthLetter = new HashMap<>();
	
	private static List<String> orderWords(Set<String> words) {
		//How to determine the best guess?
		/*
		 * All different letters
		 * Higher frequency letters in each position
		 */
		
		List<StringNumber> wordScores = new ArrayList<>();
		
		
		
		for(String word : words) {
			long distinctChars = word.chars().distinct().count();
			int score = 0;
			
			score += firstLetter.get(word.charAt(0));
			score += secondLetter.get(word.charAt(1));
			score += thirdLetter.get(word.charAt(2));
			score += fourthLetter.get(word.charAt(3));
			score += fifthLetter.get(word.charAt(4));
			
			score = (int) (score * distinctChars);
			
			wordScores.add(new StringNumber(word, score));
		}
		
		Collections.sort(wordScores);
		
//		int max = wordScores.size() > 10 ? 10 : wordScores.size();
//		for(int i = 0; i < max; i++) {
//			StringNumber strNum = wordScores.get(i);
//			System.out.println(strNum.getStr() + " " + strNum.getNumber());
//		}
		
		return wordScores.stream().map(x -> x.getStr()).collect(Collectors.toList());
	}
	
//	private static void printCharacterFrequency(Map<Character, Integer> letterFrequency) {
//		List<StringNumber> charFreqList = new ArrayList<>();
//		
//		letterFrequency.forEach((k, v) -> charFreqList.add(new StringNumber(String.valueOf(k), v)));
//		
//		Collections.sort(charFreqList);
//		
//		for(StringNumber charFreq : charFreqList) {
//			System.out.println("\t\t" + charFreq.getStr() + " " + charFreq.getNumber());
//		}
//	}
	
	private static void updateCharacterDistribution(String word) {
		updateCharacterFrequency(firstLetter, word.charAt(0));
		updateCharacterFrequency(secondLetter, word.charAt(1));
		updateCharacterFrequency(thirdLetter, word.charAt(2));
		updateCharacterFrequency(fourthLetter, word.charAt(3));
		updateCharacterFrequency(fifthLetter, word.charAt(4));
	}
	
	private static void updateCharacterFrequency(Map<Character, Integer> letterFrequency, Character ch) {
		int frequency = letterFrequency.getOrDefault(ch, 0) + 1;
		letterFrequency.put(ch, frequency);
	}
	

}
