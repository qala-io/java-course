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

*Check yourself*. You must be able to:

* Run `mvn install` successfully
* Find where in `target/` dir your compiled classes and resources appeared
* List all the dependencies that your project has (including transitives) with one command
* Explore Effective POM and find all the default sections that were applied to your project. Even those that you didn't
explicitly write.

*Tip*: dependency tree and Effective POM are the most important diagnostic tools that you need. Every time there is
an issue these 2 tools are there to help. Your IDE probably has means to generate both of them - try to find the 
shortcuts, you'll need this often.

# Step 2 - lifecycles, phases, plugins

* Explore file [maven-compose.xml] - this file contains the defaults that any project would have
* Read and learn what is a lifecycle and which lifecycles Maven has. Explore which phases each of the lifecycle has.
* Learn what Plugins are and what is a `goal`
* Declare `maven-surefire-plugin` and bind its `test` goal to a `test` phase
* Ensure that what you see in `maven-compose.xml` is what's listed in your Effective POM

*Check yourself*. You must be able to:

* Find a connection between packaging and the default plugins that are declared for it
* Find packaging and its configuration in `maven-compose.xml`
* Inspect in console which plugins and executions are run during `mvn install`. Does this match to what you see in 
Effective POM?

*Pro*: Note that `maven-compiler-plugin` is declared with `default-compile` execution for `packaging=war`. You inherit
it, but can you cancel it in your POM? Hint: you can, but find out how.

# Step 3 - Advanced Dependencies

