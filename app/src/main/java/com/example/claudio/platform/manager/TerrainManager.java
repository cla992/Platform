package com.example.claudio.platform.manager;

import com.example.claudio.platform.animations.PlayerAnimation;
import com.example.claudio.platform.entities.Entity;
import com.example.claudio.platform.renderEngine.Loader;
import com.example.claudio.platform.renderEngine.Manager;
import com.example.claudio.platform.terrains.Terrain;

import java.util.List;

/**
 * Created by Claudio on 31/05/2016.
 */
public class TerrainManager implements Manager {

    private Entity terrain;

    private boolean state = false;
    private int direction;

    public TerrainManager(Loader loader){terrain = new Terrain(loader);}

    @Override
    public void update() {
        if(state)
            move();

        state = false;
    }

    public void move(){
        switch (direction){
            case PlayerAnimation.DX:
                terrain.increasePosition(0.01f, 0f, 0f);
                break;
            case PlayerAnimation.SX:
                terrain.increasePosition(-0.01f, 0f, 0f);
                break;
        }
    }

    @Override
    public void addInEntityList(List<Entity> entities) {
        entities.add(terrain);
    }

    public void setState(int direction){
        this.state = true;
        this.direction = direction;
    }
}