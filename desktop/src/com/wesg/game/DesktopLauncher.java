package com.wesg.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import java.util.LinkedList;

import com.badlogic.gdx.math.Vector3;
import com.sun.org.apache.xpath.internal.operations.Mod;

import java.util.Random;

public class DesktopLauncher implements ApplicationListener {
	public CameraInputController camController;
	public Environment environment;
	public PerspectiveCamera cam;
	public ModelBatch modelBatch;

	public LinkedList<Model> grassModels;
	public LinkedList<Model> dirtModels;
	public LinkedList<ModelInstance> grassModelInstances;
	public LinkedList<ModelInstance> dirtModelInstances;




	public static void main(String[] args){
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("LibGDX Test");
		config.setWindowedMode(1600, 960);
		config.useVsync(true);
		config.setForegroundFPS(60);
		new Lwjgl3Application(new DesktopLauncher(), config);
	}

	@Override
	public void create() {
		Random rand = new Random();

		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(10f, 10f, 10f);
		cam.lookAt(0,0,0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);

		ModelBuilder modelBuilder = new ModelBuilder();

		grassModels = new LinkedList<Model>();
		dirtModels = new LinkedList<Model>();
		grassModelInstances = new LinkedList<ModelInstance>();
		dirtModelInstances = new LinkedList<ModelInstance>();

		for (int i = 0; i < 1000; i++){
			grassModels.add(modelBuilder.createBox(5f, 5f, 5f,
					new Material(ColorAttribute.createDiffuse(Color.GREEN)),
					Usage.Position | Usage.Normal));
		}
		for (int i = 0; i < 1000; i++){
			dirtModels.add(modelBuilder.createBox(5f, 5f, 5f,
					new Material(ColorAttribute.createDiffuse(Color.BROWN)),
					Usage.Position | Usage.Normal));
		}

		for (Model m : grassModels) {
			grassModelInstances.add(new ModelInstance(m));
		}

		int teX;
		int teZ = -1;
		for (int x = 0; x < grassModelInstances.size(); x++) {
			ModelInstance m = grassModelInstances.get(x);
			teX = (int)(x % Math.sqrt(grassModelInstances.size()));

			if (teX >= Math.sqrt(grassModelInstances.size()) - 1) {
				teZ++;
			}
			m.transform.setToTranslation(new Vector3(teX * 5, (int)(ImprovedNoise.noise(3.14, 42,rand.nextInt(14))*10), teZ * 5));


			System.out.print(teX);
			System.out.print(" ");
			System.out.println(teZ);
		}
	}

	@Override
	public void render() {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);



		// TODO
		// Go through linked list in cube class and render each cube in it
		// Each cube needs to be an instance to render it - it must be a ModelInstance
		// To be a ModelInstance first we need to make a Model and a ModelBuilder


		modelBatch.begin(cam);

		for (ModelInstance m : grassModelInstances){
			modelBatch.render(m, environment);
		}

		modelBatch.end();

		camController.update();
	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		for (Model m : grassModels) {
			m.dispose();
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
