/**
 * NoraUi is licensed under the license GNU AFFERO GENERAL PUBLIC LICENSE
 * 
 * @author Nicolas HALLOUIN
 * @author Stéphane GRILLON
 */
package com.github.noraui.cli;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.regex.Matcher;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class Model {

    /**
     * Add new model for a target application to your robot.
     * Sample if you add google: -f 5 -a google -m user --verbose
     * 
     * @param applicationName
     * @param modelName
     * @param robotContext
     * @param noraRobotName
     * @param verbose
     */
    public void add(String applicationName, String modelName, String fields, String results, Class<?> robotContext, boolean verbose) {
        System.out.println("Add a new model named [" + modelName + "] in application named [" + applicationName + "]");
        String[] fieldList = fields.split(" ");
        for (String field : fieldList) {
            System.out.println("field: [" + field + "]");
        }
        String[] resultList = results.split(" ");
        for (String result : resultList) {
            System.out.println("result: [" + result + "]");
        }
        addModel(applicationName, modelName, fieldList, resultList, robotContext, verbose);
        addModels(applicationName, modelName, fieldList, resultList, robotContext, verbose);
    }

    /**
     * Remove model for a target application to your robot.
     * Sample if you add google: -f 6 -a google -m user --verbose
     * 
     * @param applicationName
     * @param modelName
     * @param robotContext
     * @param noraRobotName
     * @param verbose
     */
    public void remove(String applicationName, String modelName, Class<?> robotContext, boolean verbose) {
        System.out.println("Remove model named [" + modelName + "] in application named [" + applicationName + "]");
        String modelPath = "src" + File.separator + "main" + File.separator + "java" + File.separator
                + robotContext.getCanonicalName().replaceAll("\\.", "/").replaceAll("utils", "application/model/" + applicationName).replaceAll("/", Matcher.quoteReplacement(File.separator))
                        .replaceAll(robotContext.getSimpleName(), modelName.toUpperCase().charAt(0) + modelName.substring(1))
                + ".java";
        String modelsPath = "src" + File.separator + "main" + File.separator + "java" + File.separator
                + robotContext.getCanonicalName().replaceAll("\\.", "/").replaceAll("utils", "application/model/" + applicationName).replaceAll("/", Matcher.quoteReplacement(File.separator))
                        .replaceAll(robotContext.getSimpleName(), modelName.toUpperCase().charAt(0) + modelName.substring(1))
                + "s.java";
        try {
            FileUtils.forceDelete(new File(modelPath));
            if (verbose) {
                System.out.println(modelPath + " removed with success.");
            }
            FileUtils.forceDelete(new File(modelsPath));
            if (verbose) {
                System.out.println(modelsPath + " removed with success.");
            }
            File applicationDirectory = new File(modelPath.substring(0, modelPath.lastIndexOf(File.separator)));
            Collection<File> l = FileUtils.listFiles(applicationDirectory, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
            if (l.size() == 0) {
                if (verbose) {
                    System.out.println("Empty directory, so remove application directory.");
                }
                FileUtils.deleteDirectory(applicationDirectory);
            }
        } catch (IOException e) {
            System.err.println("IOException " + e);
            System.exit(1);
        }
    }

    /**
     * @param applicationName
     * @param modelName
     * @param noraRobotName
     * @param robotContext
     * @param verbose
     */
    private void addModel(String applicationName, String modelName, String[] fieldList, String[] resultList, Class<?> robotContext, boolean verbose) {
        String modelPath = "src" + File.separator + "main" + File.separator + "java" + File.separator
                + robotContext.getCanonicalName().replaceAll("\\.", "/").replaceAll("utils", "application/model/" + applicationName).replaceAll("/", Matcher.quoteReplacement(File.separator))
                        .replaceAll(robotContext.getSimpleName(), modelName.toUpperCase().charAt(0) + modelName.substring(1))
                + ".java";
        StringBuilder sb = new StringBuilder();
        sb.append("/**").append(System.lineSeparator());
        sb.append(" * " + robotContext.getSimpleName().replaceAll("Context", "") + " generated free by NoraUi Organization https://github.com/NoraUi").append(System.lineSeparator());
        sb.append(" * " + robotContext.getSimpleName().replaceAll("Context", "") + " is licensed under the license BSD.").append(System.lineSeparator());
        sb.append(" * ").append(System.lineSeparator());
        sb.append(" * CAUTION: " + robotContext.getSimpleName().replaceAll("Context", "") + " use NoraUi library. This project is licensed under the license GNU AFFERO GENERAL PUBLIC LICENSE")
                .append(System.lineSeparator());
        sb.append(" */").append(System.lineSeparator());
        sb.append(robotContext.getPackage().toString().replaceAll("utils", "application.model." + applicationName) + ";").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("import org.apache.commons.lang3.builder.EqualsBuilder;").append(System.lineSeparator());
        sb.append("import org.apache.commons.lang3.builder.HashCodeBuilder;").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("import com.google.common.collect.ComparisonChain;").append(System.lineSeparator());
        sb.append("import com.google.gson.Gson;").append(System.lineSeparator());
        sb.append("import com.google.gson.GsonBuilder;").append(System.lineSeparator());
        sb.append("import com.google.gson.annotations.Expose;").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("import com.github.noraui.annotation.Column;").append(System.lineSeparator());
        sb.append("import com.github.noraui.model.Model;").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("public class " + modelName.toUpperCase().charAt(0) + modelName.substring(1) + " implements Model, Comparable<" + modelName.toUpperCase().charAt(0) + modelName.substring(1) + "> {")
                .append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("    @Expose(serialize = false, deserialize = false)").append(System.lineSeparator());
        sb.append("    private Integer nid;").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        for (String field : fieldList) {
            sb.append("    @Expose").append(System.lineSeparator());
            sb.append("    @Column(name = \"" + field + "\")").append(System.lineSeparator());
            sb.append("    private String " + field + ";").append(System.lineSeparator());
            sb.append("").append(System.lineSeparator());
        }
        for (String result : resultList) {
            sb.append("    @Column(name = \"" + result + "\")").append(System.lineSeparator());
            sb.append("    private String " + result + ";").append(System.lineSeparator());
            sb.append("").append(System.lineSeparator());
        }
        sb.append("    // constructor by default for serialize/deserialize").append(System.lineSeparator());
        sb.append("    public " + modelName.toUpperCase().charAt(0) + modelName.substring(1) + "() {").append(System.lineSeparator());
        sb.append("        this.nid = -1;").append(System.lineSeparator());
        for (String field : fieldList) {
            sb.append("        this." + field + " = \"\";").append(System.lineSeparator());
        }
        for (String result : resultList) {
            sb.append("        this." + result + " = \"\";").append(System.lineSeparator());
        }
        sb.append("    }").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("    public " + modelName.toUpperCase().charAt(0) + modelName.substring(1) + "(String nid");
        for (String field : fieldList) {
            sb.append(", String " + field);
        }
        for (String result : resultList) {
            sb.append(", String " + result);
        }
        sb.append(") {").append(System.lineSeparator());
        sb.append("        this.nid = Integer.parseInt(nid);").append(System.lineSeparator());
        for (String field : fieldList) {
            sb.append("        this." + field + " = " + field + ";").append(System.lineSeparator());
        }
        for (String result : resultList) {
            sb.append("        this." + result + " = " + result + ";").append(System.lineSeparator());
        }
        sb.append("    }").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("    /**").append(System.lineSeparator());
        sb.append("     * {@inheritDoc}").append(System.lineSeparator());
        sb.append("     */").append(System.lineSeparator());
        sb.append("    @Override").append(System.lineSeparator());
        sb.append("    public String serialize() {").append(System.lineSeparator());
        sb.append("        final GsonBuilder builder = new GsonBuilder();").append(System.lineSeparator());
        sb.append("        builder.excludeFieldsWithoutExposeAnnotation();").append(System.lineSeparator());
        sb.append("        builder.disableHtmlEscaping();").append(System.lineSeparator());
        sb.append("        final Gson gson = builder.create();").append(System.lineSeparator());
        sb.append("        return gson.toJson(this);").append(System.lineSeparator());
        sb.append("    }").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("    /**").append(System.lineSeparator());
        sb.append("     * {@inheritDoc}").append(System.lineSeparator());
        sb.append("     */").append(System.lineSeparator());
        sb.append("    @Override").append(System.lineSeparator());
        sb.append("    public void deserialize(String jsonString) {").append(System.lineSeparator());
        sb.append("        final GsonBuilder builder = new GsonBuilder();").append(System.lineSeparator());
        sb.append("        builder.excludeFieldsWithoutExposeAnnotation();").append(System.lineSeparator());
        sb.append("        final Gson gson = builder.create();").append(System.lineSeparator());
        sb.append("        " + modelName.toUpperCase().charAt(0) + modelName.substring(1) + " w = gson.fromJson(jsonString, " + modelName.toUpperCase().charAt(0) + modelName.substring(1) + ".class);")
                .append(System.lineSeparator());
        sb.append("        this.nid = w.nid;").append(System.lineSeparator());
        for (String field : fieldList) {
            sb.append("        this." + field + " = w." + field + ";").append(System.lineSeparator());
        }
        sb.append("    }").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("    /**").append(System.lineSeparator());
        sb.append("     * {@inheritDoc}").append(System.lineSeparator());
        sb.append("     */").append(System.lineSeparator());
        sb.append("    @Override").append(System.lineSeparator());
        sb.append("    public Class<" + modelName.toUpperCase().charAt(0) + modelName.substring(1) + "s> getModelList() {").append(System.lineSeparator());
        sb.append("        return " + modelName.toUpperCase().charAt(0) + modelName.substring(1) + "s.class;").append(System.lineSeparator());
        sb.append("    }").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("    /**").append(System.lineSeparator());
        sb.append("     * {@inheritDoc}").append(System.lineSeparator());
        sb.append("     */").append(System.lineSeparator());
        sb.append("    @Override").append(System.lineSeparator());
        sb.append("    public int hashCode() {").append(System.lineSeparator());
        sb.append("        return new HashCodeBuilder()");
        for (String field : fieldList) {
            sb.append(".append(" + field + ")");
        }
        sb.append(".toHashCode();").append(System.lineSeparator());
        sb.append("    }").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("    /**").append(System.lineSeparator());
        sb.append("     * {@inheritDoc}").append(System.lineSeparator());
        sb.append("     */").append(System.lineSeparator());
        sb.append("    @Override").append(System.lineSeparator());
        sb.append("    public boolean equals(Object obj) {").append(System.lineSeparator());
        sb.append("        if (obj instanceof " + modelName.toUpperCase().charAt(0) + modelName.substring(1) + ") {").append(System.lineSeparator());
        sb.append("            final " + modelName.toUpperCase().charAt(0) + modelName.substring(1) + " other = (" + modelName.toUpperCase().charAt(0) + modelName.substring(1) + ") obj;")
                .append(System.lineSeparator());
        sb.append("            return new EqualsBuilder()").append(System.lineSeparator());
        for (String field : fieldList) {
            sb.append("                    .append(" + field + ", other." + field + ")").append(System.lineSeparator());
        }
        sb.append("                    .isEquals();").append(System.lineSeparator());
        sb.append("        } else {").append(System.lineSeparator());
        sb.append("            return false;").append(System.lineSeparator());
        sb.append("        }").append(System.lineSeparator());
        sb.append("    }").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("    /**").append(System.lineSeparator());
        sb.append("     * {@inheritDoc}").append(System.lineSeparator());
        sb.append("     */").append(System.lineSeparator());
        sb.append("    @Override").append(System.lineSeparator());
        sb.append("    public int compareTo(" + modelName.toUpperCase().charAt(0) + modelName.substring(1) + " other) {").append(System.lineSeparator());

        sb.append("        return ComparisonChain.start()").append(System.lineSeparator());
        for (String field : fieldList) {
            sb.append("                .compare(" + field + ", other." + field + ")").append(System.lineSeparator());
        }
        sb.append("        .result();").append(System.lineSeparator());
        sb.append("    }").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("    @Override").append(System.lineSeparator());
        sb.append("    public String toString() {").append(System.lineSeparator());
        sb.append("        return \"{nid:\" + nid + \"");
        for (String field : fieldList) {
            sb.append(", " + field + ":\\\"\" + " + field + " + \"\\\"");
        }
        for (String result : resultList) {
            sb.append(", " + result + ":\\\"\" + " + result + " + \"\\\"");
        }
        sb.append("}\";").append(System.lineSeparator());
        sb.append("    }").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("    public Integer getNid() {").append(System.lineSeparator());
        sb.append("        return nid;").append(System.lineSeparator());
        sb.append("    }").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("    public void setNid(Integer nid) {").append(System.lineSeparator());
        sb.append("        this.nid = nid;").append(System.lineSeparator());
        sb.append("    }").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        for (String field : fieldList) {
            sb.append("    public String get" + field.toUpperCase().charAt(0) + field.substring(1) + "() {").append(System.lineSeparator());
            sb.append("        return " + field + ";").append(System.lineSeparator());
            sb.append("    }").append(System.lineSeparator());
            sb.append("").append(System.lineSeparator());
            sb.append("    public void set" + field.toUpperCase().charAt(0) + field.substring(1) + "(String " + field + ") {").append(System.lineSeparator());
            sb.append("        this." + field + " = " + field + ";").append(System.lineSeparator());
            sb.append("    }").append(System.lineSeparator());
            sb.append("").append(System.lineSeparator());
        }
        for (String result : resultList) {
            sb.append("    public String get" + result.toUpperCase().charAt(0) + result.substring(1) + "() {").append(System.lineSeparator());
            sb.append("        return " + result + ";").append(System.lineSeparator());
            sb.append("    }").append(System.lineSeparator());
            sb.append("").append(System.lineSeparator());
            sb.append("    public void set" + result.toUpperCase().charAt(0) + result.substring(1) + "(String " + result + ") {").append(System.lineSeparator());
            sb.append("        this." + result + " = " + result + ";").append(System.lineSeparator());
            sb.append("    }").append(System.lineSeparator());
            sb.append("").append(System.lineSeparator());
        }
        sb.append("}").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        try {
            FileUtils.forceMkdir(new File(modelPath.substring(0, modelPath.lastIndexOf(File.separator))));
            File newSelector = new File(modelPath);
            if (!newSelector.exists()) {
                Files.asCharSink(newSelector, Charsets.UTF_8).write(sb.toString());
            }
        } catch (Exception e) {
            System.err.println("IOException " + e);
            System.exit(1);
        }
    }

    private void addModels(String applicationName, String modelName, String[] fieldList, String[] resultList, Class<?> robotContext, boolean verbose) {
        String modelsPath = "src" + File.separator + "main" + File.separator + "java" + File.separator
                + robotContext.getCanonicalName().replaceAll("\\.", "/").replaceAll("utils", "application/model/" + applicationName).replaceAll("/", Matcher.quoteReplacement(File.separator))
                        .replaceAll(robotContext.getSimpleName(), modelName.toUpperCase().charAt(0) + modelName.substring(1))
                + "s.java";
        StringBuilder sb = new StringBuilder();
        sb.append("/**").append(System.lineSeparator());
        sb.append(" * " + robotContext.getSimpleName().replaceAll("Context", "") + " generated free by NoraUi Organization https://github.com/NoraUi").append(System.lineSeparator());
        sb.append(" * " + robotContext.getSimpleName().replaceAll("Context", "") + " is licensed under the license BSD.").append(System.lineSeparator());
        sb.append(" * ").append(System.lineSeparator());
        sb.append(" * CAUTION: " + robotContext.getSimpleName().replaceAll("Context", "") + " use NoraUi library. This project is licensed under the license GNU AFFERO GENERAL PUBLIC LICENSE")
                .append(System.lineSeparator());
        sb.append(" */").append(System.lineSeparator());
        sb.append(robotContext.getPackage().toString().replaceAll("utils", "application.model." + applicationName) + ";").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("import java.lang.reflect.Type;").append(System.lineSeparator());
        sb.append("import java.util.ArrayList;").append(System.lineSeparator());
        sb.append("import java.util.Iterator;").append(System.lineSeparator());
        sb.append("import java.util.List;").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("import com.google.gson.Gson;").append(System.lineSeparator());
        sb.append("import com.google.gson.GsonBuilder;").append(System.lineSeparator());
        sb.append("import com.google.gson.reflect.TypeToken;").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("import com.github.noraui.application.model.DemosModel;").append(System.lineSeparator());
        sb.append("import com.github.noraui.model.Model;").append(System.lineSeparator());
        sb.append("import com.github.noraui.model.ModelList;").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("public class " + modelName.toUpperCase().charAt(0) + modelName.substring(1) + "s extends DemosModel<" + modelName.toUpperCase().charAt(0) + modelName.substring(1)
                + "> implements ModelList {").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("    /**").append(System.lineSeparator());
        sb.append("     *").append(System.lineSeparator());
        sb.append("     */").append(System.lineSeparator());
        sb.append("    private static final long serialVersionUID = 9002528163560746878L;").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("    public " + modelName.toUpperCase().charAt(0) + modelName.substring(1) + "s() {").append(System.lineSeparator());
        sb.append("        super();").append(System.lineSeparator());
        sb.append("    }").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("    public " + modelName.toUpperCase().charAt(0) + modelName.substring(1) + "s(DemosModel<" + modelName.toUpperCase().charAt(0) + modelName.substring(1) + "> inputList) {")
                .append(System.lineSeparator());
        sb.append("        super(inputList);").append(System.lineSeparator());
        sb.append("    }").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("    /**").append(System.lineSeparator());
        sb.append("     * {@inheritDoc}").append(System.lineSeparator());
        sb.append("     */").append(System.lineSeparator());
        sb.append("    @Override").append(System.lineSeparator());
        sb.append("    public void deserialize(String jsonString) {").append(System.lineSeparator());
        sb.append("            Type listType = new TypeToken<ArrayList<" + modelName.toUpperCase().charAt(0) + modelName.substring(1) + ">>() {").append(System.lineSeparator());
        sb.append("            }.getType();").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("            final GsonBuilder builder = new GsonBuilder();").append(System.lineSeparator());
        sb.append("            builder.excludeFieldsWithoutExposeAnnotation();").append(System.lineSeparator());
        sb.append("             final Gson gson = builder.create();").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("            List<" + modelName.toUpperCase().charAt(0) + modelName.substring(1) + "> list = gson.fromJson(jsonString, listType);").append(System.lineSeparator());
        sb.append("            this.addAll(list);").append(System.lineSeparator());
        sb.append("        }").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("        /**").append(System.lineSeparator());
        sb.append("         * {@inheritDoc}").append(System.lineSeparator());
        sb.append("         */").append(System.lineSeparator());
        sb.append("        @Override").append(System.lineSeparator());
        sb.append("        public ModelList addModel(Model m) {").append(System.lineSeparator());
        sb.append("            super.add((" + modelName.toUpperCase().charAt(0) + modelName.substring(1) + ") m);").append(System.lineSeparator());
        sb.append("            return this;").append(System.lineSeparator());
        sb.append("       }").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("        /**").append(System.lineSeparator());
        sb.append("        * {@inheritDoc}").append(System.lineSeparator());
        sb.append("        */").append(System.lineSeparator());
        sb.append("       @Override").append(System.lineSeparator());
        sb.append("       public void subtract(ModelList list) {").append(System.lineSeparator());
        sb.append("           Iterator<?> iterator = ((" + modelName.toUpperCase().charAt(0) + modelName.substring(1) + "s) list).iterator();").append(System.lineSeparator());
        sb.append("          while (iterator.hasNext()) {").append(System.lineSeparator());
        sb.append("              this.remove(iterator.next());").append(System.lineSeparator());
        sb.append("          }").append(System.lineSeparator());
        sb.append("     }").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("    /**").append(System.lineSeparator());
        sb.append("     * {@inheritDoc}").append(System.lineSeparator());
        sb.append("     */").append(System.lineSeparator());
        sb.append("    @Override").append(System.lineSeparator());
        sb.append("    public List<Integer> getIds() {").append(System.lineSeparator());
        sb.append("        List<Integer> result = new ArrayList<>();").append(System.lineSeparator());
        sb.append("        for (" + modelName.toUpperCase().charAt(0) + modelName.substring(1) + " " + modelName + " : this) {").append(System.lineSeparator());
        sb.append("            result.add(" + modelName + ".getNid());").append(System.lineSeparator());
        sb.append("        }").append(System.lineSeparator());
        sb.append("        return result;").append(System.lineSeparator());
        sb.append("    }").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        sb.append("}").append(System.lineSeparator());
        sb.append("").append(System.lineSeparator());
        try {
            FileUtils.forceMkdir(new File(modelsPath.substring(0, modelsPath.lastIndexOf(File.separator))));
            File newSelector = new File(modelsPath);
            if (!newSelector.exists()) {
                Files.asCharSink(newSelector, Charsets.UTF_8).write(sb.toString());
            }
        } catch (Exception e) {
            System.err.println("IOException " + e);
            System.exit(1);
        }
    }

}