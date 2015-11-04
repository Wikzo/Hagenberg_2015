package net.gustavdahl.projectone;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.utils.BaseAnimationController.Transform;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;

import net.gustavdahl.projectone.EntityComponentSystem.Component;
import net.gustavdahl.projectone.EntityComponentSystem.Entity;
import net.gustavdahl.projectone.EntityComponentSystem.RenderSystem;
import net.gustavdahl.projectone.EntityComponentSystem.ServiceLocator;
import net.gustavdahl.projectone.EntityComponentSystem.SpriteComponent;
import net.gustavdahl.projectone.EntityComponentSystem.TextComponent;
import net.gustavdahl.projectone.EntityComponentSystem.TransFormComponent;
import static org.junit.Assert.*;

public class ComponentTester implements Screen
{

	public ComponentTester()
	{

		UnitTests();

	}

	Entity entity;

	// OLD
	@Test
	void UnitTests()
	{
		SpriteBatch sb = new SpriteBatch();
		assertNotNull(sb);

		ServiceLocator service = new ServiceLocator(null, null);
		assertNotNull(service);

		RenderSystem renderSystem = new RenderSystem(sb);
		assertNotNull(renderSystem);

		//ServiceLocator.RegisterSystem(renderSystem);

		entity = new Entity();
		assertNotNull(entity);

		TextComponent component1 = new TextComponent(sb, "Comp1");
		assertNotNull(component1);

		TextComponent component2 = new TextComponent(sb, "Comp2");
		assertNotNull(component2);

		entity.AddComponent(component1);
		entity.AddComponent(component2);

		// QUESTION: should the component/entity be responsible for adding it to
		// the system instead of manually doing it like this?
		//renderSystem.AddToRenderSystem(component1);
		//renderSystem.AddToRenderSystem(component2);

		Component c_temp1 = entity.GetComponent(TextComponent.class);
		assertNotNull(c_temp1);

		Component c_temp2 = entity.GetComponent(SpriteComponent.class);
		// assertNotNull(c_temp2);

		List<Component> tempList1 = entity.GetAllComponentsOfType(TextComponent.class);
		assertNotNull(tempList1);

		List<Component> tempListAll = entity.GetAllComponents();
		assertNotNull(tempListAll);
		// System.out.println("Size: " + tempListAll.size());

		assertEquals(3, entity.GetAllComponents().size());
		//entity.RemoveComponentOfType(TransFormComponent.class);
		//assertNull(entity.GetTransform()); // TODO: doesn't remove fast enough for garbage collector!
		//assertEquals(1, entity.GetAllComponents().size());

		entity.RemoveAllComponents();
		assertEquals(0, entity.GetAllComponents().size());
		
		entity.AddComponent(new TextComponent(sb));

		// entity.AddComponent(new TransFormComponent());

	}

	float i = 0f;

	@Override
	public void show()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta)
	{

		if (Gdx.input.isKeyJustPressed(Keys.SPACE) && entity.GetComponent(TextComponent.class) != null)
			((TextComponent) entity.GetComponent(TextComponent.class)).PrintText("JUMP");

	}

	@Override
	public void resize(int width, int height)
	{
		// TODO Auto-generated method stub

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

	}

	@Override
	public void dispose()
	{
		// TODO Auto-generated method stub

	}

}
