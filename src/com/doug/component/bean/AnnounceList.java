package com.doug.component.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AnnounceList {

    private List<Announce> announces;

    public AnnounceList(JSONArray array) throws JSONException {
        super();
        announces = new ArrayList<Announce>();
        int len = array.length();
        for (int i = 0; i < len; i++) {
            Announce a = new Announce();
            JSONObject o = (JSONObject) array.get(i);
            a.setTitle(o.getString("title"));
            a.setContent(o.getString("content"));
            a.setDateline(o.getLong("dateline"));
            announces.add(a);
        }
    }

    public AnnounceList(List<Announce> la, JSONArray array) throws JSONException {
        super();
        announces = la;
        int len = array.length();
        for (int i = 0; i < len; i++) {
            Announce a = new Announce();
            JSONObject o = (JSONObject) array.get(i);
            a.setTitle(o.getString("title"));
            a.setContent(o.getString("content"));
            a.setDateline(o.getLong("dateline"));
            announces.add(a);
        }
    }

    public List<Announce> getAnnounces() {
        return announces;
    }

    public void setAnnounces(List<Announce> announces) {
        this.announces = announces;
    }

    @Override
    public String toString() {
        int len = announces.size();
        String st = "";
        for (int i = 0; i < len; i++) {
            Announce a = announces.get(i);
            st += " /**" + i + a.toString();
        }
        return st;
    }

}
