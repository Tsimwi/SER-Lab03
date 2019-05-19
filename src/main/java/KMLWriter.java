import java.io.*;
import java.util.List;

import org.jdom2.*;
import org.jdom2.output.*;

public class KMLWriter {

    /* Sérialise une liste de pays et les stock dans un fichier KML */
    public void writeCountriesToKml(List<Country> countries, String kmlToCreatePath){
        try {
            Element kml = new Element("kml");
            Document root = new Document(kml);
            kml.setAttribute(new Attribute("xmlns2", "http://www.opengis.net/kml/2.2"));

            Element document = new Element("Document");
            root.getRootElement().addContent(document);

            Element placemark;
            Element name;
            Element style;
            Element lineStyle;
            Element color;
            Element polyStyle;
            Element fill;
            Element multiGeometry = null;

            for(Country country : countries) {
                placemark = new Element("Placemark");
                name = new Element("name").setText(country.getName());
                placemark.addContent(name);
                style = new Element("Style");
                placemark.addContent(style);
                lineStyle = new Element("LineStyle");
                style.addContent(lineStyle);
                color = new Element("Color").setText("ffffffff");
                lineStyle.addContent(color);
                polyStyle = new Element("PolyStyle");
                style.addContent(polyStyle);
                fill = new Element("fill").setText("0");
                polyStyle.addContent(fill);

                /* Gestion des pays composés en plusieurs polygones */
                if (country.getCoordinates().size() > 1) {
                    multiGeometry = new Element("Multigeometry");
                    placemark.addContent(multiGeometry);
                }

                for (List<StringPair> list : country.getCoordinates()) {
                    Element polygon = new Element("Polygon");

                    /* Gestion des pays composés en plusieurs polygones*/
                    if (country.getCoordinates().size() > 1) {
                        multiGeometry.addContent(polygon);
                    } else {
                        placemark.addContent(polygon);
                    }

                    Element outerBoundaryIs = new Element("outerBoundaryIs");
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
            }

            XMLOutputter xmlOutputer = new XMLOutputter();
            xmlOutputer.setFormat(Format.getPrettyFormat());
            xmlOutputer.output(root, new FileWriter(kmlToCreatePath));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
