package de.uniks.vs.graphmodel;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by alex on 16/6/8.
 */
public class GraphNodeModel extends GraphItemModel {

    private String type;
    private String name;
    private int order;

    public GraphNodeModel withType(String type){
        this.type = type;
        return this;
    }

    public GraphNodeModel withName(String name){
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    /* --------  incomming edges -------- */
//    ArrayList<GraphEdge> inEdges = new ArrayList<>();
    CopyOnWriteArrayList<GraphEdgeModel> inEdges = new CopyOnWriteArrayList<>();

    public <T> void addToInEdges(GraphEdgeModel edge) {
        if (inEdges.contains(edge))
            return;

        inEdges.add(edge);

        propertyChange(new PropertyChangeEvent(this, "addInEdge", null, edge));
        edge.withTarget(this);
    }

    public void removeFromInEdges(GraphEdgeModel edge) {
        if (!inEdges.contains(edge))
            return;

        edge.withTarget(null);

        propertyChange(new PropertyChangeEvent(this, "removeInEdge", edge, null));
        inEdges.remove(edge);
    }

    public CopyOnWriteArrayList<GraphEdgeModel> getInEdges() {
        return inEdges;
    }


    /* --------  outgoing edges -------- */
//    ArrayList<GraphEdge> outEdges = new ArrayList<>();
    CopyOnWriteArrayList<GraphEdgeModel> outEdges = new CopyOnWriteArrayList<>();

    public <T> void addToOutEdges(GraphEdgeModel edge) {
        if (outEdges.contains(edge))
            return;

        outEdges.add(edge);

        propertyChange(new PropertyChangeEvent(this, "addOutEdge", null, edge));
        edge.withSource(this);
    }

    public void removeFromOutEdges(GraphEdgeModel edge) {
        if (!outEdges.contains(edge))
            return;

        edge.withSource(null);
        propertyChange(new PropertyChangeEvent(this, "removeOutEdge", edge, null));

        outEdges.remove(edge);

    }

    public CopyOnWriteArrayList<GraphEdgeModel> getOutEdges() {
        return outEdges;
    }

    @Override
    public void removeThis() {
        status = GraphItemModel.DELETED;
//        for (GraphEdge edge : inEdges ) {
//            edge.removeYou();
//        }
//
//        for (GraphEdge edge : outEdges ) {
//            edge.removeYou();
//        }
    }

    @Override
    public String getCreationCode(CodeGeneratorTools.Syntax syntax) {
        StringBuilder stringBuilder = new StringBuilder();
        switch (syntax) {
            case JAVA:
                stringBuilder.append("        modelManager.createGraphNote(\"" + type + "\", \"" + name + "\", #modelID#);\n");
                break;
            case JSON:
                stringBuilder.append( "\""+CodeGeneratorTools.getNewID()+"\": { \n"
                        +  "\"id\": \""+ getGraphItemModelID()+"\", \n"
                        +  "\"name\": \""+getName()+"\", \n"
                        +  "\"status\": \""+getStatus()+"\", \n"
                        +  "\"type\": \""+getType().toString()+"\", \n"
                        +  "\"class\": \""+this.getClass()+"\" \n"
                        + "},\n");
                break;
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        String s = getNodeName();
        s+="\n";
        for (GraphEdgeModel edge: getOutEdges()) {

            if (edge.getTarget() != null)
                s += edge.getTarget().toString();
        }
        return s;
    }

    public String getNodeName() {
        String s = "<(" + type + ", " + name + "), " + graphItemModelID + ">";
        return s;
    }

    public ArrayList<GraphNodeModel> getNext() {
        ArrayList<GraphNodeModel> nodes = new ArrayList<>();
        for (GraphEdgeModel graphEdge: getOutEdges()) {
            nodes.add( graphEdge.getTarget());
        }
        return nodes;
    }

    @Override
    public GraphNodeModel withStatus(String status) {
        super.withStatus(status);
        return this;
    }

    @Override
    public GraphNodeModel clone() {
        GraphNodeModel node = new GraphNodeModel()
                .withName(name)
                .withStatus(status)
                .withType(type)
                .withId(graphItemModelID);
        return node;
    }

    @Override
    public GraphNodeModel withId(long id) {
        setGraphItemModelID(id);
//    	this.id = id;
        return this;
    }

    public void setDrawOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public void setX(double x) {

    }

    public void setY(double x) {

    }

    public double getWidth() {
        return 0;
    }

    public double getHeight() {
        return 0;
    }
}

