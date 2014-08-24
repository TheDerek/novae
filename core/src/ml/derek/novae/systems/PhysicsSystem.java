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
import com.sun.javafx.font.directwrite.RECT;
import ml.derek.novae.components.CameraFocus;
import ml.derek.novae.components.GravityComponent;
import ml.derek.novae.components.GravitySensor;
import ml.derek.novae.components.PhysicsComponent;

/**
 * Created by Derek Sewell on 23/08/14.
 * Copyright © 2014 Derek Sewell.
 * All rights reserved.
 */
public class PhysicsSystem extends EntitySystem implements ContactListener
{
	private ImmutableArray<Entity> entities;
	private ComponentMapper<PhysicsComponent> pm = ComponentMapper.getFor(PhysicsComponent.class);
	private ComponentMapper<GravityComponent> gm = ComponentMapper.getFor(GravityComponent.class);
	private ComponentMapper<GravitySensor> gsm = ComponentMapper.getFor(GravitySensor.class);

	World world;
	Box2DDebugRenderer debugRenderer;
	OrthographicCamera camera;



	public PhysicsSystem()
	{
		world = new World(new Vector2(0, 0), true);
		world.setContactListener(this);
		debugRenderer = new Box2DDebugRenderer();

		float scale = 11f;

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
			create(world, entity);

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
			//if(false)
			if(Family.getFor(CameraFocus.class).matches(entity))
			{
				camera.position.x = physics.body.getPosition().x;
				camera.position.y = physics.body.getPosition().y;
			}

		}

		//camera.update();
		world.step(1 / 60f, 6, 2);

		//Gdx.gl.glClearColor(0, 0, 0, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//debugRenderer.render(world, camera.combined); //TODO: Better rendering solution
	}

	public Fixture createCircle(Vector2 localpos, float raduis, boolean sensor, Body body)
	{
		FixtureDef fixtureDef = new FixtureDef();
		Shape shape = new CircleShape();
		shape.setRadius(raduis);


		fixtureDef.shape = shape;
		fixtureDef.density = 0; //Earth density more or less
		fixtureDef.friction = 0.4f;
		fixtureDef.isSensor = sensor;
		//fixtureDef.restitution = 0.6f;

		return body.createFixture(fixtureDef);
	}

	@Override
	public void removedFromEngine(Engine engine)
	{

	}


	@Override
	public void beginContact(Contact contact)
	{
		if(contact.getFixtureA().getBody().getUserData() instanceof  Entity
				&& contact.getFixtureB().getBody().getUserData() instanceof  Entity)
		{
			Entity entites[] = new Entity[2];

			entites[0] = (Entity) contact.getFixtureA().getBody().getUserData();
			entites[1] = (Entity) contact.getFixtureB().getBody().getUserData();


			for(Entity e : entites)
				if (!Family.getFor(GravityComponent.class, PhysicsComponent.class).matches(e))
				{
					return;
				}

			GravityComponent gravity1 = gm.get(entites[0]);
			GravityComponent gravity2 = gm.get(entites[1]);

			PhysicsComponent physics1 = pm.get(entites[0]);
			PhysicsComponent physics2 = pm.get(entites[1]);

			if(!contact.getFixtureA().isSensor() && !contact.getFixtureB().isSensor())
			{
				physics1.canJump = true;
				physics2.canJump = true;
			}

			if(gravity1.dynamic && gravity2.orbit)
			{
				Gdx.app.log("ingame", "new parent");
				gravity1.parent = entites[1];
			}

			if(gravity2.dynamic && gravity1.orbit)
			{
				Gdx.app.log("ingame", "new parent");
				gravity2.parent = entites[0];
			}



		}

	}

	@Override
	public void endContact(Contact contact)
	{
		if(contact.getFixtureA().getBody().getUserData() instanceof  Entity
				&& contact.getFixtureB().getBody().getUserData() instanceof  Entity)
		{
			Entity entites[] = new Entity[2];

			entites[0] = (Entity) contact.getFixtureA().getBody().getUserData();
			entites[1] = (Entity) contact.getFixtureB().getBody().getUserData();


			for(Entity e : entites)
				if (!Family.getFor(GravityComponent.class, PhysicsComponent.class).matches(e))
				{
					return;
				}




			PhysicsComponent physics1 = pm.get(entites[0]);
			PhysicsComponent physics2 = pm.get(entites[1]);

			if(!contact.getFixtureA().isSensor() && !contact.getFixtureB().isSensor())
			{
				physics1.canJump = false;
				physics2.canJump = false;
			}




		}

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold)
	{

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse)
	{

	}

	public void create(World world, Entity entity)
	{
		PhysicsComponent p = pm.get(entity);

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = p.bodyType;
		bodyDef.fixedRotation = !p.rotate;
		bodyDef.position.set(p.pos);

		p.body = world.createBody(bodyDef);
		p.body.setUserData(entity);

		FixtureDef fixtureDef = new FixtureDef();
		Shape shape = null;

		switch (p.type)
		{
			case CIRCLE:
				shape = new CircleShape();
				shape.setRadius(p.radius);
				break;

			case RECT:

				break;

		}

		if(shape == null)
			throw new NullPointerException("No type of shape has been assigned");

		fixtureDef.shape = shape;
		fixtureDef.isSensor = p.sensor;
		fixtureDef.density = p.density; //Earth density more or less
		fixtureDef.friction = 0.4f;
		//fixtureDef.restitution = 0.6f;

		p.baseFixture = p.body.createFixture(fixtureDef);

		shape.dispose();
	}
}
