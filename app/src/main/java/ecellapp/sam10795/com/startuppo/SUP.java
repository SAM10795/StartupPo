package ecellapp.sam10795.com.startuppo;

import java.util.Date;

import pl.droidsonroids.gif.GifDrawable;

/**
 * Created by SAM10795 on 17-09-2015.
 */
public class SUP {
    private String name;
    private int id;
    private int lv;
    private String zone;
    private boolean traded;
    private boolean legend;
    private String type;
    private int bag;
    private String foundate;
    private String evaluation;
    private String info;
    private String founders;
    private String funding;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public void setLegend(boolean legend) {
        this.legend = legend;
    }

    public void setTraded(boolean traded) {
        this.traded = traded;
    }

    public int getBag() {
        return bag;
    }

    public String getFoundate() {
        return foundate;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public String getFounders() {
        return founders;
    }

    public String getFunding() {
        return funding;
    }

    public String getInfo() {
        return info;
    }

    public void setBag(int bag) {
        this.bag = bag;
    }

    public void setFoundate(String date) {
        this.foundate = date;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public void setFounders(String founders) {
        this.founders = founders;
    }

    public void setFunding(String funding) {
        this.funding = funding;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public int getLv() {
        return lv;
    }

    public String getName() {
        return name;
    }

    public boolean isTraded() {
        return traded;
    }

    public String getZone() {
        return zone;
    }

    public boolean isLegend() {
        return legend;
    }

    public String getType() {
        return type;
    }
}
