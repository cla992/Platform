package com.example.claudio.platform.builder;

import android.content.res.XmlResourceParser;
import android.util.Log;

import com.example.claudio.platform.R;
import com.example.claudio.platform.animations.Animation;
import com.example.claudio.platform.animations.UiAnimation;
import com.example.claudio.platform.entities.Button;
import com.example.claudio.platform.main.MainActivity;
import com.example.claudio.platform.shaders.UiShader;
import com.example.claudio.platform.textures.Texture;
import com.example.claudio.platform.tile.Tile;
import com.example.claudio.platform.tile.Tileset;
import com.example.claudio.platform.toolBox.Util;
import com.example.claudio.platform.toolBox.Vector2f;
import com.example.claudio.platform.toolBox.Vector3f;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Claudio on 09/05/2017.
 */

public class ButtonBuilder extends EntityBuilder {

    private static final int BUTTONS_FILE = R.xml.buttons;
    private static final int BUTTONS_DEF  = R.xml.buttons_def;

    private XmlResourceParser buttonsDefParser;
    private XmlResourceParser buttonsFileParser;

    int buttonTipe;

    public ButtonBuilder(int buttonType){
        this.buttonTipe = buttonType;
    }

    @Override
    public void createEntity() {
        Log.i("point", "ButtonBuilder: creating entity");
        entity = new Button();
        entity.setType(buttonTipe);
        buttonsFileParser = MainActivity.context.getResources().getXml(BUTTONS_FILE);

        Log.i("point", "ButtonBuilder: entity created");
    }

    @Override
    public void createTile() {
        Log.i("point", "ButtonBuilder: creating tile");
        buttonsDefParser  = MainActivity.context.getResources().getXml(BUTTONS_DEF);
        int eventType   = -1;
        int tileWidth   =  0;
        int tileHeight  =  0;
        while(eventType != buttonsDefParser.END_DOCUMENT){
            if(eventType == buttonsDefParser.START_TAG){
                String node = buttonsDefParser.getName();
                if(node.equalsIgnoreCase("tileset")){
                    tileWidth   = Integer.parseInt(buttonsDefParser.getAttributeValue(null, "tileWidth"));
                    tileHeight  = Integer.parseInt(buttonsDefParser.getAttributeValue(null, "tileHeight"));
                }
            }
            try{
                eventType = buttonsDefParser.next();
            } catch (XmlPullParserException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Tile tile = new Tile(new Vector2f(tileWidth, tileHeight));
        Vector3f position = new Vector3f();
        switch (buttonTipe){
            case Util.BUTTON_LEFT:
                Log.i("builder", "button_left");
                position.x = 10; position.y = 900;
                break;
            case Util.BUTTON_RIGHT:
                Log.i("builder", "button_right");
                position.x = 300; position.y = 900;
                break;
            case Util.BUTTON_UP:
                Log.i("builder", "button_up");
                position.x = 1700; position.y = 900;
                break;
        }
        tile.setPosition(position);
        tile.setRotation(new Vector3f(0,0,1), 0);
        tile.setScale(new Vector3f(1,1,1));
        entity.setTile(tile);
        Log.i("point", "ButtonBuilder: tile created");
    }

    @Override
    public void createTileset() {
        Log.i("point", "ButtonBuilder: creating tileset");
        buttonsDefParser  = MainActivity.context.getResources().getXml(BUTTONS_DEF);
        int eventType = -1;
        int textureWidth = 0, textureHeight = 0, tilesNumber = 0, columns = 0,
                rows = 0, tileWidth = 0, tileHeight = 0;
        String tilesetName = "";
        while(eventType != buttonsDefParser.END_DOCUMENT){
            if(eventType == buttonsDefParser.START_TAG){
                String node = buttonsDefParser.getName();
                if(node.equalsIgnoreCase("tileset")){
                    textureWidth    = Integer.parseInt(buttonsDefParser.getAttributeValue(null, "width"));
                    textureHeight   = Integer.parseInt(buttonsDefParser.getAttributeValue(null, "height"));
                    tilesNumber     = Integer.parseInt(buttonsDefParser.getAttributeValue(null, "tilesNumber"));
                    tileWidth       = Integer.parseInt(buttonsDefParser.getAttributeValue(null, "tileWidth"));
                    tileHeight      = Integer.parseInt(buttonsDefParser.getAttributeValue(null, "tileHeight"));
                    tilesetName     = buttonsDefParser.getAttributeValue(null, "name");
                    columns         = textureWidth/tileWidth;
                    rows            = textureHeight/tileHeight;
                }
            }
            try{
                eventType = buttonsDefParser.next();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Tileset tileset = new Tileset();
        tileset.setName(tilesetName);
        tileset.setNumberOfRows(rows);
        tileset.setNumberOfColumns(columns);
        tileset.setTexture(new Texture(R.drawable.buttons_img));
        List<Tileset> tilesets = new ArrayList<>();
        tilesets.add(tileset);
        entity.setTileset(tilesets);
        Log.i("point", "ButtonBuilder: tileset created");
    }

    @Override
    public void createAnimation() {
        Log.i("point", "ButtonBuilder: creating animation");
        buttonsDefParser  = MainActivity.context.getResources().getXml(BUTTONS_DEF);
        int eventType = -1;
        int tileId = 0;
        while(eventType != buttonsDefParser.END_DOCUMENT){
            if(eventType == buttonsDefParser.START_TAG){
                String node = buttonsDefParser.getName();
                if(node.equalsIgnoreCase("SubTexture")){
                    String name = buttonsDefParser.getAttributeValue(null, "name");
                    int type = -1;
                    switch (name){
                        case "button_left.png":
                            type = Util.BUTTON_LEFT;
                            break;
                        case "button_right.png":
                            type = Util.BUTTON_RIGHT;
                            break;
                        case "button_up.png":
                            type = Util.BUTTON_UP;
                            break;
                    }
                    if(type == buttonTipe){
                        tileId = Integer.parseInt(buttonsDefParser.getAttributeValue(null, "id"));
                    }
                }
            }
            try{
                eventType = buttonsDefParser.next();
            } catch (XmlPullParserException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Animation animation = new UiAnimation();
        animation.setId(tileId+1);
        entity.setAnimation(animation);
        Log.i("point", "ButtonBuilder: animation created");
    }

    @Override
    public void createShader() {
        Log.i("point", "ButtonBuilder: creating shader");
        UiShader shader = new UiShader();
        entity.setShader(shader);
        Log.i("point", "ButtonBuilder: shader created");
    }

    @Override
    public void bindBuffers() {
        Log.i("point", "ButtonBuilder: binding buffer");
        entity.bindBuffers();
        Log.i("point", "ButtonBuilder: buffer binded");
    }
}