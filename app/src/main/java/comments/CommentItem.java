package comments;

public class CommentItem {

    private String tresc;
    private String autor;
    private Integer ocena;

    public CommentItem(String tresc, String autor, Integer ocena){
        this.tresc = tresc;
        this.autor = autor;
        this.ocena = ocena;
    }

    public String getTresc() {
        return tresc;
    }

    public String getAutor() {
        return autor;
    }

    public Integer getOcena() { return ocena; }
}
