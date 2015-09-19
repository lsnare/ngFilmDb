<html>
    <body>
        <#include "index.ftl">
        <form action = "/add" method = "post">
            <input type="text" name="filmTitle"/> <input type="submit" name="add" value="Add a Film" />
        </form>
        <#if filmData??>
            <form action="/insertFilm" method="post" id="searchResultsTable">
                ${filmData}
            </form>
        </#if>
        <#if message??>${message}</#if>
    </body>
</html>



