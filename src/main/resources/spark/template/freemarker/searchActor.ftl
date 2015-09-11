<html>
    <body>
        <#include "index.ftl">
        <form action = "/searchActor" method = "post">
            <input type="text" name="actorNameSearch"/> <input type="submit" name="add" value="Search for an Actor" />
        </form>

        <#if searchResultsHeader??>
            ${searchResultsHeader}
            ${actorData}
        </#if>

        <#if error??>${error}</#if>
    </body>
</html>
