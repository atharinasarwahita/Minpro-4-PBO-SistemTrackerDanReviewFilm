package model.entitas;

public class AnimatedFilm extends Film {
    private String pengisiSuara;
    private String karakterAnimasi;
    private String gayaAnimasi;

    public AnimatedFilm(final int idFilm, String judul, String sutradara, int tahunRilis, String genre, int durasi, String pengisiSuara, String karakterAnimasi, String gayaAnimasi) {
        super(idFilm, judul, sutradara, tahunRilis, genre, durasi);
        this.pengisiSuara = pengisiSuara;
        this.karakterAnimasi = karakterAnimasi;
        this.gayaAnimasi = gayaAnimasi;
    }

    public String getPengisiSuara() {
        return pengisiSuara;
    }

    public void setPengisiSuara(String pengisiSuara) {
        this.pengisiSuara = pengisiSuara;
    }

    public String getKarakterAnimasi() {
        return karakterAnimasi;
    }

    public void setKarakterAnimasi(String karakterAnimasi) {
        this.karakterAnimasi = karakterAnimasi;
    }

    public String getGayaAnimasi() {
        return gayaAnimasi;
    }

    public void setGayaAnimasi(String gayaAnimasi) {
        this.gayaAnimasi = gayaAnimasi;
    }
    
    //metode Polymorphism yaitu Override
    @Override
    public void tampilkanDetailSpesifik() {
        System.out.println("     Voice     : " + pengisiSuara + " as " + karakterAnimasi);
        System.out.println("     Tipe      : Animated Film (" + gayaAnimasi + ")");
        System.out.println("------------------------------------------------------------------");
    }
}