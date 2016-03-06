<html>
    <body>
        <#include "index.ftl">
        <form action = "/searchMyAPIFilms" method = "get">
            <p><b>Title</b> <input type="text" name="filmTitle"/></p>
            <p><input type="submit" name="add" value="Search IMDB" /></p>
        </form>
        <#if filmData??>
            <form action="/insertFilm" method="post" id="searchResultsTable">
                ${filmData}
            </form>
        </#if>
        <#if message??>${message}</#if>
        <#if error??>${error}</#if>
    </body>
</html>



