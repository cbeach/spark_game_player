package sparkGamePlayer

/* SimpleApp.scala */
import java.io.File;
import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext
import emulatorInterface.Nestopia;

object MainClass {
    def main(args: Array[String]) {
        val imageDirectory = "/home/casey/dev/emulators/data/";
        val conf = new SparkConf().setMaster("local[6]").setAppName("TemplateMatcher")
        val ssc = new StreamingContext(conf, Seconds(1))
        val nestopiaReceiverStream = ssc.receiverStream(new Nestopia(imageDirectory));
    }
}