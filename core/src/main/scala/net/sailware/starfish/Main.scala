package net.sailware.starfish

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ScreenUtils

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

    if(turtle.boundry.getBoundingRectangle.overlaps(starfish.rectangle))
      starfish.remove()
      stage.addActor(new WinOverlay())

    ScreenUtils.clear(0, 0, 0, 1F)

    stage.draw()

  override def dispose(): Unit =
    stage.dispose()

class Turtle extends Actor:

  this.setPosition(20F, 20F)
  val textures = for (i <- 1 to 6) yield
      val texture = new Texture(s"turtle-${i}.png")
      texture.setFilter(TextureFilter.Linear, TextureFilter.Linear)
      new TextureRegion(texture)

  val animation = new Animation[TextureRegion](0.1F, new Array(textures.toArray))
  animation.setPlayMode(PlayMode.LOOP)

  setSize(animation.getKeyFrame(0).getRegionWidth().toFloat, animation.getKeyFrame(0).getRegionHeight().toFloat)

  val vertices = scala.Array( 0F, 0F, getWidth(), 0F, getWidth(), getHeight(), 0, getHeight())

  val boundry: Polygon = new Polygon(vertices)

  var elapsedTime = 0F

  val acceleration = 400F
  val deceleration = 400F
  val maxSpeed = 100F
  val velocity = Vector2(0, 0)
  val accelerationVector = Vector2(0, 0)

  override def act(delta: Float): Unit =
    super.act(delta)

    if Gdx.input.isKeyPressed(Keys.LEFT) then accelerateAtAngle(180)
    if Gdx.input.isKeyPressed(Keys.RIGHT) then accelerateAtAngle(0)
    if Gdx.input.isKeyPressed(Keys.UP) then accelerateAtAngle(90)
    if Gdx.input.isKeyPressed(Keys.DOWN) then accelerateAtAngle(270)

    applyPhysics(delta)

    if velocity.len() > 0 then setRotation(velocity.angleDeg())

    if velocity.len() > 0 then elapsedTime += delta

    this.boundry.setPosition(getX(), getY())

  override def draw(batch: Batch, parentAlpha: Float): Unit =
    super.draw(batch, parentAlpha)

    batch.draw(animation.getKeyFrame(elapsedTime), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation())

  def applyPhysics(delta: Float): Unit =
    velocity.add(accelerationVector.x * delta, accelerationVector.y * delta)

    val speed = MathUtils.clamp(
        if accelerationVector.len() == 0 then velocity.len() - deceleration * delta else velocity.len(),
        0,
        maxSpeed
    )
    if velocity.len() == 0 then
      velocity.set(speed, 0)
    else
      velocity.setLength(speed)

    moveBy(velocity.x * delta, velocity.y * delta)

    accelerationVector.set(0, 0)

  private def accelerateAtAngle(angle: Float): Unit =
    accelerationVector.add(new Vector2(acceleration, 0).setAngleDeg(angle))

class Starfish extends Actor:

  this.setPosition(380F, 380F)
  val texture = new Texture("starfish.png")
  val textureRegion = new TextureRegion(texture)
  val rectangle = new Rectangle(getX(), getY(), texture.getWidth().toFloat, texture.getHeight().toFloat)

  setSize(texture.getWidth().toFloat, texture.getHeight().toFloat)

  override def act(delta: Float): Unit =
    super.act(delta)
    rectangle.setPosition(getX(), getY())

  override def draw(batch: Batch, parentAlpha: Float): Unit =
    super.draw(batch, parentAlpha)

    batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation())

class Background extends Actor:

  this.setPosition(0F, 0F)
  val texture = new Texture("water.jpg")
  val textureRegion = new TextureRegion(texture)

  setSize(texture.getWidth().toFloat, texture.getHeight().toFloat)

  override def draw(batch: Batch, parentAlpha: Float): Unit =
    super.draw(batch, parentAlpha)

    batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation())

class WinOverlay extends Actor:

  this.setPosition(180F, 180F)
  val texture = new Texture("you-win.png")
  val textureRegion = new TextureRegion(texture)

  setSize(texture.getWidth().toFloat, texture.getHeight().toFloat)

  override def draw(batch: Batch, parentAlpha: Float): Unit =
    super.draw(batch, parentAlpha)

    batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation())
