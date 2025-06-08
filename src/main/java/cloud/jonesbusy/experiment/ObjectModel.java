package cloud.jonesbusy.experiment;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a model for an object fetched from an external API.
 * This class encapsulates the ID, name, and additional data of the object.
 */
public class ObjectModel {

    private String id;
    private String name;
    private Map<String, String> data;

    public ObjectModel(String id, String name, Map<String, String> data) {
        this.id = id;
        this.name = name;
        this.data = data;
    }

    public ObjectModel() {
        // Needed for deserialization
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ObjectModel apiModel = (ObjectModel) o;
        return Objects.equals(id, apiModel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
