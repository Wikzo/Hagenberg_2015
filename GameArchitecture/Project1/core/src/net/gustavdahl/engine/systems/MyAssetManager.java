package net.gustavdahl.engine.systems;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
	public Texture RunningMan;
	public Texture Floor;

	// SPRITES end //////////////////////////////////////////////////

	private ArrayList<BitmapFont> fonts;
	public BitmapFont ArialFont;
	public BitmapFont DebugFont;

	public SpriteBatch SpriteBatch;
	public Stage Stage;
	public ShapeRenderer ShapeRenderer;
	
	private InputMultiplexer _inputMultiplexer; // TODO: move this to Input System

	// common, menu, gameplay (assets)

	public void InitializeMenuAssets()
	{
		fonts = new ArrayList<BitmapFont>();
		
		ArialFont = new BitmapFont(Gdx.files.internal("arial_black_32.fnt"));
		fonts.add(ArialFont);

		SplashTexture = new Texture("splash.png");

		textures = new ArrayList<Texture>();
		textures.add(SplashTexture);
	}

	public void InitializeDebugAssets()
	{
		// TODO: scale font via https://github.com/libgdx/libgdx/wiki/Gdx-freetype
		
		DebugFont = new BitmapFont(Gdx.files.internal("arial_black_32.fnt"));
		DebugFont.setColor(Color.RED);
		DebugFont.getData().setScale(0.5f,0.5f);
		fonts.add(DebugFont);
		
		DummyTexture = new Texture("cat-tongue.png");
		RunningMan = new Texture("spriteSheet_man.png");
		Floor = new Texture("floor.png");

		textures.add(DummyTexture);
		textures.add(RunningMan);
		textures.add(Floor);
		
		
	}

	public void InitializeCommonAssets()
	{
		SpriteBatch = new SpriteBatch();
		Stage = new Stage();
		ShapeRenderer = new ShapeRenderer();
		
		_inputMultiplexer = new InputMultiplexer();

	}
	
	public void AddInputListener(InputProcessor input)
	{
		_inputMultiplexer.addProcessor(input);
		
		Gdx.input.setInputProcessor(_inputMultiplexer);
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
