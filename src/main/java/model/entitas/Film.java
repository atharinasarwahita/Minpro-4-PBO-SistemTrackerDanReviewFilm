package model.entitas;
import java.time.Year;

public abstract class Film implements Displayable {
    protected final int idFilm;
    protected String judul;
    protected String sutradara;
    protected int tahunRilis;
    protected String genre;
    protected int durasi;
    
    public Film(final int idFilm, String judul, String sutradara, int tahunRilis, String genre, int durasi) {
        this.idFilm = idFilm;
        this.judul = judul;
        this.sutradara = sutradara;
        setTahunRilis(tahunRilis);
        this.genre = genre;
        setDurasi(durasi);
    }
    
    public final int getIdFilm() {
        return idFilm;
    }
    
    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        if (judul != null && !judul.trim().isEmpty()) {
            this.judul = judul;
        }
    }
    
    public String getSutradara() {
        return sutradara;
    }

    public void setSutradara(String sutradara) {
        if (sutradara != null && !sutradara.trim().isEmpty()) {
            this.sutradara = sutradara;
        }
    }
    
    public int getTahunRilis() {
        return tahunRilis;
    }

    public boolean setTahunRilis(int tahunRilis) {
        int tahunSekarang = Year.now().getValue();
        if (tahunRilis >= 1888 && tahunRilis <= tahunSekarang) {
            this.tahunRilis = tahunRilis;
            return true;
        } else {
            System.out.println("Tahun rilis harus antara 1888 hingga tahun " + tahunSekarang + ".");
            //minimal tahun 1888 adalah tahun dimana film pertama kali dibuat
            return false;
        }
    }
    
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDurasi() {
        return durasi;
    }
    
    public boolean setDurasi(int durasi) {
        if (durasi >= 30) {
            this.durasi = durasi;
            return true;
        } else {
            System.out.println("Durasi film harus minimal 30 menit.");
            //minimal durasi 30 menit sesuai dengan standar film pendek
            return false;
        }
    }
    
    //Overloading tanpa paramter
    @Override
    public void tampilkanInfo() {
        tampilkanInfo(false);
    }

    //Overlaoding dengan parameter + implementasi dari interface Displayable
    @Override
    public void tampilkanInfo(boolean ringkas) {
        if (ringkas) {
            System.out.println("[" + idFilm + "] " + judul + " (" + tahunRilis + ") - " + genre);
        } else {
            System.out.println(" [" + idFilm + "] " + judul + " (" + tahunRilis + ")");
            System.out.println("     Sutradara : " + sutradara);
            System.out.println("     Genre     : " + genre + " | Durasi: " + durasi + " menit");
            tampilkanDetailSpesifik(); 
        }
    }

    // abstract method yang wajib diimplementasikan oleh subclass
    public abstract void tampilkanDetailSpesifik();
}

