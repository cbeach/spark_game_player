package sparkGamePlayer

/* SimpleApp.scala */
import java.io.File
import java.net.URL

import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui.imwrite
import org.opencv.imgproc.Imgproc;

import emulatorInterface._;

object MainClass {
    def main(args: Array[String]) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        val imageDirectory = "/home/casey/dev/emulators/data/";
        val conf = new SparkConf()
            .setMaster("local[4]")
            .setAppName("TemplateMatcher")
            .set("spark.executor.memory", "4g")
            .set("spark.driver.memory", "4g")
            .set("spark.storage.memoryFraction", "1")
        val ssc = new StreamingContext(conf, Seconds(1))
        val nestopiaReceiverStream = ssc.receiverStream(new Nestopia(imageDirectory, 1055, 3000));
        val gameFrames = nestopiaReceiverStream.map(_ match {
            case RawGameFrame(nameOfGame, frameNumber, frameData, height, width, imageType) => {
                var mat: Mat = new Mat(height, width, imageType)
                mat.put(0, 0, frameData)
                
                GameFrame(nameOfGame, frameNumber, mat)
            }
        })
        
        val tiles = gameFrames.map(frame => {
            new TileFinder().findTiles(frame)
        })

        tiles.map(new TileFinder().markTiles(_)).foreachRDD(rdd => {
            rdd.foreach( 
                frame => imwrite(s"/home/casey/dev/emulators/analyzed/segmented/${frame.number}.png", frame.data)
            )
        })
        tiles.map(spriteSet => spriteSet.frame.data.release())
        
        ssc.start()
        ssc.awaitTermination()
    }
}
