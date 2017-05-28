# Maven Plugins

Maven is written in Java and its core part is responsible for reading POM files, downloading dependencies, running
plugins. But all the rest is done by the actual plugins, examples: 

* Maven compiles your code? It's `maven-compiler-plugin`.
* Maven runs the tests? It's `maven-surefire-plugin`.
* Maven deletes the directories when you run `mvn clean`? It's `maven-clean-plugin`.

Whenever you want to do something with your project - you either find a plugin that suits you or you write your own 
(which is just another jar with some interfaces implemented). 

# How plugins work

Plugin can be declared in `pom.xml` or you specify all its details in the console to run it: 
`org.apache.maven.plugins:maven-clean-plugin:2.5:clean`

where:
* `org.apache.maven.plugins` is the groupId of the plugin. It actually defaults to this particular value so for the
official plugins this part can be omitted.
* `maven-clean-plugin` - artifactId. If plugins are defined in `pom.xml` you can refer to plugins by their shorter names
 which are defined in the metadata.
* `2.5` - version
* `clean` - goal. A plugin can do multiple different things. E.g. `maven-compiler-plugin` can compile production sources
as well as test sources. For these it has 2 different goals: `compile` and `testCompile`. If you decide to read the
source code of the plugins (which I'd encourage) each goal is represented with a separate class (usually suffixed 
with `Mojo`).

Since this plugin is defined in `pom.xml` and it has a shorter name the string could be decreased to: `mvn clean:clean`. 

