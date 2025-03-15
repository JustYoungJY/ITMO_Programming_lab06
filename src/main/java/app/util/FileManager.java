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
 * <p>
 * The collection is wrapped in a CollectionWrapper,
 * which allows Jackson to serialize it as a list of maps with individual tags for keys and entries.
 */
public class FileManager {
    private final String fileName;
    private final XmlMapper xmlMapper;

    public FileManager(String fileName) {
        this.fileName = fileName;
        // Создаем XmlMapper для работы с XML
        this.xmlMapper = new XmlMapper();
        // Регистрируем модуль для поддержки Java Time (например, ZonedDateTime)
        xmlMapper.registerModule(new JavaTimeModule());
        // Отключаем запись дат в виде числовых таймштампов (будут в ISO-формате)
        xmlMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // Включаем режим pretty printing для красивого форматирования XML
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public TreeMap<Long, HumanBeing> loadCollection() throws Exception {
        File file = new File(fileName);
        if (!file.exists()) {
            return new TreeMap<>();
        }
        // Считываем весь список из XML
        HumanBeingListWrapper wrapper = xmlMapper.readValue(file, HumanBeingListWrapper.class);

        // Перекладываем в TreeMap, ключ = h.getId()
        TreeMap<Long, HumanBeing> map = new TreeMap<>();
        for (HumanBeing h : wrapper.HumanBeing) {
            map.put(h.getId(), h);
        }
        return map;
    }

    public void saveCollection(TreeMap<Long, HumanBeing> map) throws Exception {
        // Превращаем map.values() в список
        List<HumanBeing> list = new ArrayList<>(map.values());
        HumanBeingListWrapper wrapper = new HumanBeingListWrapper(list);

        File file = new File(fileName);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs()) {
                throw new Exception("Failed to create directory: " + parent.getAbsolutePath());
            }
        }
        // Записываем в XML
        xmlMapper.writeValue(file, wrapper);
    }
}
