package de.uniks.vs.graphmodel;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by alex on 16/6/10.
 */
public class GraphModel extends GraphItemModel {

    public enum Type {
        PG("PG"),
        DG("DG"),
        PATTERN("PATTERN"),
        RULE("RULE"),
        UNDEFINED("UNDEFINED");

        private final String stringValue;

        private Type(String value) {
            stringValue = value;
        }

        public String getStringValue() {
            return stringValue;
        }

        public Type getEnumFromString (String s) {
            return Type.valueOf(s.toUpperCase(Locale.ENGLISH));
        }
    }

    private long uuid_counter;
    private long uuid_edgecounter;
    private Type type;
    private String name;

    /* --------  graphitems -------- */
//    ArrayList<GraphItem> graphItems = new ArrayList<>();
    CopyOnWriteArrayList<GraphItemModel> graphItems = new CopyOnWriteArrayList<>();

    public GraphModel() {
        uuid_edgecounter = 0;
        uuid_counter = 0;
        type = Type.UNDEFINED;
    }

    public GraphModel withUuidEdgecounter(long counter) {
        uuid_edgecounter = counter;
        return this;
    }


    public GraphModel withUuidCounter(long counter) {
        uuid_counter = counter;
        return this;
    }

    public GraphModel withType(Type type) {
        this.type = type;
        return this;
    }

    public Type getType() {
        return type;
    }

    public void removeFromGraphItems(GraphItemModel item) {
        graphItems.remove(item);
        item.removeYou();
        propertyChange(new PropertyChangeEvent(this, "deleteGraphItem", item, null));

    }

    public <T> void addToGraphItems(GraphItemModel item) {
        if (graphItems.contains(item))
            return;

        item.setModel(this);
        item.addPropertyChangeListener(this);
        graphItems.add(item);
        propertyChange(new PropertyChangeEvent(this, "newGraphItem", null, item));
    }

    //    public ArrayList<GraphItem> getGraphItems() {
//        return graphItems;
//    }
    public CopyOnWriteArrayList<GraphItemModel> getGraphItems() {
        return graphItems;
    }


    @Override
    public void removeThis() { }

    @Override
    public GraphModel clone() {

        GraphModel model = new GraphModel()
                .withUuidCounter(uuid_counter)
                .withUuidEdgecounter(uuid_edgecounter)
                .withName(name);

        // all nodes
        for (GraphItemModel item : graphItems) {

            if (item instanceof GraphNodeModel) {
                GraphItemModel clone = item.clone();
                model.addToGraphItems(clone);
            }
        }

        // all edges
        for (GraphItemModel item : graphItems) {

            if (item instanceof GraphEdgeModel) {
                GraphEdgeModel clone = (GraphEdgeModel)item.clone();

                long sourceID = ((GraphEdgeModel) item).getSource().getGraphItemModelID();
                GraphNodeModel source = (GraphNodeModel)model.getGraphItemWithID(sourceID);

                long targetID = ((GraphEdgeModel) item).getTarget().getGraphItemModelID();
                GraphNodeModel target = (GraphNodeModel)model.getGraphItemWithID(targetID);

                clone.withSource(source);
                clone.withTarget(target);

                model.addToGraphItems(clone);
            }
        }

        return model;
    }

    private GraphModel withName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getCreationCode(CodeGeneratorTools.Syntax syntax) {
        StringBuilder stringBuilder = new StringBuilder();

        switch (syntax) {
            case JAVA:
                stringBuilder.append("        modelManager.addGraphModel(new de.uniks.vs.evolution.graphmodels.GraphModel(), #modelID#);\n");

                for (GraphNodeModel graphNode : getNodes()) {
                    stringBuilder.append(graphNode.getCreationCode(syntax));
                }

                for (GraphEdgeModel graphEdge : getEdges()) {
                    stringBuilder.append(graphEdge.getCreationCode(syntax));
                }
                break;

            case JSON:
                if (this.getGraphModel() == null)
                    stringBuilder.append( "{");

                stringBuilder.append( "\""+ CodeGeneratorTools.getNewID()+"\": { \n"
                        +  "\"id\": \""+ getGraphItemModelID()+"\", \n"
                        +  "\"name\": \""+getName()+"\", \n"
                        +  "\"status\": \""+getStatus()+"\", \n"
                        +  "\"type\": \""+getType().toString()+"\", \n"
                        +  "\"class\": \""+this.getClass()+"\", \n"
                        +   "\"items\": {\n");
//               + "},\n");

                for (GraphNodeModel graphNode : getNodes()) {
                    stringBuilder.append(graphNode.getCreationCode(syntax));
                }

                for (GraphEdgeModel graphEdge : getEdges()) {
                    stringBuilder.append(graphEdge.getCreationCode(syntax));
                }

                for (GraphModel graphModel : getModels()) {
                    stringBuilder.append(graphModel.getCreationCode(syntax));
                    stringBuilder.append( "},\n");
                }


                stringBuilder.replace(stringBuilder.lastIndexOf(","), stringBuilder.lastIndexOf(",") + 1, "");
                stringBuilder.append( "}\n");

                if (this.getGraphModel() == null) {
//                   stringBuilder.replace(stringBuilder.lastIndexOf(","), stringBuilder.lastIndexOf(",") + 1, "}");
                    stringBuilder.append( "}\n");

//                   if (getModels().size() == 0) {
                    stringBuilder.append( "}\n");
//                   }
                }
                break;
        }

        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        String s = this.getClass().getSimpleName();
        s+="\n";

        for (GraphItemModel graphItem: graphItems) {

            if (graphItem instanceof GraphNodeModel)
                s += graphItem.getGraphItemModelID() + " " + ((GraphNodeModel)graphItem).getType() + " " + ((GraphNodeModel)graphItem).getName() + " " + graphItem.getStatus() +  "\n";
            else if (graphItem instanceof GraphEdgeModel)
                s += graphItem.getGraphItemModelID() + " " + ((GraphEdgeModel)graphItem).getSource().getGraphItemModelID() + " " + ((GraphEdgeModel)graphItem).getTarget().getGraphItemModelID() + " " + graphItem.getStatus() +  "\n";
        }
        return s;
    }

    public long addNode(GraphNodeModel graphNode) {
        graphNode.setGraphItemModelID(uuid_counter++);
        graphNode.setModel(this);
        this.addToGraphItems(graphNode);

        getPropertyChangeSupport().firePropertyChange("addNode", null, graphNode);
        return graphNode.getGraphItemModelID();
    }

    public GraphItemModel getGraphItemWithID(String id) {

        if (id.matches(".*\\D.*")) {
            int indexOf = id.lastIndexOf("_");
            String[] split = new String[] {id.substring(0, indexOf), id.substring(indexOf+1)};
            return findItem(split);
        }
        else
            return getGraphItemWithID(Long.valueOf(id));
    }

    public GraphItemModel getGraphItemwithIDEndsWith(String string) {
        synchronized (graphItems){
            for (GraphItemModel graphItem : graphItems) {

                if (graphItem instanceof GraphNodeModel && ((GraphNodeModel) graphItem).getName().equals(string))
                    return graphItem;
            }
        }
        System.err.println("GraphModel::getGraphItemWithID Item not find "+ string);
        return null;
//
//
//        if (id.matches(".*\\D.*")) {
//            int indexOf = id.lastIndexOf("_");
//            String[] split = new String[] {id.substring(0, indexOf), id.substring(indexOf+1)};
//            return findItem(split);
//        }
//        else
//            return getGraphItemWithID(Long.valueOf(id));
    }


    private GraphItemModel findItem(String[] split) {

        if (this.getName().endsWith(split[0])) {
            return getGraphItemWithID(Long.valueOf(split[1]));
        }
        else {
            for (GraphModel model : getModels()) {
                GraphItemModel item = model.findItem(split);

                if (item != null)
                    return item;
            }
        }
        return null;
    }


    public GraphItemModel getGraphItemWithID(long itemId) {

        for (GraphItemModel graphItem : graphItems) {

            if ( graphItem.getGraphItemModelID() == itemId)
                return graphItem;
        }
        return null;
    }

    public long addEdge(GraphEdgeModel graphEdge) {
//        graphEdge.setId(uuid_edgecounter++);
        graphEdge.setGraphItemModelID(uuid_counter++);
        graphEdge.setModel(this);
        this.addToGraphItems(graphEdge);
        return graphEdge.getGraphItemModelID();
    }

    public ArrayList<GraphEdgeModel> getEdges(){
        return getGraphItemsByClass(GraphEdgeModel.class);
    }

    public ArrayList<GraphNodeModel> getNodes(){ return getGraphItemsByClass(GraphNodeModel.class); }

    public ArrayList<GraphModel> getModels(){
        return getGraphItemsByClass(GraphModel.class);
    }

    public <T> ArrayList<T> getGraphItemsByClass(Class<T> clazz){
        ArrayList<T> items = new ArrayList<>();

        for (GraphItemModel graphItem:graphItems) {

            if(clazz.isInstance(graphItem))
                items.add((T) graphItem);
        }

        return items;
    }

    public GraphNodeModel getNode(long sourceID) {

        for (GraphNodeModel graphNode: getNodes()) {

            if(graphNode.getGraphItemModelID() == sourceID)
                return graphNode;
        }
        return null;
    }

    public ArrayList<GraphNodeModel> getNodesByType(String type) {
        ArrayList<GraphNodeModel> nodes = new ArrayList<>();
        ArrayList<GraphNodeModel> graphNodes = getNodes();

        for (GraphNodeModel node: graphNodes) {

            if (node.getType().equals(type))
                nodes.add(node);
        }
        return nodes;
    }


    @Override
    public GraphModel withId(long id) {
        this.graphItemModelID = id;
        return this;
    }

    public long createUUID() {
        return uuid_counter++;
    }


    private String print(GraphNodeModel graphNode, String prefix, boolean isTail) { //, GraphModel model, HashMap<String, String> corespondenceNodes) {

        String s = prefix + (!prefix.isEmpty() ? (isTail ? "└── " : "├── ") : "") + /*corespondent +*/ "(" + graphNode.getName() + ")"  + "\n";

        for (GraphEdgeModel graphEdge : graphNode.getOutEdges()) {
            s += print(graphEdge.getTarget(), prefix + (isTail ? "    " : "│   "), false);
        }

        if (graphNode.getOutEdges().size() > 0) {
            s += print(graphNode.getOutEdges().get(graphNode.getOutEdges().size() - 1).getTarget(), prefix + (isTail ?"    " : "│   "), true);
        }
        System.out.println(s);
        return s;
    }

    public void printGraph() {
        ArrayList<GraphNodeModel> startEvents = getStartEvents();

        for (GraphNodeModel graphNode : startEvents) {
//			System.out.println(print(graphNode, "", true));
            System.out.println(print(graphNode));
        }
    }

    public void printNamedGraph() {
        ArrayList<GraphNodeModel> startEvents = getStartEvents();

        for (GraphNodeModel graphNode : startEvents) {
//			System.out.println(print(graphNode, "", true));
            System.out.println(printNodeName(graphNode));
        }
    }

    private String print(GraphNodeModel graphNode) {
        String s = ""+graphNode.getGraphItemModelID();
        if (graphNode.getType().startsWith("abstract_sub_graph"))
            s += "_SG";

        if(graphNode.getOutEdges().size() > 0) {
            GraphEdgeModel edge = graphNode.getOutEdges().get(graphNode.getOutEdges().size()-1);
            s += " -("+edge.getGraphItemModelID()+")-> ";
            s += print(edge.getTarget());
        }
        return s;
    }

    private String printNodeName(GraphNodeModel graphNode) {
        String s = ""+graphNode.getName();
        if (graphNode.getType().startsWith("abstract_sub_graph"))
            s += "_SG";

        if(graphNode.getOutEdges().size() > 0) {
            GraphEdgeModel edge = graphNode.getOutEdges().get(graphNode.getOutEdges().size()-1);
            s += " -("+edge.getGraphItemModelID()+")-> ";
            s += printNodeName(edge.getTarget());
        }
        return s;
    }

    private ArrayList<GraphNodeModel> getStartEvents() {
        ArrayList<GraphNodeModel> startEvents = new ArrayList<>();

        for (GraphItemModel graphItem : graphItems) {

            if (graphItem instanceof GraphNodeModel && ((GraphNodeModel)graphItem).getName().startsWith("startevent_")) {
                startEvents.add((GraphNodeModel) graphItem);
            }
        }

        return startEvents;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
