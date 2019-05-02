package com.example.inigo.aplicaciomusicainigoreal;

public class UploadElement {

    private String urlUpload;
    private String songUrl;
    private String imageUrl;
    private String autor;
    private String name;

    public UploadElement(){

    }

    public UploadElement(String urlUpload, String songUrl, String imageUrl, String autor, String name) {
        this.urlUpload = urlUpload;
        this.songUrl = songUrl;
        this.imageUrl = imageUrl;
        this.autor = autor;
        this.name = name;
    }

    public String getUrlUpload() {
        return urlUpload;
    }

    public void setUrlUpload(String urlUpload) {
        this.urlUpload = urlUpload;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
