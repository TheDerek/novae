package ml.derek.novae.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;

/**
 * Created by Derek Sewell on 23/08/14.
 * Copyright © 2014 Derek Sewell.
 * All rights reserved.
 */
public class GravityComponent extends Component
{
	public Entity parent;
	public boolean orbit;
	public int direction = 1;
	public boolean dynamic = false;

	public boolean escaping = false;

	public GravityComponent(Entity parent, boolean orbit)
	{
		this(parent, 1);
		this.orbit = orbit;
	}

	public GravityComponent(Entity parent, boolean orbit, boolean dynamic)
	{
		this(parent, 1);
		this.orbit = orbit;
		this.dynamic = dynamic;
	}

	public GravityComponent(boolean dynamic)
	{
		orbit = false;
		this.dynamic = dynamic;
	}

	public GravityComponent(Entity parent, int direction)
	{
		//TODO: FIX ERROR CHECKING
		//if(!Family.getFor(GravityComponent.class).matches(parent))
		//throw new Exception("Required Component not found: " + GravityComponent.class.toString());

		this.parent = parent;
		orbit = true;
		this.direction = direction;
	}
}
