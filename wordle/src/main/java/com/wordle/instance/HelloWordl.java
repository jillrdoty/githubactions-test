/*
 * (C) Copyright IBM Copr. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.wordle.instance;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HelloWordl implements WordleApp {

	private final WebDriver driver;
	
	public HelloWordl(WebDriver driver) {
		this.driver = driver;
	}
	
	@Override
	public String getUrl() {
		return "https://hellowordl.net";
	}

	@Override
	public WebElement getGame() {
		return driver.findElement(By.className("Game"));
	}

	@Override
	public List<WebElement> getRows(WebElement game) {
		WebElement gameRows = game.findElement(By.className("Game-rows"));
		return gameRows.findElements(By.tagName("tr"));
	}

	@Override
	public boolean isRowLockedIn(WebElement row) {
		return row.getAttribute("class").contains("locked-in");
	}

	@Override
	public List<WebElement> getLetters(WebElement row) {
		return row.findElements(By.tagName("td"));
	}

	@Override
	public LetterState getLetterState(WebElement letter) {
		
		if (letter.getAttribute("class").contains("absent")) {
			return LetterState.ABSENT;
		} else if (letter.getAttribute("class").contains("elsewhere")) {
			return LetterState.ELSEWHERE;
		} else {
			return LetterState.CORRECT;
		}
	}
	
	@Override
	public String getLetter(WebElement letter) {
		return letter.getText();
	}
}
