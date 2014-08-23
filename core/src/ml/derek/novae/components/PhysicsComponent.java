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

	public float radius;
	private Vector2 pos;
	private boolean dynamic;
	private float density = 1.2f;
	private boolean rotate = false;
	
	private Type type;

	public PhysicsComponent(Vector2 pos, float radius, boolean dynamic)
	{
		this.pos = pos;
		this.radius = radius;
		this.dynamic = dynamic;
		this.type = Type.CIRCLE;

	}

	public PhysicsComponent(Vector2 pos, float radius, float density, boolean dynamic)
	{
		this(pos, radius, dynamic);
		this.density = density;
	}

	public PhysicsComponent(Vector2 pos, float radius, float density, boolean rotate, boolean dynamic)
	{
		this(pos, radius, dynamic);
		this.density = density;
		this.rotate = rotate;
	}

	public void create(World world)
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = dynamic ? BodyDef.BodyType.DynamicBody : BodyDef.BodyType.StaticBody;
		bodyDef.fixedRotation = rotate; //TODO: Look into this
		bodyDef.position.set(pos);

		body = world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();
		Shape shape = null;

		switch (type)
		{
			case CIRCLE:
				shape = new CircleShape();
				shape.setRadius(radius);
				break;

			case RECT:
				
				break;

		}

		if(shape == null)
			throw new NullPointerException("No type of shape has been assigned");

		fixtureDef.shape = shape;
		fixtureDef.density = density; //Earth density more or less
		fixtureDef.friction = 0.4f;
		//fixtureDef.restitution = 0.6f;

		baseFixture = body.createFixture(fixtureDef);

		shape.dispose();
	}

	private enum Type
	{
		RECT,
		CIRCLE
	}
}


