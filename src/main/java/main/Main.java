package main;

import controller.*;
import config.Koneksi;
import view.FilmView;

public class Main {
    public static void main(String[] args) {
        
        if (Koneksi.getConnection() == null) {
            System.out.println("Program tidak bisa dijalankan karena database belum terhubung. Pastikan MySQL sudah aktif.");
            return;
        }
        FilmController controller = new FilmController();
        FilmView view = new FilmView();

        while (true) {
            view.tampilkanMenuUtama();
            int pilihan = ValidasiInput.inputAngka("Pilih menu (1-6): ");
            
            switch (pilihan) {
                case 1 -> controller.tambahFilm();
                case 2 -> controller.tambahReview();
                case 3 -> controller.lihatFilm();
                case 4 -> controller.updateReview();
                case 5 -> controller.hapusReview();
                case 6 -> {
                    Koneksi.tutupKoneksi(); 
                    System.out.println("Terima kasih telah menggunakan sistem. Sampai jumpa!");
                    return;
                }
                default -> System.out.println("Menu yang kamu pilih tidak tersedia, masukkan angka 1-6.");
            }
        }
    }
}