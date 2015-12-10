import com.aylien.textapi.TextAPIClient;
import com.aylien.textapi.parameters.*;
import com.aylien.textapi.responses.*;
import java.net.URL;

class Example
{
	public static void main(String[] args) throws Exception
	{
		
		// http://docs.aylien.com/docs/sentiment
		
		TextAPIClient client = new TextAPIClient("87aa215a", "7a4e24952e4f20db11fc1508abd12eac");

		SentimentParams.Builder builder = SentimentParams.newBuilder();
		builder.setText("John is a very bad football player");
		//builder.setMode("tweet");
		Sentiment sentiment = client.sentiment(builder.build());
		
		double score = 0;
		
		score = sentiment.getPolarityConfidence();
		
		if (sentiment.getPolarity().equals("negative"))
			score *= -1;
		
		System.out.println(sentiment.getPolarity());
		System.out.println(score);
		
		// good 0.8693919697854208
		// bad 0.8963085163984621

	}
}