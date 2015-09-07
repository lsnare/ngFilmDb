<html>
    <body>
        <#include "index.ftl">
        <form action = "/addFilm" method = "post">
            <input type="text" name="filmTitle"/> <input type="submit" name="add" value="Add a Film" />
            ${message}
        </form>
    </body>
</html>



