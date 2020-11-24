package de.uniks.vs.graphmodel;

public class GraphCursorModel extends GraphItemModel {

    private Point2D position;

    @Override
    public void removeThis() {

    }

    @Override
    public GraphItemModel clone() {
        GraphCursorModel graphCursorModel = new GraphCursorModel();
        return graphCursorModel;
    }

    @Override
    public String getCreationCode(CodeGeneratorTools.Syntax syntax) {
        return "";
    }

    @Override
    public GraphItemModel withId(long id) {
        setGraphItemModelID( id);
        return this;
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }


    // ----------- Point2D Class --------------

    public class Point2D {
        private double x;
        private double y;

        public Point2D(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public Point2D(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public void setX(double x) {
            this.x = x;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }
}
