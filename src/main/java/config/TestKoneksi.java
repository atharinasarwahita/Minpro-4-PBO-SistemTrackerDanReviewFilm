package config;

import java.sql.Connection;

public class TestKoneksi {
    public static void main(String[] args) {
        Connection conn = Koneksi.getConnection();
        
        if(conn != null){
            System.out.println("Koneksi Berhasil");
        }else{
            System.out.println("Gagal");
        }
    }
}
