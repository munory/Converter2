import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String fileName = "data.xml";

        List<Employee> list = parseXML(fileName);

        String json = listToJson(list);

        writeString(json);
    }

    public static List<Employee> parseXML(String fileName) {

        var result = new ArrayList<Employee>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileName));

            Node root = doc.getDocumentElement();

            NodeList nodeList = root.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    Element employee = (Element) node;

                    result.add(new Employee(
                            Long.parseLong(employee.getElementsByTagName("id").item(0).getTextContent()),
                            employee.getElementsByTagName("firstName").item(0).getTextContent(),
                            employee.getElementsByTagName("lastName").item(0).getTextContent(),
                            employee.getElementsByTagName("country").item(0).getTextContent(),
                            Integer.parseInt(employee.getElementsByTagName("age").item(0).getTextContent()))
                    );
                }
            }

        } catch (IOException | ParserConfigurationException | SAXException e) {

            e.printStackTrace();
        }

        return result;
    }

    public static void writeString(String json) {

        try (FileWriter fileWriter = new FileWriter("data.json")) {

            fileWriter.write(json);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static String listToJson(List<Employee> list) {

        GsonBuilder builder = new GsonBuilder();

        Gson gson = builder.create();

        Type listType = new TypeToken<List<Employee>>() {
        }.getType();

        String json = gson.toJson(list, listType);

        return json;

    }

}
