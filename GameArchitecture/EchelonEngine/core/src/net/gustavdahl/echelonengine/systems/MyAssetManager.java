package net.gustavdahl.echelonengine.systems;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import net.gustavdahl.echelonengine.components.visual.SpriteAnimator;

public class MyAssetManager
{

	private ArrayList<Texture> textures;
	public Texture SplashTexture;
	public Texture MenuBackground;
	public Texture GameBackground;
	public Texture RunningMan;
	public Texture Floor;
	public Texture CogWheelAtlas;
	public TextureRegion[] CogWheels;
	public TextureRegion[] RunningManRegion;

	private ArrayList<BitmapFont> fonts;
	public BitmapFont ArialFont;
	public BitmapFont DebugFont;
	public BitmapFont DebugFont2;
	public BitmapFont InnerMenuFont;

	public SpriteBatch SpriteBatch;
	public Stage Stage;
	public ShapeRenderer ShapeRenderer;
	
	private InputMultiplexer _inputMultiplexer;


	public void InitializeMenuAssets()
	{
		fonts = new ArrayList<BitmapFont>();
		
		ArialFont = new BitmapFont(Gdx.files.internal("arial_black_32.fnt"));
		fonts.add(ArialFont);

		SplashTexture = new Texture("echelon_engine_logo.png");
		MenuBackground = new Texture("menu_background.png");
		GameBackground = new Texture("game_background.png");

		textures = new ArrayList<Texture>();
		textures.add(SplashTexture);
		textures.add(MenuBackground);
		textures.add(GameBackground);
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
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Rokkitt.otf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 32;
		InnerMenuFont = generator.generateFont(parameter); 
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
		fonts.add(InnerMenuFont);
		
		RunningMan = new Texture("spriteSheet_man.png");
		Floor = new Texture("floor.png");
		CogWheelAtlas = new Texture("cogwheels_atlas.png");

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
	}

}
