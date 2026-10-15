package model.dao;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import config.Koneksi;
import model.entitas.AnimatedFilm;
import model.entitas.Film;

public class AnimatedFilmDAO {
    
    private Connection conn;
    
    public AnimatedFilmDAO() {
        this.conn = Koneksi.getConnection();
    }
           
    public void tambahAnimatedFilm(AnimatedFilm film) {
        String sqlFilm = "INSERT INTO film (judul, sutradara, tahun_rilis, genre, durasi, tipe_film) VALUES (?, ?, ?, ?, ?, 'ANIMATED')";
        String sqlAnimated = "INSERT INTO animated_film (id_film, pengisi_suara, karakter_animasi, gaya_animasi) VALUES (?, ?, ?, ?)";
        
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
            
                try (PreparedStatement pstmtAnimated = conn.prepareStatement(sqlAnimated)){
                    pstmtAnimated.setInt(1, generatedId);
                    pstmtAnimated.setString(2, film.getPengisiSuara());
                    pstmtAnimated.setString(3, film.getKarakterAnimasi());
                    pstmtAnimated.setString(4, film.getGayaAnimasi());   
                    pstmtAnimated.executeUpdate();
                }
            }
        } catch (SQLException e){
            System.out.println("Gagal menambahkan film - " + e.getMessage());
        }    
    }
    
    //BACA Film
    public List<Film> ambilFilm() {
    List<Film> list = new ArrayList<>();
    String sql = "SELECT f.*, af.pengisi_suara, af.karakter_animasi, af.gaya_animasi " +
                 "FROM film f JOIN animated_film af ON f.id_film = af.id_film";

    try (PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            list.add(new AnimatedFilm(
                rs.getInt("id_film"),
                rs.getString("judul"),
                rs.getString("sutradara"),
                rs.getInt("tahun_rilis"),
                rs.getString("genre"),
                rs.getInt("durasi"),
                rs.getString("pengisi_suara"),
                rs.getString("karakter_animasi"),
                rs.getString("gaya_animasi")
            ));
        }
    } catch (SQLException e) {
        System.out.println("Gagal mengambil data film animasi - " + e.getMessage());
    }
    return list;
    }
    
    //Cari ID Film
        public boolean adaIdFilm(int idFilm) {
        String sql = "SELECT 1 FROM film WHERE id_film = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idFilm);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengecek film: " + e.getMessage());
            return false;
        }
    }
        
}