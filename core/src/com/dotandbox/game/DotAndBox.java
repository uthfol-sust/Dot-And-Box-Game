package com.dotandbox.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class DotAndBox extends Game {
	public SpriteBatch batch;
	public BitmapFont font;

	@Override
	public void create() {
		batch =new SpriteBatch();
		font=new BitmapFont();
		this.setScreen(new FirstScreen(this));

	}
	public void  render()
	{
		super.render();
	}
	public void dispose()
	{
		batch.dispose();
		font.dispose();

	}

}
