package net.gustavdahl.bucketdrop.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.gustavdahl.bucketdrop.BucketDrop;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "Drop";
		config.width = 800;
		config.height = 480;
		
		new LwjglApplication(new BucketDrop(), config);
	}
}
