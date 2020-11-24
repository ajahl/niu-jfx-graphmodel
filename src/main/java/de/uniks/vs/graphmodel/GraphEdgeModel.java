package de.uniks.vs.graphmodel;

import java.beans.PropertyChangeEvent;

/**
 * Created by alex on 16/6/8.
 */
public class GraphEdgeModel extends GraphItemModel {

    /* -------- target node -------- */
    GraphNodeModel target;

    public GraphEdgeModel withTarget(GraphNodeModel target) {
        if (this.target == target)
            return this;

        propertyChange(new PropertyChangeEvent(this, "newTarget", this.target, target));

//        if (this.target != null)
//        	this.target.removeFromInEdges(this);

        this.target = target;

        if (this.target != null)
            this.target.addToInEdges(this);

        return this;
    }

    public GraphNodeModel getTarget() {
        return target;
    }


    @Override
    public GraphEdgeModel withStatus(String status) {
        super.withStatus(status);
        return this;
    }

    /* -------- source node -------- */
    GraphNodeModel source;

    public GraphEdgeModel withSource(GraphNodeModel source) {
        if (this.source == source)
            return this;

        listeners.firePropertyChange("<-", this.source, source);
        propertyChange(new PropertyChangeEvent(this, "newSource", this.source, source));


//        if (this.source != null)
//        	this.source.removeFromOutEdges(this);

        this.source = source;

        if (this.source != null)
            this.source.addToOutEdges(this);

        return this;
    }

    public GraphNodeModel getSource() {
        return source;
    }

    @Override
    public void removeThis() {
        if (this.target != null)
            this.target.removeFromInEdges(this);
        this.target = null;

        if ( this.source != null)
            this.source.removeFromOutEdges(this);
        this.source = null;
    }

    @Override
    public String getCreationCode(CodeGeneratorTools.Syntax syntax) {
        StringBuilder stringBuilder = new StringBuilder();

        long sourceID = getSource().getGraphItemModelID();
        long targetID = getTarget().getGraphItemModelID();

        switch (syntax) {
            case JAVA:
                stringBuilder.append("        modelManager.createGraphEdge(" + sourceID + ", " + targetID + ", #modelID#);\n");
                break;
            case JSON:
                stringBuilder.append( "\""+ CodeGeneratorTools.getNewID()+"\": { \n"
                        +  "\"id\": \""+ getGraphItemModelID()+"\", \n"
                        +  "\"source\": \""+sourceID+"\", \n"
                        +  "\"target\": \""+targetID+"\", \n"
                        +  "\"status\": \""+getStatus()+"\", \n"
                        +  "\"class\": \""+this.getClass()+"\" \n"
                        + "},\n");
                break;
        }

        return stringBuilder.toString();
    }

    @Override
    public GraphEdgeModel clone() {
        GraphEdgeModel edge = new GraphEdgeModel()
                .withStatus(status)
                .withId(graphItemModelID);

        return edge;
    }

    @Override
    public GraphEdgeModel withId(long id) {
        setGraphItemModelID( id);
//    	this.id = id;
        return this;
    }

    public void setStartX(double startX) {

    }
    public void setStartY(double startY) {

    }
    public void setEndX(double endX) {

    }
    public void setEndY(double endY) {

    }
}
