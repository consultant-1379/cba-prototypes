package com.ericsson.testProjects.handler.util;

import java.io.File;
import java.io.FileWriter;

import javax.ejb.Stateless;

/**
 * Used by the handler
 * @author ecaoodo
 *
 */
@Stateless
public class GreeterImpl {

    /**
     * Just say hello.
     * @param msg the content of the file
     */
    public void sayHello(final String msg) {
        final File file = new File("target/HelloWorld.txt");
        try {
            file.createNewFile();
            final FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(msg);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
