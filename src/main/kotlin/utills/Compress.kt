package ru.topbun.utills

import java.awt.Graphics2D
import java.awt.image.BufferedImage

fun compressImage(image: BufferedImage, maxWidth: Int, maxHeight: Int): BufferedImage {
    val aspectRatio = image.width.toDouble() / image.height
    val newWidth = if (image.width > maxWidth) maxWidth else image.width
    val newHeight = (newWidth / aspectRatio).toInt()

    val resizedImage = BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB)
    val graphics: Graphics2D = resizedImage.createGraphics()
    graphics.drawImage(image, 0, 0, newWidth, newHeight, null)
    graphics.dispose()
    return resizedImage
}