package de.uniks.vs.graphmodel;

import java.util.ArrayList;

public class GraphGroupModel extends GraphItemModel {

    @Override
    public void removeThis() {

    }

    @Override
    public GraphItemModel clone() {
        return null;
    }

    @Override
    public String getCreationCode(CodeGeneratorTools.Syntax syntax) {
        return null;
    }

    @Override
    public GraphItemModel withId(long id) {
        return null;
    }

    public double getHeight() {
        return 0;
    }

    public double getWidth() {
        return 0;
    }

    public ArrayList<GraphItemModel> getChildren() {
        return null;
    }
}
