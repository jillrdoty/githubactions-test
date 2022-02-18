/*
 * (C) Copyright IBM Copr. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.wordle.dicthelp;

import java.io.IOException;
import java.util.List;

import org.languagetool.JLanguageTool;
import org.languagetool.language.BritishEnglish;
import org.languagetool.rules.RuleMatch;

public class Trying {

	public static void main(String[] args) throws IOException {
		JLanguageTool langTool = new JLanguageTool(new BritishEnglish());
	    // comment in to use statistical ngram data:
	    //langTool.activateLanguageModelRules(new File("/data/google-ngram-data"));
	    List<RuleMatch> matches = langTool.check("jhool");
	    for (RuleMatch match : matches) {
//	    	match.
	    }

	}

}
