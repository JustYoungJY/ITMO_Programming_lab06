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
    private final ZonedDateTime initDate;
    private TreeMap<Long, T> collection;

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

    public void insert(T element) {
        collection.put(element.getId(), element);
    }

    public boolean updateById(long id, T newElement) {
        // Если запись с данным id существует, заменяем её
        if (collection.containsKey(id)) {
            collection.put(id, newElement);
            return true;
        }
        return false;
    }

    public T removeById(Long id) {
        return collection.remove(id);
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
