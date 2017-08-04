# edrp #

## Create a Database ##

Postgresql 9.6
Use the scripts to create the schema.

## Set your environment variables ##

you need environment variables 

* EDRP_DB_HOST
* EDRP_DB_PORT
* EDRP_DB_USER
* EDRP_DB_PASS
* EDRP_DB_NAME

## Load your data ##

Download systems.csv and bodies.jsonl from eddb's api https://eddb.io/api

Run the preprocessing scripts parseSystems.sh and parseValuableBodies.sh

Copy the results to /usr/share/edrp/

Uncomment the "Store.loadSystems()" and "Store.loadBodies()" in ScalatraBootstrap.scala

When you first run edrp, your data will load.  It will take some hours because ngram index.

Make sure you comment those lines again, after your data is loaded into the database.

## Build & Run ##

```sh
$ cd edrp
$ ./sbt
> jetty:start
> browse
```

If `browse` doesn't launch your browser, manually open [http://localhost:8080/](http://localhost:8080/) in your browser.
