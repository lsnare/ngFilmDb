<html>
    <head>
        <link rel="stylesheet" type="text/css" href="/stylesheets/film.css" />
    </head>
    <body bgcolor="#7BA05B"> <!-- Asparagus -->
        <h1>Welcome to MyFilmDB</h1>
        <form action="/login" method="post">
            <dl>
                <dt> Username <dd><input type=text name="username">
                <dt> Password <dd><input type=text name="password">
            </dl>
            <input type=submit value="Sign In">
        </form>
        <#if error??>${error}</#if>
        Not a member? Apply for membership <a href="/register">here</a>
    </body>
</html>