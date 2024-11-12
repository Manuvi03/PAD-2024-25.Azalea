package es.ucm.fdi.azalea.business.model;

// estoy probando el firebase database (REALMENTE ESTO ES UN TRANSFER)
public class Subject {
    private String name;

    public Subject() {
        // Consutructor estandar necesitado para las llamadas a DataSnapshot.getValue(Subject.class)
        // permite que Firebase Database trabaje con esta clase y sus atributos (que tienen getters
        // publicos) directamente
    }

    public Subject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
