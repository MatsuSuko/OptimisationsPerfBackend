package org.sebsy.grasps;

import org.sebsy.grasps.beans.Client;
import org.sebsy.grasps.beans.Reservation;
import org.sebsy.grasps.beans.TypeReservation;
import org.sebsy.grasps.daos.ClientDao;
import org.sebsy.grasps.daos.TypeReservationDao;

import java.time.LocalDateTime;

/**
 * Controlleur qui prend en charge la gestion des réservations client
 */
public class ReservationController {

    /**
     * DAO permettant d'accéder à la table des clients
     */
    private ClientDao clientDao;

    /**
     * DAO permettant d'accéder à la table des types de réservation
     */
    private TypeReservationDao typeReservationDao;

    public ReservationController() {
        this.clientDao = new ClientDao();
        this.typeReservationDao = new TypeReservationDao();
    }

    public ReservationController(ClientDao clientDao, TypeReservationDao typeReservationDao) {
        this.clientDao = clientDao;
        this.typeReservationDao = typeReservationDao;
    }

    /**
     * Méthode qui créée une réservation pour un client à partir des informations transmises
     *
     * @param params contient toutes les infos permettant de créer une réservation
     * @return Reservation
     */
    public Reservation creerReservation(Params params) {

        // 1) Récupération des infos provenant de la classe appelante
        String identifiantClient = params.getIdentifiantClient();
        String dateReservationStr = params.getDateReservation();
        String typeReservation = params.getTypeReservation();
        int nbPlaces = params.getNbPlaces();

        // 2) Conversion déléguée à Params (GRASP - Information Expert)
        LocalDateTime dateReservation = params.getDateReservationAsLocalDateTime();

        // 3) Extraction de la base de données des informations client
        Client client = clientDao.extraireClient(identifiantClient);

        // 4) Extraction de la base de données des infos concernant le type de la réservation
        TypeReservation type = typeReservationDao.extraireTypeReservation(typeReservation);

        // 5) Création de la réservation
        Reservation reservation = new Reservation(dateReservation);
        reservation.setNbPlaces(nbPlaces);

        // 6) Ajout délégué à Client (GRASP - Information Expert)
        client.ajouterReservation(reservation);

        // 7) Calcul du montant total délégué à TypeReservation (GRASP - Information Expert)
        reservation.setTotal(type.calculerTotal(nbPlaces, client.isPremium()));
        return reservation;
    }

}
