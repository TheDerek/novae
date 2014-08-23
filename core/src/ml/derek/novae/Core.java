package ml.derek.novae;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import ml.derek.novae.components.*;
import ml.derek.novae.systems.GravitySystem;
import ml.derek.novae.systems.InputSystem;
import ml.derek.novae.systems.PhysicsSystem;

public class Core extends ApplicationAdapter
{
	private Engine engine;
	private Entity player;
	
	@Override
	public void create ()
	{
		engine = new Engine();

		float sunSize = 20f;
		float minDistance = 5f;

		Entity sun = new Entity();
		{
			sun.add(new PhysicsComponent(new Vector2(), sunSize, 1000000f, true, BodyDef.BodyType.DynamicBody));
			sun.add(new GravityComponent(null, false, true));
			engine.addEntity(sun);
		}

		float distance = sunSize + minDistance + MathUtils.random(minDistance + 4);



		for(float i = 5; i > 0; i--)
		{
			float size = MathUtils.random(10f, 15f);
			distance += minDistance + (size * 2f) + MathUtils.random(minDistance + 7);

			Entity planet = new Entity();
			planet.add(new PhysicsComponent(new Vector2(distance, 0), size, 1000000f, false, BodyDef.BodyType.DynamicBody));
			planet.add(new GravityComponent(sun, 1));
			engine.addEntity(planet);

			if(i == 1)
			{
				Entity player = new Entity();
				player.add(new PhysicsComponent(new Vector2(distance + 5, 0), 5f, 0.1f, true, BodyDef.BodyType.DynamicBody));
				player.add(new GravitySensor(20f));
				player.add(new GravityComponent(planet, false, true));
				player.add(new CameraFocus(1));
				player.add(new InputComponent(5000f, 100000000000f));
				engine.addEntity(player);
			}



		}

		/*//Test planet
		{
			Entity planet = new Entity();
			planet.add(new PhysicsComponent(new Vector2(10f, 10f), 2f * scale, true));
			planet.add(new GravityComponent(sun, 1));
			engine.addEntity(planet);
		}

		//Test planet
		{
			Entity planet = new Entity();
			planet.add(new PhysicsComponent(new Vector2(10f, 10f), 2f * scale, true));
			planet.add(new GravityComponent(sun, 1));
			engine.addEntity(planet);
		}

		//Test moon
		{
			Entity moon = new Entity();
			moon.add(new PhysicsComponent(new Vector2(14f, 14f), 1f * scale, true));
			moon.add(new GravityComponent(sun, 1));
			engine.addEntity(moon);
		}*/





		{
			player = new Entity();

		}

		engine.addSystem(new InputSystem());
		engine.addSystem(new PhysicsSystem());
		engine.addSystem(new GravitySystem());


	}

	@Override
	public void render ()
	{
		engine.update(Gdx.app.getGraphics().getDeltaTime());
	}
}
