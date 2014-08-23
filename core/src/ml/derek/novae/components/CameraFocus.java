package ml.derek.novae.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Derek Sewell on 23/08/14.
 * Copyright © 2014 Derek Sewell.
 * All rights reserved.
 */
public class CameraFocus extends Component
{
	public final float scale;

	public CameraFocus(float scale)
	{
		this.scale = scale;
	}
}
