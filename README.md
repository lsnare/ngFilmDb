<h1>&#9991; MyFilmDB &#9991;</h1>

A simple app for cataloging my film collection. Its main functions are 
1. Adding films owned to the Database
  A. Searching IMDB for a film and its related actors, directors, and genres using MyAPIFilms.com.
  B. Parsing the returned JSON into mathcing film, actor, director, and genre objects
  C. Inserting these obejcts into the matching tables in a Postgres database
2. Search the database for relations between actors, directors, and genres. For example, all films owned that Christian Bale appears in, or all films owned where Samuel L. Jackson worked with Quentin Tarantino.

This project is based off of the "Getting Started with Java" project from Heroku's tutorials.
