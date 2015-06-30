package emulatorInterface

import java.io.File
import java.nio.ByteBuffer
import java.nio.file.Files
import java.nio.file.Paths
import java.awt.Image

import org.apache.spark.Logging
import org.apache.spark.streaming.receiver.Receiver
import org.apache.spark.storage.StorageLevel
import org.apache.spark._
import org.apache.spark.streaming._
import org.opencv.core._
import org.opencv.highgui.Highgui
import org.opencv.imgproc.Imgproc

import sparkGamePlayer.TileFinder
import sparkGamePlayer.RawGameFrame


class Nestopia extends Receiver[RawGameFrame](StorageLevel.MEMORY_AND_DISK_2) with Logging {
    var fileCount: Int = 0
    var dir: String = ""
    var startingFrame: Int = 0
    var throttle: Int = 0
    def this(directory: String, startingFrame: Int = 0, throttle: Int = 0) = {
        this()
        var file = new File(directory);
        this.startingFrame = startingFrame
        this.throttle = throttle
        dir = directory
        fileCount = file.listFiles().length;
    }
  
    def onStart() {
        // Start the thread that receives data over a connection
        new Thread("Socket Receiver") {
            override def run() { receive() }
        }.start()
    }
    def onStop() {}
    def receive() {
        for (fileNumber <- startingFrame to fileCount) {
            Thread.sleep(throttle)
            val image: Mat = Highgui.imread(s"${dir}${fileNumber}.png", Highgui.CV_LOAD_IMAGE_COLOR)
            val grayScaleImage: Mat = new Mat(image.height, image.width, image.`type`())
            var data: Array[Byte] = new Array[Byte](image.total().toInt * image.channels)
            image.get(0, 0, data)

            store(new RawGameFrame("SuperMarioBros-DuckHunt", fileNumber, data, image.height, image.width, image.`type`()))
        }
    }
}
