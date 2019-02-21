# Multithreaded WebScraper with Logging

Implementation of a multi-threaded web scraper. The scraped websites are captured in a log. In case of an error or cancel, the scraping can continue from the last seen page.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Java
- Jsoup
- Apache Maven

### Installing

1. Download source code
2. `cd mwsl`
3. configure settings in /src/main/resources/config.properties
4. `mvn clean install` in project root
5. `cd target`
6. `java -classpath jarName-jar-with-dependencies.jar lenngro.mwsl.WebScraper`

Note: `sudo` rights might be required in order to create new directories while saving the scraped pages.
