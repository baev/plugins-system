package com.github.baev;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.15
 */
public class HelloWorldProvider implements MyProvider {

    @Override
    public void provide(File resultDirectory) throws IOException {
        FileUtils.writeStringToFile(
                new File(resultDirectory, "hello-world.txt"),
                "Hello, world!"
        );
    }

    @Override
    public String getName() {
        return "hello-world";
    }
}
