package ml.derek.novae.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Derek Sewell on 23/08/14.
 * Copyright © 2014 Derek Sewell.
 * All rights reserved.
 */
public class GravitySensor extends Component
{
	public final float radius;

	public GravitySensor(float radius)
	{
		this.radius = radius;
	}
}
