# Maven Phases

If you decide to do something meaningful (e.g. compile your sources and run tests) with bear plugins, the command would
be:

```
mvn resources:resources compiler:compile \
    resources:testResources compiler:testCompile \
    surefire:test
```

Which is far from being concise. To simplify this task Maven defines Lifecycles which consist of multiple Phases.
And now instead of invoking the plugins from the console you can invoke phases like this: `mvn package`. To make it do
something you bind plugins and their goals to phases:

```
<plugin>
  <artifactId>maven-clean-plugin</artifactId>
  <version>2.5</version>
  <executions>
    <execution>
      <id>default-clean</id>
      <phase>clean</phase>
      <goals>
        <goal>clean</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

where:
* `goals` determines which goals of the plugin to invoke and
* `phase` determines at which phases this should happen

Multiple plugins can be bound to the same phase and vice versa - you can bind the same plugin to multiple phases.

# Maven Lifecycles

What makes phases even more useful is the fact that they are grouped in Lifecycles and are invoked sequentially. There
are 3 lifecycles: 

* default - has phases like `compile`, `test`, `package`, `install` and many more. These phases are used to build 
stuff. 
* clean consists of 3 phases `pre-clean`, `clean`, `post-clean`. These almost always are just deleting `target`
directory.
* site has phases to create some static HTML pages (e.g. javadocs files or test reports)
 
While their names look meaningful you can actually define completely unrelated logic to any of these phases. The full 
list of phases in each lifecycle is defined in the 
[official docs](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html).  
 
So what's important about phases is that when you invoke one phase Maven first invokes phases that go before it. E.g.
if you run `mvn compile` what actually is invoked is:
_validate initialize generate-sources process-sources generate-resources process-resources compile_. And each of these
phases can have plugins bound.