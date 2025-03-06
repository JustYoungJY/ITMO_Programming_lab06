package app.collection;

import app.model.HumanBeing;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.TreeMap;

/**
 * A class for managing a collection of HumanBeing objects.
 * The collection is contained in a TreeMap, so it`s sorted by key.
 */
public class CollectionManager<T extends HumanBeing> {
    private TreeMap<Long, T> collection;
    private final ZonedDateTime initDate;

    public CollectionManager() {
        this.collection = new TreeMap<>();
        this.initDate = ZonedDateTime.now();
    }

    public TreeMap<Long, T> getCollection() {
        return collection;
    }

    public void setCollection(TreeMap<Long, T> collection) {
        this.collection = collection;
    }

    public void insert(Long key, T element) {
        collection.put(key, element);
    }

    public boolean updateById(long id, T newElement) {
        for (Long key : collection.keySet()) {
            T element = collection.get(key);
            if (element.getId() == id) {
                collection.put(key, newElement);
                return true;
            }
        }
        return false;
    }

    public T removeKey(Long key) {
        return collection.remove(key);
    }

    public void clear() {
        collection.clear();
    }

    public Collection<T> getElements() {
        return collection.values();
    }

    public Collection<T> getElementsDescending() {
        return collection.descendingMap().values();
    }

    public String info() {
        return "Collection type: " + collection.getClass().getName() +
                "\nInitialization date" + initDate +
                "\nNumber of elements " + collection.size();
    }
}
