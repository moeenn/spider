## spider
Crawl through any website and get report of all pages within the website.


#### Usage

```
usage: spider [options]
 -url <arg>            Starting url to begin the spider
 -report <arg>         Report file name
 -max-parallel <arg>   Maximum number of parallel requests
 -help                 Display program help
```


### Commands

```bash
# install dependencies.
$ mvn install

# compile.
$ mvn clean compile assembly:single

# run jar.
$ java -jar ./target/spider-1.0-jar-with-dependencies.jar
```