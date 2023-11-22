package activeRecord;

import java.sql.*;
import java.util.ArrayList;

public class Film {
    private String titre;
    private int id;
    private int id_real;


    public Film(String titred,Personne auteur){
        titre = titred;
        id=-1;
        id_real = auteur.getId();
    }

    private Film(String titred,int idd,int id_reald){
        titre = titred;
        id = idd;
        id_real = id_reald;
    }

    public static Film findByID(int idd) throws SQLException {
        DBConnection connection = DBConnection.getInstance();
        Connection connect = connection.getConnection();
        String SQLPrep = "SELECT * FROM Film WHERE id=?;";
        PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
        prep1.setInt(1, idd);
        prep1.execute();
        ResultSet rs = prep1.getResultSet();
        Film resultat = new Film(rs.getString("titre"),rs.getInt("id"),rs.getInt("id_real"));
        return resultat;
    }

    public ArrayList<Film> findByRealisateur(Personne rea) throws SQLException {
        ArrayList resultat = new ArrayList<Film>();
        int idrea = rea.getId();
        DBConnection connection = DBConnection.getInstance();
        Connection connect = connection.getConnection();
        String SQLPrep = "SELECT * FROM Film WHERE id_real=?;";
        PreparedStatement prep1 = connect.prepareStatement(SQLPrep);
        prep1.setInt(1, idrea);
        prep1.execute();
        ResultSet rs = prep1.getResultSet();
        while (rs.next()) {
            resultat.add(new Film(rs.getString("titre"), rs.getInt("id"), rs.getInt("id_real")));
        }
        return resultat;
    }

    public Personne getRealisateur(int idRea) throws SQLException {
        return Personne.findById(idRea);
    }

    public void delete() throws SQLException {
        DBConnection connection = DBConnection.getInstance();
        Connection connect = connection.getConnection();
        PreparedStatement prep = connect.prepareStatement("DELETE FROM Film WHERE id=?");
        prep.setInt(1, this.id);
        prep.execute();
        this.id = -1;
    }

    public void save() throws SQLException, RealisateurAbsentException {
        if(id_real==-1){
            throw new RealisateurAbsentException("Realisateur absent");
        }
        DBConnection connection = DBConnection.getInstance();
        Connection connect = connection.getConnection();
        if(this.id == -1) {
            String SQLPrep = "INSERT INTO Film (titre, id_real) VALUES (?,?);";
            PreparedStatement prep = connect.prepareStatement(SQLPrep, Statement.RETURN_GENERATED_KEYS);
            prep.setString(1, titre);
            prep.setInt(2, id_real);
            prep.executeUpdate();
        }else{
            String SQLPrep = "UPDATE Film SET titre = ?, id_real = ? WHERE id=?;";
            PreparedStatement prep = connect.prepareStatement(SQLPrep, Statement.RETURN_GENERATED_KEYS);
            prep.setString(3, String.valueOf(this.id));
            prep.setString(1, titre);
            prep.setInt(2, id_real);
            prep.executeUpdate();
        }
    }
}
