package net.gustavdahl.projectone;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Assets
{

	// TODO: make use of AssetManager
	// https://github.com/libgdx/libgdx/wiki/Managing-your-assets
	private static ArrayList<BitmapFont> fonts;
	public static BitmapFont Font;

	private static ArrayList<Texture> textures;
	public static Texture SplashTexture;

	public static SpriteBatch SpriteBatch;
	public static Stage Stage;

	// static initializer -
	// https://stackoverflow.com/questions/335311/static-initializer-in-java
	static
	{
		InitializeAssets();

	}

	static void InitializeAssets()
	{
		Font = new BitmapFont(Gdx.files.internal("arial_black_32.fnt"));
		fonts = new ArrayList<BitmapFont>();
		fonts.add(Font);

		SplashTexture = new Texture("splash.png");
		textures = new ArrayList<Texture>();
		textures.add(SplashTexture);

		SpriteBatch = new SpriteBatch();
		Stage = new Stage();

		// System.out.println("AssetManager initialized");
	}

	public static void DisposeAllAssets(String name)
	{
		for (BitmapFont f : fonts)
			f.dispose();

		for (Texture t : textures)
			t.dispose();

		System.out.println("All assets have been disposed by " + name);

	}

}
