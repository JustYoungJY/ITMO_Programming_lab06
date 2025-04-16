package app.util;

import app.model.HumanBeing;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * FileManager: Handles reading and writing the collection to an XML file.
 */
public class FileManager {
    private final String fileName;
    private final XmlMapper xmlMapper;

    public FileManager(String fileName) {
        this.fileName = fileName;
        // Creating an Xml Mapper for working with XML
        this.xmlMapper = new XmlMapper();
        // Registering a module to support Java Time
        xmlMapper.registerModule(new JavaTimeModule());
        // Disabling the recording of dates in the form of numeric timestamps
        xmlMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // Enabling the pretty printing mode for beautiful XML formatting
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public TreeMap<Long, HumanBeing> loadCollection() throws Exception {
        File file = new File(fileName);
        if (!file.exists()) {
            return new TreeMap<>();
        }
        // Reading the entire list from XML
        HumanBeingListWrapper wrapper = xmlMapper.readValue(file, HumanBeingListWrapper.class);

        TreeMap<Long, HumanBeing> map = new TreeMap<>();
        for (HumanBeing h : wrapper.HumanBeing) {
            map.put(h.getId(), h);
        }
        return map;
    }

    public void saveCollection(TreeMap<Long, HumanBeing> map) throws Exception {
        List<HumanBeing> list = new ArrayList<>(map.values());
        HumanBeingListWrapper wrapper = new HumanBeingListWrapper(list);

        File file = new File(fileName);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs()) {
                throw new Exception("Failed to create directory: " + parent.getAbsolutePath());
            }
        }
        // We write it in XML
        xmlMapper.writeValue(file, wrapper);
    }
}
