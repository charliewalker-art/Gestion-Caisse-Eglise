package caiseeglise.Models;

import java.sql.*;
import java.util.*;

public class Entre extends Table {

    public Entre(Connection conn) {
        super(
            conn,
            "ENTRE",
            Arrays.asList("identre", "motif", "montantEntre", "dateEntre", "ideglise"),
            "identre"
        );
    }

    @Override
    protected String getEntityLabel() {
        return "Entrée";
    }

public String insertEntre(String motif, int montantEntre, String dateEntre, String ideglise) {
    String sql = "INSERT INTO ENTRE (motif, montantEntre, dateEntre, ideglise) VALUES (?, ?, ?, ?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, motif);
        stmt.setInt(2, montantEntre);
        stmt.setString(3, dateEntre);
        stmt.setString(4, ideglise);

        int rows = stmt.executeUpdate();
        if (rows == 1) {
            return "INSERT_OK";
        } else {
            return "INSERT_ERROR: Aucune ligne insérée.";
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return "INSERT_EXCEPTION: " + e.getMessage();
    }
}



}
