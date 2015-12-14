import com.aylien.textapi.TextAPIClient;
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

		SentimentParams.Builder builder = SentimentParams.newBuilder();

		/*String fileName = "data.txt";

		try
		{
			List<String> lines = Files.readAllLines(Paths.get(fileName), Charset.defaultCharset());
			for (String line : lines)
			{
				System.out.println(line);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
*/
		builder.setText(
				"Just under a week left to order a fab  AudreLorde mug in time for Christmas  amp  make a difference at the same time     t co MSFnvdjypC");
		// builder.setMode("tweet");
		Sentiment sentiment = client.sentiment(builder.build());

		double score = 0;

		score = sentiment.getPolarityConfidence();

		System.out.println(sentiment.getPolarity());
		System.out.println(score);

		// good 0.8693919697854208
		// bad 0.8963085163984621

	}
}