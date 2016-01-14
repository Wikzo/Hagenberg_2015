/**
 * Copyright 2013 Twitter, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package com.twitter.hbc.example;

import java.io.Console;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.aylien.textapi.TextAPIClient;
import com.aylien.textapi.TextAPIException;
import com.aylien.textapi.parameters.LanguageParams;
import com.aylien.textapi.parameters.SentimentParams;
import com.aylien.textapi.responses.Language;
import com.aylien.textapi.responses.Sentiment;
import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import twitter4j.JSONException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TwitterMoodMap
{

	static TextAPIClient textApiClient = new TextAPIClient("87aa215a", "7a4e24952e4f20db11fc1508abd12eac");

	public static void run(String consumerKey, String consumerSecret, String token, String secret)
			throws InterruptedException, JSONException, IOException, TextAPIException
	{

		// TWITTER SERVER SETUP STUFF BEGIN
		// -----------------------------------------
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);
		StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
		// add some track terms
		endpoint.trackTerms(Lists.newArrayList("Rickman"));
		// endpoint.getURI();

		Authentication auth = new OAuth1(consumerKey, consumerSecret, token, secret);
		// Authentication auth = new BasicAuth(username, password);

		// Create a new BasicClient. By default gzip is enabled.
		Client client = new ClientBuilder().hosts(Constants.STREAM_HOST).endpoint(endpoint).authentication(auth)
				.processor(new StringDelimitedProcessor(queue)).build();

		// Establish a connection
		client.connect();

		// TWITTER SERVER SETUP STUFF END
		// -----------------------------------------

		JSONObject outputJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		twitter4j.JSONObject twitterJSON;

		// Do whatever needs to be done with messages
		for (int validatedTweets = 0; validatedTweets < 100;)
		{
			// get next json message
			String msg = queue.take();

			// get text and location via json
			String text = null;
			String location = null;

			twitterJSON = new twitter4j.JSONObject(msg);

			if (twitterJSON.has("text"))
			{
				text = twitterJSON.getString("text");

				if (twitterJSON.getJSONObject("user").has("location"))
					location = twitterJSON.getJSONObject("user").getString("location");
			}

			// validate if it has both a text and a location
			if (text != null && location != null)
			{
				location = ValidateData(text, location);
				if (location != "")
				{

					// validatedTweets++;

					text = ClearTheString(text);

					JSONObject myJson = new JSONObject();
					myJson.put("Location", location);//
					// myJson.put("Text", text);

					String mood = SentimentAnalysis(text, textApiClient);

					if (mood != "")
					{
						validatedTweets++;

						myJson.put("Mood", mood);

						jsonArray.add(myJson);

						System.out.println(myJson.toString());
						
						
						System.out.println("Gathered tweets: " + validatedTweets);
						WriteToJSONFile(jsonArray);
					}

					

				}

				
				//System.out.println("Gathered tweets: " + validatedTweets);
				// Thread.sleep(1000);

			}
		}

		WriteToJSONFile(jsonArray);

		client.stop();

	}

	private static void WriteToJSONFile(JSONArray jsonArray) throws IOException
	{

		// http://crunchify.com/how-to-write-json-object-to-file-in-java/
		// System.out.println(jsonArray);

		// try-with-resources statement based on post comment below :)
		try (FileWriter file = new FileWriter("C:/Users/Wikzo/Documents/Hagenberg_2015/SemanticTextAnalysis/Twitter_MyMoodMap/Web/tweets.json"))
		{
			file.write(jsonArray.toJSONString());
			//System.out.println("Successfully Copied JSON Object to File...");
			//System.out.println("\nJSON Object: " + jsonArray);

		}
	}

	public static String ValidateData(String text, String location)
	{
		boolean bothAreNotNull = false;
		if (!text.equalsIgnoreCase("null") && !location.equalsIgnoreCase("null"))
			bothAreNotNull = true;

		String validatedLocation = "";
		if (bothAreNotNull)
			validatedLocation = ValidateLocation(location);

		return validatedLocation;

	}

	public static String ValidateLocation(String location)
	{
		String l = location.trim().toLowerCase();

		String validatedString = l;
		String[] validLocations =
		{ "ireland", "newzeland", "australia", "norway", "denmark", "sweden", "canada", "singapore" };

		boolean valid = false;
		for (int i = 0; i < validLocations.length; i++)
		{
			if (l.equalsIgnoreCase(validLocations[i]))
				valid = true;
		}

		// USA
		if (l.equalsIgnoreCase("us") || l.equalsIgnoreCase("usa") || l.equalsIgnoreCase("america")
				|| l.equalsIgnoreCase("unitedstates") || l.equalsIgnoreCase("northamerica"))
		{
			valid = true;
			validatedString = "usa";
		}

		// UK
		if (l.equalsIgnoreCase("uk") || l.equalsIgnoreCase("england") || l.equalsIgnoreCase("uk")
				|| l.equalsIgnoreCase("britain") || l.equalsIgnoreCase("greatbritain")
				|| l.equalsIgnoreCase("unitedkingdom") || l.equalsIgnoreCase("london"))
		{
			valid = true;
			validatedString = "uk";
		}

		if (valid)
			return validatedString;
		else
			return "";
	}

	public static String ClearTheString(String input)
	{
		// https://stackoverflow.com/questions/3756657/replace-remove-string-between-two-character

		String output = input;
		// System.out.println("Before: " + output);

		// only letters and numbers
		String lettersAndNumbers = output.replaceAll("[^a-zA-Z0-9]", " ");

		// remove HTTPS and RT
		String removeHTTPS = lettersAndNumbers.replace("https", "");
		String removeRT = removeHTTPS.replace("RT", "");

		// remove white spaces
		String trimmed = removeRT.trim();

		// System.out.println("After: " + trimmed);

		return trimmed;
	}

	public static void main(String[] args) throws InterruptedException, JSONException, IOException, TextAPIException
	{
		run("g8V1ywv6SEJphCGseyWagmeoy", "7zX8CHnstU9gUBFC3qRVClsKG9x8MqdQbuReMb3q1AR4VeAkS1",
				"14982223-t7GxCHppDi3FQzuCedS3H95r39WgtUNOhvKp1DYcR", "Nj8Vu4R8Vn11NXMa5jH0iTiWfroLleQwSj71Tb4BPMTsi");

	}

	private static String SentimentAnalysis(String text, TextAPIClient textAPIClient) throws TextAPIException
	{
		// http://docs.aylien.com/docs/sentiment
		// http://kodejava.org/how-do-i-read-all-lines-from-a-file/

		boolean isEnglish = true;
		// isEnglish = DetectEnglishLanguage(textAPIClient, text);

		if (isEnglish)
		{
			SentimentParams.Builder builder = SentimentParams.newBuilder();

			builder.setMode("tweet");
			builder.setText(text);

			Sentiment sentiment = textAPIClient.sentiment(builder.build());

			
			String polarity = sentiment.getPolarity();
			//System.out.println(polarity);
			
			if (polarity.equals("positive"))
				return "1";
			else if (polarity.equals("negative"))
				return "-1";
			else
				return "0";
			
			//return sentiment.getPolarity();
			// System.out.println(score);

			// good 0.8693919697854208
			// bad 0.8963085163984621
		}

		return "";
	}

	private static boolean DetectEnglishLanguage(TextAPIClient client, String text) throws TextAPIException
	{
		LanguageParams.Builder builder = LanguageParams.newBuilder();
		builder.setText(text);
		Language language = client.language(builder.build());
		// System.out.println("language: " + language + ", confidence: " +
		// language.getConfidence());

		if (language.getLanguage() == "en" && language.getConfidence() > 0.7)
			return true;
		else
			return false;
	}

}
