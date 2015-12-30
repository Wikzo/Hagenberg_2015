package net.gustavdahl.echelonengine.systems;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.sun.xml.internal.stream.events.DummyEvent;

import net.gustavdahl.echelonengine.components.SpriteAnimator;

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
	public Texture CogWheelAtlas;
	public TextureRegion[] CogWheels;
	public TextureRegion[] RunningManRegion;

	// SPRITES end //////////////////////////////////////////////////

	private ArrayList<BitmapFont> fonts;
	public BitmapFont ArialFont;
	public BitmapFont DebugFont;
	public BitmapFont DebugFont2;
	public BitmapFont InnerMenuFont;

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
		DebugFont = new BitmapFont(Gdx.files.internal("arial_black_32.fnt"));
		DebugFont.setColor(Color.YELLOW);
		DebugFont.getData().setScale(0.5f,0.5f);
		fonts.add(DebugFont);
		
		DebugFont2 = new BitmapFont(Gdx.files.internal("arial_black_32.fnt"));
		DebugFont2.setColor(Color.WHITE);
		DebugFont2.getData().setScale(0.5f,0.5f);
		fonts.add(DebugFont2);
		
		// from: https://github.com/libgdx/libgdx/wiki/Gdx-freetype
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ROUGD__.TTF"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 22;
		InnerMenuFont = generator.generateFont(parameter); 
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
		fonts.add(InnerMenuFont);
		
		DummyTexture = new Texture("cat-tongue.png");
		RunningMan = new Texture("spriteSheet_man.png");
		Floor = new Texture("floor.png");
		CogWheelAtlas = new Texture("cogwheels_atlas.png");

		textures.add(DummyTexture);
		textures.add(RunningMan);
		textures.add(Floor);
		
		CogWheels = SpriteAnimator.CreateSpriteSheet(CogWheelAtlas, 3, 3, 1);
		RunningManRegion = SpriteAnimator.CreateSpriteSheet(RunningMan, 30, 6, 5);
		
		
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
