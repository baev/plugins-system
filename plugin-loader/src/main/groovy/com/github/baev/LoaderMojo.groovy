package com.github.baev

import net.sf.corn.cps.CPScanner
import net.sf.corn.cps.ResourceFilter
import org.apache.commons.io.FilenameUtils
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugin.MojoFailureException
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.plugins.annotations.ResolutionScope
import org.apache.maven.project.MavenProject
import org.codehaus.gmaven.mojo.GroovyMojo

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.04.15
 */
@Mojo(name = "load", requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
class LoaderMojo extends GroovyMojo {

    @Parameter(defaultValue = '${project}', readonly = true, required = true)
    MavenProject project

    /**
     * Directory to output file with information about imported cases
     */
    @Parameter(defaultValue = '${project.build.directory}')
    File outputDirectory

    @Override
    void execute() throws MojoExecutionException, MojoFailureException {
        outputDirectory.mkdirs()

        def loader = getClass().classLoader
        def providers = ServiceLoaderUtils.load(loader, MyProvider)
        log.info("found ${providers.size()} providers")
        for (def provider : providers) {
            def name = provider.name
            def className = provider.class.canonicalName

            log.info(provider.class.canonicalName)
            def pluginDirectory = new File(outputDirectory, name)
            pluginDirectory.mkdir()

            provider.provide(pluginDirectory)

            def resources = CPScanner.scanResources(new ResourceFilter()
                    .packageName(className)
                    .resourceName("*")
            )
            resources.each {
                def resourceName = FilenameUtils.getName(it.toString())
                def input = it.openStream()
                def output = new FileOutputStream(new File(pluginDirectory, resourceName))
                output << input
                output.flush()
            }
        }
    }
}
