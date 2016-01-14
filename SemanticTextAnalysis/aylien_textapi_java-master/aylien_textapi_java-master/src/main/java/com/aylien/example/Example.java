package com.aylien.example;
import com.aylien.textapi.TextAPIClient;
import com.aylien.textapi.TextAPIException;
import com.aylien.textapi.parameters.*;
import com.aylien.textapi.responses.*;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class Example
{

	public static void main(String[] args) throws Exception
	{

		// http://docs.aylien.com/docs/sentiment
		// http://kodejava.org/how-do-i-read-all-lines-from-a-file/

		TextAPIClient client = new TextAPIClient("87aa215a", "7a4e24952e4f20db11fc1508abd12eac");

		String text = "hej med dig";

		boolean isEnglish = true;
		//isEnglish = DetectEnglishLanguage(client, text);
		
		if (isEnglish)
		{
			SentimentParams.Builder builder = SentimentParams.newBuilder();

			builder.setMode("tweet");
			builder.setText(text);

			Sentiment sentiment = client.sentiment(builder.build());

			double score = 0;

			score = sentiment.getPolarityConfidence();

			System.out.println(sentiment.getPolarity());
			System.out.println(score);

			// good 0.8693919697854208
			// bad 0.8963085163984621
		}

		

	}

	static boolean DetectEnglishLanguage(TextAPIClient client, String text) throws TextAPIException
	{
		LanguageParams.Builder builder = LanguageParams.newBuilder();
		builder.setText(text);
		Language language = client.language(builder.build());
		//System.out.println("language: " + language + ", confidence: " + language.getConfidence());

		if (language.getLanguage() == "en" && language.getConfidence() > 0.7)
			return true;
		else
			return false;
	}
}