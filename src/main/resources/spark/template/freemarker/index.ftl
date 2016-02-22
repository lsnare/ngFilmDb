<html>
    <head>
        <link rel="stylesheet" type="text/css" href="/stylesheets/film.css" />
        <Script src="/scripts/JSUtils.js"></Script>
    </head>
    <body bgcolor="#7BA05B"> <!-- Asparagus -->
        <h1><a href="/">Welcome to MyFilmDB</a></h1>
        <#if loggedIn??>
            <#if loggedIn == true>
                <a href="/logout">Logout</a>
            </#if>
        </#if>
        <ul>
            <li><a href="add">Add a Film to the Database</a></li>
            <li>
                <a> Search the Database </a>
                <ul class="dropdown">
                    <li><a href="search">Search Films by Title</a></li>
                    <li><a href="searchActor">Search Films by Actor</a></li>
                    <li><a href="searchDirector">Search Films by Director</a></li>
                </ul>
            </li>
        </ul>
    </body>
</html>
