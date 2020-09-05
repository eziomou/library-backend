package pl.zmudzin.library.core.application.reservation;

public final class ReservationEventData {

    private final String status;
    private final String occurrenceDate;

    public ReservationEventData(String status, String occurrenceDate) {
        this.status = status;
        this.occurrenceDate = occurrenceDate;
    }

    public String getStatus() {
        return status;
    }

    public String getOccurrenceDate() {
        return occurrenceDate;
    }
}
