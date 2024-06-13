package com.dotandbox.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {

    DotAndBox game;
    OrthographicCamera camera;
    public GameScreen( DotAndBox game) {
        this.game=game;



    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(1,1,1,1);
        camera=new OrthographicCamera();
        camera.setToOrtho(false,800,480);
        




    }
    @Override
    public void show() {

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
