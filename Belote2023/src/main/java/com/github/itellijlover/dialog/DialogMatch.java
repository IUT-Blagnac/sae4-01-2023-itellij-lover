package com.github.itellijlover.dialog;

import com.github.itellijlover.db.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * TODO à transformer en DAO
 */
public class DialogMatch {

    public static ResultSet getTournois() throws SQLException {
        Statement statement = DatabaseConnection.getInstance().getStatement();
        String query = "SELECT * FROM tournoi;";
        return statement.executeQuery(query);
    }

    public static ResultSet getMatchTermines(int idTour) throws SQLException {
        Statement statement = DatabaseConnection.getInstance().getStatement();
        String query = "Select count(*) as total, (Select count(*) from match m2  WHERE m2.id_tournoi = m.id_tournoi  AND m2.termine='oui' ) as termines from match m  WHERE m.id_tournoi=" + idTour +" GROUP by id_tournoi ;";
        return statement.executeQuery(query);
    }

    public static ResultSet getToursParMatch(int idTour) throws SQLException {
        Statement statement = DatabaseConnection.getInstance().getStatement();
        String query = "Select num_tour,count(*) as tmatchs, (Select count(*) from match m2  WHERE m2.id_tournoi = m.id_tournoi  AND m2.num_tour=m.num_tour  AND m2.termine='oui' ) as termines from match m  WHERE m.id_tournoi=" + idTour + " GROUP BY m.num_tour,m.id_tournoi;";
        return statement.executeQuery(query);
    }

    public static ResultSet getResultMatch(int idTour) throws SQLException{
        Statement statement = DatabaseConnection.getInstance().getStatement();
        String query = "SELECT equipe,(SELECT nom_j1 FROM equipe e WHERE e.id_equipe = equipe AND e.id_tournoi = " + idTour + ") as joueur1,(SELECT nom_j2 FROM equipe e WHERE e.id_equipe = equipe AND e.id_tournoi = " + idTour + ") as joueur2, SUM(score) as score, (SELECT count(*) FROM match m WHERE (m.equipe1 = equipe AND m.score1 > m.score2  AND m.id_tournoi = id_tournoi) OR (m.equipe2 = equipe AND m.score2 > m.score1 )) as matchs_gagnes, (SELECT COUNT(*) FROM match m WHERE m.equipe1 = equipe OR m.equipe2=equipe) as matchs_joues FROM  (select equipe1 as equipe,score1 as score from match where id_tournoi=" + idTour + " UNION select equipe2 as equipe,score2 as score from match where id_tournoi=" + idTour + ") GROUP BY equipe ORDER BY matchs_gagnes DESC;";
        return statement.executeQuery(query);
    }

}
