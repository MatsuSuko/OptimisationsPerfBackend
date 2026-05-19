package org.sebsy.grasps;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Params {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private String identifiantClient;
    private int nbPlaces;
    private String dateReservation;
    private String typeReservation;

    /**
     * Getter
     *
     * @return the identifiantClient
     */
    public String getIdentifiantClient() {
        return identifiantClient;
    }

    /**
     * Setter
     *
     * @param identifiantClient the identifiantClient to set
     */
    public void setIdentifiantClient(String identifiantClient) {
        this.identifiantClient = identifiantClient;
    }

    /**
     * Getter
     *
     * @return the nbPlaces
     */
    public int getNbPlaces() {
        return nbPlaces;
    }

    /**
     * Setter
     *
     * @param nbPlaces the nbPlaces to set
     */
    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    /**
     * Getter
     *
     * @return the dateReservation
     */
    public String getDateReservation() {
        return dateReservation;
    }

    /**
     * Setter
     *
     * @param dateReservation the dateReservation to set
     */
    public void setDateReservation(String dateReservation) {
        this.dateReservation = dateReservation;
    }

    /**
     * Getter
     *
     * @return the typeReservation
     */
    public String getTypeReservation() {
        return typeReservation;
    }

    /**
     * Setter
     *
     * @param typeReservation the typeReservation to set
     */
    public void setTypeReservation(String typeReservation) {
        this.typeReservation = typeReservation;
    }
}
