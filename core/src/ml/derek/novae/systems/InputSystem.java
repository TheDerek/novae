package ml.derek.novae.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.physics.box2d.Body;
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
	private ImmutableArray<Entity> entities;

	@Override
	public void addedToEngine(Engine engine)
	{
		entities = engine.getEntitiesFor(Family.getFor(PhysicsComponent.class));
	}

	@Override
	public void processEntity(Entity entity, float deltaTime)
	{
		PhysicsComponent physics = pm.get(entity);
		InputComponent input = im.get(entity);

		Body body = physics.body;

		if(key)
	}


}
