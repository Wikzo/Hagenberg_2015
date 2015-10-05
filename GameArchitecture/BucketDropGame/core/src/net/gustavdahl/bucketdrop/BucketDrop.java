package net.gustavdahl.bucketdrop;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.w3c.dom.css.Rect;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

// from: https://github.com/libgdx/libgdx/wiki/A-simple-game

public class BucketDrop implements ApplicationListener
{
	// assets
	private Texture dropImage;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;

	// camera
	private OrthographicCamera cam;
	private SpriteBatch spriteBatch;

	// movement
	private Rectangle bucket;
	private Vector3 mousePos;

	// rain drops
	private Array<Rectangle> raindrops;
	private long lastDropTime;

	@Override
	public void create()
	{
		dropImage = new Texture("cat-tongue.png");
		bucketImage = new Texture("bucket.png");

		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		rainMusic.play();
		rainMusic.setLooping(true);

		cam = new OrthographicCamera();
		cam.setToOrtho(false, 800, 480);
		spriteBatch = new SpriteBatch();

		bucket = new Rectangle();
		bucket.x = (800 / 2) - (64 / 2); // centered
		bucket.y = 20; // draws from bottom left corner
		bucket.width = 64;
		bucket.height = 64;

		mousePos = new Vector3();

		raindrops = new Array<Rectangle>();
		SpawnRaindrop();

	}

	@Override
	public void resize(int width, int height)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void render()
	{
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		cam.update();

		spriteBatch.setProjectionMatrix(cam.combined);

		spriteBatch.begin();
		spriteBatch.draw(bucketImage, bucket.x, bucket.y);

		for (Rectangle r : raindrops)
			spriteBatch.draw(dropImage, r.x, r.y);

		spriteBatch.end();

		// input mouse
		if (Gdx.input.isTouched())
		{
			mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			cam.unproject(mousePos);
			bucket.x = mousePos.x - 64 / 2;
		}

		// input keyboard
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		else if (Gdx.input.isKeyPressed(Keys.RIGHT))
			bucket.x += 200 * Gdx.graphics.getDeltaTime();

		// screen bounds
		if (bucket.x < 0)
			bucket.x = 0;
		if (bucket.x > 800 - 64)
			bucket.x = 800 - 64;

		// rain drops
		if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
			SpawnRaindrop();

		Iterator<Rectangle> iter = raindrops.iterator();
		while (iter.hasNext())
		{
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if (raindrop.y + 64 < 0)
				iter.remove();

			if (raindrop.overlaps(bucket))
			{
				dropSound.play();
				iter.remove();
			}
		}

	}

	private void SpawnRaindrop()
	{
		Rectangle drop = new Rectangle();
		drop.x = MathUtils.random(0, 800 - 64);
		drop.y = 480;
		drop.width = 64;
		drop.height = 64;
		raindrops.add(drop);
		lastDropTime = TimeUtils.nanoTime();

	}

	@Override
	public void pause()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void resume()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose()
	{
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		spriteBatch.dispose();

	}
}
