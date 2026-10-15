package controller;

import model.entitas.Film;
import model.entitas.AnimatedFilm;
import model.entitas.FeatureFilm;
import model.entitas.Review;
import java.util.ArrayList;
import java.util.List;
import view.FilmView;
import model.dao.*;

public class FilmController {
    private final FeatureFilmDAO featureDAO = new FeatureFilmDAO();
    private final AnimatedFilmDAO animatedDAO = new AnimatedFilmDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();
    private final FilmView view = new FilmView();

    private List<Film> getSemuaFilmList(){
        List<Film> semua = new ArrayList<>();
        semua.addAll(featureDAO.ambilFilm());
        semua.addAll(animatedDAO.ambilFilm());
        semua.sort((f1, f2) -> Integer.compare(f1.getIdFilm(), f2.getIdFilm()));
        
        return semua;
    }

    // CREATE FILM
    public void tambahFilm() {
        view.tampilkanTambahFilm();
        int jenis = ValidasiInput.inputAngka("Pilih jenis (1/2): ");
        if (jenis < 1 || jenis > 2) {
            System.out.println("Pilihan jenis film tidak valid!");
            return;
        }

        String judul = ValidasiInput.inputNonKosong("Masukkan Judul Film: ");
        String sutradara = ValidasiInput.inputNonKosong("Masukkan Sutradara Film: ");
        String genre = ValidasiInput.inputNonKosong("Masukkan Genre Film: ");

        Film validator;
        if (jenis == 1){
            validator = new FeatureFilm(0, judul, sutradara, 2000, genre, 120, "-", "-");
        } else { 
            validator = new AnimatedFilm(0, judul, sutradara, 2000, genre, 120, "-", "-", "-");
        }

        int tahunRilis;
        while (true) {
            tahunRilis = ValidasiInput.inputAngka("Masukkan Tahun Rilis Film: ");
            if (validator.setTahunRilis(tahunRilis)) {
                break;
            }
        }

        int durasi;
        while (true) {
            durasi = ValidasiInput.inputAngka("Masukkan Durasi Film (menit): ");
            if (validator.setDurasi(durasi)) {
                break;
            }
        }

        System.out.println("------------------------------------------------------------------");

        switch (jenis) {
            case 1 -> {
                String pemeranUtama = ValidasiInput.inputNonKosong("Masukkan Nama Aktor Utama: ");
                String karakter = ValidasiInput.inputNonKosong("Masukkan Nama Karakter/Tokoh: ");
                featureDAO.tambahFeatureFilm(new FeatureFilm(0, judul, sutradara, tahunRilis, genre, durasi, pemeranUtama, karakter));
            }
            case 2 -> {
                String va = ValidasiInput.inputNonKosong("Masukkan Nama Voice Actor (VA): ");
                String karakter = ValidasiInput.inputNonKosong("Masukkan Nama Karakter Animasi: ");
                String gaya = ValidasiInput.inputNonKosong("Masukkan Gaya Animasi: ");
                animatedDAO.tambahAnimatedFilm(new AnimatedFilm(0, judul, sutradara, tahunRilis, genre, durasi, va, karakter, gaya));
            }
        }

        System.out.println("------------------------------------------------------------------");
        System.out.println("\nYeayy, film kamu berhasil ditambahkan!");
        lihatFilm();
    }

    // CREATE REVIEW
    public void tambahReview() {
        List<Film> daftarFilm = getSemuaFilmList();
        if (daftarFilm.isEmpty()) {
            System.out.println("Belum ada film di sini!");
            return;
        }

        view.tampilkanRingkasanFilm(daftarFilm, reviewDAO.ambilSemuaReview());
        view.tampilkanTambahReview();

        int cariFilm;
        while (true) {
            cariFilm = ValidasiInput.inputAngka("Masukkan ID Film yang mau di-review: ");

            if (!reviewDAO.adaIdFilm(cariFilm)) {
                System.out.println("ID film tidak ada di sistem! Silakan coba lagi.");
            } else {
                break;
            }
        }

        boolean isRewatch = !reviewDAO.ambilReviewId(cariFilm).isEmpty();

        if (isRewatch) {
            System.out.println("\nFilm ini sudah pernah kamu ulas. Review baru akan disimpan sebagai [REWATCH].");
        }

        Review reviewSemn = new Review(0, cariFilm, 5.0, "", isRewatch);

        double rating;
        while (true) {
            rating = ValidasiInput.inputRating("Masukkan Rating Film (1 - 5): ");
            if (reviewSemn.setRating(rating)) {
                break;
            }
        }

        String ulasan = ValidasiInput.inputNonKosong("Masukkan Ulasan Film: ");

        reviewDAO.tambahReview(new Review(0, cariFilm, rating, ulasan, isRewatch));

        System.out.println("\nYeayy, ulasan berhasil ditambahkan!");
        lihatFilm();
    }

    // READ (baca film)
    public void lihatFilm() {
        view.tampilkanFilm(getSemuaFilmList(), reviewDAO.ambilSemuaReview());
    }

    // UPDATE REVIEW
    public void updateReview() {
        List<Review> daftarReview = reviewDAO.ambilSemuaReview();
        if (daftarReview.isEmpty()) {
            System.out.println("\nBelum ada review yang bisa diubah!");
            return;
        }

        lihatFilm();
        view.tampilkanUpdateReview();
        int updateIdReview = ValidasiInput.inputAngka("Masukkan ID review yang mau di-update: ");

        Review reviewTarget = null;
        for (Review r : daftarReview) {
            if (r.getIdReview() == updateIdReview) {
                reviewTarget = r;
                break;
            }
        }
        if (reviewTarget == null){
            System.out.println("ID Review tidak ditemukan!");
            return;
        }
        
        System.out.println("(1) Rating");
        System.out.println("(2) Ulasan");
        int pilihan = ValidasiInput.inputAngka("Pilih bagian yang ingin diubah: ");

        switch (pilihan) {
            case 1 -> {
                double ratingBaru;
                while (true) {
                    ratingBaru = ValidasiInput.inputRating("Rating Film Baru (1 - 5): ");
                    if (reviewTarget.setRating(ratingBaru)) {
                        break;
                    }
                }
                
                reviewDAO.updateReview(reviewTarget);
                System.out.println("\nYeayy, rating baru kamu berhasil diubah!");
                lihatFilm();
            }
                    case 2 -> {
                        String ulasanBaru = ValidasiInput.inputNonKosong("Ulasan Film Baru: ");
                        reviewTarget.setUlasan(ulasanBaru);
                        reviewDAO.updateReview(reviewTarget);
                        System.out.println("\nYeayy, ulasan terbaru kamu berhasil diubah!");
                        lihatFilm();
                    }
                    default -> System.out.println("Pilihan tidak valid!");
                }
            }
  
    // DELETE REVIEW
    public void hapusReview() {
        List<Review> daftarReview = reviewDAO.ambilSemuaReview();
        if (daftarReview.isEmpty()) {
            System.out.println("\nBelum ada review yang bisa dihapus!");
            return;
        }

        lihatFilm();
        view.tampilkanHapusReview();
        int hapusReview = ValidasiInput.inputAngka("Masukkan ID Review yang mau dihapus: ");

        boolean ketemu =false;
        for (Review r : daftarReview) {
            if (r.getIdReview() == hapusReview) {
                ketemu = true;
                boolean konfirmasi = ValidasiInput.inputKonfirmasi("Apakah kamu benar ingin menghapus review ini? (Y/N): ");
                if (konfirmasi) {
                    reviewDAO.hapusReview(hapusReview);
                    System.out.println("\nYeayy, review berhasil dihapus!");
                    lihatFilm();
                } else {
                    System.out.println("Review kamu batal dihapus!");
                }
                return;
            }
        }
        if (!ketemu){
            System.out.println("ID Review tidak ditemukan!");
        }
    }
}