package com.example.stressApp.Model;

import java.util.List;

public class YogaModel {
    private String name;
    private String info;
    private String stretchedPart;
    private String help;
    private List<String> howToDo;
    private List<String> tips;
    private String path;

    public YogaModel() {}

    public YogaModel(String name, String info, String stretchedPart, String help, List<String> howToDo, List<String> tips, String path) {
        this.name = name;
        this.info = info;
        this.stretchedPart = stretchedPart;
        this.help = help;
        this.howToDo = howToDo;
        this.tips = tips;
        this.path = path;
    }

    // Getters and setters for all fields
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getInfo() { return info; }
    public void setInfo(String info) { this.info = info; }

    public String getStretchedPart() { return stretchedPart; }
    public void setStretchedPart(String stretchedPart) { this.stretchedPart = stretchedPart; }

    public String getHelp() { return help; }
    public void setHelp(String help) { this.help = help; }

    public List<String> getHowToDo() { return howToDo; }
    public void setHowToDo(List<String> howToDo) { this.howToDo = howToDo; }

    public List<String> getTips() { return tips; }
    public void setTips(List<String> tips) { this.tips = tips; }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

