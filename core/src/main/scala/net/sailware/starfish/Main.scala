package net.sailware.starfish

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.Screen
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
import scala.collection.mutable.ListBuffer

class Main extends Game:

  override def create(): Unit =
    setScreen(MenuScreen(this))

class MenuScreen(game: Game) extends Screen:

  val stage = Stage()
  stage.addActor(Background())
  stage.addActor(MenuImage("starfish-collector.png", (0, 100)))
  stage.addActor(MenuImage("message-start.png", (0, -100)))

  override def render(delta: Float): Unit =
    stage.act(delta)

    if Gdx.input.isKeyPressed(Keys.S) then
      game.setScreen(new LevelScreen())

    ScreenUtils.clear(0, 0, 0, 1F)

    stage.draw()

  override def dispose(): Unit =
    stage.dispose()

  override def hide(): Unit = {  }

  override def pause(): Unit =  {  }

  override def resize(x: Int, y: Int): Unit = {  }

  override def resume(): Unit = { }

  override def show(): Unit = {  }

class MenuImage(src: String, offset: (Float, Float)) extends Actor:
  val texture = new Texture(src)
  val textureRegion = new TextureRegion(texture)
  setSize(texture.getWidth().toFloat, texture.getHeight().toFloat)
  this.setPosition(400F - getWidth() / 2, 300F - getHeight() / 2)
  moveBy(offset._1, offset._2)

  override def draw(batch: Batch, parentAlpha: Float): Unit =
    super.draw(batch, parentAlpha)

    batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation())

class LevelScreen extends Screen:
  val stage = Stage()
  val turtle = Turtle()
  val starfishes = List(
      Starfish((400, 400)),
      Starfish((500, 100)),
      Starfish((100, 450)),
      Starfish((250, 250))
  )
  stage.addActor(Background())
  starfishes.foreach(starfish => stage.addActor(starfish))
  stage.addActor(turtle)

  override def render(delta: Float): Unit =
    val delta = Gdx.graphics.getDeltaTime()

    stage.act(delta)

    for starfish <- starfishes do
      if(turtle.boundry.getBoundingRectangle.overlaps(starfish.rectangle))
        starfish.remove()
        stage.addActor(WinOverlay())

    ScreenUtils.clear(0, 0, 0, 1F)

    stage.draw()

  override def dispose(): Unit =
    stage.dispose()

  override def hide(): Unit = {  }

  override def pause(): Unit =  {  }

  override def resize(x: Int, y: Int): Unit = {  }

  override def resume(): Unit = { }

  override def show(): Unit = {  }

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

    // set rotation when velocity is moving
    if velocity.len() > 0 then setRotation(velocity.angleDeg())

    // display move animation
    if velocity.len() > 0 then elapsedTime += delta

    this.boundry.setPosition(getX(), getY())

  override def draw(batch: Batch, parentAlpha: Float): Unit =
    super.draw(batch, parentAlpha)

    batch.draw(animation.getKeyFrame(elapsedTime), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation())

  private def applyPhysics(delta: Float): Unit =
    velocity.add(accelerationVector.x * delta, accelerationVector.y * delta)

    val speed = computeSpeed(delta)
    if velocity.len() == 0 then
      velocity.set(speed, 0)
    else
      velocity.setLength(speed)

    moveBy(velocity.x * delta, velocity.y * delta)

    accelerationVector.set(0, 0)

  private def accelerateAtAngle(angle: Float): Unit =
    accelerationVector.add(new Vector2(acceleration, 0).setAngleDeg(angle))

  private def computeSpeed(delta: Float): Float =
    val speed = if accelerationVector.len() == 0 then velocity.len() - deceleration * delta else velocity.len()
    MathUtils.clamp(speed, 0, maxSpeed)

class Starfish(position: (Float, Float)) extends Actor:

  this.setName("Starfish")
  this.setPosition(position._1, position._2)
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
