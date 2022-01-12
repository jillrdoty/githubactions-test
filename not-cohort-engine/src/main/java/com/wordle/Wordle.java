/*
 * (C) Copyright IBM Copr. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.wordle;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Wordle {

	private static final String NOT = "~";
	
	public static void main(String[] args) throws Exception {
		
		int length = 5;
		Set<Character> contains = new HashSet<>();
		Set<Character> doesNotContain = new HashSet<>();
		
		Map<Integer, Character> positionMap = new HashMap<>();
		Map<Integer, Character> notPositionMap = new HashMap<>();
		
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
					
					notPositionMap.put(pos, character);
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
						for(Entry<Integer, Character> entry : notPositionMap.entrySet()) {
							char wordCharAtPos = word.charAt(entry.getKey());
							if(entry.getValue().equals(wordCharAtPos)) {
								matches = false;
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
					
					if(matches) {
						updateCharacterDistribution(word);
						
						fiveLetterWords.add(word);
					}
				}
			}
		}
		
		System.out.println("Constraints");
		System.out.print("\tContains:");
		for(Character character : contains) {
			System.out.print(" " + character);
		}
		System.out.println();
		
		System.out.print("\tDoes Not Contain:");
		for(Character character : doesNotContain) {
			System.out.print(" " + character);
		}
		System.out.println();
		
		System.out.println(fiveLetterWords.size() + " five letter words");
		
//		if(fiveLetterWords.size() > 10) {
//			System.out.println("One example: " + fiveLetterWords.iterator().next());
//		}
//		else {
//			for(String word : fiveLetterWords) {
//				System.out.println("\t" + word);
//			}
//		}
		
		bestWord(fiveLetterWords);
		
//		System.out.println("Character frequency: ");
//		System.out.println("\tFirst Letter");
//		printCharacterFrequency(firstLetter);
//		System.out.println("\tSecond Letter");
//		printCharacterFrequency(secondLetter);
//		System.out.println("\tThird Letter");
//		printCharacterFrequency(thirdLetter);
//		System.out.println("\tFourth Letter");
//		printCharacterFrequency(fourthLetter);
//		System.out.println("\tFifth Letter");
//		printCharacterFrequency(fifthLetter);
	
	}
	
	private static Map<Character, Integer> firstLetter = new HashMap<>();
	private static Map<Character, Integer> secondLetter = new HashMap<>();
	private static Map<Character, Integer> thirdLetter = new HashMap<>();
	private static Map<Character, Integer> fourthLetter = new HashMap<>();
	private static Map<Character, Integer> fifthLetter = new HashMap<>();
	
	private static String bestWord(Set<String> words) {
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
//			score += fifthLetter.get(word.charAt(4));
			
			score = (int) (score * distinctChars);
			
			wordScores.add(new StringNumber(word, score));
		}
		
		Collections.sort(wordScores);
		
		
		int max = wordScores.size() > 10 ? 10 : wordScores.size();
		for(int i = 0; i < max; i++) {
			StringNumber strNum = wordScores.get(i);
			System.out.println(strNum.getStr() + " " + strNum.getNumber());
		}
		
		return wordScores.get(0).getStr();
	}
	
	private static void printCharacterFrequency(Map<Character, Integer> letterFrequency) {
		List<StringNumber> charFreqList = new ArrayList<>();
		
		letterFrequency.forEach((k, v) -> charFreqList.add(new StringNumber(String.valueOf(k), v)));
		
		Collections.sort(charFreqList);
		
		for(StringNumber charFreq : charFreqList) {
			System.out.println("\t\t" + charFreq.getStr() + " " + charFreq.getNumber());
		}
	}
	
	private static void updateCharacterDistribution(String word) {
		updateCharacterFrequency(firstLetter, word.charAt(0));
		updateCharacterFrequency(secondLetter, word.charAt(1));
		updateCharacterFrequency(thirdLetter, word.charAt(2));
		updateCharacterFrequency(fourthLetter, word.charAt(3));
//		updateCharacterFrequency(fifthLetter, word.charAt(4));
	}
	
	private static void updateCharacterFrequency(Map<Character, Integer> letterFrequency, Character ch) {
		int frequency = letterFrequency.getOrDefault(ch, 0) + 1;
		letterFrequency.put(ch, frequency);
	}
	

}
