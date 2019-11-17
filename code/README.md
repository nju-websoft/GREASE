# Grease

source code of the WSDM'2020 committed paper 

## Environment

Java 1.8

## Use

A demo graph is in the data/ folder and run  /src/main/java/relsearch/Main.main() to perform one query for the graph. The code of main() function is quite self-explanatory.

You can change the graph to your own data.

Here TextReader are used to load KG and index for functional displaying. To experiment on real KGs, all the data are stored in database to be more efficient. To read data from database, one needs to implement  interfaces in class SqlReader.

## License

Apache 2.0




