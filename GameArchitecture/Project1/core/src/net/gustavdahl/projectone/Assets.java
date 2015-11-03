package net.gustavdahl.projectone;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.sun.xml.internal.stream.events.DummyEvent;

public class Assets
{

	// QUESTION: should all assets be static???
	
	
	// TODO: make use of SKIN (visible things)
	
	// https://github.com/libgdx/libgdx/wiki/Managing-your-assets
	
	// SPRITES ///////////////////////////////////////////////////////
	private ArrayList<Texture> textures;
	public Texture SplashTexture;
	public Texture DummyTexture;
	
	// SPRITES end //////////////////////////////////////////////////

	private ArrayList<BitmapFont> fonts;
	public BitmapFont ArialFont;
	
	
	public SpriteBatch SpriteBatch;
	public Stage Stage;


	// common, menu, gameplay (assets)
	
	public void InitializeMenuAssets()
	{
		ArialFont = new BitmapFont(Gdx.files.internal("arial_black_32.fnt"));
		fonts = new ArrayList<BitmapFont>();
		fonts.add(ArialFont);
		
		SplashTexture = new Texture("splash.png");
		DummyTexture = new Texture("cat-tongue.png");
		textures = new ArrayList<Texture>();
		textures.add(SplashTexture);
		textures.add(DummyTexture);
	}
	
	public void InitializeCommonAssets()
	{
		SpriteBatch = new SpriteBatch();
		Stage = new Stage();
		
		
	}
	
	public void InitializeGamePlayAssets()
	{
		
	}

	public void DisposeAllAssets()
	{
		
		System.out.println("[All assets have been disposed]");
		
		for (BitmapFont f : fonts)
			f.dispose();

		for (Texture t : textures)
			t.dispose();

		// System.out.println("All assets have been disposed by " + name);

	}

}
