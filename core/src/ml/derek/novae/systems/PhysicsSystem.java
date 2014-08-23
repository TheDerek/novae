package ml.derek.novae.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ml.derek.novae.components.CameraFocus;
import ml.derek.novae.components.GravitySensor;
import ml.derek.novae.components.PhysicsComponent;

/**
 * Created by Derek Sewell on 23/08/14.
 * Copyright © 2014 Derek Sewell.
 * All rights reserved.
 */
public class PhysicsSystem extends EntitySystem
{
	private ImmutableArray<Entity> entities;
	private ComponentMapper<PhysicsComponent> pm = ComponentMapper.getFor(PhysicsComponent.class);
	private ComponentMapper<GravitySensor> gsm = ComponentMapper.getFor(GravitySensor.class);

	World world;
	Box2DDebugRenderer debugRenderer;
	OrthographicCamera camera;

	public PhysicsSystem()
	{
		world = new World(new Vector2(0, 0), true);
		debugRenderer = new Box2DDebugRenderer();

		float scale = 10f;

		camera = new OrthographicCamera(64 * scale, 48 * scale);


	}

	@Override
	public void addedToEngine(Engine engine)
	{
		entities = engine.getEntitiesFor(Family.getFor(PhysicsComponent.class));
		Gdx.app.log("init", "PhysicsComponent added to system. Number of entities: " + entities.size());


		for (int i = 0; i < entities.size(); ++i)
		{
			Entity entity = entities.get(i);
			PhysicsComponent physics = pm.get(entity);

			Gdx.app.log("init", "Creating body");
			physics.create(world);

			if(Family.getFor(GravitySensor.class).matches(entity))
			{
				GravitySensor s = gsm.get(entity);
				createCircle(new Vector2(), s.radius, true, physics.body);
			}


		}
	}

	@Override
	public void update(float deltaTime)
	{
		for (int i = 0; i < entities.size(); ++i)
		{
			Entity entity = entities.get(i);
			PhysicsComponent physics = pm.get(entity);
			//Gdx.app.log("loop", physics.body.getPosition().toString());
			if(false)
			if(Family.getFor(CameraFocus.class).matches(entity))
			{
				camera.position.x = physics.body.getPosition().x;
				camera.position.y = physics.body.getPosition().y;
			}

		}

		camera.update();
		world.step(1 / 60f, 6, 2);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		debugRenderer.render(world, camera.combined); //TODO: Better rendering solution
	}

	public Fixture createCircle(Vector2 localpos, float raduis, boolean sensor, Body body)
	{
		FixtureDef fixtureDef = new FixtureDef();
		Shape shape = new CircleShape();
		shape.setRadius(raduis);


		fixtureDef.shape = shape;
		fixtureDef.density = 1.2f; //Earth density more or less
		fixtureDef.friction = 0.4f;
		fixtureDef.isSensor = sensor;
		//fixtureDef.restitution = 0.6f;

		return body.createFixture(fixtureDef);
	}

	@Override
	public void removedFromEngine(Engine engine)
	{

	}


}
