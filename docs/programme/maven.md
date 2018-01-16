Maven
-----

Maven is the most popular build tool for Java apps. What it does:

- Manages dependencies by downloading libraries from repositories
- Compiles the code, runs the tests
- Distributes your own binaries to remote repositories
- ... basically you can ask it to do anything

Maven is often compared to other tools especially to Ant and Gradle. The best thing about Maven comparing to these 
tools (especially to Ant) is that it strives for standardization which is good for several important reasons:
 
- Projects don't differ that much with regards to building process. It's easy for people to change the projects as
  they have the same structures, phases and share most of the plugins.
- Tools (like IDE, CI Servers) can easily recognize Maven Projects and import them. Both IntelliJ and Eclipse can 
recognize a Maven project and thus it becomes IDE-independent

With Ant and Gradle every time you enter a project there is its own custom set of scripts.

# Step 1 - Getting Acquainted

* Read about dependencies and transitive dependencies
* Learn what Super POM is and what Effective POM is
* Read about `packaging` and create a project with one module that has `packaging=war`
* Add a dependency `org.testng:testng:6.10`
* Add any Java class to your `src/main/java` and an XML file to your `src/main/resources`. And do the same in 
`src/test/xxx` dirs.
* After `mvn test-compile` find where in `target/` dir your classes and resources ended up
* After `mvn package` unpack your JAR file from `target/` dir and find where your classes and resources are

*Check yourself*. You must be able to:

* Run `mvn install` successfully
* List all the dependencies that your project has (including transitives) with one command
* Explore Effective POM and find all the default sections that were applied to your project. Even those that you didn't
explicitly write.

*Tip*: dependency tree and Effective POM are the most important diagnostic tools that you need. Every time there is
an issue these 2 tools are there to help. Your IDE probably has means to generate both of them - try to find the 
shortcuts, you'll need this often.

# Step 2 - Lifecycle, phases, plugins

* Learn what Plugins are and what is a `goal` ([link](./articles/maven-plugins.md))
* Read and learn what is a lifecycle and which lifecycles Maven has. Explore which phases each of the lifecycle has.
([link](./articles/maven-lifecycles-n-phases.md))
* Declare `maven-surefire-plugin` and bind its `test` goal to a `test` phase
* Explore file [default-bindings.xml](https://git-wip-us.apache.org/repos/asf?p=maven.git;a=blob;f=maven-core/src/main/resources/META-INF/plexus/default-bindings.xml) - 
this file contains the default plugins 
* Ensure that what you see in `default-bindings.xml` is what's listed in your Effective POM

*Check yourself*. You must be able to:

* Find a connection between packaging and the default plugins that are declared for it
* Find packaging and its configuration in `default-bindings.xml`
* Inspect in console which plugins and executions are run during `mvn install`. Does this match to what you see in 
Effective POM?

*Pro*: Note that `maven-compiler-plugin` is declared with `default-compile` execution for `packaging=war`. You inherit
it, but can you cancel it in your POM? Hint: you can, but find out how.

# Step 3 - Inheritance and Aggregation

* Create 3 modules - 1 is parent and the other 2 are included as `<modules>`. Child1 needs to have Child2 as a 
dependency.
* Read about reactor in Maven
* Play with `<parent>` tag - what difference does it make?
* Find a way to override dependencies and plugins in child modules
* Research mvn flags like `-pl`, `-am`, `-amd`

*Check yourself*. You must be able to:

* Explain the difference between Inheritance and Aggregation in Maven.
* Invoke phases and plugins against one child but not the other
* Explain what does "reactor" term mean in Maven 

# Step 4 - Advanced Dependencies
  
* Read about `dependencyManagement`
* Find out which scopes can a dependency have ([link](./articles/maven-scopes.md))
* Come up with examples of libs for each of the scopes (explore SLF4J, Lombok as some interesting examples)
* Get acquainted with classifiers and types - last parts of dependency coordinates
* Find your local maven repository, learn which phase and plugin delivers the artifacts there
* Read about maven repositories and what plugin and phase deploy artifacts there
* Try to deploying artifacts of the projects to the local maven repo on file system (not install, but deploy)

*Check yourself*. You must be able to:

* Override a version of transitive dependency
* Install and deploy multiple artifacts built from *single* maven module

# Step 5 - Debugging

* Create a unit test with `assertTrue(true)` statement. Put a breakpoint on this line. Find a way to debug the tests
that are run by `maven-surefire-plugin` (hint: look for info about forks in Surefire).
* Download the source code of any of the Maven plugins that you see in Effective POM. Use `mvnDebug` utility to debug 
that plugin.

*Tip*: How does Remote Debugging work? This is a client-server interaction where server (JVM that's debugged) 
communicates with the client (IDE) sending the information like current line of code executed in every thread, 
variables, etc. Client (IDE) can ask the server to stop the execution of the program and receive some alternatives
commands. Note, that if the binary that you debug is built not from the code that you look at - the lines of code won't
match and the IDE won't stop at the places where you would expect but will stop at lines with JavaDocs for instance. 
So make sure your binary was built from the sources at hand. 

*Tip*: Often if we don't understand how things work instead of googling and guessing it's much (much!) easier to read 
the source code of the tool. If the code is not simple - debugging becomes a real life saver. Have it as a habit to work
with the sources of the tools instead of googling - you'll have a much better grip on the technologies.

# Puzzles

There is a problematic test in branch `puzzle-nBZyIqbL` - it passes in IDE and when you run `mvn test`. But strangely
it fails when `mvn package` is run. Figure out why.