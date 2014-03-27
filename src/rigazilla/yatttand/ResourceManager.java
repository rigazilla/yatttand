package rigazilla.yatttand;

import java.util.EnumMap;
import java.util.concurrent.FutureTask;

import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.debug.Debug;

import android.provider.ContactsContract.PhoneLookup;
import rigazilla.yatttand.GameManager.Player;

public class ResourceManager {
	private static ResourceManager theInstance;
	public ITextureRegion mCrossTextureRegion;
	public ITextureRegion mCircleTextureRegion;
	public ITextureRegion mHBarTextureRegion;
	public ITextureRegion mVBarTextureRegion;
	public ITextureRegion mThinkingTextureRegion;
	public ITextureRegion mReplayTextureRegion;
	public TextureRegion mInvisibleButtonTextureRegion;
	public TiledTextureRegion mMenuButton01TextureRegion;
	public TiledTextureRegion mMenuButton02TextureRegion;
	public TiledTextureRegion mMenuButton03TextureRegion;
	public Entity menuLayer;
	public Entity boardLayer;
	public static final int[][] positions = { { 180, 20 }, { 340, 20 },
			{ 500, 20 }, { 180, 180 }, { 340, 180 }, { 500, 180 },
			{ 180, 340 }, { 340, 340 }, { 500, 340 } };
	public static final float[][] winnerLine = { { 250, 80, 570, 80 },
			{ 250, 240, 570, 240 }, { 250, 400, 570, 400 },
			{ 250, 80, 250, 400 }, { 410, 80, 410, 400 },
			{ 570, 80, 570, 400 }, { 250, 80, 570, 400 }, { 578, 80, 250, 400 } };
	private ButtonSprite buttonSprite01;
	private ButtonSprite buttonSprite02;
	private ButtonSprite buttonSprite03;
	// Declare a Scene object for our activity
	Scene gameScene;
	Scene menuScene;
	public Line wLine;
	public EnumMap<Player, Sprite[]> spriteBox = new EnumMap<GameManager.Player, Sprite[]>(
			GameManager.Player.class);
	public EnumMap<Player, ITextureRegion> textureBox = new EnumMap<GameManager.Player, ITextureRegion>(
			GameManager.Player.class);
	private Sprite thinking;
	public ButtonSprite replay;
	public FutureTask<Integer> aiWork;
	public MainActivity mainActivity;

	protected ResourceManager() {
		boardLayer = new Entity();
		menuLayer = new Entity();

		spriteBox.put(Player.FREE, new ButtonSprite[9]);
		spriteBox.put(Player.CIRCLE, new ButtonSprite[9]);
		spriteBox.put(Player.CROSS, new ButtonSprite[9]);
	}

	public static ResourceManager getInstance(MainActivity mainActivity) {
		if (theInstance == null) {
			theInstance = new ResourceManager();
		}
		theInstance.mainActivity = mainActivity;
		return theInstance;
	}

	void loadTexture(MainActivity mainActivity) {
		// TODO Auto-generated method stub
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		// Create the texture atlas at a size of 120x120 pixels
		BuildableBitmapTextureAtlas mBoardTextureAtlas = new BuildableBitmapTextureAtlas(
				mainActivity.getEngine().getTextureManager(), 1024, 512);
		mCrossTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBoardTextureAtlas, mainActivity, "cross.png");
		mCircleTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBoardTextureAtlas, mainActivity, "circle.png");
		mHBarTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBoardTextureAtlas, mainActivity, "hbar.png");
		mVBarTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBoardTextureAtlas, mainActivity, "vbar.png");
		mInvisibleButtonTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBoardTextureAtlas, mainActivity, "button.png");
		mThinkingTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBoardTextureAtlas, mainActivity,
						"thinking.png");
		mReplayTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBoardTextureAtlas, mainActivity,
						"rigiochiamo.png");
		// Buildable bitmap texture atlases require a try/catch statement
		try {
			/*
			 * Build the mBuildableBitmapTextureAtlas, supplying a
			 * BlackPawnTextureAtlasBuilder as its only parameter. Within the
			 * BlackPawnTextureAtlasBuilder's parameters, we provide 1 pixel in
			 * texture atlas source space and 1 pixel for texture atlas source
			 * padding. This will alleviate the chance of texture bleeding.
			 */
			mBoardTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 1));
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}
		mBoardTextureAtlas.load();

		BuildableBitmapTextureAtlas mMenuTextureAtlas = new BuildableBitmapTextureAtlas(
				mainActivity.getEngine().getTextureManager(), 1204, 512);
		mMenuButton01TextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(mMenuTextureAtlas, mainActivity,
						"button01.png", 2, 1);
		mMenuButton02TextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(mMenuTextureAtlas, mainActivity,
						"button02.png", 2, 1);
		mMenuButton03TextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(mMenuTextureAtlas, mainActivity,
						"button03.png", 2, 1);
		try {
			mMenuTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 1, 4));
			mMenuTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
		textureBox.put(Player.FREE, mInvisibleButtonTextureRegion);
		textureBox.put(Player.CIRCLE, mCircleTextureRegion);
		textureBox.put(Player.CROSS, mCrossTextureRegion);

	}

	void buildBoard(MainActivity mainActivity) {
		if (boardLayer.getChildCount() == 0) {
			Debug.e("rebuild all");
			gameScene.attachChild(boardLayer);
			Sprite hBar11 = new Sprite(170, 310, mHBarTextureRegion,
					mainActivity.getEngine().getVertexBufferObjectManager());
			Sprite hBar12 = new Sprite(330, 310, mHBarTextureRegion,
					mainActivity.getEngine().getVertexBufferObjectManager());
			Sprite hBar13 = new Sprite(490, 310, mHBarTextureRegion,
					mainActivity.getEngine().getVertexBufferObjectManager());
			Sprite hBar21 = new Sprite(170, 150, mHBarTextureRegion,
					mainActivity.getEngine().getVertexBufferObjectManager());
			Sprite hBar22 = new Sprite(330, 150, mHBarTextureRegion,
					mainActivity.getEngine().getVertexBufferObjectManager());
			Sprite hBar23 = new Sprite(490, 150, mHBarTextureRegion,
					mainActivity.getEngine().getVertexBufferObjectManager());

			Sprite vBar11 = new Sprite(312, 328, mVBarTextureRegion,
					mainActivity.getEngine().getVertexBufferObjectManager());
			Sprite vBar12 = new Sprite(312, 168, mVBarTextureRegion,
					mainActivity.getEngine().getVertexBufferObjectManager());
			Sprite vBar13 = new Sprite(312, 8, mVBarTextureRegion, mainActivity
					.getEngine().getVertexBufferObjectManager());
			Sprite vBar21 = new Sprite(472, 328, mVBarTextureRegion,
					mainActivity.getEngine().getVertexBufferObjectManager());
			Sprite vBar22 = new Sprite(472, 168, mVBarTextureRegion,
					mainActivity.getEngine().getVertexBufferObjectManager());
			Sprite vBar23 = new Sprite(472, 8, mVBarTextureRegion, mainActivity
					.getEngine().getVertexBufferObjectManager());
			thinking = new Sprite(280, 60, mThinkingTextureRegion, mainActivity
					.getEngine().getVertexBufferObjectManager());
			replay = new ButtonSprite(280, 60, mReplayTextureRegion,
					mainActivity.getEngine().getVertexBufferObjectManager(),
					mainActivity);

			replay.setTag(32);
			replay.setZIndex(50);

			thinking.setZIndex(50);

			// The last step is to attach our Sprite to the Scene, as is
			// necessary in order to display any type of Entity on the device's
			// display:
			/* Attach the marble to the Scene */

			boardLayer.attachChild(hBar11);
			boardLayer.attachChild(hBar12);
			boardLayer.attachChild(hBar13);
			boardLayer.attachChild(hBar21);
			boardLayer.attachChild(hBar22);
			boardLayer.attachChild(hBar23);
			boardLayer.attachChild(vBar11);
			boardLayer.attachChild(vBar12);
			boardLayer.attachChild(vBar13);
			boardLayer.attachChild(vBar21);
			boardLayer.attachChild(vBar22);
			boardLayer.attachChild(vBar23);
			boardLayer.attachChild(thinking);
			boardLayer.attachChild(replay);
			setThinkingVisible(false);
			replay.setVisible(false);
			for (int i = 0; i < 9; i++) {
				placeButtonOnBoard(mainActivity, i);
				activateButtonOnBoard(mainActivity, i);
			}
		} else // scene exists and needs a cleanup
		{
			// remove cross and circle
			setThinkingVisible(false);
			replay.setVisible(false);
			if (wLine != null) {
				mainActivity.runOnUpdateThread(new Runnable() {

					@Override
					public void run() {
						wLine.detachSelf();
					}
				});
			}
			for (int i = 0; i < GameManager.BOARDSIZE; i++) {
				final int j = i;
				if (GameManager.board[j] != Player.FREE) {
					mainActivity.runOnUpdateThread(new Runnable() {

						@Override
						public void run() {
							boardLayer.detachChild(spriteBox
									.get(GameManager.board[j])[j]);
							GameManager.board[j] = Player.FREE;
							activateButtonOnBoard(
									ResourceManager.theInstance.mainActivity, j);
						}
					});
				} 
//				else {
//					placeButtonOnBoard(
//							ResourceManager.theInstance.mainActivity, j);
//					activateButtonOnBoard(
//							ResourceManager.theInstance.mainActivity, j);
//				}
			}
		}
	}

	public void setThinkingVisible(boolean f) {
		thinking.setVisible(f);
		boardLayer.sortChildren(true);
	}

	private void placeButtonOnBoard(MainActivity mainActivity, int i) {
		ButtonSprite buttonSprite = (ButtonSprite) spriteBox.get(Player.FREE)[i];
		buttonSprite.setOnClickListener(mainActivity);
		buttonSprite.setTag(i);
		GameManager.board[i] = Player.FREE;
		boardLayer.attachChild(buttonSprite);
	}

	private void activateButtonOnBoard(MainActivity mainActivity, int i) {
		ButtonSprite buttonSprite = (ButtonSprite) spriteBox.get(Player.FREE)[i];
		gameScene.registerTouchArea(buttonSprite);
	}

	private void deactivateButtonOnBoard(MainActivity mainActivity, int i) {
		ButtonSprite buttonSprite = (ButtonSprite) spriteBox.get(Player.FREE)[i];
		gameScene.unregisterTouchArea(buttonSprite);
	}

	public void activateAllFreeButtons() {
		for (int i = 0; i < GameManager.BOARDSIZE; i++) {
			if (GameManager.board[i] == Player.FREE) {
				activateButtonOnBoard(this.mainActivity, i);
			}
		}
	}

	public void deactivateAllFreeButtons() {
		for (int i = 0; i < GameManager.BOARDSIZE; i++) {
			if (GameManager.board[i] == Player.FREE) {
				deactivateButtonOnBoard(this.mainActivity, i);
			}
		}
	}

	void buildMenu(MainActivity mainActivity) {
		menuScene.attachChild(menuLayer);
		buttonSprite01 = new ButtonSprite(MainActivity.WIDTH * 0.5f - 128,
				MainActivity.HEIGHT * 0.5f - 132, mMenuButton01TextureRegion,
				mainActivity.getEngine().getVertexBufferObjectManager(),
				mainActivity);
		buttonSprite01.setTag(16);
		menuLayer.attachChild(buttonSprite01);

		buttonSprite02 = new ButtonSprite(MainActivity.WIDTH * 0.5f - 128,
				MainActivity.HEIGHT * 0.5f - 32, mMenuButton02TextureRegion,
				mainActivity.getEngine().getVertexBufferObjectManager(),
				mainActivity);
		buttonSprite02.setTag(17);
		menuLayer.attachChild(buttonSprite02);

		buttonSprite03 = new ButtonSprite(MainActivity.WIDTH * 0.5f - 128,
				MainActivity.HEIGHT * 0.5f + 68, mMenuButton03TextureRegion,
				mainActivity.getEngine().getVertexBufferObjectManager(),
				mainActivity);
		buttonSprite03.setTag(18);
		menuLayer.attachChild(buttonSprite03);
	}

	public void activateTouchMenu() {
		menuScene.registerTouchArea(buttonSprite01);
		menuScene.registerTouchArea(buttonSprite02);
		menuScene.registerTouchArea(buttonSprite03);
	}

	public void deactivateTouchMenu() {
		menuScene.unregisterTouchArea(buttonSprite01);
		menuScene.unregisterTouchArea(buttonSprite02);
		menuScene.unregisterTouchArea(buttonSprite03);
	}

	public void place(int pos, Player p) {
		this.deactivateButtonOnBoard(mainActivity, pos);
		boardLayer.attachChild(spriteBox.get(p)[pos]);
		GameManager.board[pos] = p;
	}

	public void loadSprites(MainActivity mainActivity) {
		for (Player p : Player.values()) {
			for (int i = 0; i < 9; i++) {
				spriteBox.get(p)[i] = new ButtonSprite(
						ResourceManager.positions[i][0],
						ResourceManager.positions[i][1], textureBox.get(p),
						mainActivity.getEngine().getVertexBufferObjectManager());
			}
		}

	}

	public void cleanup() {
		detachChildren(menuLayer);
		detachChildren(boardLayer);
		detachChildren(menuScene);
		detachChildren(gameScene);
	}

	void detachChildren(final Entity e) {
		if (e == null)
			return;
		e.detachChildren();
	}

	void detachChildrenAndUnregTouch(final Scene e) {
		if (e == null)
			return;
		mainActivity.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				e.detachChildren();
				e.unregisterTouchAreas(new ITouchArea.ITouchAreaMatcher() {

					@Override
					public boolean matches(ITouchArea pObject) {
						// TODO Auto-generated method stub
						return true;
					}
				});
			}
		});
	}

	void placeSign(Integer userData) {
		place(userData, GameManager.getInstance().getCurrentTurn());
	}

	public void removeFreeButtons() {

		final Sprite[] spritesFree = spriteBox.get(Player.FREE);
		for (int i = 0; i < spritesFree.length; i++) {
			if (spritesFree[i].hasParent()) {
				spritesFree[i].detachSelf();
				gameScene.unregisterTouchArea(spritesFree[i]);
				GameManager.board[i] = null;
			}

		}

	}

	public void addReplayButton() {
		replay.setVisible(true);
		replay.setAlpha(0);
		gameScene.registerTouchArea(replay);
		boardLayer.sortChildren(true);
	}

	public void removeReplayButton() {
		if (replay.isVisible()) {
			replay.setVisible(false);
			gameScene.unregisterTouchArea(replay);
			boardLayer.sortChildren(true);
		}
	}

	public void showReplayButton() {
		replay.setAlpha(1);
		
	}

}
