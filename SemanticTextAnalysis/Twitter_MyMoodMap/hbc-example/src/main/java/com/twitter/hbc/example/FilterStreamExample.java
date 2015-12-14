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

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import twitter4j.JSONException;
import twitter4j.JSONObject;



public class FilterStreamExample
{

	static List<String> Texts = new ArrayList<String>();
	static List<String> Locations = new ArrayList<String>();

	public static void run(String consumerKey, String consumerSecret, String token, String secret)
			throws InterruptedException
	{
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);
		StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
		// add some track terms
		endpoint.trackTerms(Lists.newArrayList("Christmas"));
		// endpoint.getURI();

		Authentication auth = new OAuth1(consumerKey, consumerSecret, token, secret);
		// Authentication auth = new BasicAuth(username, password);

		// Create a new BasicClient. By default gzip is enabled.
		Client client = new ClientBuilder().hosts(Constants.STREAM_HOST).endpoint(endpoint).authentication(auth)
				.processor(new StringDelimitedProcessor(queue)).build();

		// Establish a connection
		client.connect();

		PrintWriter out = null;
		try
		{
			out = new PrintWriter("myTwitter.txt");
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<String> strings = new ArrayList<String>();
		String t = "";
		// Do whatever needs to be done with messages
		for (int msgRead = 0; msgRead < 10; msgRead++)
		{
			String msg = queue.take();

			JSONObject obj = null;
			String text = null;
			String location = null;

			try
			{
				text = new JSONObject(msg).getString("text");
				location = new JSONObject(msg).getJSONObject("user").getString("location");

				// validate
				if (text != null && location != null)
				{
					if (ValidateData(text, location))
					{
						text = ClearTheString(text);
						Texts.add(text);
						Locations.add(location);

						t += "LOCATION: " + location + " - TEXT: " + text + "\n";
					}
				}

			} catch (JSONException e)
			{
				e.printStackTrace();
			}

			// System.out.println(t);

		}

		client.stop();
		out.println(t);
		out.close();
	}

	public static boolean ValidateData(String text, String location)
	{
		if (!text.equalsIgnoreCase("null") && !location.equalsIgnoreCase("null"))
			return true;

		return false;

	}

	public static String ClearTheString(String input)
	{
		// https://stackoverflow.com/questions/3756657/replace-remove-string-between-two-character

		String output = input;
		System.out.println("Before: " + output);

		// only letters and numbers
		String lettersAndNumbers = output.replaceAll("[^a-zA-Z0-9]", " ");

		// remove HTTPS and RT
		String removeHTTPS = lettersAndNumbers.replace("https", "");
		String removeRT = removeHTTPS.replace("RT", "");
		
		// remove white spaces
		String trimmed = removeRT.trim();

		System.out.println("After: " + trimmed);

		return trimmed;
	}

	public static void main(String[] args)
	{
		try
		{
			FilterStreamExample.run("g8V1ywv6SEJphCGseyWagmeoy", "7zX8CHnstU9gUBFC3qRVClsKG9x8MqdQbuReMb3q1AR4VeAkS1",
					"14982223-t7GxCHppDi3FQzuCedS3H95r39WgtUNOhvKp1DYcR",
					"Nj8Vu4R8Vn11NXMa5jH0iTiWfroLleQwSj71Tb4BPMTsi");
		} catch (InterruptedException e)
		{
			System.out.println(e);
		}
	}
}
