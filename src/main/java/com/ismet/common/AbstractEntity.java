package com.ismet.common;

import com.google.gson.annotations.SerializedName;

public abstract class AbstractEntity {

    @SerializedName("id") //since each POJO has id, we can abstract it away in this class
    private String id;

    public abstract String validate(); // used to validate json request payload for missing/invalid fields

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractEntity other = (AbstractEntity) obj;
        return !(this.id == null || other.id == null || !this.id.equals(other.id));
    }

    public  String generateAlias(String alias){
        return alias!=null && !alias.isEmpty() ? (alias.contains(".") ? alias : alias + ".") : "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
