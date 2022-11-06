# properties-maven-plugin
Write properties file.

# Plugin
Plugin reports available at [plugin info](https://chonton.github.io/properties-maven-plugin/plugin-info.html).

There is a single goal: [write](https://chonton.github.io/properties-maven-plugin/write-mojo.html),
which by default binds to the "generate-resources" phase.  This goal reads properties from the `defaults` location,
adds properties specified in the configuration, and writes a properties file at the `filename` location.

## Configuration
| Parameter  | Property                | Default                                | Description                  |
|------------|-------------------------|----------------------------------------|------------------------------|
| comment    | ${properties.comment}   |                                        | Comment added to properties  |
| defaults   | ${properties.defaults}  | Relative to ${project.build.directory} | Filename to read defaults    |
| filename   | ${properties.filename}  | Relative to ${project.build.directory} | Filename to write properties |
| properties |                         |                                        | Properties to write          |                                                                                          |
| skip       | ${properties.skip}      | false                                  | Skip writing properties      |

# Examples

## Typical Use
```xml
  <build>
    <pluginManagement>
        <plugins>
          <plugin>
            <groupId>org.honton.chas</groupId>
            <artifactId>properties-maven-plugin</artifactId>
            <version>1.0.0</version>
          </plugin>
        </plugins>
    </pluginManagement>

    <plugins>
      <plugin>
        <groupId>org.honton.chas</groupId>
        <artifactId>properties-maven-plugin</artifactId>
        <executions>
          <execution>
            <goals>
              <goal>write</goal>
            </goals>
          </execution>
        </executions>
        <configuration>
          <defaults>../target/env/properties</defaults>
          <filename>docker/env.properties</filename>
        </configuration>
      </plugin>
    </plugins>
  </build>
```
