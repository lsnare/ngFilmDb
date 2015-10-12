<h1>&#9991; MyFilmDB &#9991;</h1>

A simple app for cataloging my film collection. Its main functions are 
<ol>
  <li>Adding films owned to the Database</li>
    <ol>
      <li>Searching IMDB for a film and its related actors, directors, and genres using MyAPIFilms.com.</li>
      <li>Parsing the returned JSON into mathcing film, actor, director, and genre objects.</li>
      <li>Inserting these obejcts into the matching tables in a Postgres database.</li>
    </ol>
  <li>Search the database for relations between actors, directors, and genres. For example: 
    <ol>
      <li>Find all films that Christian Bale appears in</li>
      <li>Find all films where Samuel L. Jackson works with Quentin Tarantino</li>
      <li>Find all Horror films that Jennifer Connelly appears in</li>
    </ol>
</ol>
