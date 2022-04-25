/*
 * (C) Copyright IBM Copr. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.wordle.dicthelp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class EvaluateWords {
	public static void main(String[] args) throws IOException {
//		FileInputStream fis = new FileInputStream("/Users/jdoty/Desktop/testInput");
		FileInputStream fis = new FileInputStream("/usr/share/dict/words");
	    
		String password = "29d149f1-bc44-e7a0-c729-c7b770308565";
		
		int length = 5;
	    
		Set<String> knownFiveLetterWords = new HashSet<>();
		Set<String> unknownFiveLetterWords = new HashSet<>();
		
		String startingWord = null;
		if(args.length > 0) {
			startingWord = args[0];
		}
		
		try {
			try(Scanner input = new Scanner(System.in)) {
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
							
							if(startingWord == null || startingWord.compareTo(word) < 0) {
								
								
								System.out.print(word + ": ");
								
								Response response = getResponse(input);
								
								switch(response) {
								case INVALID:
									unknownFiveLetterWords.add(word);
									break;
								case VALID:
									knownFiveLetterWords.add(word);
									break;
								default:
									throw new RuntimeException("Not sure how we got here");
								}
							}
							
						}
					}
				}
			}
		}
		catch(Exception e) {
			//Write out stuff anyway!
		}
		
		try(BufferedWriter bw = new BufferedWriter(new FileWriter("/Users/jdoty/Desktop/knownFive", true))) {
			for(String knownWord : knownFiveLetterWords) {
				bw.write(knownWord + "\n");
			}
			bw.flush();
		}
		
		try(BufferedWriter bw = new BufferedWriter(new FileWriter("/Users/jdoty/Desktop/unknownFive", true))) {
			for(String unknownWord : unknownFiveLetterWords) {
				bw.write(unknownWord + "\n");
			}
			bw.flush();
		}
	}
	
	public enum Response {
		VALID,
		INVALID
	}
	
	private static Response getResponse(Scanner input) {
		Response retval = null;
		
		while(retval == null) {
			String response = input.next();
			
			if(response.contains("y")) {
				retval = Response.VALID;
			}
			else if(response.equals("break")) {
				throw new RuntimeException("Halting");
			}
			else if(response.contains("n")){
				retval = Response.INVALID;
			}
			else {
				System.out.print("Invalid response, please try again: ");
			}
		}
		
		return retval;
	}
}
