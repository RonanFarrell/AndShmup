package com.ronan.andshmup;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.opengl.font.Font;

public class HUD
{
	public ChangeableText score;
	public ChangeableText lives;
	
	private ChangeableText level;
	
	private Scene mScene;
	
	/**
	 * @param pFont
	 * @param pScene
	 */
	public HUD(Font pFont, Scene pScene, int pLevel, int pScore)
	{
		mScene = pScene;
		
		score = new ChangeableText(160, 5, pFont, "Score: " + pScore, "Score: XXXXX".length());
		mScene.attachChild(score);
		
		lives = new ChangeableText(5, 5, pFont, "Lives: ");
		mScene.attachChild(lives);
		
		level = new ChangeableText(350, 5, pFont, "Level: " + pLevel);
		mScene.attachChild(level);
	}
	
	public void delete()
	{
		mScene.detachChild(lives);
		mScene.detachChild(score);
		mScene.detachChild(level);
	}
}