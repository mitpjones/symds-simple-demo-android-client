package demo.simple.com.symdssimpledemo.model;


import android.util.Log;

import java.io.Serializable;
import java.util.Date;


public class Demo implements Serializable {

    final long serialVersionUID = 42L;

    private String id;

    private String text;
    
    private String active;

    private Date updateDateTime;

    private int version;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public void setIsActive(Boolean active) {

        Log.d(Demo.class.getName(), "setIsActive() - active =<" + active + ">");

        if (active != null) {

            if (active) {

                this.active = "Y";

            } else {

                this.active = "N";

            }

        }

        Log.d(Demo.class.getName(), "setIsActive() - this.active =<" + this.active + ">");

    }


    public Boolean getIsActive() {

        if (active != null) {

            if (active.equals("Y")) {

                return true;

            } else {

                return false;

            }

        }
        else {

            return false;

        }


    }


    public Date getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(Date updateDateTime) {
        this.updateDateTime = updateDateTime;
    }


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Demo demo = (Demo) o;

        return id.equals(demo.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


    @Override
    public String toString() {
        return "Demo{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", active='" + active + '\'' +
                ", updateDateTime=" + updateDateTime +
                ", version=" + version +
                '}';
    }
}
