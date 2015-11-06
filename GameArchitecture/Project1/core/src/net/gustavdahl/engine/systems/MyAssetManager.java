package net.gustavdahl.engine.systems;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.sun.xml.internal.stream.events.DummyEvent;

public class MyAssetManager
{

	// TODO: make use of SKIN (visible things)

	// https://github.com/libgdx/libgdx/wiki/Managing-your-assets

	// SPRITES ///////////////////////////////////////////////////////
	private ArrayList<Texture> textures;
	public Texture SplashTexture;
	public Texture DummyTexture;
	public Texture BraidSpriteSheet;

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

		textures = new ArrayList<Texture>();
		textures.add(SplashTexture);
	}

	public void InitializeDebugAssets()
	{
		DummyTexture = new Texture("spritesheet_dummy.png");
		BraidSpriteSheet = new Texture("braid_spritesheet_trimmed.png");

		textures.add(DummyTexture);
		textures.add(BraidSpriteSheet);
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

		if (fonts.size() > 0)
		{
			for (BitmapFont f : fonts)
				f.dispose();
		}

		if (textures.size() > 0)
		{
			for (Texture t : textures)
				t.dispose();
		}

		// System.out.println("All assets have been disposed by " + name);

	}

}
