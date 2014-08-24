package ml.derek.novae;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import ml.derek.novae.components.*;
import ml.derek.novae.systems.GravitySystem;
import ml.derek.novae.systems.InputSystem;
import ml.derek.novae.systems.PhysicsDraw;
import ml.derek.novae.systems.PhysicsSystem;

public class Core extends ApplicationAdapter
{
	private Engine engine;
	private Entity player;
	
	@Override
	public void create ()
	{
		engine = new Engine();

		float sunSize = 30f;
		float minDistance = 12f;

		//Load textures (Planets, etc)
		AssetManager manager = new AssetManager();
		{
			FileHandle planetDict = Gdx.files.internal("planets");
			Gdx.app.log("init", "There are " + planetDict.list().length + " in the planet directory");

			for(FileHandle file : planetDict.list())
			{
				Gdx.app.log("init", "Found " + file.path());
				manager.load(file.path(), Texture.class);
			}
			//while(!manager.update()); //Load all the thing!
			manager.finishLoading();

		}

		Entity sun = new Entity();
		{
			sun.add(new PhysicsComponent(new Vector2(), sunSize, 1000000f, true, true, BodyDef.BodyType.DynamicBody));
			sun.add(new GravityComponent(null, true, false));
			engine.addEntity(sun);
		}

		float distance = sunSize*2 + minDistance + MathUtils.random(minDistance);

		for(float i = 20; i > 0; i--)
		{

			Texture texture = manager.get("planets/worldgen0" + (MathUtils.random(8) + 1) + ".gif");
			TextureRegion region = new TextureRegion(texture);


			float radius = MathUtils.random(10f, 30f);
			float angle = MathUtils.random(MathUtils.PI * 2f);
			distance += minDistance + (radius * 2f) + MathUtils.random(minDistance + 7);
			float x = MathUtils.cos(angle)*distance;
			float y = MathUtils.sin(angle)*distance;

			Entity planet = new Entity();
			planet.add(new PhysicsComponent(new Vector2(x, y), radius, 1000000f, false, BodyDef.BodyType.DynamicBody));
			planet.add(new GravityComponent(sun, 1));
			planet.add(new RenderComponent(region));
			engine.addEntity(planet);

			if(i == 10)
			{
				Entity player = new Entity();
				player.add(new PhysicsComponent(new Vector2(x, y), 5f, 0.1f, true, BodyDef.BodyType.DynamicBody));
				player.add(new GravitySensor(30f));
				player.add(new GravityComponent(planet, false, true).addDefParent(sun));
				player.add(new CameraFocus(1));
				player.add(new InputComponent(5000f, 100000000000f));
				player.add(new RenderComponent(region));
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
		engine.addSystem(new PhysicsDraw());


	}

	@Override
	public void render ()
	{
		engine.update(Gdx.app.getGraphics().getDeltaTime());
	}
}
