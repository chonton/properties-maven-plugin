package org.honton.chas.properties;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/** Write Properties */
@Mojo(name = "write", defaultPhase = LifecyclePhase.GENERATE_RESOURCES, threadSafe = true)
public class WriteProperties extends AbstractMojo {

  @Parameter(property = "project", readonly = true)
  protected MavenProject project;
  /** Properties filename */
  @Parameter(property = "properties.filename", required = true)
  String filename;
  /** Default properties filename */
  @Parameter(property = "properties.defaults")
  String defaults;
  /** Properties to save */
  @Parameter
  Properties properties;
  /** Comment for properties file */
  @Parameter(property = "properties.comment")
  String comment;
  /** Skip writing properties */
  @Parameter(property = "properties.skip")
  boolean skip;
  @Parameter(property = "mojoExecution", readonly = true)
  MojoExecution execution;

  private Path buildDirectory;

  public void execute() throws MojoExecutionException {

    if (skip) {
      getLog().info("Skipping");
      return;
    }

    buildDirectory = Path.of(project.getBuild().getDirectory());
    Path location = buildDirectory.resolve(filename);
    createParentDirectory(location);

    writeProperties(location, getMerged());
  }

  private void createParentDirectory(Path location) throws MojoExecutionException {
    Path parent = location.getParent();

    if (!Files.exists(parent)) {
      try {
        getLog().debug("Creating " + parent);
        Files.createDirectory(parent);
        getLog().debug("Created " + parent);
      } catch (IOException e) {
        throw new MojoExecutionException("Unable to create " + parent, e);
      }
    }
  }

  private Properties getMerged() throws MojoExecutionException {
    if (defaults != null) {
      Path location = buildDirectory.resolve(defaults);
      if (Files.exists(location)) {
        Properties merged = readProperties(location);
        merged.putAll(properties);
        return merged;
      } else {
        getLog().warn("Inherited properties " + location + " does not exist");
      }
    }
    return properties;
  }

  private void writeProperties(Path location, Properties properties) throws MojoExecutionException {
    getLog().debug("Saving " + location);
    try (BufferedWriter bw = Files.newBufferedWriter(location)) {
      properties.store(bw, getComment());
      getLog().debug("Saved " + location);
    } catch (IOException e) {
      throw new MojoExecutionException("Unable to write " + location, e);
    }
  }

  private Properties readProperties(Path location) throws MojoExecutionException {
    Properties properties = new Properties();
    getLog().debug("Reading " + location);
    try (BufferedReader br = Files.newBufferedReader(location)) {
      properties.load(br);
      getLog().debug("Read " + location);
    } catch (IOException e) {
      throw new MojoExecutionException("Unable to read " + location, e);
    }
    return properties;
  }

  private String getComment() {
    String work = comment;
    if (work == null) {
      work = "";
    } else {
      work = work.trim();
    }
    if (!work.isEmpty()) {
      return work;
    }
    return execution.getExecutionId();
  }
}
