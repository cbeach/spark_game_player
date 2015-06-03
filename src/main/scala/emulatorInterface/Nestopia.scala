package emulatorInterface

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.Image;

import org.apache.spark.Logging
import org.apache.spark.streaming.receiver.Receiver
import org.apache.spark.storage.StorageLevel
import org.apache.spark._;
import org.apache.spark.streaming._;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;


class Nestopia extends Receiver[Mat](StorageLevel.MEMORY_AND_DISK_2) with Logging {
    var fileCount: Int = 0;
    def this(directory: String) = {
        this()
        var file = new File(directory);
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
        for (fileNumber <- 0 to fileCount) {
            store(Highgui.imread("${directory}/${fileNumber}.png"));
        }
    }
}