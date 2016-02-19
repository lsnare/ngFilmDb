<html>
    <body>
        <#include "index.ftl">
        <form action = "/search" method = "post">
            <input type="text" name="filmTitleSearch"/> <input type="submit" name="add" value="Search IMDB" />
        </form>

        <#if searchResultsHeader??>
            ${searchResultsHeader}
            <#if filmData??>${filmData}</#if>
        </#if>

        <#if error??>${error}</#if>
    </body>
</html>
