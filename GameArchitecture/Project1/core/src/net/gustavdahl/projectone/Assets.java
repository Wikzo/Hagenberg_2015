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

	// TODO: make use of SKIN (visible things)
	
	// https://github.com/libgdx/libgdx/wiki/Managing-your-assets
	private static ArrayList<BitmapFont> fonts;
	public static BitmapFont ArialFont;

	private static ArrayList<Texture> textures;
	public static Texture SplashTexture;

	public static SpriteBatch SpriteBatch;
	public static Stage Stage;


	// common, menu, gameplay (assets)
	
	public static void InitializeMenuAssets()
	{
		ArialFont = new BitmapFont(Gdx.files.internal("arial_black_32.fnt"));
		fonts = new ArrayList<BitmapFont>();
		fonts.add(ArialFont);
		
		SplashTexture = new Texture("splash.png");
		textures = new ArrayList<Texture>();
		textures.add(SplashTexture);
	}
	
	public static void InitializeCommonAssets()
	{
		SpriteBatch = new SpriteBatch();
		Stage = new Stage();
	}
	
	public static void InitializeGamePlayAssets()
	{
		
	}

	public static void DisposeAllAssets()
	{
		
		System.out.println("[All assets have been disposed]");
		
		for (BitmapFont f : fonts)
			f.dispose();

		for (Texture t : textures)
			t.dispose();

		// System.out.println("All assets have been disposed by " + name);

	}

}
