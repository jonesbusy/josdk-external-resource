package cloud.jonesbusy.experiment;

import java.util.Map;
import java.util.Objects;

public class APIModel {

    private final String id;
    private final String name;
    private final Map<String, String> data;

    public APIModel(String id, String name, Map<String, String> data) {
        this.id = id;
        this.name = name;
        this.data = data;
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
        APIModel apiModel = (APIModel) o;
        return Objects.equals(id, apiModel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
