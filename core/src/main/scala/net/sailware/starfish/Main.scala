package net.sailware.starfish

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils

class Main extends ApplicationAdapter:

  var batch: SpriteBatch = null
  var image: Texture = null

  override def create(): Unit =
    batch = new SpriteBatch()
    image = new Texture("libgdx.png")

  override def render(): Unit =
    ScreenUtils.clear(0.15F, 0.15F, 0.2F, 1F)
    batch.begin()
    batch.draw(image, 140, 210)
    batch.end()

  override def dispose(): Unit =
    batch.dispose()
    image.dispose()
