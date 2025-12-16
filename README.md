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


### Sample report

```csv
URL, Elapsed, Status, Remarks
https://github.com/moeenn, 0ms, Skipped, External link
https://moeenn.github.io/, 817ms, Completed, 
https://moeenn.github.io/projects/a-team, 1022ms, Completed, 
https://moeenn.github.io/projects/align, 477ms, Completed, 
https://moeenn.github.io/projects/aptzy, 478ms, Completed, 
https://moeenn.github.io/projects/dms, 1017ms, Completed, 
https://moeenn.github.io/projects/doxy, 722ms, Completed, 
https://moeenn.github.io/projects/fn-integrator, 1017ms, Completed, 
https://moeenn.github.io/projects/hr-ai, 1009ms, Completed, 
https://moeenn.github.io/projects/lab-on-web, 833ms, Completed, 
https://moeenn.github.io/projects/loannerd, 1007ms, Completed, 
https://moeenn.github.io/projects/meritorious, 992ms, Completed, 
https://moeenn.github.io/projects/octane-club, 1003ms, Completed, 
https://moeenn.github.io/resume.pdf, 0ms, Skipped, Asset/media file
https://wa.me/923364220030, 0ms, Skipped, External link
https://www.linkedin.com/in/moeenn, 0ms, Skipped, External link
mailto:moeen.v8@gmail.com, 0ms, Skipped, Email link
tel:+92 336-4220030, 0ms, Skipped, Phone number link
```


### Commands

```bash
# install dependencies.
$ mvn install

# compile.
$ mvn clean package

# run jar.
$ java -jar ./target/spider-1.0-jar-with-dependencies.jar
```


### Todo

- [ ] Add response status codes to report.
- [ ] Extend crawl to discover broken asset links.