package es.ucm.fdi.azalea.business.model;

public class TokenModel {

    private String id;
    private String idAssociated;

    public TokenModel(){}

    public TokenModel(String token){
        this.id = token;
        this.idAssociated = "";
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getIdAssociated() {
        return idAssociated;
    }

    public void setIdAssociated(String idAssociated) {
        this.idAssociated = idAssociated;
    }
}
