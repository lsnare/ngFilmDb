<html>
    <body>
        <#include "index.ftl">
        <form action = "/search" method = "post">
            <p><b>Title</b> <input type="text" name="filmTitleSearch"/></p>
            <p><input type="submit" name="add" value="Search the Database" /></p>
        </form>

        <#if searchResultsHeader??>
            ${searchResultsHeader}
            <#if filmData??>${filmData}</#if>
        </#if>

        <#if error??>${error}</#if>
    </body>
</html>
