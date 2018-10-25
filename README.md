
[![Google Code-in](./assets/codein_logo.png)](https://codein.withgoogle.com/)

This repository is original and follows the
[student terms](https://codein.withgoogle.com/student-terms/).

## Concurrent Accumulation
[![JDK](https://img.shields.io/badge/java-SE8-blue.svg)](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
[![Code Style](https://img.shields.io/badge/codestyle-google-blue.svg)](https://google.github.io/styleguide/javaguide.html)
![](https://sonarcloud.io/api/project_badges/measure?project=merlinweber_gci-java&metric=sqale_rating)

The Concurrent accumulation task required me start numerous threads that concurrently increment an integer value in an endless iteration,
by the iteration-count. Whereas the integer value exists on the threads stack and is therefor not shared
while the thread operates upon it. I used some synchronizers of the
Java Concurrency Library to coordinate the threads and transfer their
accumulation result to the "main" thread. 
Because of the extensive use of synchronizers which mostly require to know
the number of threads that are running, the application requires all threads
to run at the same time. Thread pools may still be used and can be turned on 
and off by adding the `THREAD_POOL` argument. By default a new thread is allocated
when the accumulation begins and terminates after the result has been
transferred to the "main" thread. 

To run the Solution you just need to build the project
by using gradle:

``` 
gradle jar
```

and then you can run the generated jar file:
```
java -jar accumulation-1.0.jar 3 10
```

You may or may not define arguments. If you come across any issues while reading, building
or executing the code, just let me know by kindly creating an issue.
