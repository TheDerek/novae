package ml.derek.novae.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Derek Sewell on 24/08/14.
 * Copyright © 2014 Derek Sewell.
 * All rights reserved.
 */
public class RenderComponent extends Component
{
	public TextureRegion region;

	public RenderComponent(TextureRegion region)
	{
		this.region = region;
	}

}
