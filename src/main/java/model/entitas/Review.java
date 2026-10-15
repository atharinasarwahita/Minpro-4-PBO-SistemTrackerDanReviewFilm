package model.entitas;

public class Review implements Displayable{
    private final int idReview;
    private final int idFilm;
    private double rating;
    private String ulasan;
    private final boolean isRewatch;
    
    //Constructor
    public Review(int idReview, int idFilm, double rating, String ulasan, boolean isRewatch) {
        this.idReview = idReview;
        this.idFilm = idFilm;
        setRating(rating);
        this.ulasan = ulasan;
        this.isRewatch = isRewatch;
    }

    //Overloading constructor (rewatch false)
    public Review(int idReview, int idFilm, double rating, String ulasan) {
        this(idReview, idFilm, rating, ulasan, false);
    }
        
    public int getIdReview() {
        return idReview; 
    }
    
    public int getIdFilm() {
        return idFilm;
    }
    
    public String getUlasan() {
        return ulasan; 
    }
    
    public void setUlasan(String ulasan) {
        this.ulasan = ulasan;
    }
    
    public double getRating() {
        return rating; 
    }
    
    public boolean setRating(double rating) {
    if (rating >= 1.0 && rating <= 5.0) {
        this.rating = rating;
        return true;
    } else {
        System.out.println("Rating harus antara 1.0 sampai 5.0!");
        return false;
      }
    }
    
    public boolean isRewatch(){
        return isRewatch;
    }
    
    //Overloading tanpa parameter
    @Override
    public void tampilkanInfo() {
        tampilkanInfo(false);
    }
    
    // Overloading dengan parameter sekaligus implementasi dari interface Displayable
    @Override
    public void tampilkanInfo(boolean ringkas) {
            if (ringkas) {
                String tag = isRewatch ? " (Rewatch)" : "";
                System.out.println("   > Review " + idReview + tag + " | Rating: " + getRating() + "/5.0");
            } else {
                    if (isRewatch) {
                        System.out.println("     -------------------------------------------------------------");
                        System.out.println("     [REWATCH] [ID Review: " + idReview + "]");
                    } else {
                        System.out.println("     [ID Review: " + idReview + "]");
                    }
                    System.out.println("     Rating : " + getRating());
                    System.out.println("     Ulasan : " + ulasan);
                    }
    }
}