import com.aylien.textapi.TextAPIClient;
import com.aylien.textapi.parameters.*;
import com.aylien.textapi.responses.*;
import java.net.URL;

class Example
{
	public static void main(String[] args) throws Exception
	{
		TextAPIClient client = new TextAPIClient("87aa215a", "7a4e24952e4f20db11fc1508abd12eac");

		RelatedParams params = new RelatedParams("rapidminer", 20);
		Related related = client.related(params);
		for (Phrase phrase: related.getPhrases()) {
		  System.out.println(phrase);
		}
	}
}