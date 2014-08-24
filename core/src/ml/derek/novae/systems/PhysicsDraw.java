package ml.derek.novae.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import ml.derek.novae.components.*;

/**
 * Created by Derek Sewell on 24/08/14.
 * Copyright © 2014 Derek Sewell.
 * All rights reserved.
 */
public class PhysicsDraw extends EntitySystem
{
	private ImmutableArray<Entity> entities;
	private ComponentMapper<PhysicsComponent> pm = ComponentMapper.getFor(PhysicsComponent.class);
	private ComponentMapper<GravityComponent> gm = ComponentMapper.getFor(GravityComponent.class);
	private ComponentMapper<RenderComponent> rm = ComponentMapper.getFor(RenderComponent.class);

	SpriteBatch batch;
	Box2DDebugRenderer debugRenderer;
	OrthographicCamera camera;

	public PhysicsDraw()
	{
		float scale = 8f;

		camera = new OrthographicCamera(64 * scale, 48 * scale);
		batch = new SpriteBatch();

	}

	@Override
	public void addedToEngine(Engine engine)
	{
		entities = engine.getEntitiesFor(Family.getFor(RenderComponent.class, PhysicsComponent.class));
		Gdx.app.log("init", "RenderComponent + PhysicsComp added to system. Number of entities: " + entities.size());

	}

	@Override
	public void update(float deltaTime)
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (int i = 0; i < entities.size(); ++i)
		{
			Entity entity = entities.get(i);
			PhysicsComponent physics = pm.get(entity);
			RenderComponent render = rm.get(entity);
			float x = physics.body.getPosition().x;
			float y = physics.body.getPosition().y;

			float width = physics.width;
			float height = physics.height;

			batch.draw(render.region, x - width/2f, y - width/2f, width/2, height/2, width, height, 1, 1, physics.body.getAngle());

			//if(false)
			if(Family.getFor(CameraFocus.class).matches(entity))
			{
				camera.position.x = physics.body.getPosition().x;
				camera.position.y = physics.body.getPosition().y;

				camera.rotate(-getCameraCurrentXYAngle(camera));
				camera.rotate(physics.body.getAngle());
				//camera.rotate(physics.body.getAngle());
			}

		}
		batch.end();
		//debugRenderer.render(world, camera.combined); //TODO: Better rendering solution
		camera.update();



	}

	private float getCameraCurrentXYAngle(Camera cam)
	{
		return (float)Math.atan2(cam.up.x, cam.up.y)* MathUtils.radiansToDegrees;
	}



}
