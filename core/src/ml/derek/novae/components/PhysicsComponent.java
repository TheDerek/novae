package ml.derek.novae.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by Derek Sewell on 23/08/14.
 * Copyright © 2014 Derek Sewell.
 * All rights reserved.
 */
public class PhysicsComponent extends Component
{
	public FixtureDef fixtureDef;
	public Body body;
	public BodyDef bodyDef;
	public Fixture baseFixture;

	public boolean sensor = false;

	public float radius;
	public Vector2 pos;
	public float density = 1.2f;
	public boolean rotate = false;
	public BodyDef.BodyType bodyType;
	
	public Type type;

	public boolean canJump = true;

	public boolean lockedIn = false; //TODO: REFACTOR THIS MONSTROSITY

	public float width;
	public float height;

	public PhysicsComponent(Vector2 pos, float radius, BodyDef.BodyType bodyType)
	{
		this.pos = pos;
		this.radius = radius;
		this.bodyType = bodyType;
		this.type = Type.CIRCLE;
		width = radius * 2f;
		height = radius * 2f;

	}

	public PhysicsComponent(Vector2 pos, float radius, float density, BodyDef.BodyType bodyType)
	{
		this(pos, radius, bodyType);
		this.density = density;
	}

	public PhysicsComponent(Vector2 pos, float radius, float density, boolean rotate, BodyDef.BodyType bodyType)
	{
		this(pos, radius, bodyType);
		this.density = density;
		this.rotate = rotate;
	}

	public PhysicsComponent(Vector2 pos, float radius, float density, boolean rotate, boolean sensor, BodyDef.BodyType bodyType)
	{
		this(pos, radius, bodyType);
		this.density = density;
		this.rotate = rotate;
		this.sensor = sensor;
	}



	public enum Type
	{
		RECT,
		CIRCLE
	}
}


