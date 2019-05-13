import java.io.*;
import java.util.List;

import org.jdom2.*;
import org.jdom2.output.*;

public class KMLWriter {

    private static final String kmlFilePath = "src/newKMLfile.kml";

    public static void main(String[] args) {
        try {
            Element kml = new Element("kml");
            Document root = new Document(kml);
            kml.setAttribute(new Attribute("xmlns2", "http://www.opengis.net/kml/2.2"));

            Element document = new Element("Document");
            root.getRootElement().addContent(document);
            Parser parser = new Parser();
            List<Country> countries = parser.parse();
            Element placemark;

            for(Country country : countries) {
                placemark = new Element("Placemark");
                Element name = new Element("Name").setText(country.getName());
                placemark.addContent(name);
                Element style = new Element("Style");
                placemark.addContent(style);
                Element lineStyle = new Element("LineStyle");
                style.addContent(lineStyle);
                Element color = new Element("Color").setText("ffffffff");
                lineStyle.addContent(color);
                Element polyStyle = new Element("LineStyle");
                style.addContent(polyStyle);
                Element fill = new Element("Fill").setText("0");
                polyStyle.addContent(fill);
                Element multiGeometry = null;

                if (country.getCoordinates().size() > 1) {
                    multiGeometry = new Element("Multigeometry");
                    placemark.addContent(multiGeometry);
                }

                for (List<StringPair> list : country.getCoordinates()) {
                    Element polygon = new Element("Polygon");

                    if (country.getCoordinates().size() > 1) {
                        multiGeometry.addContent(polygon);
                    } else {
                        placemark.addContent(polygon);
                    }
                    /*
                    Element temp = country.getCoordinates().size() > 1 ? new Element("Multigeometry") : polygon;
                    placemark.addContent(temp);*/

                    Element outerBoundaryIs = new Element("OuterBoundaryIs");
                    polygon.addContent(outerBoundaryIs);
                    Element linearRing = new Element("LinearRing");
                    outerBoundaryIs.addContent(linearRing);
                    StringBuffer sb = new StringBuffer();
                    for (StringPair sp : list) {
                        sb.append(sp).append("\n");
                    }
                    Element coordinates = new Element("coordinates").setText(sb.toString());
                    linearRing.addContent(coordinates);

                }

                document.addContent(placemark);


               // menuFichier.addContent(new Element("nom").setText("Fichier"))
            }


            XMLOutputter xmlOutputer = new XMLOutputter();
            xmlOutputer.setFormat(Format.getPrettyFormat());
            xmlOutputer.output(root, new FileWriter(kmlFilePath));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
