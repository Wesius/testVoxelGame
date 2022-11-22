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
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.Vector3;
import com.sun.org.apache.xpath.internal.operations.Mod;

import com.wesg.game.Cube;

import java.util.Random;

public class DesktopLauncher implements ApplicationListener {
    public CameraInputController camController;
    public Environment environment;
    public PerspectiveCamera cam;
    public ModelBatch modelBatch;


    public Model testModel;
    public ModelInstance testModelInstance;


    public static void main(String[] args) {
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

        cam = new PerspectiveCamera(80, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        Cube.genCubes();



        ModelBuilder modelBuilder = new ModelBuilder();



        modelBuilder.begin();
        modelBuilder.node();
        MeshPartBuilder mpb = modelBuilder.part(
                "box",
                GL20.GL_TRIANGLES,
                Usage.Position | Usage.Normal
                , new Material(ColorAttribute.createDiffuse(Color.GREEN))
        );

        for (int x = 0; x < Cube.cubes.length; x++) {
            for (int y = 0; y < Cube.cubes[x].length; y++) {
                for (int z = 0; z < Cube.cubes[x][y].length; z++) {
                    if (Cube.cubes[x][y][z].type == 0) {
                        BoxShapeBuilder.build(mpb, 1, 1, 1);
                    }
                }
            }
        }

        testModel = modelBuilder.end();
        testModelInstance = new ModelInstance(testModel);

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

        modelBatch.render(testModelInstance, environment);

        modelBatch.end();

        camController.update();
    }

    @Override
    public void dispose() {
        modelBatch.dispose();
        // Dispose of all individual models
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
