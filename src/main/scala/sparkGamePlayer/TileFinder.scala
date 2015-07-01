package sparkGamePlayer

import org.opencv.core.Core
import org.opencv.core.Core.{rectangle => drawRectangle}
import org.opencv.core.Core.MinMaxLocResult
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.highgui.Highgui.{imread, IMREAD_GRAYSCALE}
import org.opencv.imgproc.Imgproc


class TileFinder {
    val runtime: Runtime = Runtime.getRuntime()
    val templates: Map[String, Mat] = Map(
        ("small_jump", imread("/home/casey/dev/emulators/sprites/mario/small_jump.png", IMREAD_GRAYSCALE)),
        ("small_run_1", imread("/home/casey/dev/emulators/sprites/mario/small_run_1.png", IMREAD_GRAYSCALE)),
        ("small_run_2", imread("/home/casey/dev/emulators/sprites/mario/small_run_2.png", IMREAD_GRAYSCALE)),
        ("small_run_3", imread("/home/casey/dev/emulators/sprites/mario/small_run_3.png", IMREAD_GRAYSCALE)),
        ("big_standing", imread("/home/casey/dev/emulators/sprites/mario/big_standing.png", IMREAD_GRAYSCALE)),
        ("bround", imread("/home/casey/dev/emulators/sprites/tiles/ground.png", IMREAD_GRAYSCALE)),
        ("block", imread("/home/casey/dev/emulators/sprites/tiles/block.png", IMREAD_GRAYSCALE)),
        ("qblock_spent", imread("/home/casey/dev/emulators/sprites/tiles/qblock_spent.png", IMREAD_GRAYSCALE)),
        ("bricks", imread("/home/casey/dev/emulators/sprites/tiles/bricks.png", IMREAD_GRAYSCALE)),
        ("qblock_3", imread("/home/casey/dev/emulators/sprites/tiles/qblock_3.png", IMREAD_GRAYSCALE)),
        ("qblock_1", imread("/home/casey/dev/emulators/sprites/tiles/qblock_1.png", IMREAD_GRAYSCALE)),
        ("qblock_2", imread("/home/casey/dev/emulators/sprites/tiles/qblock_2.png", IMREAD_GRAYSCALE)),
        ("mushroom", imread("/home/casey/dev/emulators/sprites/entities/mushroom.png", IMREAD_GRAYSCALE)),
        ("goomba_1", imread("/home/casey/dev/emulators/sprites/entities/goomba_1.png", IMREAD_GRAYSCALE)),
        ("goomba_2", imread("/home/casey/dev/emulators/sprites/entities/goomba_2.png", IMREAD_GRAYSCALE)),
        ("mario_small_standing", imread("/home/casey/dev/emulators/sprites/mario/small_standing.png", IMREAD_GRAYSCALE))
    )

    def findTiles(frame: GameFrame): ImageSpriteSet = {
        val matchingThreshold: Double = 0.05
        var spriteList: List[SpriteOccurances] = List()
        var grayScaleFrame: Mat = new Mat(
                    frame.data.cols,
                    frame.data.rows,
                    CvType.CV_8U
                )
        templates.foreach {
            case (name: String, template: Mat) => {
                var boxes: List[BoundingBox] = List()
                Imgproc.cvtColor(frame.data, grayScaleFrame, Imgproc.COLOR_BGR2GRAY);
                
                var resultantFrame: Mat = new Mat(
                    frame.data.cols - template.cols + 1,
                    frame.data.rows - template.rows + 1,
                    CvType.CV_8U
                )
                Imgproc.matchTemplate(grayScaleFrame, template, resultantFrame, Imgproc.TM_SQDIFF_NORMED)
                for (x <- 0 until resultantFrame.rows) {
                    for (y <- 0 until resultantFrame.cols) {
                        if (resultantFrame.get(x, y)(0) < matchingThreshold) {
                            boxes = BoundingBox(y, x, template.cols, template.rows) :: boxes
                        }
                    }
                }
                resultantFrame.release()
                spriteList = SpriteOccurances(name, boxes) :: spriteList
            }
        }
        return ImageSpriteSet(frame, spriteList)
    }

    def apply(frame: GameFrame): ImageSpriteSet = {
        return findTiles(frame)
    }

    def markTiles(spriteSet: ImageSpriteSet): GameFrame = {
        val oldImage = spriteSet.frame.data
        val newImage = oldImage.clone()
        spriteSet.spriteSet.foreach(spriteOccurances => {
            spriteOccurances.boundingBoxes.foreach(bb => {
                drawRectangle(newImage, new Point(bb.x, bb.y), new Point(bb.x + bb.height, bb.y + bb.width), new Scalar(255, 0, 0))
            })
        })
        GameFrame(spriteSet.frame.gameName, spriteSet.frame.number, newImage)        
    }
}