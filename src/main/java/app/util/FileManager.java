package app.util;

import app.model.HumanBeing;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
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
        xmlMapper = new XmlMapper();
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
        // Десериализуем XML в объект CollectionWrapper
        CollectionWrapper wrapper = xmlMapper.readValue(file, CollectionWrapper.class);
        // Преобразуем обертку обратно в TreeMap
        return wrapper.toMap();
    }

    public void saveCollection(TreeMap<Long, HumanBeing> collection) throws Exception {
        // Оборачиваем карту в CollectionWrapper
        CollectionWrapper wrapper = new CollectionWrapper(collection);
        File file = new File(fileName);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new Exception("Failed to create directory: " + parentDir.getAbsolutePath());
            }
        }
        // Сериализуем обертку в XML и записываем в файл
        xmlMapper.writeValue(file, wrapper);
    }
}
