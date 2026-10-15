package model.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import config.Koneksi;
import model.entitas.Review;

public class ReviewDAO {
    
    public void tambahReview(Review review) {
        String sql = "INSERT INTO review (id_film, rating, ulasan, is_rewatch) VALUES (?, ?, ?, ?)";
        try (Connection conn = Koneksi.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, review.getIdFilm());
            pstmt.setDouble(2, review.getRating());
            pstmt.setString(3, review.getUlasan());
            pstmt.setBoolean(4, review.isRewatch());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Gagal menambah review: " + e.getMessage());
        }
    }
    
    public List<Review> ambilSemuaReview() {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT * FROM review";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Review(
                    rs.getInt("id_review"),
                    rs.getInt("id_film"),
                    rs.getDouble("rating"),
                    rs.getString("ulasan"),
                    rs.getBoolean("is_rewatch")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil daftar review: " + e.getMessage());
        }
        return list;
    }
    
    public List<Review> ambilReviewId(int idFilm) {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT * FROM review WHERE id_film = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idFilm);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Review(
                        rs.getInt("id_review"),
                        rs.getInt("id_film"),
                        rs.getDouble("rating"),
                        rs.getString("ulasan"),
                        rs.getBoolean("is_rewatch")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengambil review film: " + e.getMessage());
        }
        return list;
    }
    
    public void updateReview(Review review) {
        String sql = "UPDATE review SET rating = ?, ulasan = ? WHERE id_review = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, review.getRating());
            pstmt.setString(2, review.getUlasan());
            pstmt.setInt(3, review.getIdReview());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Gagal mengupdate review: " + e.getMessage());
        }
    }
    
    public void hapusReview(int idReview) {
        String sql = "DELETE FROM review WHERE id_review = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idReview);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Gagal menghapus review: " + e.getMessage());
        }
    }
    
    public boolean adaIdFilm(int idFilm) {
        String sql = "SELECT 1 FROM film WHERE id_film = ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
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