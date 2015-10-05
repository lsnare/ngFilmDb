<html>
    <body>
        <#include "index.ftl">
        <form action = "/searchMyAPIFilms" method = "get">
            <input type="text" name="filmTitle"/> <input type="submit" name="add" value="Add a Film" />
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



