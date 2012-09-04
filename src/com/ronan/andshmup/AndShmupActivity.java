package com.ronan.andshmup;

import java.io.IOException;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.AutoParallaxBackground;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.widget.EditText;

public class AndShmupActivity extends BaseGameActivity {

	 //Constants
	static final int CAMERA_WIDTH = 1280;
	static final int CAMERA_HEIGHT = 800;
	
	private static final int FONT_SIZE = 16;
	
	public static enum GameState {MainMenu, Loading, Running, BetweenLevels, Leaderboards, GameOver};
	public static GameState gameState = GameState.MainMenu;
	
	private Camera mCamera;
	
	//Objects
	private MainMenu mMainMenu;
	private EndOfLevelStats mEndOfLevelStats;
	private LeaderboardScreen mLeaderboardScreen;
	private GameOverScreen mGameOverScreen;
	private LeaderboardDB leaderboard = new LeaderboardDB(this);
	
	//Textures
	private BitmapTextureAtlas mTexture;
	private TextureRegion mTextureRegion;
	
	private BitmapTextureAtlas mMineTexture;
	private TextureRegion mMineTextureRegion;
	
	private BitmapTextureAtlas mLaserTexture;
	private TextureRegion mLaserTextureRegion;
	
	private BitmapTextureAtlas mAsteroidTexture;
	private TextureRegion mAsteroidTextureRegion;
	
	private BitmapTextureAtlas mExplosionTexture;
	private TiledTextureRegion mExplosionTextureRegion;
	
	private BitmapTextureAtlas mThrusterTexture;
	private TextureRegion mThrusterTextureRegion;
	
	private BitmapTextureAtlas mLifeTexture;
	private TextureRegion mLifeTextureRgion;
	
	//Sounds
	private Music mMainMenuMusic;
	private Music mLevel1Music;
	private Music mLevel2Music;
	
	private Sound mExplosionSound;
	private Sound mLaserSound;
	
	private Level mLevel;
	
	//Fonts
	private Font m8BitFont;
	private Font m8BitFontMenu;
	private Font m8BitFontStats;
	
	private BitmapTextureAtlas m8BitFontStatsTexture;
	private BitmapTextureAtlas m8BitFontMenuTexture;
	private BitmapTextureAtlas m8BitFontTexture;
	
	private BitmapTextureAtlas mAutoParallaxBackgroundTexture;
    private TextureRegion mParallaxLayerBack;
    private TextureRegion mParallaxLayerMid;
    private TextureRegion mParallaxLayerFront;
	
	private final int mLevel1NumEnemies = 15;
	private final int mLevel2NumEnemies = 25;
	
	private int mCurrentLevel = 1;
	private final int mMaxLevels = 2;
	
	private int score = 0;
	private int lives = 3;
	private String name;
    
	

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
		return new Engine(new EngineOptions(
				true,
				ScreenOrientation.LANDSCAPE,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
				this.mCamera
				).setNeedsMusic(true).setNeedsSound(true));
	}

	@Override
	public void onLoadResources() {
		
		//Player Ship
		this.mTexture = new BitmapTextureAtlas(128, 64, TextureOptions.BILINEAR);
		this.mTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mTexture, this,
																					"gfx/playerShip64x128.png", 0, 0);
		//Laser
		this.mLaserTexture = new BitmapTextureAtlas(32, 16, TextureOptions.BILINEAR);
		this.mLaserTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mLaserTexture, this,
																							"gfx/projectileBlue16x32.png", 0, 0);
		//Mine
		this.mMineTexture = new BitmapTextureAtlas(64, 64, TextureOptions.BILINEAR);
		this.mMineTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mMineTexture, this,
																							"gfx/mine64x64.png", 0, 0);
		//Asteroid
		this.mAsteroidTexture = new BitmapTextureAtlas(64, 64, TextureOptions.BILINEAR);
		this.mAsteroidTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAsteroidTexture, this, 
																								"gfx/asteroid64x64.png", 0, 0);
		//Explosion
		this.mExplosionTexture = new BitmapTextureAtlas(1024, 64, TextureOptions.BILINEAR);
		this.mExplosionTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mExplosionTexture, this,
																									"gfx/explosion1024x64.png", 0, 0, 17, 1);
		//Fireball
		this.mThrusterTexture = new BitmapTextureAtlas(32, 32, TextureOptions.BILINEAR);
		this.mThrusterTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mThrusterTexture, this, 
																								"gfx/fireball32x32.png", 0, 0);
		
		//Heart
		this.mLifeTexture = new BitmapTextureAtlas(16, 16, TextureOptions.BILINEAR);
		this.mLifeTextureRgion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mLifeTexture, this,
																							"gfx/heart.png", 0, 0);
		
		//Parallax Background
		this.mAutoParallaxBackgroundTexture = new BitmapTextureAtlas(1024, 1024, TextureOptions.DEFAULT);
        this.mParallaxLayerFront = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, this, "gfx/space3.png", 0, 0);
        this.mParallaxLayerBack = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, this, "gfx/space1.png", 0, 0);
        this.mParallaxLayerMid = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, this, "gfx/space2.png", 0, 0);

		//Fonts
		this.m8BitFontTexture = new BitmapTextureAtlas(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m8BitFont = FontFactory.createFromAsset(m8BitFontTexture, this, "fonts/8BitFont.TTF", FONT_SIZE, true, Color.WHITE);
		
		this.m8BitFontMenuTexture = new BitmapTextureAtlas(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m8BitFontMenu = FontFactory.createFromAsset(m8BitFontMenuTexture, this, "fonts/8BitFont.TTF", 48, true, Color.WHITE);
		
		this.m8BitFontStatsTexture = new BitmapTextureAtlas(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m8BitFontStats = FontFactory.createFromAsset(m8BitFontStatsTexture, this, "fonts/8BitFont.TTF", 36, true, Color.WHITE);
		
		//Music
		try {
			this.mMainMenuMusic = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "ost/menu.ogg");
			this.mMainMenuMusic.setLooping(true);
			
			this.mLevel1Music = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "ost/Level1Music.mp3");
			this.mLevel1Music.setLooping(true);
			
			this.mLevel2Music = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "ost/Level2Music.mp3");
			this.mLevel2Music.setLooping(true);
		} 
		catch (final IOException e) {
			Debug.e(e);
		}
		
		//Sound Effects
		try {
			this.mExplosionSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sfx/explosion.wav");
			this.mLaserSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "sfx/laser.wav");
		} 
		catch (final IOException e) {
			Debug.e(e);
		}
		
		this.mEngine.getTextureManager().loadTextures(
				this.mTexture,
				this.mMineTexture,
				this.mLaserTexture,
				this.mAsteroidTexture,
				this.mExplosionTexture,
				this.mThrusterTexture,
				this.m8BitFontTexture,
				this.m8BitFontMenuTexture,
				this.m8BitFontStatsTexture,
				this.mLifeTexture,
				this.mAutoParallaxBackgroundTexture);
		
		this.mEngine.getFontManager().loadFonts(this.m8BitFont, this.m8BitFontMenu, this.m8BitFontStats);
	}

	@Override
	public Scene onLoadScene() {
		
		final Scene scene = new Scene();
		//scene.setBackground(new ColorBackground(0.0f, 0.0f, 0.0f));
		
		final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
        autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, new Sprite(0, 0, this.mParallaxLayerBack)));
        autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-5.0f, new Sprite(0, 0, this.mParallaxLayerMid)));
        autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-10.0f, new Sprite(0, 0, this.mParallaxLayerFront)));
        scene.setBackground(autoParallaxBackground);
		showAlertDialog();
		return scene;
	}

	@Override
	public void onLoadComplete() {
				
		this.mEngine.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void onUpdate(float pSecondsElapsed)
			{	
				manageGame();
			}

			@Override
			public void reset() {}
			
		});//end IUpdateHandler
        
	}
	
	public void manageGame()
	{
		switch (gameState)
		{
			case MainMenu:
				if (mMainMenu == null)
				{
					mMainMenu = new MainMenu(m8BitFontMenu, mEngine.getScene());
					mCurrentLevel = 1;
					mMainMenuMusic.play();
					
					if (mEndOfLevelStats != null)
					{
						mEndOfLevelStats.delete();
						mEndOfLevelStats = null;
					}
				}
				else
				{
					if (mMainMenu.hidden)
					{
						mMainMenu.show();
					}
					if (mMainMenu.startGame)
					{
						// Start Game
						mMainMenu.delete();
						mMainMenu = null;
						score = 0;
						lives = 3;
						mMainMenuMusic.pause();
						gameState = GameState.Loading;
					}
					else if (mMainMenu.showLeaderboard)
					{
						mMainMenu.hide();
						mMainMenu.showLeaderboard = false;
						gameState = GameState.Leaderboards;
					}
				}
				break;
				
			case Loading:
				loadLevel();
				gameState = GameState.Running;
				break;
				
			case Running:
				mLevel.update();
				if (mLevel.levelOver)
				{
					gameState = GameState.BetweenLevels;
					mCurrentLevel++;
				}
				else if (mLevel.gameOver)
				{
					gameState = GameState.GameOver;
				}
				break;
				
			case BetweenLevels:
				if (mEndOfLevelStats == null)
				{
					mEndOfLevelStats = new EndOfLevelStats(mLevel.mPlayer.score, mLevel.mPlayer.lives, mLevel.mPlayer.lasersFired,
														   mLevel.mPlayer.lasersHit, mLevel.mPlayer.highestCombo,
														   mLevel.mPlayer.enemiesDestroyed, 
														   m8BitFontStats, mEngine.getScene());
					
					lives = mLevel.mPlayer.lives;
					score = mLevel.mPlayer.score;
					
					mLevel.delete();
					mLevel = null;
				}
				else if (mEndOfLevelStats.m_continue)
				{
					if (mCurrentLevel <= mMaxLevels)
					{
						gameState = GameState.Loading;
						mEndOfLevelStats.delete();
						mEndOfLevelStats = null;
						
						if (mLevel1Music.isPlaying())
						{
							mLevel1Music.pause();
						}
					}
					else
					{
						//showAlertDialog();
						if (mLevel2Music.isPlaying())
						{
							mLevel2Music.pause();
						}
						leaderboard.open();
						leaderboard.insertEnty(name, score);
						leaderboard.close();
						gameState = GameState.Leaderboards;
					}
				}
				System.gc();
				break;
				
			case Leaderboards:
				if (mLeaderboardScreen == null)
				{
					if (mEndOfLevelStats != null)
					{
						mEndOfLevelStats.delete();
						mEndOfLevelStats = null;
					}
					
					leaderboard.open();
					mLeaderboardScreen = new LeaderboardScreen(leaderboard.getAllEntries(), this.mEngine.getScene(), m8BitFontStats);
					leaderboard.close();
				}
				else
				{
					if (mLeaderboardScreen.gotoMainMenu)
					{
						mLeaderboardScreen.delete();
						mLeaderboardScreen = null;
						
						gameState = GameState.MainMenu;
					}
				}
				break;
				
			case GameOver:
				if (mGameOverScreen == null)
				{
					mLevel.delete();
					mLevel = null;
					
					mGameOverScreen = new GameOverScreen(m8BitFontMenu, this.mEngine.getScene());
				}
				else
				{
					if (mGameOverScreen.gotoMainMenu)
					{
						mGameOverScreen.delete();
						mGameOverScreen = null;
						
						gameState = GameState.MainMenu;
					}
				}
				break;
		
		}// end switch
	}// end manageGame
	
	public void loadLevel()
	{
		switch (mCurrentLevel)
		{
			case 1:
				mLevel = new Level(mLevel1NumEnemies, this.mEngine.getScene(), mEngine);
				mLevel1Music.play();
				break;
			case 2:
				mLevel = new Level(mLevel2NumEnemies, this.mEngine.getScene(), mEngine);
				mLevel2Music.play();
				break;
		}
		
		mLevel.loadPlayer(mTextureRegion, mThrusterTextureRegion, mLaserTextureRegion, mLifeTextureRgion, mLaserSound, score, lives);
		mLevel.loadEnemies(mAsteroidTextureRegion, mMineTextureRegion, mExplosionTextureRegion, mExplosionSound);
		mLevel.loadHUD(m8BitFont, mCurrentLevel, score);
	}
	
	public void showAlertDialog()
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("AndShmup");
		alert.setMessage("Enter your name");
		
		final EditText input = new EditText(this);
		alert.setView(input);
		
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				name = input.getText().toString();
			}
		});
		
//		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
		alert.show();
	}
}