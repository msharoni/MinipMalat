package unittests.renderer;
import org.junit.jupiter.api.Test;
import renderer.*;
import primitives.*;



class ImageWriterTest {
	@Test
	void imageWriterTest() {
        ImageWriter imageWriter = new ImageWriter("Test32", 800, 500);
        Color background = new Color(255,255,0);
        Color grid = Color.BLACK;
        double squreLength = imageWriter.getNx()/16;
        for (int i = 0; i < imageWriter.getNy(); i++) {
            for (int j = 0; j < imageWriter.getNx(); j++) {
                if (j % squreLength < 2 ||
                        i % squreLength < 2)
                    imageWriter.writePixel(j, i, grid);
                else
                    imageWriter.writePixel(j, i, background);
            }
        }
        imageWriter.writeToImage();
    }
}
