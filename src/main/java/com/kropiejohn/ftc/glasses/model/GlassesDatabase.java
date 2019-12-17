package com.kropiejohn.ftc.glasses.model;

import com.kropiejohn.ftc.glasses.FtcGlasses;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public enum GlassesDatabase {
    INSTANCE(FXCollections.observableHashMap());

    final MapChangeListener<Integer, Glasses> listener = change -> save();

    public int getNewIndex() {
        return db.size() + 1;
    }

    ObservableMap<Integer, Glasses> db;

    GlassesDatabase(ObservableMap<Integer, Glasses> db) {
        this.db = db;

        restore();
        db.addListener(listener);
    }

    public ObservableMap<Integer, Glasses> get() {
        return db;
    }

    public void replaceContents(List<Glasses> toAdd) {
        db.removeListener(listener);
        db.clear();
        toAdd.forEach(glasses -> db.put(glasses.getNumber(), glasses));
        save();
        db.addListener(listener);
    }

    private void save() {
        var glassesList = new JSONArray();
        //noinspection unchecked
        db.values().forEach(v -> glassesList.add(getGlassesJsonObj(v)));

        try (var writer = new FileWriter(FtcGlasses.class.getResource("database/GlassesDatabase.json").getFile())) {
            writer.write(glassesList.toJSONString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restore() {
        var jsonParse = new JSONParser();
        try (var reader = new FileReader(FtcGlasses.class.getResource("database/GlassesDatabase.json").getFile())) {
            var obj = jsonParse.parse(reader);
            var glassesList = (JSONArray) obj;
            for (Object glassesJson : glassesList) {
                var glasses = restoreGlasses((JSONObject) glassesJson);
                db.put(glasses.getNumber(), glasses);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private Glasses restoreGlasses(JSONObject glassesJson) {
        var glasses = new Glasses();

        glasses.setRightSphere((Double) glassesJson.get("rightSphere"));
        glasses.setRightCylinder((Double) glassesJson.get("rightCylinder"));
        glasses.setRightAxis(Math.toIntExact((Long) glassesJson.get("rightAxis")));
        glasses.setRightFocal(Math.toIntExact((Long) glassesJson.get("rightFocal")));

        glasses.setLeftSphere((Double) glassesJson.get("leftSphere"));
        glasses.setLeftCylinder((Double) glassesJson.get("leftCylinder"));
        glasses.setLeftAxis(Math.toIntExact((Long) glassesJson.get("leftAxis")));
        glasses.setLeftFocal(Math.toIntExact((Long) glassesJson.get("leftFocal")));

        glasses.setGender(Gender.valueOf((String) glassesJson.get("sex")));
        glasses.setAge(Age.valueOf((String) glassesJson.get("age")));
        glasses.setBifocals(YesNo.valueOf((String) glassesJson.get("bifocals")));

        glasses.setNumber(Math.toIntExact((Long) glassesJson.get("number")));
        glasses.setEntryDate(new Date((Long) glassesJson.get("entryDate")));
        glasses.setRemoved((Boolean) glassesJson.get("removed"));

        return glasses;
    }

    @SuppressWarnings("unchecked")
    private JSONObject getGlassesJsonObj(final Glasses glasses) {
        var glassesJson = new JSONObject();
        glassesJson.put("rightSphere", glasses.getRightSphere());
        glassesJson.put("rightCylinder", glasses.getRightCylinder());
        glassesJson.put("rightAxis", glasses.getRightAxis());
        glassesJson.put("rightFocal", glasses.getRightFocal());

        glassesJson.put("leftSphere", glasses.getLeftSphere());
        glassesJson.put("leftCylinder", glasses.getLeftCylinder());
        glassesJson.put("leftAxis", glasses.getLeftAxis());
        glassesJson.put("leftFocal", glasses.getLeftFocal());

        glassesJson.put("sex", glasses.getGender().name());
        glassesJson.put("age", glasses.getAge().name());
        glassesJson.put("bifocals", glasses.getBifocals().name());

        glassesJson.put("number", glasses.getNumber());
        glassesJson.put("entryDate", glasses.getEntryDate().getTime());
        glassesJson.put("removed", glasses.isRemoved());

        return glassesJson;
    }
}
