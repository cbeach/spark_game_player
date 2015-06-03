package sparkGamePlayer

import java.awt.Toolkit;
import java.awt.Image;
import java.net.URL;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

class TemplateMatcher {
  var tk: Toolkit = Toolkit.getDefaultToolkit(); 
  var url: URL = getClass().getResource("path/to/img.png"); 
  var img: Image = tk.createImage(url); 
  
  def TemplateMatcher() {
    tk.prepareImage(img, -1, -1, null);
  }
}