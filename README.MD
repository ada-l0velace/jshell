# Es16tg_21-project
Software Engineering project

# Compile & run
(Linux)
Run without tests
``` 
> mvn clean compile exec:java
``` 
Run on terminal (with Dbclean)
``` 
> mvn clean dbclean:dbclean package exec:java
``` 
Run on terminal (without Dbclean)
``` 
> mvn clean package exec:java -Dexec.args="info/drive.xml"
``` 

Compile with Converage/Site generation and open site on chromium
```
> mvn clean package cobertura:cobertura site:site
> chromium target/site/cobertura/index.html --incognito
```

#Unit/IT Test Runing

Run all Tests
```
> mvn clean test
```

Compile without running any Tests.
```
> mvn clean test -DskipTests
```

Run tests a specific class of tests.
```
> mvn clean test -Dtest=ClassTest
```

Run a specific method test.
```
> mvn clean test -Dtest=ClassTest#Method
```

# Development
<a href="#">To Do tasks - Trello</a>

# UML Chart

![alt tag](https://github.com/tecnico-softeng/es16tg_21-project/blob/master/info/drive.png)

# Java code conventions
<a href="https://google.github.io/styleguide/javaguide.html">Google conventions</a></br>

# Research Material
<a href="https://fenix-framework.github.io/">Fenix-Framework documentation</a></br>
<a href="http://www.joda.org/joda-time/">Joda-Time documentation</a>

# Useful links
