Spring & REST
-------------

# Spring IoC

Historically Spring played a huge role in Java community and it all started with IoC implementation. Suppose your Dog 
has a House. IoC (Inversion of Control) is an approach of moving the responsibility for creating dependencies (House) 
from owning objects (Dog) to some other objects so that you don't need to put `new House(...)` right in the Dog code 
(which leads to all sorts of problems). Most popular implementations of this approach are:

* **Factories**. The key here is that the owning object (Dog) would explicitly ask a factory (HouseFactory) to create 
him a House: `houseFactory.create(...)`. This implementation has limitations that are hard to overcome - sometimes 
you need to provide the _same_ instance of the House to multiple owning objects.
* **Dependency Injection** (DI). This represents basically a factory for all the objects in the application. And this
time it's the DI mechanism who proactively injects the dependencies (House) into the owning objects (Dogs). This 
way it's very trivial to provide the same dependency to multiple objects. Additionally it becomes trivial to test 
the owning objects as you may override its dependencies in the same way that DI mechanism uses - passing them into
 constructors or via setters. This is the approach that Spring IoC took.

## Some History

Why SpringSource became so popular? Back in the days there was this monster that was eating people's brains - 
Enterprise Java Beans (EJB2 to be precisely - today we have EJB3 that fixed most of the problems its ancestor had). 
The monster was making developers implement complicated interfaces and even worse - 
you couldn't initialize your code outside of its container (J2EE App Servers). That effectively meant that oftentimes 
you couldn't write unit tests for the classes that you create because there was no way of creating them in the 
tests.

Spring IoC came to rescue. Its main goal was to provide a **non-intrusive** container - you just write your POJOs 
(simple class that didn't depend on the container) and create them in any way you want - either by Spring IoC or 
manually (mostly in tests). 

But today's SpringSource is different. While originally it strove for simplicity today it's becoming more and more
like the heavy technologies it was fighting against. It still provides an ability to be non-intrusive, but in the same
time it _encourages_ the opposite - `@Autowired` on private members, package scans, Spring Boot, etc. So nowadays we
may already be in need for better solutions.

## Other IoC implementations

**Google Guice** - is an alternative but a more lightweight DI implementation. By the time it was introduced the world
was already using Spring IoC extensively so it never became as popular.

**CDI** - recent JavaEE spec that was inspired by Spring IoC. It uses a different set of annotations (e.g. `@Inject` 
instead of `@Autowired`). Today's App Servers implement this spec and allow for DI without Spring. The latter also
implements this spec and recognizes CDI annotations.

# REST

