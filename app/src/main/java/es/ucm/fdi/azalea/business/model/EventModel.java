package es.ucm.fdi.azalea.business.model;

public class EventModel {

    private String date; //YYYY-MM-DD
    private String title;
    private String description;
    private String time;
    private String location;
    private String idClass;
    private String id;

    EventModel(){}

    EventModel(String date, String title, String description, String time,
               String location, String idClass, String id){
        this.date = date;
        this.title = title;
        this.description = description;
        this.time = time;
        this.location = location;
        this.idClass = idClass;
        this.id = id;
    }

    public String getTitle(){
        return this.title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    public String getIdClass(){ return idClass; }

    public String getId(){return id;}

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIdClass(String idClass){this.idClass = idClass;}

    public void setId(String id){this.id = id;}
}
