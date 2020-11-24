package de.uniks.vs.graphmodel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Created by alex on 16/6/8.
 */
public abstract class GraphItemModel implements PropertyChangeListener {

    public static final String UNDEFINED 	= "undefined"; // means removed or unchanged
    public static final String NEW 			= "new";
    public static final String DELETED 		= "deleted";
    public static final String REMOVED 		= "removed";
    public static final String UNCHANGED 	= "unchanged";
    public static final String CHANGED 		= "changed";


    protected long graphItemModelID;
    protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    protected String status;

    private GraphModel graphModel;
    private VisualProperties properties = new VisualProperties();

    public abstract void removeThis();

    public abstract GraphItemModel clone();

    public abstract String getCreationCode(CodeGeneratorTools.Syntax syntax);

    public boolean addPropertyChangeListener(PropertyChangeListener listener)
    {
        getPropertyChangeSupport().addPropertyChangeListener(listener);
        return true;
    }

    public boolean removePropertyChangeListener(PropertyChangeListener listener) {
        getPropertyChangeSupport().removePropertyChangeListener(listener);
        return true;
    }

    public void removeYou()
    {
        removeThis();
        getPropertyChangeSupport().firePropertyChange("REMOVE_YOU", this, null);
    }

    public void setProperties(VisualProperties properties) {
        this.properties = properties;
    }

    public VisualProperties getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + String.valueOf(getGraphItemModelID())+" \n";
    }

    public long getGraphItemModelID() {
        return graphItemModelID;
    }

    public PropertyChangeSupport getPropertyChangeSupport()
    {
        return listeners;
    }

    public void setGraphItemModelID(long id) {
        long temp = this.graphItemModelID;
        this.graphItemModelID = id;
        propertyChange(new PropertyChangeEvent(this, "setId", temp, id));

    }

    public abstract GraphItemModel withId(long id);

    public GraphItemModel withStatus(String status) {
        String temp = this.status;
        this.status = status;
        propertyChange(new PropertyChangeEvent(this, "setStatus", temp, status));
        return this;
    }

    public String getStatus() {
        return status;
    }

    public GraphModel getGraphModel() {
        return graphModel;
    }

    public void setModel(GraphModel graphModel) {
        this.graphModel = graphModel;
    }

    private static class StaticCounter {
        public static int count;
    }

    public boolean addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        getPropertyChangeSupport().addPropertyChangeListener(propertyName, listener);
        return true;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
//        System.out.println(evt.getSource().toString() + "  " + evt.getPropertyName() + "  " +  evt.getNewValue());
        listeners.firePropertyChange(evt);
    }

    public void setX(double x) {

    }
    public void setY(double x) {

    }

    public double getX() {
        return 0;
    }

    public double getY() {
        return 0;
    }

    public void toBack() {

    }

}

