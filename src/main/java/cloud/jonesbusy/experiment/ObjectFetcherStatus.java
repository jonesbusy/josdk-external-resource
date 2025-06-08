package cloud.jonesbusy.experiment;

public record ObjectFetcherStatus(String status, Long observedGeneration) {

    public ObjectFetcherStatus() {
        this("UNKNOWN", 0L);
    }
}
