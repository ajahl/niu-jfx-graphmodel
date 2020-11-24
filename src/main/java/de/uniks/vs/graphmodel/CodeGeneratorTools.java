package de.uniks.vs.graphmodel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class CodeGeneratorTools {

    private static long idCount;
    public static long getNewID() {
        return idCount++;
    }
    public static void resetIDCounter() {
        idCount = 0;
    }

    public enum Syntax {
        JAVA,
        JSON
    }

//    public void generateCreaterClass(ModelManager pgModelManager, GraphModel model, String creatorPath) {
//        String creationCode = pgModelManager.getCreationCode(pgModelManager.getLastModelID());
//        String modelID = pgModelManager.getModelID(model);
//
//        int indexOf = modelID.lastIndexOf("/")+1;
////        if (indexOf > -1)
////            indexOf += 1;
//
//        String className = modelID.substring(indexOf, modelID.indexOf(".", indexOf)) + "Creator";
//
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append( "package de.uniks.vs.evolution.tests.dicore.creators;\n" +
//                "\n" +
//                "import de.uniks.vs.evolution.graphmodels.GraphNode;\n" +
//                "import de.uniks.vs.evolution.graphmodels.GraphEdge;\n" +
//                "import de.uniks.vs.evolution.graphmodels.GraphModel;\n" +
//                "import de.uniks.vs.evolution.graphmodels.ModelManager;\n" +
//                "\n" +
//                "import java.util.ArrayList;\n" +
//                "\n" +
//                "/**\n" +
//                " * Created by alex jahl code generation.\n" +
//                " */\n" +
//                "public class "+className+" {\n" +
//                "\n" +
//                "    public static ModelManager createModel() {\n" +
//                creationCode +
//                "        return modelManager;\n" +
//                "    }\n" +
//                "}");
//
//        try {
//            Files.write(Paths.get(creatorPath + className +".java"), Arrays.asList(stringBuilder.toString()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void exportModelToJson(ModelManager pgModelManager, GraphModel model, String creatorPath) {
//        resetIDCounter();
////        String jsonCode = pgModelManager.exportModelAsJSON(pgModelManager.getLastModelID());
//        String jsonCode = pgModelManager.exportModelAsJSON(model);
//        String modelID = pgModelManager.getModelID(model);
//
//        int indexOf = modelID.lastIndexOf("/")+1;
////        if (indexOf > -1)
////            indexOf += 1;
//        int indexOf1 = modelID.indexOf(".", indexOf) > indexOf? modelID.indexOf(".", indexOf): modelID.length();
//
//        String className = modelID.substring(indexOf, indexOf1);
//
//        try {
//            Files.write(Paths.get(creatorPath + className +".json"), Arrays.asList(jsonCode));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
