package com.sts.testing;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class LoadSceneTest implements ApplicationListener {
    PerspectiveCamera camera;
    CameraInputController camController;
    ModelBatch modelBatch;
    AssetManager assets;
    Array<ModelInstance> instances = new Array<>();
    Environment environment;
    boolean loading;

    Array<ModelInstance> blocks = new Array<>();
    Array<ModelInstance> invaders = new Array<>();
    ModelInstance ship;
    ModelInstance space;

    @Override
    public void create() {
        modelBatch = new ModelBatch();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0f, 7f, 10f);
        camera.lookAt(0,0,0);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();

        assets = new AssetManager();
        assets.load("testing/loadscene/ship.obj", Model.class);
        assets.load("testing/loadscene/block.obj", Model.class);
        assets.load("testing/loadscene/invader.obj", Model.class);
        assets.load("testing/loadscene/spacesphere.obj", Model.class);
        loading = true;

        camController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(camController);
    }

    private void doneLoading() {
        ship = new ModelInstance(assets.get("testing/loadscene/ship.obj", Model.class));
        ship.transform.setToRotation(Vector3.Y, 180).trn(0, 0, 6f);
        instances.add(ship);

        Model blockModel = assets.get("testing/loadscene/block.obj", Model.class);
        for (float x = -5f; x <= 5f; x += 2f) {
            ModelInstance block = new ModelInstance(blockModel);
            block.transform.setToTranslation(x, 0, 3f);
            instances.add(block);
            blocks.add(block);
        }

        Model invaderModel = assets.get("testing/loadscene/invader.obj", Model.class);
        for (float x = -5f; x <= 5f; x += 2f) {
            for (float z = -8f; z <= 0f; z += 2f) {
                ModelInstance invader = new ModelInstance(invaderModel);
                invader.transform.setToTranslation(x, 0, z);
                instances.add(invader);
                invaders.add(invader);
            }
        }

        space = new ModelInstance(assets.get("testing/loadscene/spacesphere.obj", Model.class));

        loading = false;
    }

    @Override
    public void render() {
        if (loading && assets.update())
            doneLoading();
        camController.update();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        modelBatch.begin(camera);
        modelBatch.render(instances, environment);
        if (space != null)
            modelBatch.render(space);
        modelBatch.end();
    }

    @Override
    public void dispose () {
        modelBatch.dispose();
        instances.clear();
        assets.dispose();
    }

    @Override
    public void resume () {
    }

    @Override
    public void resize (int width, int height) {
    }

    @Override
    public void pause () {
    }
}
