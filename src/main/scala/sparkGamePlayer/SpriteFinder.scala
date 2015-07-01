package sparkGamePlayer

import org.opencv.core.Core
import org.opencv.core.Core.{rectangle => drawRectangle}
import org.opencv.core.Core.MinMaxLocResult
import org.opencv.core.CvType
import org.opencv.core.{Mat, MatOfKeyPoint}
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.features2d.Features2d
import org.opencv.highgui.Highgui.{imread, imwrite, IMREAD_GRAYSCALE}
import org.opencv.imgproc.Imgproc
import org.opencv.features2d.{DescriptorExtractor, FeatureDetector}

class SpriteFinder {
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
    
    def findSprites(frame: GameFrame) = {
        
       
    }
    
    def extractDescriptors(frame: GameFrame) = {
        val extractor: DescriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF)
    }
    
    def extractFeatures(frame: GameFrame): Unit = {
        val detector: FeatureDetector = FeatureDetector.create(FeatureDetector.PYRAMID_FAST)
        var keyPoints: MatOfKeyPoint = new MatOfKeyPoint()
        detector.detect(frame.data, keyPoints)
        val markedPoints = new Mat(frame.data.rows, frame.data.cols, frame.data.`type`())
        Features2d.drawKeypoints(frame.data, keyPoints, markedPoints)
        keyPoints.release()
        imwrite(s"/home/casey/dev/emulators/analyzed/segmented/${frame.number}.png", markedPoints)
        frame.data.release()
              
        //val harrisCorners: Mat = new Mat(frame.data.rows, frame.data.cols, frame.data.`type`())
    }
}