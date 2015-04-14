package com.github.baev;

import java.io.File;
import java.io.IOException;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.15
 */
public interface MyProvider {

    void provide(File resultDirectory) throws IOException;

    String getName();
}
