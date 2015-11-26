import com.aylien.textapi.TextAPIClient;
import com.aylien.textapi.parameters.*;
import com.aylien.textapi.responses.*;
import java.net.URL;

class Example
{
	public static void main(String[] args) throws Exception
	{
		TextAPIClient client = new TextAPIClient("87aa215a", "7a4e24952e4f20db11fc1508abd12eac");

		ImageTagsParams.Builder builder = ImageTagsParams.newBuilder();
		java.net.URL url = new java.net.URL("https://c1.staticflickr.com/5/4112/5170590074_714d36db83_b.jpg");
		builder.setUrl(url);
		ImageTags imageTags = client.imageTags(builder.build());
		for (ImageTag tag: imageTags.getTags()) {
		    System.out.println(tag.getName() + " (" + tag.getConfidence() + ")");
		}
	}
}