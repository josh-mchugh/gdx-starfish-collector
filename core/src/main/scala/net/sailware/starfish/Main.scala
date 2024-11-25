package net.sailware.starfish

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.Input.Keys

class Main extends ApplicationAdapter:

  var stage: Stage = null
  var background: Background = null
  var turtle: Turtle = null
  var starfish: Starfish = null

  override def create(): Unit =
    background = new Background()
    turtle = new Turtle()
    starfish = new Starfish()

    stage = new Stage()

    stage.addActor(background)
    stage.addActor(starfish)
    stage.addActor(turtle)

  override def render(): Unit =
    val delta = Gdx.graphics.getDeltaTime()

    stage.act(delta)

    if(turtle.rectangle.overlaps(starfish.rectangle))
      starfish.remove()
      stage.addActor(new WinOverlay())

    ScreenUtils.clear(0, 0, 0, 1F)

    stage.draw()

  override def dispose(): Unit =
    stage.dispose()

class Turtle extends Actor:

  this.setX(20F)
  this.setY(20F)
  val texture = new Texture("turtle-1.png")
  val textureRegion = new TextureRegion(texture)
  val rectangle = new Rectangle(getX(), getY(), texture.getWidth().toFloat, texture.getHeight().toFloat)

  setSize(texture.getWidth().toFloat, texture.getHeight().toFloat)

  override def act(delta: Float): Unit =
    super.act(delta)
    if (Gdx.input.isKeyPressed(Keys.LEFT))
      this.moveBy(-1, 0)
    if (Gdx.input.isKeyPressed(Keys.RIGHT))
      this.moveBy(1, 0)
    if (Gdx.input.isKeyPressed(Keys.UP))
      this.moveBy(0, 1)
    if (Gdx.input.isKeyPressed(Keys.DOWN))
      this.moveBy(0, -1)

    rectangle.setPosition(getX(), getY())

  override def draw(batch: Batch, parentAlpha: Float): Unit =
    super.draw(batch, parentAlpha)

    batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation())

class Starfish extends Actor:

  this.setX(380F)
  this.setY(380F)
  val texture = new Texture("starfish.png")
  val textureRegion = new TextureRegion(texture)
  val rectangle = new Rectangle(getX(), getY(), texture.getWidth().toFloat, texture.getHeight().toFloat)

  setSize(texture.getWidth().toFloat, texture.getHeight().toFloat)

  override def act(delta: Float): Unit =
    rectangle.setPosition(getX(), getY())

  override def draw(batch: Batch, parentAlpha: Float): Unit =
    super.draw(batch, parentAlpha)

    batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation())

class Background extends Actor:

  this.setX(0F)
  this.setY(0F)
  val texture = new Texture("water.jpg")
  val textureRegion = new TextureRegion(texture)

  setSize(texture.getWidth().toFloat, texture.getHeight().toFloat)

  override def draw(batch: Batch, parentAlpha: Float): Unit =
    super.draw(batch, parentAlpha)

    batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation())

class WinOverlay extends Actor:

  this.setX(180F)
  this.setY(180F)
  val texture = new Texture("you-win.png")
  val textureRegion = new TextureRegion(texture)

  setSize(texture.getWidth().toFloat, texture.getHeight().toFloat)

  override def draw(batch: Batch, parentAlpha: Float): Unit =
    super.draw(batch, parentAlpha)

    batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation())
