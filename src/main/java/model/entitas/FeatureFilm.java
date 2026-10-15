package model.entitas;

public class FeatureFilm extends Film {
    private String pemeranUtama;
    private String karakterLiveAction;

    public FeatureFilm(final int idFilm, String judul, String sutradara, int tahunRilis, String genre, int durasi, String pemeranUtama, String karakterLiveAction) {
        super(idFilm, judul, sutradara, tahunRilis, genre, durasi);
        this.pemeranUtama = pemeranUtama;
        this.karakterLiveAction = karakterLiveAction;
    }
    
    public String getPemeranUtama() {
        return pemeranUtama;
    }

    public void setPemeranUtama(String pemeranUtama) {
        this.pemeranUtama = pemeranUtama;
    }

    public String getKarakterLiveAction() {
        return karakterLiveAction;
    }

    public void setKarakterLiveAction(String karakterLiveAction) {
        this.karakterLiveAction = karakterLiveAction;
    }

    //metode Polymorphism yaitu Override dari method di superclass
    @Override
    public void tampilkanDetailSpesifik() {
        System.out.println("     Cast      : " + pemeranUtama + " as " + karakterLiveAction);
        System.out.println("     Tipe      : Feature Film (Live-Action)");
        System.out.println("------------------------------------------------------------------");
    }
}
