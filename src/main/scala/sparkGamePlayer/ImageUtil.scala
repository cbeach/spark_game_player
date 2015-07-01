package sparkGamePlayer

import org.opencv.core.Mat

case class BoundingBox(x: Int, y: Int, height: Int, width: Int)
case class SpriteOccurances(name: String, boundingBoxes: List[BoundingBox])
case class ImageSpriteSet(frame: GameFrame, spriteSet: List[SpriteOccurances])
case class GameFrame(gameName: String, number: Int, data: Mat)
case class RawGameFrame(nameOfGame: String, frameNumber: Int, frameData: Array[Byte], height: Int, widtgh: Int, imageType: Int)

object ImUtil {
    def pallette(image: Mat): Set[(Int, Int, Int)] = {
        Set((1, 1, 1))
    }
    
    
}