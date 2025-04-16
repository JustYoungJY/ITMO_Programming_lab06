package app.util;

import app.model.HumanBeing;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * CollectionWrapper is a helper class that wraps a Map<Long, HumanBeing>
 * as a list of entries, each with separate <key> and <value> XML elements.
 */
public class CollectionWrapper {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "entry")
    public List<MapEntry> entryList = new ArrayList<>();

    // Empty constructor for Jackson
    public CollectionWrapper() {
    }

    public CollectionWrapper(TreeMap<Long, HumanBeing> map) {
        for (Map.Entry<Long, HumanBeing> entry : map.entrySet()) {
            entryList.add(new MapEntry(entry.getKey(), entry.getValue()));
        }
    }

    public TreeMap<Long, HumanBeing> toMap() {
        TreeMap<Long, HumanBeing> map = new TreeMap<>();
        for (MapEntry entry : entryList) {
            map.put(entry.key, entry.value);
        }
        return map;
    }

    public static class MapEntry {
        @JacksonXmlProperty(localName = "key")
        public Long key;

        @JacksonXmlProperty(localName = "value")
        public HumanBeing value;

        public MapEntry() {
            // Empty constructor for Jackson
        }

        public MapEntry(Long key, HumanBeing value) {
            this.key = key;
            this.value = value;
        }
    }
}
