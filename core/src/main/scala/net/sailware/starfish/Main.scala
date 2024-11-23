package net.sailware.starfish

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.Input.Keys

class Main extends ApplicationAdapter:

  var batch: SpriteBatch = null

  var turtleTexture: Texture = null
  var turtleRectangle: Rectangle = null
  var turtleX = 0F
  var turtleY = 0F

  var starfishTexture: Texture = null
  var starfishRectangle: Rectangle = null
  var starfishX = 0F
  var starfishY = 0F

  var oceanTexture: Texture = null

  var winMessageTexture: Texture = null

  var win = false

  override def create(): Unit =
    batch = new SpriteBatch()
    turtleTexture = new Texture("turtle-1.png")
    turtleX = 20F
    turtleY = 20F
    turtleRectangle = new Rectangle(turtleX, turtleY, turtleTexture.getWidth().toFloat, turtleTexture.getHeight().toFloat)

    starfishTexture = new Texture("starfish.png")
    starfishX = 380F
    starfishY = 380F
    starfishRectangle = new Rectangle(starfishX, starfishY, starfishTexture.getWidth().toFloat, starfishTexture.getHeight().toFloat)

    oceanTexture = new Texture("water.jpg")

    winMessageTexture = new Texture("you-win.png")

  override def render(): Unit =
    if (Gdx.input.isKeyPressed(Keys.LEFT))
        turtleX -= 1
    if (Gdx.input.isKeyPressed(Keys.RIGHT))
        turtleX += 1
    if (Gdx.input.isKeyPressed(Keys.UP))
        turtleY += 1
    if (Gdx.input.isKeyPressed(Keys.DOWN))
      turtleY -= 1

    turtleRectangle.setPosition(turtleX, turtleY)

    if (turtleRectangle.overlaps(starfishRectangle))
      win = true

    ScreenUtils.clear(0, 0, 0, 1F)
    batch.begin()
    batch.draw(oceanTexture, 0, 0)
    if (!win)
      batch.draw(starfishTexture, starfishX, starfishY)
    else
      batch.draw(winMessageTexture, 180, 180)
    batch.draw(turtleTexture, turtleX, turtleY)
    batch.end()

  override def dispose(): Unit =
    batch.dispose()
    turtleTexture.dispose()
    oceanTexture.dispose()
