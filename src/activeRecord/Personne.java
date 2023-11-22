package activeRecord;

import java.sql.*;
import java.util.ArrayList;

public class Personne {
    private int id;
    private String nom;
    private String prenom;
    private Connection connect;

    public Personne(String n,String p) throws SQLException {
        nom = n;
        prenom = p;
        id = -1;
        DBConnection connection = DBConnection.getInstance();
        connect = connection.getConnection();
    }

    public ArrayList<String> findAll() throws SQLException {
        ArrayList<String> result = new ArrayList<>();
        String SQLPrep = "SELECT * FROM Personne;";
        PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
        prep1.execute();
        ResultSet rs = prep1.getResultSet();
        // s'il y a un resultat
        while (rs.next()) {
            String nom = rs.getString("nom");
            String prenom = rs.getString("prenom");
            int id = rs.getInt("id");
            result.add("  -> (" + id + ") " + nom + ", " + prenom);
        }
        return result;
    }

    public static Personne findById(int idD) throws SQLException {
        DBConnection connection = DBConnection.getInstance();
        Connection connect = connection.getConnection();
        String SQLPrep = "SELECT * FROM Personne WHERE id=?;";
        PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
        prep1.setInt(1, idD);
        prep1.execute();
        ResultSet rs = prep1.getResultSet();
        Personne result = new Personne(rs.getString("nom"),rs.getString("prenom"));
        return result;
    }

    public static Personne findByName(String nomD) throws SQLException {
        DBConnection connection = DBConnection.getInstance();
        Connection connect = connection.getConnection();
        String SQLPrep = "SELECT * FROM Personne WHERE nom=?;";
        PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
        prep1.setString(1, nomD);
        prep1.execute();
        ResultSet rs = prep1.getResultSet();
        Personne result = new Personne(rs.getString("nom"),rs.getString("prenom"));
        return result;
    }

    public static void createTable() throws SQLException {
        DBConnection connection = DBConnection.getInstance();
        Connection ctConnect = connection.getConnection();
        String createString = "CREATE TABLE Personne ( " + "ID INTEGER  AUTO_INCREMENT, " + "NOM varchar(40) NOT NULL, " + "PRENOM varchar(40) NOT NULL, " + "PRIMARY KEY (ID))";
        Statement stmt = ctConnect.createStatement();
        stmt.executeUpdate(createString);
    }

    public static void deleteTable() throws SQLException {
        DBConnection connection = DBConnection.getInstance();
        Connection dtConnect = connection.getConnection();
        String drop = "DROP TABLE Personne";
        Statement stmt = dtConnect.createStatement();
        stmt.executeUpdate(drop);
    }

    public void delete() throws SQLException {
        PreparedStatement prep = connect.prepareStatement("DELETE FROM Personne WHERE id=?");
        prep.setInt(1, this.id);
        prep.execute();
        this.id = -1;
    }

    public void save() throws SQLException {
        if(this.id == -1) {
            String SQLPrep = "INSERT INTO Personne (nom, prenom) VALUES (?,?);";
            PreparedStatement prep = connect.prepareStatement(SQLPrep, Statement.RETURN_GENERATED_KEYS);
            prep.setString(1, this.nom);
            prep.setString(2, this.prenom);
            prep.executeUpdate();
        }else{
            String SQLPrep = "UPDATE Personne SET nom = ?, prenom = ? WHERE id=?;";
            PreparedStatement prep = connect.prepareStatement(SQLPrep, Statement.RETURN_GENERATED_KEYS);
            prep.setString(3, String.valueOf(this.id));
            prep.setString(1, this.nom);
            prep.setString(2, this.prenom);
            prep.executeUpdate();
        }
    }

    public int getId() {
        return id;
    }
}
