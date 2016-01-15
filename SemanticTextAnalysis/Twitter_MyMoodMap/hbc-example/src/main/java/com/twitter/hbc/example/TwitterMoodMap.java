package com.twitter.hbc.example;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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

	static String searchTerm = "";
	static int numberOfTweets = 0;
	static boolean infiniteNumberOfTweets = false;
	static String found = "";

	public static void run(String consumerKey, String consumerSecret, String token, String secret)
			throws InterruptedException, JSONException, IOException, TextAPIException
	{

		// TWITTER SERVER SETUP STUFF BEGIN
		// -----------------------------------------
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);
		StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
		// add some track terms
		endpoint.trackTerms(Lists.newArrayList(searchTerm));
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
		int validatedTweets = 0;

		// gather the tweets
		while (validatedTweets < numberOfTweets || infiniteNumberOfTweets)
		// for (int validatedTweets = 0; validatedTweets < numberOfTweets;)
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
				// System.out.println(text);
				String originalLocation = location;
				location = ValidateData(text, location);
				if (location != "")
				{

					text = ClearTheString(text);

					JSONObject myJson = new JSONObject();
					myJson.put("Location", location);//
					// myJson.put("Text", text);

					Thread.sleep(100);
					String mood = SentimentAnalysis(text, textApiClient);

					if (mood != "")
					{

						validatedTweets++;

						myJson.put("Mood", mood);

						jsonArray.add(myJson);

						found = "Found: " + myJson.toString() + " - Original location: " + originalLocation + " - Text: " + text;
						System.out.println(found);

						// validatedTweets);
						WriteToJSONFile(jsonArray);
					}

				}
				
				

				System.out.println("Gathered tweets: " + validatedTweets);

			}
		}

		WriteToJSONFile(jsonArray);

		client.stop();

	}

	private static void WriteToJSONFile(JSONArray jsonArray) throws IOException
	{

		// http://crunchify.com/how-to-write-json-object-to-file-in-java/
		try (FileWriter file = new FileWriter("tweets.json"))
		{
			file.write(jsonArray.toJSONString());
			// System.out.println("Successfully Copied JSON Object to File...");
			// System.out.println("\nJSON Object: " + jsonArray);

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

		//System.out.println(l);

		String validatedString = l;
		boolean valid = false;

		// Netherlands
		if (l.contains("netherlands") || l.contains("holland") || l.contains("amsterdam") || l.contains("rotterdam")
				|| l.contains("utrecht"))
		{
			valid = true;
			validatedString = "amsterdam";
		}

		// Canada
		if (l.contains("canada") || l.contains("danmark") || l.equalsIgnoreCase("british columbia")
				|| l.contains("quebec") || l.contains("toronto") || l.contains("ontario") || l.contains("Winnipeg"))
		{
			valid = true;
			validatedString = "canada";
		}

		// Denmark
		if (l.contains("denmark") || l.contains("danmark") || l.contains("copenhagen") || l.contains("aalborg"))
		{
			valid = true;
			validatedString = "denmark";
		}

		// Sweden
		if (l.contains("sweden") || l.contains("stockholm") || l.contains("gothenburg") || l.contains("malmo"))
		{
			valid = true;
			validatedString = "sweden";
		}

		// Norway
		if (l.contains("norway") || l.contains("oslo") || l.contains("bergen") || l.contains("trondheim"))
		{
			valid = true;
			validatedString = "norway";
		}

		// Australia
		if (l.contains("australia") || l.contains("sidney") || l.contains("melbourne") || l.contains("brisbane")
				|| l.contains("perth"))
		{
			valid = true;
			validatedString = "australia";
		}

		// Ireland
		if (l.contains("ireland") || l.contains("dublin") || l.contains("cork") || l.contains("belfast"))
		{
			valid = true;
			validatedString = "ireland";
		}

		// New Zealand
		if (l.contains("newzeland") || l.contains("auckland") || l.contains("wellington"))
		{
			valid = true;
			validatedString = "newzeland";
		}

		// USA (special case)
		if (l.contains("us") || l.contains("usa") || l.contains("america") || l.contains("los angeles")
				|| l.contains("california") || l.contains("york") || l.equalsIgnoreCase("unitedstates")
				|| l.equalsIgnoreCase("newyork") || l.contains("washington"))
		{
			valid = true;
			validatedString = "usa";
		}

		// UK (special case)
		if (l.contains("uk") || l.contains("england") || l.contains("uk") || l.contains("u.k.") || l.contains("britain")
				|| l.contains("great britain") || l.equalsIgnoreCase("unitedkingdom") || l.contains("london"))
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
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Write your search term:");
		searchTerm = keyboard.nextLine();

		System.out.println("Write how many tweets you want to gather ('-1' is infinite):");
		numberOfTweets = keyboard.nextInt();

		if (numberOfTweets <= -1)
		{
			numberOfTweets = 1;
			infiniteNumberOfTweets = true;
		}

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
			// System.out.println(polarity);

			if (polarity.equals("positive"))
				return "1";
			else if (polarity.equals("negative"))
				return "-1";
			else
				return "0";

			// return sentiment.getPolarity();
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
