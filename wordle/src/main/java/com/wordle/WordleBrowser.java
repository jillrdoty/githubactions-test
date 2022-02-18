/*
 * (C) Copyright IBM Copr. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.wordle;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import com.wordle.instance.HelloWordl;
import com.wordle.instance.WordleApp;
import com.wordle.instance.WordleApp.LetterState;
import com.wordle.instance.WordleUK;

public class WordleBrowser {
	
	public enum WordleAppType {
		HELLO,
		UK
	}
	
	private static WordleApp getWordleApp(String appType, WebDriver driver) {
		WordleAppType type = WordleAppType.valueOf(appType);
		
		switch(type) {
		case HELLO:
			return new HelloWordl(driver);
		case UK:
			return new WordleUK(driver);
		default:
			throw new UnsupportedOperationException("Unsupported wordle app");
		}
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		
		SafariOptions options = new SafariOptions();
		WebDriver driver = new SafariDriver(options);

		WordleApp wordleApp = getWordleApp(args[0], driver);
		
		int iterations = 1;
		if(args.length > 1) {
			iterations = Integer.parseInt(args[1]);
		}
		
		String startWord = "taser";
//		if(args.length == 3) {
//			
//		} 
		
		driver.get(wordleApp.getUrl());
		
		Thread.sleep(1000);

		WebElement game = wordleApp.getGame();
		
		
		List<WebElement> tableRows = wordleApp.getRows(game);

		Map<Integer, Integer> attempts = new HashMap<>();
		
		for(int iter = 0; iter < iterations; iter++) {
			int guesses = 0;
			int correctCount = 0;
			Set<String> absent = new HashSet<>();
			Set<String> present = new HashSet<>();
			Set<String> elsewhere = new HashSet<>();
			
			while (correctCount < 5 && guesses < 6) {
				int bestWordIndex = 0;
				
//				String wordToGuess = getWordToGuess(absent, present, elsewhere, bestWordIndex++);
				String wordToGuess = guesses == 0 ? startWord : getWordToGuess(absent, present, elsewhere, bestWordIndex++);
				
				game.sendKeys(wordToGuess, Keys.RETURN);
	
				WebElement row = tableRows.get(guesses);
	
				while (!wordleApp.isRowLockedIn(row)) {
					// Word was not considered valid
//					System.out.println("Bad guess, trying again");
					Thread.sleep(100);

					game.sendKeys(Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE);
					wordToGuess = getWordToGuess(absent, present, elsewhere, bestWordIndex++);
					game.sendKeys(wordToGuess, Keys.RETURN);
	
				}
	
				List<WebElement> letters = wordleApp.getLetters(row);
				
				for (int i = 0; i < 5; i++) {
					WebElement letter = letters.get(i);
					LetterState letterState = wordleApp.getLetterState(letter);
					
					switch(letterState) {
					case ABSENT:
						absent.add(wordleApp.getLetter(letter));
						break;
					case CORRECT:
						present.add(wordleApp.getLetter(letter) + i);
						correctCount++;
						break;
					case ELSEWHERE:
						present.add(wordleApp.getLetter(letter) + "~" + i);
						break;
					default:
						throw new UnsupportedOperationException("Unsupported letter state" + letterState);
					}
				}
	
				if (correctCount == 5) {
//					System.out.println("Congrats, you won in " + (guesses+1) + " tries");
					Thread.sleep(500);
					
					int freq = attempts.getOrDefault(guesses + 1, 0);
					attempts.put(guesses + 1, freq + 1);

					break;
				}
	
				guesses++;
				correctCount = 0;
			}
			
			if(guesses == 6) {
				int freq = attempts.getOrDefault(guesses + 1, 0);
				attempts.put(guesses + 1, freq + 1);
				Thread.sleep(10000);

			}
			game.sendKeys(Keys.RETURN);
		}
		
		
		System.out.println("Summary of " + iterations + " iterations - start word: " + startWord);
		int sum = 0;
		for(Entry<Integer, Integer> entry : attempts.entrySet()) {
			if(entry.getKey() < 7) {
				System.out.println("\tWon in " + entry.getKey() + ": " + entry.getValue());
			}
			else {
				System.out.println("\tLost games: " + entry.getValue());
			}
			sum += entry.getKey() * entry.getValue();
		}
		
		System.out.println("Average: " + (double)sum/(double)iterations);
		
		

//		  WebElement first = tableRows.get(0);
//		  List<WebElement> letters = first.findElements(By.tagName("td"));
//		  
//		  for(int i = 0; i < 5; i++) {
//			  WebElement letter = letters.get(i);
//			  if(letter.getAttribute("class").contains("absent")) {
//				  System.out.println("Absent: " + letter.getText());
//				  absent.add(letter.getText());
//			  }
//			  else if(letter.getAttribute("class").contains("elsewhere")) {
//				  System.out.println("Elsewhere: " + letter.getText());
//				  present.add(letter.getText() + "~" + i);
//			  }
//			  else {
//				  System.out.println("Correct: " + letter.getText());
//				  present.add(letter.getText() + i);
//			  }
//		  }
//		  
//		  String absentArg = absent.stream().collect(Collectors.joining());
//		  String presentArg = present.stream().collect(Collectors.joining(" "));
//		  String elsewhereArg = elsewhere.stream().collect(Collectors.joining(" "));

//		  String[] wordleArgs = new String[2 + present.size() + elsewhere.size()];
//		  wordleArgs[0] = "5";
//		  wordleArgs[1] = absent.stream().collect(Collectors.joining());
//		  
//		  int index = 2;
//		  for(String correctLetter : present) {
//			  wordleArgs[index++] = correctLetter;
//		  }
//		  for(String elsewhereLeter : elsewhere) {
//			  wordleArgs[index++] = elsewhereLeter;
//		  }
//		  
//		  
//		  List<String> bestWords = Wordle.getOrderedFiveWords(wordleArgs);
//		  

		Thread.sleep(1000);
		driver.quit();
	}

	private static String getWordToGuess(Set<String> absent, Set<String> present, Set<String> elsewhere, int wordIndex)
			throws IOException {
		String[] wordleArgs = new String[2 + present.size() + elsewhere.size()];
		wordleArgs[0] = "5";
		wordleArgs[1] = absent.stream().collect(Collectors.joining());

		int index = 2;
		for (String correctLetter : present) {
			wordleArgs[index++] = correctLetter;
		}
		for (String elsewhereLeter : elsewhere) {
			wordleArgs[index++] = elsewhereLeter;
		}

		List<String> bestWords = Wordle.getOrderedFiveWords(wordleArgs);

		if(bestWords.size() <= wordIndex) {
			System.out.println("No valid words found.  Time to start making stuff up");
		}
		
		return bestWords.get(wordIndex);
	}

}
