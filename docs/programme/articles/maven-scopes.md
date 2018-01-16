Maven Scopes
------------

Most of the time if we need a library, we need it everywhere:

- During compilation - because we reference its classes in our Java files
- When testing - because the classes we test reference library classes
- When creating the final artifact (e.g. WAR file or an Executable JAR) - because the library is used when the
application is running

That's why the default (`compile`) scope in Maven makes libs available at all these stages. But there are exceptional
cases:

* Some dependencies are not referenced directly by our classes. E.g. we could instantiate a class using reflection by
loading it dynamically: `Class.forName("some.DynamicallyInstantiatedClass")`. In that case we don't need such library
during compilation and it can be included with scope `runtime`. Such dependency will be required when we run
our code and thus it's going to be included in the final binary _and_ is available during testing.
* Other dependencies are needed only during testing. Most notably - JUnit, TestNG and so on. These are testing libs and
they shouldn't be included into the final artifact, neither our production classes depend on these libs. Thus they can
be declared with scope `test`.
* Yet another type of dependencies are those that are required during compilation, but are not needed in the final
artifact. Why? Because the the library is available anyway in the environment (on the target machine, in the target
App Server). Such dependencies should be marked as `provided`.

Some examples:

* Classes of our logger API are explicitly referenced by our own classes and they are needed when app is running. This
is clearly a `compile` scope.
* JDBC drivers on the other hand are usually not needed during compilation. They are loaded dynamically by
`DriverManager`. Our classes instead work with JDBC API which is included directly in Java SDK. Thus JDBC Driver can
be defined as `runtime` dependency.
* The most famous example of `provided` dependencies is Servlet API. We do need it during compilation (because we
implement HttpServlet or some listener or Filter and thus we reference the classes from that lib). But we don't need it
when we run the application because all standard App Servers have this library anyway. Including such lib 2 times may
cause conflicts if they are of different versions.

So to sum up:

```
                | compile | provided | runtime | test
----------------|---------|----------|---------|--------
Available       |         |          |         |
during          |    V    |     V    |    -    |   -
compilation     |         |          |         |
----------------|---------|----------|---------|--------
Included into   |         |          |         |
final           |    V    |     -    |    V    |   -
binary          |         |          |         |
----------------|---------|----------|---------|--------
Available during|         |          |         |
test compilation|    V    |     V    |    V    |   V
and execution   |         |          |         |
```

There are also a couple of more exotic scopes:

* `import` allows declaring `<dependencyManagement>` in some other 3d-party POM and then import those into your own POM.
Theoretically this can be useful if there are dependencies that are hard to find compatible versions of. So you create
a re-usable POM that could be included in multiple projects. This way teams don't need to research compatibility of
libs every time a new team/project is started.
* `system` allows referencing local libraries on your machines. This is not recommended and is useful only when your
builds are platform-dependent and require some OS-specific artifacts to be found on file system.