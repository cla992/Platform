package com.example.claudio.platform.main;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.example.claudio.platform.builder.ButtonBuilder;
import com.example.claudio.platform.builder.EntityCreationDirector;
import com.example.claudio.platform.builder.PlayerBuilder;
import com.example.claudio.platform.builder.TileMapBuilder;
import com.example.claudio.platform.camera.Camera;
import com.example.claudio.platform.communication.Data;
import com.example.claudio.platform.communication.Server;
import com.example.claudio.platform.entities.Button;
import com.example.claudio.platform.entities.Entity;
import com.example.claudio.platform.entities.Player;
import com.example.claudio.platform.manager.DisplayManager;
import com.example.claudio.platform.physicsEngine.Physics;
import com.example.claudio.platform.renderEngine.Renderable;
import com.example.claudio.platform.renderEngine.Renderer;
import com.example.claudio.platform.terrains.TileMap;
import com.example.claudio.platform.text.Font;
import com.example.claudio.platform.text.Text;
import com.example.claudio.platform.text.TextRenderer;
import com.example.claudio.platform.toolBox.Util;
import com.example.claudio.platform.toolBox.Vector2f;
import com.example.claudio.platform.toolBox.Vector3f;
import com.example.claudio.platform.toolBox.Vector4f;


import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Claudio on 28/05/2016.
 */

public class GLRenderer implements GLSurfaceView.Renderer {

    private Renderer renderer;
    private TextRenderer textRenderer;

    private Physics physics;

    private int width;
    private int height;

    private List<Renderable> renderables    = new ArrayList<>();
    private List<Entity> physical           = new ArrayList<>();
    private List<Button> buttons            = new ArrayList<>();
    private TileMap tileMap;
    private Player player;

    private float x,y;

    private Camera camera;

    private Server server;

    private Context context;

    private Font roboto;
    List<Text> texts = new ArrayList<>();
    Text fps;


    public GLRenderer(Context context, int width, int height){
        this.context = context;
        this.width  = width;
        this.height = height;
        physics = new Physics();



        Data.setDataDimension(16);
    }

    public List<Button> getButtons(){return buttons;}

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        server = new Server();
        new Thread(server).start();

        Log.i("point", "Creating tileMap");
        TileMapBuilder tileMapBuilder = new TileMapBuilder();
        tileMapBuilder.createEntity();
        tileMapBuilder.createTileset();
        tileMapBuilder.createTileLevels();
        tileMapBuilder.createShader();
        tileMapBuilder.bindBuffers();
        renderables.add(tileMapBuilder.getEntity());
        tileMap = (TileMap) tileMapBuilder.getEntity();
        Util.checkError();
        Log.i("point", "tileMap created");

        Log.i("point", "Creating player");
        EntityCreationDirector director = new EntityCreationDirector();
        director.setEntityBuilder(new PlayerBuilder());
        director.createEntity();
        player = (Player) director.getEntity();
        renderables.add(director.getEntity());
        physical.add(director.getEntity());
        Util.checkError();
        Log.i("point", "player Created");

        Log.i("point", "Creating buttons");
        director.setEntityBuilder(new ButtonBuilder(Util.BUTTON_LEFT));
        director.createEntity();
        renderables.add(director.getEntity());
        buttons.add((Button) director.getEntity());
        Log.i("point", "button 1 created");
        director.setEntityBuilder(new ButtonBuilder(Util.BUTTON_RIGHT));
        director.createEntity();
        renderables.add(director.getEntity());
        buttons.add((Button) director.getEntity());
        Log.i("point", "button 2 created");
        director.setEntityBuilder(new ButtonBuilder(Util.BUTTON_UP));
        director.createEntity();
        renderables.add(director.getEntity());
        buttons.add((Button) director.getEntity());
        Log.i("point", "button 3 created");
        Log.i("point", "buttons created");

        roboto = new Font(context.getAssets(), "Roboto-Regular.ttf", 100, new Vector2f(2,2));

        fps = new Text("", new Vector2f(50,50), new Vector4f(1,1,1,1), roboto);
        //Text text2 = new Text("Hello World", new Vector2f(800,600), new Vector4f(0,1,0,0.2f), roboto);

        texts.add(fps);
        //texts.add(text2);

        GLES20.glClearColor(1.0f, 0.5f, 0.5f, 1.0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        camera = new Camera(new Vector3f(0,0,0), new Vector3f(0,0,-1), new Vector3f(0,1,0));//position, look, up
        x=0;y=0;
        renderer = new Renderer(width, height);
        textRenderer = new TextRenderer();

        DisplayManager.start();
        Log.i("point", "GLRenderer: surfaceCreated");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        this.height = height;
        this.width  = width;
        renderer.setProjectionMatrix(width, height);
        textRenderer.setProjectionMatrix(width, height);
        Log.i("point", "GLRenderer: surfaceChanged");
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        DisplayManager.update();

        fps.setText(Integer.toString(DisplayManager.getFps()));

        Vector2f playerPosition = player.getPosition();
        camera.move(new Vector3f(playerPosition.x-width/2,playerPosition.y-height/2,0));

        for(Renderable renderable : renderables)
            renderable.handleInput();
        physics.update(physical, tileMap);
        renderer.render(renderables, camera);

        /*textRenderer.begin();
        textRenderer.render(texts, camera);
        textRenderer.end();
*/
        Data.setData(ByteBuffer.allocate(20).putFloat(playerPosition.x).putFloat(playerPosition.y)
                .putFloat(player.SPEED).putFloat(player.JUMP_SPEED).putInt(DisplayManager.getFps()).array());

    }
}
