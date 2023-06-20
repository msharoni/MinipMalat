package scene;

import lighting.AmbientLight;
import org.w3c.dom.*;
import org.xml.sax.*;

import javax.xml.*;
import javax.xml.parsers.*;
import java.io.*;

import primitives.*;
import geometries.*;

public class xmlReader {

    private static final String FILENAME = "C:/Users/mor/IdeaProjects/Minip_Malat/src/xmlfiles/basicRenderTestTwoColors.xml";

    public static Scene parse(String name) {

        Scene scene = new Scene(name);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(FILENAME));
            doc.getDocumentElement().normalize();

            // Background
            Node backgroundNode = doc.getElementsByTagName("scene").item(0);
            Double3 bc = xmlReader.XmlToDouble3("background-color", backgroundNode);
            scene.setBackground(new Color(bc));

            // Ambient Light
            Node ambientLightNode = doc.getElementsByTagName("ambient-light").item(0);
            Double3 x = xmlReader.XmlToDouble3("color", ambientLightNode);
            scene.setAmbientLight(new AmbientLight(new Color(x), new Double3(1, 1, 1)));

            // Geometries
            Node geometries = doc.getElementsByTagName("geometries").item(0);

            for (int i = 0; i < geometries.getChildNodes().getLength(); i++) {

                Node node = geometries.getChildNodes().item(i);

                if (node.getNodeName() == "triangle"){
                    scene.geometries.add(new Triangle(new Point(XmlToDouble3("p0",node)), new Point(XmlToDouble3("p1",node)) , new Point(XmlToDouble3("p2",node))));
                }
                else if (node.getNodeName() == "sphere"){
                    scene.geometries.add(new Sphere( new Point(XmlToDouble3("center",node)) , Double.parseDouble(node.getAttributes().getNamedItem("radius").getTextContent())));
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return scene;
    }

    public static Double3 XmlToDouble3(String name, Node root) {
        String color = root.getAttributes().getNamedItem(name).getTextContent();
        String[] colors = color.split(" ");
        return new Double3(Double.parseDouble(colors[0]), Double.parseDouble(colors[1]), Double.parseDouble(colors[2]));
    }
}