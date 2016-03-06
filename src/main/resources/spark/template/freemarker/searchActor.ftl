<html>
    <body>
        <#include "index.ftl">
        <form action = "/searchActor" method = "post">
            <p><b>Actor Name</b> <input type="text" name="actorNameSearch"/></p>
            <p><input type="submit" name="add" value="Search for an Actor" /></p>
        </form>

        <#if searchResultsHeader??>
            ${searchResultsHeader}
            ${actorData}
        </#if>

        <#if error??>${error}</#if>
    </body>
</html>
