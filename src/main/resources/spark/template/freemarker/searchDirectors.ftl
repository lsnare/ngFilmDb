<html>
    <body>
        <#include "index.ftl">
        <form action = "/searchDirector" method = "post">
            <input type="text" name="directorNameSearch"/> <input type="submit" name="search" value="Search for a Director" />
        </form>

        <#if searchResultsHeader??>
            ${searchResultsHeader}
            ${directorData}
        </#if>

        <#if error??>${error}</#if>
    </body>
</html>
