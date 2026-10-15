package model.dao;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import config.Koneksi;
import model.entitas.FeatureFilm;
import model.entitas.Film;

public class FeatureFilmDAO {
    
    private Connection conn;
    
    public FeatureFilmDAO() {
        this.conn = Koneksi.getConnection();
    }
    
    public void tambahFeatureFilm(FeatureFilm film) {
        String sqlFilm = "INSERT INTO film (judul, sutradara, tahun_rilis, genre, durasi, tipe_film) VALUES (?, ?, ?, ?, ?, 'FEATURE')";
        String sqlFeature = "INSERT INTO feature_film (id_film, pemeran_utama, karakter_live_action) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmtFilm = conn.prepareStatement(sqlFilm, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmtFilm.setString(1, film.getJudul());
            pstmtFilm.setString(2, film.getSutradara());
            pstmtFilm.setInt(3, film.getTahunRilis());
            pstmtFilm.setString(4, film.getGenre());
            pstmtFilm.setInt(5, film.getDurasi());
            
            pstmtFilm.executeUpdate();
            
            ResultSet rs = pstmtFilm.getGeneratedKeys();
            if (rs.next()){
                int generatedId = rs.getInt(1);
             
            
                try (PreparedStatement pstmtFeature = conn.prepareStatement(sqlFeature)){
                    
                pstmtFeature.setInt(1, generatedId);
                pstmtFeature.setString(2, film.getPemeranUtama());
                pstmtFeature.setString(3, film.getKarakterLiveAction());
                
                pstmtFeature.executeUpdate();
                }
            }
        } catch (SQLException e){
            System.out.println("Gagal menambahkan film - " + e.getMessage());
        }    
    }
    
    //BACA Film
    public List<Film> ambilFilm() {
        List<Film> list = new ArrayList<>();
        String sql = "SELECT f.*, ff.pemeran_utama, ff.karakter_live_action " +
                     "FROM film f JOIN feature_film ff ON f.id_film = ff.id_film";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                list.add(new FeatureFilm(
                    rs.getInt("id_film"),
                    rs.getString("judul"),
                    rs.getString("sutradara"),
                    rs.getInt("tahun_rilis"),
                    rs.getString("genre"),
                    rs.getInt("durasi"),
                    rs.getString("pemeran_utama"),
                    rs.getString("karakter_live_action")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil data film - " + e.getMessage());
        }
        return list;
    }  
}