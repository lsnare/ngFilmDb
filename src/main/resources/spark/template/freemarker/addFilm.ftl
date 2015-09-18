<html>
    <body>
        <#include "index.ftl">
        <form action = "/add" method = "post">
            <input type="text" name="filmTitle"/> <input type="submit" name="add" value="Add a Film" />
        </form>
        <form action="/insertFilm" method="post" id="searchResultsTable">
            <#if filmData??>${filmData}</#if>
        </form>
            <#if message??>${message}</#if>

    </body>
</html>



