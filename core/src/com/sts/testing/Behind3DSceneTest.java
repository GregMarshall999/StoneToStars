package com.sts.testing;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.model.NodePart;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.g3d.utils.TextureProvider;
import com.badlogic.gdx.utils.JsonReader;

public class Behind3DSceneTest implements ApplicationListener {
    public PerspectiveCamera cam;
    public CameraInputController camController;
    public Shader shader;
    public RenderContext renderContext;
    public Model model;
    public Environment environment;
    public Renderable renderable;

    @Override
    public void create () {
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(2f, 2f, 2f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        ModelLoader<ModelLoader.ModelParameters> modelLoader = new G3dModelLoader(new JsonReader());
        ModelData modelData = modelLoader.loadModelData(Gdx.files.internal("testing/behind3dscene/invaderscene.g3dj"));
        model = new Model(modelData, new TextureProvider.FileTextureProvider());

        NodePart blockPart = model.getNode("ship").parts.get(0);

        renderable = new Renderable();
        renderable.meshPart.set(blockPart.meshPart);
        renderable.material = blockPart.material;
        renderable.environment = environment;
        renderable.worldTransform.idt();

        renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.LRU, 1));
        shader = new DefaultShader(renderable);
        shader.init();
    }

    @Override
    public void render () {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camController.update();

        renderContext.begin();
        shader.begin(cam, renderContext);
        shader.render(renderable);
        shader.end();
        renderContext.end();
    }

    @Override
    public void dispose() {
        shader.dispose();
        model.dispose();
    }

    @Override public void resume () {}
    @Override public void resize (int width, int height) {}
    @Override public void pause () {}
}
