/*
 * (C) Copyright IBM Copr. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.wordle.instance;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WordleUK implements WordleApp {

	private final WebDriver driver;
	
	public WordleUK(WebDriver driver) {
		this.driver = driver;
	}
	
	@Override
	public String getUrl() {
		return "https://www.powerlanguage.co.uk/wordle/";
	}

	@Override
	public WebElement getGame() {
		WebElement gameAppRoot = driver.findElement(By.tagName("game-app"));
		WebElement shadowGameApp = expandShadowRoot(driver, gameAppRoot);
		
		WebElement gameThemeManagerRoot = shadowGameApp.findElement(By.cssSelector("game-theme-manager"));

		WebElement gameRoot = gameThemeManagerRoot.findElement(By.id("game"));
		
		WebElement gameModalRoot = gameRoot.findElement(By.cssSelector("game-modal"));
		WebElement shadowGameModal = expandShadowRoot(driver, gameModalRoot);
		
		WebElement closeInstructions = shadowGameModal.findElement(By.className("close-icon"));
		closeInstructions.click();
		
		return gameRoot.findElement(By.id("board-container"));
	}
	
	private WebElement expandShadowRoot(WebDriver driver, WebElement element) {
		return (WebElement) ((JavascriptExecutor) driver).executeScript("return arguments[0].shadowRoot", element);
	}

	@Override
	public List<WebElement> getRows(WebElement game) {
		return game.findElements(By.tagName("game-row"));
	}

	@Override
	public boolean isRowLockedIn(WebElement row) {
		return !row.getAttribute("letters").isEmpty();
	}

	@Override
	public List<WebElement> getLetters(WebElement row) {
		//Wait for wordle to flip the letters
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		WebElement shadowRow = expandShadowRoot(driver, row);
		WebElement rowClass = shadowRow.findElement(By.cssSelector("div.row"));
		
		return rowClass.findElements(By.tagName("game-tile"));
	}

	@Override
	public LetterState getLetterState(WebElement letter) {
		if (letter.getAttribute("evaluation").equals("absent")) {
			return LetterState.ABSENT;
		} else if (letter.getAttribute("evaluation").contains("present")) {
			return LetterState.ELSEWHERE;
		} else {
			return LetterState.CORRECT;
		}
	}

	@Override
	public String getLetter(WebElement letter) {
		return letter.getAttribute("letter");
	}

}
