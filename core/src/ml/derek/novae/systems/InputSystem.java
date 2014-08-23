package ml.derek.novae.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import ml.derek.novae.components.GravityComponent;
import ml.derek.novae.components.InputComponent;
import ml.derek.novae.components.PhysicsComponent;

/**
 * Created by Derek Sewell on 23/08/14.
 * Copyright © 2014 Derek Sewell.
 * All rights reserved.
 */
public class InputSystem extends IteratingSystem
{
	private ComponentMapper<PhysicsComponent> pm = ComponentMapper.getFor(PhysicsComponent.class);
	private ComponentMapper<InputComponent> im = ComponentMapper.getFor(InputComponent.class);
	private ComponentMapper<GravityComponent> gm = ComponentMapper.getFor(GravityComponent.class);
	public boolean canJump = true;

	float speedMod = 1;

	public InputSystem()
	{
		super(Family.getFor(InputComponent.class, PhysicsComponent.class, GravityComponent.class));
	}

	@Override
	public void processEntity(Entity entity, float deltaTime)
	{
		PhysicsComponent physics = pm.get(entity);
		GravityComponent gravity = gm.get(entity);
		InputComponent input = im.get(entity);

		physics.body.setAngularDamping(100f);

		Body body = physics.body;


		if(Gdx.input.isKeyPressed(Input.Keys.D))
		{
			body.applyAngularImpulse(input.speed, true);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.A))
		{
			body.applyAngularImpulse(-input.speed, true);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.W) && gravity.parent != null)
		{
			if(physics.canJump)
			{
				Gdx.app.log("ingame", "Jumped!");
				Body parentBody = pm.get(gravity.parent).body;
				Vector2 direction = body.getPosition().sub(parentBody.getPosition()).nor();

				//body.setLinearVelocity(0, 0);
				body.applyForceToCenter(direction.scl(input.jump), true);
				physics.canJump = false;

				PhysicsComponent parentPhysics = pm.get(gravity.parent);
				if(parentPhysics.lockedIn)
				{
					Gdx.app.log("ingame", "Parent escaping");
					GravityComponent parentGravity = gm.get(gravity.parent);

					parentBody.getFixtureList().get(0).setSensor(true);
					parentGravity.escaping = true;
				}
			}
		}

		if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && gravity.parent != null)
		{
			PhysicsComponent parentPhysics = pm.get(gravity.parent);

			if(physics.canJump && !parentPhysics.lockedIn)
			{
				Gdx.app.log("ingame", "Locked In");
				physics.lockedIn = true;
				physics.canJump = false;
			}

		}
	}


}
