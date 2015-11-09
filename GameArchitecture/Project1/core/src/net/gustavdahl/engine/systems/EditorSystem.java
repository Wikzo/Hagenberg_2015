package net.gustavdahl.engine.systems;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import net.gustavdahl.engine.MainGameLoopStuff;
import net.gustavdahl.engine.components.Collider;
import net.gustavdahl.engine.components.Component;
import net.gustavdahl.engine.components.EditorComponent;
import net.gustavdahl.engine.components.IRenderable;

public class EditorSystem extends BaseSystem
{

	private List<EditorComponent> _editorComponents;
	private OrthographicCamera _camera;
	private Ray _ray;
	private Vector3 _mousePosition;

	public EditorSystem(OrthographicCamera camera)
	{
		super();

		_editorComponents = new ArrayList<EditorComponent>();
		//_ray = new Ray();
		_camera = camera;
		_mousePosition = new Vector3();
	}

	@Override
	public void Update(float deltaTime)
	{
		
		_mousePosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		
		//Ray _ray = _camera.getPickRay(_mousePosition.x, _mousePosition.y);
		Ray _ray = MainGameLoopStuff._camera.getPickRay(_mousePosition.x, _mousePosition.y);
		
		//System.out.println(_ray.origin);
		
		
		
		for (int i = 0; i < _editorComponents.size(); i++)
		{
			Collider.Intersect(_ray, _editorComponents.get(i).GetCollider());
		}
	}

	@Override
	public boolean AddToSystem(Component c)
	{
		boolean succesfullyAdded = false;

		if (c instanceof EditorComponent)
		{
			succesfullyAdded = true;
			_editorComponents.add((EditorComponent) c);

		} else
			throw new RuntimeException("ERROR - " + c.Name() + " is not a EditorComponent!");

		return succesfullyAdded;

	}

	

}
