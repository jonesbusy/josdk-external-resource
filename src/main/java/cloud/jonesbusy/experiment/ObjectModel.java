package cloud.jonesbusy.experiment;

import java.util.Objects;

/**
 * Represents a model for an object fetched from an external API.
 * This class encapsulates the ID, name, and additional data of the object.
 */
public class ObjectModel {

    private final String id;
    private final String name;

    public ObjectModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
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
