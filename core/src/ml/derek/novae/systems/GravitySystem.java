package ml.derek.novae.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import ml.derek.novae.components.GravityComponent;
import ml.derek.novae.components.PhysicsComponent;

/**
 * Created by Derek Sewell on 23/08/14.
 * Copyright © 2014 Derek Sewell.
 * All rights reserved.
 */
public class GravitySystem extends EntitySystem
{
	private ImmutableArray<Entity> entities;
	private ImmutableArray<Entity> smallEntities;

	private ComponentMapper<PhysicsComponent> pm = ComponentMapper.getFor(PhysicsComponent.class);
	private ComponentMapper<GravityComponent> gm = ComponentMapper.getFor(GravityComponent.class);

	private final float gCon = 0.0007f;

	public GravitySystem()
	{

	}

	@Override
	public void addedToEngine(Engine engine)
	{
		entities = engine.getEntitiesFor(Family.getFor(PhysicsComponent.class, GravityComponent.class));

		for(int i = 0; i < entities.size(); i++)
		{
			Entity entity = entities.get(i);
			PhysicsComponent physics = pm.get(entity);
			GravityComponent gravity = gm.get(entity);
			Body body = physics.body;

			//Apply velocity to force object into orbit
			if(gravity.parent != null && gravity.orbit)
			{
				PhysicsComponent parentPhysics = pm.get(gravity.parent);
				Body parentBody = parentPhysics.body;

				Vector2 direction = parentBody.getPosition().sub(body.getPosition()).cpy();
				direction.set(direction.rotate90(1));
				direction.nor();

				float r = parentBody.getPosition().sub(body.getPosition()).len();

				float v = gCon * parentBody.getMass();
				v /= r;
				v = (float) Math.sqrt(v);

				Gdx.app.log("init", "Orbit velocity: " + direction);

				if(gravity.direction == -1)
					direction.rotate(180);


				body.setLinearVelocity(direction.scl(v));

			}
		}
	}

	private float distance(Entity e, Entity o)
	{
		PhysicsComponent ep = pm.get(e);
		PhysicsComponent eo = pm.get(o);

		return ep.body.getPosition().sub(eo.body.getPosition()).len();
	}

	@Override
	public void update(float deltaTime)
	{
		for (int i = 0; i < entities.size(); ++i)
		{
			Entity entity = entities.get(i);
			PhysicsComponent physics = pm.get(entity);
			GravityComponent gravity = gm.get(entity);
			Body body = physics.body;

			if(gravity.parent == null && !gravity.escaping && gravity.defaultParent != null)
				gravity.parent = gravity.defaultParent;

			//Head towards parent
			if(gravity.parent != null && !gravity.escaping)
			{
				Entity parent = gravity.parent;
				PhysicsComponent parentPhysics = pm.get(parent);
				GravityComponent parentGravity = gm.get(parent);
				Body parentBody = parentPhysics.body;

				Vector2 direction = parentPhysics.body.getPosition().sub(body.getPosition());
				float r = direction.len() * direction.len(); //Inverse size of direction for accurate gravity
				float M = body.getMass() *parentBody.getMass();

				body.applyForceToCenter(direction.nor().scl(1f / r).scl(gCon).scl(M), true);

				//Keep up with moving planet
				//TODO: Fix this
				{
					Vector2 a = body.getLinearVelocity();
					//body.applyForceToCenter(direction.nor().scl(a.len()), true);

				}
			}



		}

	}

	@Override
	public void removedFromEngine(Engine engine)
	{

	}


}
