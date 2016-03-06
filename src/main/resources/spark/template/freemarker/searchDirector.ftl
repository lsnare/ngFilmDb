<html>
    <body>
        <#include "index.ftl">
        <form action = "/searchDirector" method = "post">
            <p><b>Director Name<b> <input type="text" name="directorNameSearch"/></p>
            <p><input type="submit" name="search" value="Search for a Director" /></p>
        </form>

        <#if searchResultsHeader??>
            ${searchResultsHeader}
            ${directorData}
        </#if>

        <#if error??>${error}</#if>
    </body>
</html>
