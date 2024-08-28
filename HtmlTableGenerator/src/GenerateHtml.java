/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2014
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * This is a simple program to generate HTML code from the input MIM files 
 */
public class GenerateHtml {

    public static void main(final String[] args) throws IOException {

        System.out.println("Processing ...");

        //get all MIM files
        final List<String> inputMimFiles = getFilesInFolder("input");

        //generate table for all MIM files in output folder
        processFiles(inputMimFiles);

        System.out.println("All file processed successfully");
        System.out.println(inputMimFiles);
    }

    private static void processFiles(final List<String> inputMimFiles) throws IOException {

        //process each file
        for (final String mimFile : inputMimFiles) {
            generateTableHTML(mimFile, "output", mimFile + ".html");
        }
    }

    private static void generateTableHTML(final String fileName, final String outputFolder, final String outputFileName) throws IOException {
        final String mimFileContents = readFile("input", fileName);

        final List<Map> valueList = getAttributeValuesList(mimFileContents);

        final StringBuilder s = new StringBuilder();

        final List<String> lines = new ArrayList<String>();

        lines.add("<table class=\"confluenceTable\"><tbody>");
        lines.add(readFile("src", "tableHeadRow.xml"));

        for (final Map m : valueList) {
            lines.add("<tr>");
            lines.add("<td colspan=\"1\" width=\"\" class=\"confluenceTd\"><br data-mce-bogus=\"1\">" + m.get("namespace") + "</td>");
            lines.add("<td colspan=\"1\" width=\"\" class=\"confluenceTd\"><br data-mce-bogus=\"1\">" + m.get("version") + "</td>");
            lines.add("<td colspan=\"1\" width=\"\" class=\"confluenceTd\"><br data-mce-bogus=\"1\">" + m.get("referenceMimNamespace") + "</td>");
            lines.add("<td colspan=\"1\" width=\"\" class=\"confluenceTd\"><br data-mce-bogus=\"1\">" + m.get("referenceMimVersion") + "</td>");
            lines.add("</tr>");
        }

        lines.add("</tbody></table>");
        final Path file = Paths.get(outputFolder, outputFileName);
        Files.write(file, lines, Charset.forName("UTF-8"));
    }

    static String readFile(final String folder, final String fileName) throws IOException {
        final byte[] bytes = Files.readAllBytes(Paths.get(folder, fileName));
        return new String(bytes, StandardCharsets.UTF_8);
    }

    static List<Map> getAttributeValuesList(final String mimFileContents) {

        final List<Map> result = new ArrayList<Map>();

        //find everything in mimMappedTo
        final Pattern pattern = Pattern.compile(Pattern.quote("<mimMappedTo") + "(.*?)" + Pattern.quote("/>"));

        final Matcher m = pattern.matcher(mimFileContents);
        while (m.find()) {
            final Map map = new HashMap<String, String>();
            final String mimMappedTo = m.group(1);
            map.put("namespace", getAttributeValue("namespace", mimMappedTo));
            map.put("version", getAttributeValue("version", mimMappedTo));
            map.put("referenceMimNamespace", getAttributeValue("referenceMimNamespace", mimMappedTo));
            map.put("referenceMimVersion", getAttributeValue("referenceMimVersion", mimMappedTo));
            result.add(map);
        }
        return result;
    }

    static String getAttributeValue(final String attributeName, final String div) {
        //find everything in mimMappedTo
        final Pattern pattern = Pattern.compile(Pattern.quote(attributeName + "=\"") + "(.*?)" + Pattern.quote("\""));

        final Matcher m = pattern.matcher(div);
        while (m.find()) {
            return m.group(1);
        }
        return "";
    }

    static List<String> getFilesInFolder(final String path) throws IOException {
        final List<String> results = new ArrayList<String>();
        final File[] files = new File(path).listFiles();
        for (final File file : files) {
            if (file.isFile()) {
                results.add(file.getName());
            }
        }
        return results;
    }
}
