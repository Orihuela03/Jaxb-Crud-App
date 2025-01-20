package xml;

import model.*;
import jakarta.xml.bind.*;
import java.io.*;

public class XmlHandler {
    private static final String XML_FILE = "persons.xml";

    public static PersonList loadXml() {
        try {
            File file = new File(XML_FILE);
            if (file.exists()) {
                JAXBContext context = JAXBContext.newInstance(PersonList.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                return (PersonList) unmarshaller.unmarshal(file);
            } else {
                return new PersonList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new PersonList();
        }
    }

    public static void saveXml(PersonList personList) {
        try {
            JAXBContext context = JAXBContext.newInstance(PersonList.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(personList, new File(XML_FILE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}