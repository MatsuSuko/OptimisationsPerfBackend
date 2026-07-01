package org.sebsy.grasps.beans;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Client {

    @Id
    private String identifiantClient;

    private boolean premium;

    @OneToMany(mappedBy = "client")
    private List<Reservation> reservations = new ArrayList<>();

    public Client() {

    }

    public Client(String identifiantClient, boolean premium) {
        super();
        this.identifiantClient = identifiantClient;
        this.premium = premium;
    }

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
     * @return the premium
     */
    public boolean isPremium() {
        return premium;
    }

    /**
     * Setter
     *
     * @param premium the premium to set
     */
    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    /**
     * Getter
     *
     * @return the reservations
     */
    public List<Reservation> getReservations() {
        return reservations;
    }

    /**
     * Setter
     *
     * @param reservations the reservations to set
     */
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    /**
     * GRASP - Information Expert : Client possède sa liste de réservations,
     * c'est donc lui qui est responsable de l'ajout d'une réservation.
     *
     * @param reservation la réservation à ajouter
     */
    public void ajouterReservation(Reservation reservation) {
        reservation.setClient(this);
        reservations.add(reservation);
    }
}
