package net.gustavdahl.projectone;

import java.sql.Time;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;

public class Menu implements Screen
{

	
	private Project1 project1;
	private Stage stage;

	private Texture texture;
	private SpriteBatch spriteBatch;
	private OrthographicCamera cam;

	private float displayTime;

	public Menu(Project1 project)
	{
		// TODO Auto-generated constructor stub
		this.project1 = project;
		this.stage = project1.getStage();

		this.spriteBatch = project.getSpriteBatch();
		this.cam = new OrthographicCamera();

		texture = new Texture("splash.png");

		this.displayTime = 2f;
		
			
		font = new BitmapFont(Gdx.files.internal("arial_black_32.fnt"),
                Gdx.files.internal("arial_black_32.png"), false);
	}

	 public BitmapFont font;
	 
	 
	 // TODO: transitions states (for changing bg color)
	 void DebugStuff()
	 {
		 
	 }
	
	@Override
	public void show()
	{
		// TODO Auto-generated method stub
		
		Image img = new Image(texture);
		img.setPosition(0, 0, Align.center);
		img.getColor().a = 0f;
		img.setName("Splash");

		//img.addAction(Actions.sequence(Actions.fadeOut(5f), Actions.hide()));



		stage.addActor(img);

	}

	@Override
	public void render(float delta)
	{
		Gdx.gl.glClearColor(0.1f, 1f, 0.8f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		spriteBatch.setProjectionMatrix(cam.combined);

		spriteBatch.begin();

		float w = texture.getWidth();
		float h = texture.getHeight();

		//spriteBatch.draw(texture, -w / 2, -h / 2);
		
		// DRAW MENU
		this.font.draw(spriteBatch, "Menu Item 1 ", 100, 150);
		
		// label
		Label l;
		Label.LabelStyle f;


		spriteBatch.end();

		/*if (displayTime > 0)
			displayTime -= delta;
		else
		{
			//System.out.println("New splash screen");
			project1.setScreen(new SplashScreen2(this.project1));

		}*/

	}
	


	@Override
	public void resize(int width, int height)
	{
		/*float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		float aspect = w / h;

		// https://github.com/libgdx/libgdx/wiki/Viewports

		cam.setToOrtho(false, project1.V_WIDTH, project1.V_HEIGHT / aspect);
		cam.position.set(0, 0, 0);
		cam.update();*/
		
		float ar = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
		cam.setToOrtho(false, project1.V_WIDTH, project1.V_WIDTH / ar);
		cam.position.set(0, 0, 0);
		cam.update();

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
	public void hide()
	{
		// TODO Auto-generated method stub
		dispose();
		

	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub
		texture.dispose();

	}

}
