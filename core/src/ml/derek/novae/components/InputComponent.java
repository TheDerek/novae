package ml.derek.novae.components;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Derek Sewell on 23/08/14.
 * Copyright © 2014 Derek Sewell.
 * All rights reserved.
 */
public class InputComponent extends Component
{
	public float speed;
	public float jump;
	public InputComponent(float speed, float jump)
	{
		this.speed = speed;
		this.jump = jump;
	}
}
